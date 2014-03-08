/*
 * Copyright 2014 Herb Bowie
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

  import java.io.*;

/**
 A class that will save Notes to disk. 

 @author Herb Bowie
 */
public class NoteWriter {
  
  public static final String FILE_EXT = ".txt";
  
  private             File                homeFolder = null;
  
  private             File                altFolder = null;
  
  private             BufferedWriter      outBuffered;
  
  public NoteWriter() {
    
  }
  
  public NoteWriter(File homeFolder) {
    this.homeFolder = homeFolder;
  }
  
  public void setHomeFolder (File homeFolder) {
    this.homeFolder = homeFolder;
  }
  
  public boolean exists (Note note) {
    return getFile(homeFolder, note).exists();
  }

   /**
   Does the given Note already exist on disk?
 
   @param folder    The folder in which the item is to be stored.
   @param Note      The Note to be stored.
 
   @return True if a disk file with the same path already exists,
           false if not.
   */
  public boolean exists (File folder, Note note) {
    return getFile(folder, note).exists();
  }
 
  /**
   Does the given Note already exist on disk?
 
   @param folder    The folder in which the item is to be stored.
   @param localPath The local path (folder plus file name) for the
                    Note to be stored.
 
   @return True if a disk file with the same path already exists,
           false if not.
   */
  public boolean exists (File folder, String localPath) {
    return getFile(folder, localPath).exists();
  }
  
  public boolean delete (Note note) {
    return getFile(homeFolder, note).delete();
  }
 
  /**
   Delete the passed note from disk.
 
   @param folder    The folder in which the note is to be stored.
   @param Note      The Note to be stored.
 
   @return True if the file was deleted successfully,
           false if not.
   */
  public boolean delete (File folder, Note Note) {
    return getFile(folder, Note).delete();
  }
 
  /**
   Delete the passed note from disk.
 
   @param folder    The folder in which the item is to be stored.
   @param localPath The local path (folder plus file name) for the
                    Note to be stored.
 
   @return True if the file was deleted successfully,
           false if not.
   */
  public boolean delete (File folder, String localPath) {
    return getFile(folder, localPath).delete();
  }
  
  public File getFile (Note note) {
    return new File (homeFolder, note.getFileName() + FILE_EXT);
  }
 
  /**
   Return a standard File object representing the note's stored location on disk.
 
   @param folder  The folder in which the item is to be stored.
   @param Note    The Note to be stored.
 
   @return The File pointing to the intended disk location for the given note.
   */
  public File getFile (File folder, Note note) {
    return new File (folder, note.getFileName() + FILE_EXT);
  }
 
  /**
   Return a standard File object representing the item's stored location on disk.
 
   @param folder    The folder in which the item is to be stored.
   @param localPath The local path (folder plus file name) for the
                    Note to be stored.
 
   @return The File pointing to the intended disk location for the given item.
   */
  public File getFile (File folder, String localPath) {
    StringBuilder completePath = new StringBuilder();
    try {
      completePath = new StringBuilder (folder.getCanonicalPath());
    } catch (Exception e) {
      completePath = new StringBuilder (folder.getAbsolutePath());
    }
    completePath.append('/');
    completePath.append(localPath);
    completePath.append(FILE_EXT);
    return new File (completePath.toString());
  }
  
  public void  save (NoteList noteList) 
      throws IOException {
    
    save (homeFolder, noteList, true);
    
  }
 
  public void save (File folder, NoteList noteList, boolean primaryLocation) 
      throws IOException {
    for (int i = 0; i < noteList.size(); i++) {
      Note nextNote = noteList.get(i);
      save (folder, nextNote, primaryLocation);
    }
  } // end method save
  
  public void save (Note note, boolean primaryLocation) 
      throws IOException {
    save (homeFolder, note, primaryLocation);
  }
 
  public void save (File folder, Note note, boolean primaryLocation) 
      throws IOException {
 
    File file = new File (folder, note.getFileName() + FILE_EXT);
    openOutput (file);
    String oldDiskLocation = note.getDiskLocation();
    saveOneItem (note);
    if (primaryLocation) {
      note.setDiskLocation (file);
    }
    closeOutput();
 
  }
 
  /**
   Open the output writer.
 
   @param outFile The file to be opened.
 
  */
  private void openOutput (File outFile) 
      throws IOException {

    FileOutputStream outStream = new FileOutputStream (outFile);
    OutputStreamWriter outWriter = new OutputStreamWriter (outStream, "UTF-8");
    outBuffered = new BufferedWriter (outWriter);
  }
 
  private void saveOneItem (Note note) 
      throws IOException {
    
    // Write Title
    writeFieldName (Note.TITLE_FIELD_NAME);
    writeFieldValue (note.getTitle());
    writeLine("");
    
    // Write Link, if one is present
    if (note.hasLink()) {
      writeFieldName (Note.LINK_FIELD_NAME);
      writeFieldValue (note.getLinkAsString());
      writeLine("");
    }
    
    // Write Tags, if they've been assigned
    if (note.hasTags()) {
      writeFieldName (Note.TAGS_FIELD_NAME);
      writeFieldValue (note.getTagsAsString());
      writeLine("");
    }
    
    // Write Body of note, if it's not blank
    if (note.hasBody()) {
      writeFieldName (Note.BODY_FIELD_NAME);
      writeLine("");
      writeFieldValue (note.getBody());
    }

  } // end of method saveOneItem
 
  private void writeFieldName (String fieldName) 
      throws IOException {
    write(fieldName);
    write(": ");
    for (int i = fieldName.length(); i < 6; i++) {
      write (" ");
    }
  }
 
  private void writeFieldValue (String fieldValue) 
      throws IOException {
    writeLine (fieldValue);
  }
 
  private void writeLine (String s) 
      throws IOException {
    outBuffered.write (s);
    outBuffered.newLine();
  }
 
  private void write (String s) 
      throws IOException {
    outBuffered.write (s);
  }
 
  /**
   Close the output writer.
 
   @return True if close worked ok.
  */
  public void closeOutput() 
      throws IOException {
    outBuffered.close();
  }
  
}
