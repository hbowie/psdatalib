/*
 * Copyright 2012 - 2014 Herb Bowie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.powersurgepub.psdatalib.notenik;

  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.util.*;

/**
 Loads a ClubEventList from a folder containing club events. 
 */
public class NoteReader {
  
  private    File                    folder = null;
  private    BufferedReader          inBuffered;
  private    NoteList                list = null;
  private    int                     notesLoaded = 0;
  
  private    ArrayList<DirToExplode> dirList;
  private		 int							       dirNumber = 0;
  
  private    File                    currDirAsFile = null;
  private    int                     currDirDepth = 0;
  
  private		 ArrayList<String>       dirEntries;
  private		 int							       entryNumber = 0;
  
  public NoteReader(File folder) {
    this.folder = folder;
  } 
  
  public void load (NoteList list) 
      throws IOException {
    
    notesLoaded = 0;
    this.list = list;
    dirList = new ArrayList();
    dirList.add (new DirToExplode (1, folder.getAbsolutePath()));
    dirNumber = 0;
    while (dirNumber < dirList.size()) {
      nextDirectory(dirNumber);
      dirNumber++;
    }
    Logger.getShared().recordEvent(LogEvent.NORMAL, 
        String.valueOf(notesLoaded) + " Notes loaded", false);
  }
  
  public int getNotesLoaded() {
    return notesLoaded;
  }
  
  /**
   Let's explode the next directory, if we have any more.
  */
  private void nextDirectory(int dirIndex) 
      throws IOException {

    DirToExplode currDir = dirList.get(dirIndex);
    currDirAsFile = new File (currDir.path);
    currDirDepth = currDir.depth;
    String[] dirEntry = currDirAsFile.list();
    if (dirEntry != null) {
      dirEntries = new ArrayList (Arrays.asList(dirEntry));
    }
    entryNumber = 0;
    while (entryNumber < dirEntries.size()) {
      nextDirEntry (entryNumber);
      entryNumber++;
    }
  }
  
  private void nextDirEntry (int entryIndex) 
      throws IOException {
    
    String nextDirEntry = dirEntries.get (entryIndex);
    File dirEntryFile = new File (currDirAsFile, nextDirEntry);
    if (dirEntryFile.isDirectory()) {
      if (nextDirEntry.equalsIgnoreCase("templates")
          || nextDirEntry.equalsIgnoreCase("publish")) {
        // skip
      } else {
        DirToExplode newDirToExplode = new DirToExplode 
            (currDirDepth + 1, dirEntryFile.getAbsolutePath());
        dirList.add (newDirToExplode);
      }
    } 
    else
    if (isInterestedIn (dirEntryFile)) {
      Note nextNote = getNote(dirEntryFile);
      if (nextNote != null) {
        list.add(nextNote);
        notesLoaded++;
      }
    }
  }
  
