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

  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.pstextio.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.util.*;
  import org.pegdown.*;

/**
 A class to perform input and output for note files. 

 @author Herb Bowie
 */
public class NoteIO 
    implements DataSource 
  {
  
  public static final String              PARMS_TITLE         = "Collection Parms";
  public static final String              FILE_EXT            = ".txt";
  
  private             NoteParms           noteParms 
      = new NoteParms(NoteParms.NOTES_ONLY_TYPE);
  
  private             File                homeFolder          = null;
  private             String              homePath            = "";
  
  private             File                altFolder           = null;
  private             NoteList            list                = null;
  private             int                 notesLoaded         = 0;
  
  /** Sequential number identifying last record read or written. */
  private             int                 recordNumber;
  
  private             ArrayList<DirToExplode> dirList;
  private             int							    dirNumber = 0;
  
  private             File                currDirAsFile = null;
  private             int                 currDirDepth = 0;
  private             int                 maxDepth = 99;
  
  private             ArrayList<String>   dirEntries;
  private             int							    entryNumber = 0;
  
  private             File                noteFileToRead = null;
  private             Note                nextNote = null;
  
  private             BufferedReader      inBuffered;
  private             BufferedWriter      outBuffered;
  
  private             int                 ioStyle = IO_STYLE_UNDETERMINED;
  public static final int IO_STYLE_UNDETERMINED   = -1;
  public static final int IO_EXPLICIT             = 0;
  public static final int IO_IMPLICIT             = 1;
  public static final int IO_IMPLICIT_UNDERLINES  = 2;
  public static final int IO_IMPLICIT_FILENAME    = 3;
  
  private             PegDownProcessor   pegDown;
  
  private             NoteBuilder builder;
  
  // The following fields are used for logging
  
  /** Log to record events. */
  private  Logger       log;
  
  /** Do we want to log all data, or only data preceding significant events? */
  private  boolean      dataLogging = false;
  
  /** Data to be sent to the log. */
  private  LogData      logData;
  
  /** Events to be logged. */
  private  LogEvent     logEvent;
  
  /** Debug instance. */
  private  Debug				debug = new Debug(false);
  
  /** Identifier for this file (to be printed in the log as a source ID). */
  private  String       fileId;
  
  public NoteIO () {
     initialize();
  }
  
  /**
     Constructs a NoteIO object.
    
     @param  inPath Directory path to be read.
   */
  public NoteIO (String inPath, int inType) {
    if (inPath.startsWith("http")) {
      noteFileToRead = null;
    } else {
      noteFileToRead = new File (inPath);
    }
    setHomeFolder(null);
    noteParms.setNoteType(inType);
    initialize();
  }

  /**
     Constructs a NoteIO object.
    
     @param  inPathFile Directory path to be read.
   */
  public NoteIO (File fileOrFolder, int inType) {
    if (fileOrFolder.isDirectory()) {
      setHomeFolder(fileOrFolder);
    } else {
      noteFileToRead = fileOrFolder;
      setHomeFolder(null);
    }
    noteParms.setNoteType(inType);
    initialize();
  }
  
  public NoteIO (TextLineReader lineReader, int inType) {
    noteFileToRead = null;
    if (lineReader instanceof FileLineReader) {
      FileLineReader fileLineReader = (FileLineReader) lineReader;
      noteFileToRead = fileLineReader.getFile();
    }
    setHomeFolder(null);
    noteParms.setNoteType(inType);
    initialize();
  }
  
  public NoteIO (RecordDefinition recDef, File folder) {
    noteParms.setRecDef(recDef);
    setHomeFolder(folder);
    initialize();
  } 
  
  public NoteIO (File folder) {
    setHomeFolder(folder);
    initialize();
  }
  
  /**
     Performs standard initialization for all the constructors.
     By default, fileId is set to "directory".
   */
  private void initialize () {
    
    int pegDownOptions = 0;
    pegDownOptions = pegDownOptions + Extensions.SMARTYPANTS;
    pegDownOptions = pegDownOptions + Extensions.TABLES;
    pegDown = new PegDownProcessor(pegDownOptions);
    
    fileId = "NoteIO";
    logData = new LogData ("", fileId, 0);
    logEvent = new LogEvent (0, "");
  }
  
  public void setHomeFolder (File homeFolder) {
    this.homeFolder = homeFolder;
    if (homeFolder == null) {
      homePath = "";
    } else {
      try {
        homePath = homeFolder.getCanonicalPath();
      } catch (IOException e) {
        homePath = homeFolder.getAbsolutePath();
      }
    }
  }
  
  public void setNoteType(int noteType) {
    noteParms.setNoteType(noteType);
  }
  
  public void setNoteParms(NoteParms noteParms) {
    this.noteParms = noteParms;
  }
  
  /**
   Pass any metadata lines to the markdown parser as well. 
  
   @param metadataAsMarkdown True if metadata lines should appear as part
                             of output HTML, false otherwise. 
  */
  public void setMetadataAsMarkdown (boolean metadataAsMarkdown) {
    noteParms.setMetadataAsMarkdown(metadataAsMarkdown); 
  }
  
  public boolean treatMetadataAsMarkdown() {
    return noteParms.treatMetadataAsMarkdown();
  }
  
  /* =======================================================================
   * 
   * This section of the class contains input routines. 
   *
   * ======================================================================= */
  
  public void load (NoteList list) 
      throws IOException {
    
    notesLoaded = 0;
    this.list = list;
    openForInput();
    Note note = readNextNote();
    while (note != null) {
      list.add(note);
      notesLoaded++;
      note = readNextNote();
    }
    close();

    Logger.getShared().recordEvent(LogEvent.NORMAL, 
        String.valueOf(notesLoaded) + " Notes loaded", false);
  }
  
  public int getNotesLoaded() {
    return notesLoaded;
  }
  
  /**
     Opens the reader for input.
    
     @param inDict A data dictionary to use.
    
     @throws IOException If there is trouble opening a disk file.
   */
  public void openForInput (DataDictionary inDict)
      throws IOException {
    noteParms.newRecordDefinition(inDict);
    noteParms.buildRecordDefinition();
    openForInputCommon();
  }
      
  /**
     Opens the reader for input.
    
     @param inRecDef A record definition to use.
    
     @throws IOException If there is trouble opening a disk file.
   */
  public void openForInput (RecordDefinition inRecDef)
      throws IOException {
    noteParms.setRecDef(inRecDef);
    openForInputCommon();
  }
  
  public void openForInput () 
      throws IOException {
    
    noteParms.buildRecordDefinition();
    openForInputCommon();
  }
  
  public NoteParms getNoteParms() {
    File noteParmsFile = new File (homeFolder, NoteParms.FILENAME);
    if (noteParmsFile.exists() && noteParmsFile.canRead()) {
      NoteParms noteParms = new NoteParms();
      return noteParms;
    } else {
      return null;
    }
  }
  
  private void openForInputCommon () 
      throws IOException {
    notesLoaded = 0;
    dirList = new ArrayList();
    dirList.add (new DirToExplode (1, homeFolder.getAbsolutePath()));
    dirNumber = -1;
    noteFileToRead = null;
    nextNote = null;
    entryNumber = 0;
    dirEntries = new ArrayList<String>();
    recordNumber = 0;
    nextNote();
  }
  
  /**
     Sets the file ID to be passed to the Logger.
    
     @param fileId Used to identify the source of the data being logged.
   */
  public void setFileId (String fileId) {
    this.fileId = fileId;
    logData.setSourceId (fileId);
  }
  
  /**
     Sets the maximum directory explosion depth.
    
     @param maxDepth Desired directory/sub-directory explosion depth.
   */
  public void setMaxDepth (int maxDepth) {
    this.maxDepth = maxDepth;
  }
  
  /**
     Returns the next input data record.
    
     @return Next data record.
    
     @throws IOException If reading from a source that might generate
                         these.
   */
  public DataRecord nextRecordIn ()
      throws IOException {
    return readNextNote();
  }
  
  /**
   Read the next note.
  
   @return Next note, or null if no more notes left. 
  
   @throws IOException 
  */
  public Note readNextNote() 
      throws IOException {
    
    Note noteToReturn = nextNote;
    nextNote();
    recordNumber++;
    return noteToReturn;
  }
  
  /**
     Returns the record number of the last record
     read or written.
    
     @return Number of last record read or written.
   */
  public int getRecordNumber () {
    return recordNumber;
  }
  
  /**
     Indicates whether there are more records to return.
    
     @return True if no more records to return.
   */
  public boolean isAtEnd() {
    return (nextNote == null);
  }
  
  public void close() {
    noteFileToRead = null;
    nextNote = null;
  }
  
  private void nextNote() 
      throws IOException {
    nextNote = null;
    while (nextNote == null && dirNumber < dirList.size()) {
      nextDirEntry();
    }
  }
  
  private void nextDirEntry () 
      throws IOException {
    
    if (entryNumber >= 0 && entryNumber < dirEntries.size()) {
      String nextDirEntry = dirEntries.get (entryNumber);
      noteFileToRead = new File (currDirAsFile, nextDirEntry);
      if (noteFileToRead.isDirectory()) {
        if (nextDirEntry.equalsIgnoreCase("templates")
            || nextDirEntry.equalsIgnoreCase("publish")
            || currDirDepth >= maxDepth) {
          // skip
        } else {
          DirToExplode newDirToExplode = new DirToExplode 
              (currDirDepth + 1, noteFileToRead.getAbsolutePath());
          dirList.add (newDirToExplode);
        }
      } 
      else
      if (isInterestedIn (noteFileToRead)) {
        nextNote = getNote(noteFileToRead, "");
        if (nextNote == null) {
        }
      } else {
        // No interest
      }
      entryNumber++;
    } else {
      dirNumber++;
      nextDirectory();
    }
  }
  
  /**
   Let's explode the next directory, if we have any more.
  */
  private void nextDirectory() 
      throws IOException {

    if (dirNumber >= 0 && dirNumber < dirList.size()) {
      DirToExplode currDir = dirList.get(dirNumber);
      currDirAsFile = new File (currDir.path);
      currDirDepth = currDir.depth;
      String[] dirEntry = currDirAsFile.list();
      if (dirEntry != null) {
        dirEntries = new ArrayList (Arrays.asList(dirEntry));
      }
      entryNumber = 0;
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
    if (candidate.getName().contains(PARMS_TITLE)) {
      return false;
    }
    else
    if (candidate.getName().endsWith (".txt")
        || candidate.getName().endsWith (".text")
        || candidate.getName().endsWith (".markdown")
        || candidate.getName().endsWith (".md")
        || candidate.getName().endsWith (".mdown")
        || candidate.getName().endsWith (".mkdown")
        || candidate.getName().endsWith (".mdtext")) {
      return true;
    } else {
      return false;
    } 
  }
  
  public Note getNote(String fileName) 
      throws IOException, FileNotFoundException {
    return getNote(getFile(fileName), "");
  }
  
  /**
   Read one note from disk and return it as a note object. 
  
   @param noteFile The file containing the note on disk. 
   @param syncPrefix An optional prefix that might be appended to the front
          of the note's title to form the file name. 
   @return A Note object. 
   @throws IOException
   @throws FileNotFoundException 
  */
  public Note getNote(File noteFile, String syncPrefix) 
      throws IOException, FileNotFoundException {
    
    Note note = null;
    
    if (noteFile.exists()
        && noteFile.canRead()
        && noteFile.isFile()) {
      
      FileName noteFileName = new FileName(noteFile);
      String fileNameIn = "";
      if (syncPrefix != null
          && syncPrefix.length() > 0
          && noteFileName.getBase().startsWith(syncPrefix)) {
        fileNameIn = noteFileName.getBase().substring(syncPrefix.length());
      } else {
        fileNameIn = noteFileName.getBase();
      }
      NoteBuilder builder = new NoteBuilder (noteParms);
      note = new Note(noteParms.getRecDef());
      note.setDiskLocation(noteFile);
      
      // Use the file name (minus the path and extension) as the default title
      note.setTitle(fileNameIn);
      
      // Set the last modified date
      Date lastModDate = new Date(noteFile.lastModified());
      note.setLastModDate(lastModDate);
      
      // Get ready to read the text file
      FileInputStream fileInputStream = new FileInputStream(noteFile);
      InputStreamReader inReader = new InputStreamReader (fileInputStream);
      inBuffered = new BufferedReader (inReader);
      
      this.builder = new NoteBuilder(noteParms);
      
      String line = inBuffered.readLine();
      
      // For each line in the file
      while (line != null) {
        NoteLine noteLine = new NoteLine(noteParms, builder, note, line);
        line = inBuffered.readLine();
      }
      inBuffered.close();
    }
    
    return note;
  }
  
  public void  save (NoteList noteList) 
      throws IOException {
    
    save (homeFolder, noteList, true);
    
  }
  
  /* =======================================================================
   * 
   * This section of the class contains output routines. 
   *
   * ======================================================================= */
 
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
    File file = getFile(folder, note);
    openOutput (file);
    String oldDiskLocation = note.getDiskLocation();
    saveOneItem (note);
    if (primaryLocation) {
      note.setDiskLocation (file);
    }
    closeOutput();
  }
  
  /**
   Save one note to a sync folder. 
  
   @param syncFolder The folder to which the note is to be saved. 
   @param syncPrefix The prefix to be appended to the front of the file name. 
   @param note       The note to be saved. 
  
   @throws IOException 
  */
  public void saveToSyncFolder (String syncFolder, String syncPrefix, Note note) 
      throws IOException {
    openOutput (getSyncFile(syncFolder, syncPrefix, note.getTitle()));
    saveOneItem (note);
    closeOutput();
  }
  
  public File getSyncFile (String syncFolderStr, String syncPrefix, String title) {
    File syncFolder = new File(syncFolderStr);
    return new File(syncFolder, syncPrefix + title + FILE_EXT);
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
    for (int i = 0; i < note.getNumberOfFields(); i++) {
      DataField nextField = note.getField(i);
      if (nextField != null
          && nextField.hasData()) {
        writeFieldName (nextField.getProperName());
        if (nextField.getCommonFormOfName().equals("body")
            || nextField.getCommonFormOfName().equals("comments")) {
          writeLine("");
          writeLine(" ");
        }
        writeFieldValue (nextField.getData());
        writeLine("");
      }
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
  
  /* =======================================================================
   * 
   * This section of the class contains other file-related routines. 
   *
   * ======================================================================= */
  
  /**
   Does the given note exist on disk?
  
   @param note The note to be evaluated. 
  
   @return True if a disk file is found, false otherwise. 
  */
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
  
  public boolean exists (String localPath) {
    return getFile(homeFolder, localPath).exists();
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
  
  public File getFile(String localPath) {

    return getFile (homeFolder, localPath);

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
  
  /**
     Returns the record definition for the file.
    
     @return Record definition for this tab-delimited file.
   */
  public RecordDefinition getRecDef () {
    return noteParms.getRecDef();
  }
  
  /**
     Retrieves the path to the parent folder of the notes (if any).
    
     @return Path to the parent folder of the notes (if any).
   */
  public String getDataParent () {
    if (homeFolder == null) {
      return null;
    } else {
      return homeFolder.getAbsolutePath();
    }
  }
  
 /**
     Sets the Logger object to be used for logging. 
    
     @param log The Logger object being used for logging significant events.
   */
  public void setLog (Logger log) {
    this.log = log;
  }
  
  /**
     Gets the Logger object to be used for logging. 
    
     @return The Logger object being used for logging significant events.
   */
  public Logger getLog () {
    return log;
  } 
  
  /**
     Sets the option to log all data off or on. 
    
     @param dataLogging True to send all data read or written to the
                        log file.
   */
  public void setDataLogging (boolean dataLogging) {
    this.dataLogging = dataLogging;
  }
  
  /**
     Gets the option to log all data. 
    
     @return True to send all data read or written to the
             log file.
   */
  public boolean getDataLogging () {
    return dataLogging;
  }
  
  /**
     Sets the debug instance to the passed value.
    
     @param debug Debug instance. 
   */
  public void setDebug (Debug debug) {
    this.debug = debug;
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