  /**
   Is this input module interested in processing the specified file?
  
   @param candidate The file being considered. 
  
   @return True if the input module thinks this file is worth processing,
           otherwise false. 
  */
  public static boolean isInterestedIn(File candidate) {
    if (candidate.isHidden()) {
      return false;
    }
    else
    if (candidate.getName().startsWith(".")) {
      return false;
    }
    else
    if (! candidate.canRead()) {
      return false;
    }
    else
    if (candidate.isFile() 
        && candidate.length() == 0
        && candidate.getName().equals("Icon\r")) {
      return false;
    }
    else
    if (candidate.isDirectory()) {
      return false;
    }
    else
    if (candidate.getParent().endsWith("templates")) {
      return false;
    }
    else
    if (candidate.getName().equalsIgnoreCase("New Event.txt")) {
      return false;
    }
    else
    if (candidate.getName().contains("conflicted copy")) {
      return false;
    }
    else
    if (candidate.getName().endsWith (".txt")) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
   Read one note from disk and return it as a note object. 
  
   @param noteFile The file containing the note on disk. 
   @return A Note object. 
   @throws IOException
   @throws FileNotFoundException 
  */
  public Note getNote(File noteFile) 
      throws IOException, FileNotFoundException {
    
    Note note = null;
    
    if (noteFile.exists()
        && noteFile.canRead()
        && noteFile.isFile()) {
      
      FileName noteFileName = new FileName(noteFile);
      String fileNameIn = noteFileName.getBase();
      
      // Use the file name (minus the path and extension) as the default title
      note = new Note(fileNameIn);
      
      // Get ready to read the text file
      FileInputStream fileInputStream = new FileInputStream(noteFile);
      InputStreamReader inReader = new InputStreamReader (fileInputStream);
      inBuffered = new BufferedReader (inReader);
      boolean bodyStarted = false;
      String line = inBuffered.readLine();
      int lineCount = 0;
      
      // For each line in the file
      while (line != null) {
        lineCount++;
        if (bodyStarted) {
          // Once we've started the body, assume the rest is all body
          note.appendLineToBody(line);
        } else {
          
          // Haven't started the body yet -- look for metadata
          
          // Find the beginning and end of the data on the line
          int start = 0;
          while (start < line.length() 
              && Character.isWhitespace(line.charAt(start))) {
            start++;
          }
          int end = line.length();
          while (end > 0 
              && Character.isWhitespace(line.charAt(end - 1))) {
            end--;
          }
          
          // See if the line underlines a heading
          char underlineChar = ' ';
          int underlineCount = 0;
          boolean underlines = false;
          if (start < end
              && (line.charAt(start) == '-' || line.charAt(start) == '=')) {
            underlineChar = line.charAt(start);
            underlines = true;
            underlineCount = 1;
            while (underlines && (start + underlineCount) <= end) {
              if (line.charAt(start + underlineCount) == underlineChar) {
                underlineCount++;
              } else {
                underlines = false;
              }
            } // end while finding more underline characters
          }
          
          // If the line contains a colon, look for field name and value
          int colonPosition = line.indexOf(":", start);
          String fieldName = "";
          if (colonPosition > 0) {
            int fieldNameEnd = colonPosition;
            while (fieldNameEnd > start 
                && Character.isWhitespace(line.charAt(fieldNameEnd - 1))) {
              fieldNameEnd--;
            }
            fieldName = StringUtils.commonName
                (line.substring(start, fieldNameEnd));
            int fieldValueStart = colonPosition + 1;
            while (fieldValueStart < end
                && Character.isWhitespace(line.charAt(fieldValueStart))) {
              fieldValueStart++;
            }
            if (fieldName.equals(Note.TITLE_COMMON_NAME)) {
              note.setTitle(line.substring(fieldValueStart, end));
            }
            else
            if (fieldName.equals(Note.LINK_COMMON_NAME)) {
              note.setLink(line.substring(fieldValueStart, end));
            }
            else
            if (fieldName.equals(Note.TAGS_COMMON_NAME)) {
              note.setTags(line.substring(fieldValueStart, end));
            }
            else
            if (fieldName.equals(Note.BODY_COMMON_NAME)) {
              note.setBody(line.substring(fieldValueStart, end));
              bodyStarted = true;
            }
            else {
              note.appendLineToBody(line);
              bodyStarted = true;
            }
          } // end if colon found on line
          else
          if (start >= end) {
            // If the line is blank, then just ignore it
          }
          else
          if (underlines) {
            // If the line underlines a heading, then just ignore it
          }
          else
          if (lineCount == 1
              && fileNameIn.equals 
                (StringUtils.makeReadableFileName(line.substring(start, end)))) {
            note.setTitle(line.substring(start, end));
          }
          else
          if (line.substring(start, end).startsWith("http://")
              || line.substring(start, end).startsWith("https://")
              || line.substring(start, end).startsWith("mailto:")) {
            note.setLink(line.substring(start, end));
          } else {
            note.appendLineToBody(line);
            bodyStarted = true;
          }
          
        } // end if not body started
        line = inBuffered.readLine();
      }
      inBuffered.close();
    }
    
    return note;
  }
  
  /**
     Inner class to define a directory to be processed.
   */
  class DirToExplode {
    int 		depth = 0;
    String	path  = "";
    
    DirToExplode (int depth, String path) {
      this.depth = depth;
      this.path = path;
    } // DirToExplode constructor
  } // end DirToExplode inner class

}
