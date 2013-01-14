package com.powersurgepub.psdatalib.clubplanner;

  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.pstextio.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.util.*;
  

/**
   A reader that reads Club Planner event records. <p>
   
   This code is copyright (c) 1999-2012 by Herb Bowie of PowerSurge Publishing.
   All rights reserved. <p>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
   
 */
 public class ClubEventReader 
     extends File
         implements  
             DataSource {
  
  /** 
     The number of levels of directories and sub-directories to be read. 
     A value of 1 (the default) indicates that only the top level directory
     should be read. A value of 2 indicates one level of sub-directories, and
     so forth.
   */
  
  private		 int							currDirDepth;
  
  private		 File							currDirAsFile;
  


  /** The logger to use to log events. */    
  private    Logger           log;
  
  /* Let's not log all data. */
  private    boolean          dataLogging = false;
  
  /** Debug instance. */
  private		 Debug						debug = new Debug (false);
  
  /** The data dictionary to be used by this record. */
  private    DataDictionary   dict = new DataDictionary();
  
  /** The record definition to be used by this record. */
  private    RecordDefinition recDef = null;
  
  /** Pointer to a particular record within the array. */
  private    int              recordNumber;
  
  /** Data to be sent to the log. */
  private    LogData          logData;
  
  /** An event to be sent to the log. */
  private    LogEvent         logEvent;
  
  /** The identifier for this reader. */
  private    String           fileId;
  
  /** The directory to be read. */
  private    String           inPath;
  
  /** The type of data set to generate: planner or minutes. */
  private    int              inType = 1;
  public static final int     PLANNER_TYPE = 1;
  public static final int     NOTES_TYPE = 2;
  
  
  /** The number of directories in the top directory to be read. */
  private		 int							directoryNumberOfFolders;
  
  private    ClubEventCalc    clubEventCalc = null;
  
  private    StringBuilder    headerWord;
  
  private    int              headerPosition;
  private static final int    NOTES_FROM = 1;
  private static final int    NOTES_FOR  = 2;
  private static final int    NOTES_VIA  = 3;
  private static final int    NOTES_MIN  = NOTES_FROM;
  private static final int    NOTES_MAX  = NOTES_VIA;
  
  private    StringBuilder    headerElement;
  
  private    boolean          endOfNotesBlock = false;
  
  private    StringBuilder    eventFieldValue = new StringBuilder();
  private    StringBuilder    noteFieldValue = new StringBuilder();
  
  private    ClubEvent        clubEvent;
  private    EventNote        eventNote;
  private    int              noteIndex = 0;
  
  private    int              fieldAsHTMLNumber = -1;
  private    TextLineReader   reader = null;
  private    FileName         inPathFileName;
  
  private    boolean          atEnd = false;
  
  private    boolean          blockComment = false;
  
  // The next line to be processed, and info about that line. 
  private    String           line = "";
  private    int              lineStart = 0;
  private    int              lineEnd = line.length();
  private    int              blockCommentStart = -1;
  private    int              blockCommentEnd = -1;
  private    int              lineCommentStart = -1;
  
  private    int              fieldNumber = -1;
  private    int              valueStart = 0;
  private    String           appendValue = "";
  
  
  private    boolean          notesHeaderLine = false;
  private    int              notesHeaderDashStart = 0;

  /**
     Constructs a club event reader given a path defining the directory
     to be read.
    
     @param  inPath Directory path to be read.
   */
  public ClubEventReader (String inPath, int inType) {
    super (inPath);
    this.inPath = inPath;
    this.inType = inType;
    initialize();
  }

  /**
     Constructs a club event reader given a file object 
     defining the directory to be read.
    
     @param  inPathFile Directory path to be read.
   */
  public ClubEventReader (File inPathFile, int inType) {
    super (inPathFile.getAbsolutePath());
    this.inPath = this.getAbsolutePath();
    this.inType = inType;
    initialize();
  }
  
  /**
     Performs standard initialization for all the constructors.
     By default, fileId is set to "directory".
   */
  private void initialize () {
    if (this.isDirectory()) {
      FileName dirName = new FileName (inPath, FileName.DIR_TYPE);
      directoryNumberOfFolders = dirName.getNumberOfFolders();
    } else {
      FileName dirName = new FileName (this.getParentFile());
      directoryNumberOfFolders = dirName.getNumberOfFolders();
    }
    
    recDef = new RecordDefinition(dict);
    for (int i = 0; i < ClubEvent.COLUMN_COUNT; i++) {
      recDef.addColumn (ClubEvent.getColumnName(i));
    }
    
    if (inType == NOTES_TYPE) {
      for (int i = 0; i < EventNote.COLUMN_COUNT; i++) {
        recDef.addColumn (EventNote.getColumnName(i));
      }
    }
    
    fileId = "ClubPlannerDataSource";
    logData = new LogData ("", fileId, 0);
    logEvent = new LogEvent (0, "");
  }
  
  /**
   Set the club event calculator to use. 
  
   @param clubEventCalc The club event calculator to use. 
  */
  public void setClubEventCalc (ClubEventCalc clubEventCalc) {
    this.clubEventCalc = clubEventCalc;
  }
  
  /**
   Ensure that we have a club event calculator. If one hasn't been passed, 
   then let's create a new one. 
  */
  private void ensureClubEventCalc() {
    if (clubEventCalc == null) {
      this.clubEventCalc = new ClubEventCalc();
    }
  }
    
  /**
     Opens the reader for input using a newly defined
     data dictionary.
    
     @throws IOException If there are problems reading the directory.
   */
  public void openForInput () 
      throws IOException {
    openForInput (new DataDictionary());
  }
  
  /**
     Opens for input with the supplied record definition.
    
     @param  inRecDef Record definition already constructed.
    
     @throws IOException If there are problems reading the directory.
   */
  public void openForInput (RecordDefinition inRecDef) 
      throws IOException {
    openForInput (inRecDef.getDict());
  } // end of openForInput method
  
  public void openForInput (ClubEvent clubEvent) 
      throws IOException {
    dict = new DataDictionary();
    this.clubEvent = clubEvent;
    openForInputCommon();
  }
  
  /**
     Opens for input with the supplied data dictionary.
    
     @param  inDict Data dictionary already constructed.
    
     @throws IOException If there are problems opening the file.
   */
  public void openForInput (DataDictionary inDict) 
      throws IOException {
    
    dict = inDict;
    clubEvent = new ClubEvent();
    openForInputCommon();
    
  } // end of openForInput method
  
  private void openForInputCommon() {
    
    ensureLog();
    
    recordNumber = 0;
    atEnd = false;
    fieldNumber = -1;

    reader = new FileLineReader (this);
    ensureClubEventCalc();
    clubEventCalc.setFileName(this);
    reader.open();

    // Now gather field values from the input file
    blockComment = false;
    
    eventNote = new EventNote();
    if (clubEventCalc.ifOpYearFromFolder()) {
      clubEvent.setYear(clubEventCalc.getOpYearFromFolder());
    }
    // clubEvent.setFileName(inPathFileName.getBase());
    if (clubEventCalc.ifStatusFromFolder()) {
      clubEvent.setStatus(clubEventCalc.getStatusFromFolder());
    }
    if (clubEventCalc.ifTypeFromFolder()) {
      clubEvent.setType(clubEventCalc.getTypeFromFolder());
    }
    clubEvent.resetModified();
    clubEvent.setDiskLocation(this);

    while (reader != null
        && reader.isOK()
        && (! reader.isAtEnd())) {
      readAndEvaluateNextLine();
      processNextLine();
    } // end while reader has more lines
    setLastNoteFieldValue();
    setLastEventFieldValue();
    clubEventCalc.calcAll(clubEvent);
    noteIndex = 0;
  }
  
  /**
   Return next ClubEvent object. 
  
   @return Next ClubEvent object, or null if no more to return.
  */
  public ClubEvent nextClubEvent() {
    if (recordNumber == 0 
        && clubEvent.isModified() 
        && clubEvent.getWhat().length() > 0) {
      recordNumber++;
      return clubEvent;
    } else {
      atEnd = true;
      return null;
    }
  }
  
  public ClubEvent getClubEvent() {
    return clubEvent;
  }
  
  public DataRecord nextRecordIn () {
    if (isAtEnd()) {
      return null;
    }
    else
    if (inType == PLANNER_TYPE) {
      return nextEventRecordIn();
    }
    else
    if (inType == NOTES_TYPE) {
      return nextNotesRecordIn();
    } else {
      return null;
    }
  }

  /**
     Returns the next directory entry.
    
     @return Next directory entry as a data record.
   */
  private DataRecord nextEventRecordIn () {
    if (recordNumber == 0 
        && clubEvent.isModified() 
        && clubEvent.getWhat().length() > 0) {
      recordNumber++;
      return clubEvent.getDataRec();
    } else {
      atEnd = true;
      return null;
    }
  } // end of nextRecordIn method
  
  /**
   Returns the next minutes entry as a record. 
  
   @return Next minutes entry.
  */
  private DataRecord nextNotesRecordIn () {
    
    if (atEnd) {
      return null;
    }
    else
    if ((! clubEvent.isModified())
        || clubEvent.getWhat().length() == 0) {
      atEnd = true;
      return null;
    }
    else
    if (noteIndex >=  clubEvent.sizeEventNoteList()) {
      atEnd = true;
      return null;
    } else {
      DataRecord dataRec = clubEvent.getDataRec();
      eventNote = clubEvent.getEventNote(noteIndex);
      for (int i = 0; i < EventNote.COLUMN_COUNT; i++) {
        Object noteField = eventNote.getColumnValue(i);
        if (noteField == null) {
          int dataRecFieldNumber = dataRec.addField
              (recDef, "");
        } else {
          int dataRecFieldNumber = dataRec.addField
              (recDef, noteField.toString());
        }
      }
      noteIndex++;
      return dataRec;
    }
  }
  
  /**
    Read the next line and see what we've got. 
   */
  private void readAndEvaluateNextLine () {
    
    line = reader.readLine();
    lineStart = 0;
    lineEnd = line.length();

    // Check for comments
    blockCommentStart = -1;
    blockCommentEnd = -1;
    lineCommentStart = -1;
    
    notesHeaderLine = false;

    if (blockComment) {
      lineEnd = 0;
    }

    int slashScanStart;
    int slashIndex = line.indexOf('/');
    while (slashIndex >= 0 && slashIndex < line.length()) {
      slashScanStart = slashIndex + 1;

      // Get characters before and after slash
      char beforeSlash = ' ';
      if (slashIndex > 0) {
        beforeSlash = line.charAt(slashIndex - 1);
      }
      char afterSlash = ' ';
      if (slashIndex < (line.length() - 1)) {
        afterSlash = line.charAt(slashIndex + 1);
      }

      if (blockComment) {
        if (beforeSlash == '*') {
          lineStart = slashIndex + 1;
          lineEnd = line.length();
          blockComment = false;
        }
      } else {
        if (afterSlash == '*') {
          lineEnd = slashIndex;
          blockComment = true;
        }
        else
        if (beforeSlash == ' ' && afterSlash == '/') {
          lineEnd = slashIndex;
          slashScanStart++;
        }
      }
      slashIndex = line.indexOf('/', slashScanStart);
    }
    int colonPos = line.indexOf(':', lineStart);
    valueStart = lineStart;

    int commonNameIndex = -1;
    if (colonPos > 0 && colonPos < lineEnd) {
      String possibleFieldName 
          = line.substring(lineStart, colonPos).trim();
      int dataStartFollowingColon = colonPos + 1;
      while (dataStartFollowingColon < lineEnd 
          && Character.isWhitespace(line.charAt(dataStartFollowingColon))) {
        dataStartFollowingColon++;
      }
      commonNameIndex = ClubEvent.commonNameStartsWith(possibleFieldName);
      if (commonNameIndex >= 0) {
        valueStart = dataStartFollowingColon;
      }
    } // end if colon found on the line
    
    appendValue = line.substring(valueStart, lineEnd).trim();
    
    if (commonNameIndex >= 0) {
      setLastEventFieldValue();
      fieldNumber = commonNameIndex;
    }
    
    if (fieldNumber == ClubEvent.NOTES_COLUMN_INDEX) {
      // This line is part of the Notes field
      notesHeaderDashStart = 0;
      while (notesHeaderDashStart < appendValue.length() 
          && Character.isWhitespace(appendValue.charAt(notesHeaderDashStart))) {
        notesHeaderDashStart++;
      }
      int k = notesHeaderDashStart + 1;
      if (notesHeaderDashStart < appendValue.length() 
          && appendValue.charAt(notesHeaderDashStart) == '-'
          && k < appendValue.length() && appendValue.charAt(k) == '-') {
        notesHeaderLine = true;
        setLastNoteFieldValue();        
      } // end if notes line starts with two hyphens
    } // end if field is part of the notes
  } // end method readAndEvaluateNextLine
  
  /**
   At the end of a file, or when starting a new field, take the accumulated
   String value found and apply it to the last field. 
   */
  private void setLastEventFieldValue() {
    if (fieldNumber >= 0
        && eventFieldValue.length() > 0) {
      clubEvent.setColumnValue(fieldNumber, eventFieldValue.toString());
      eventFieldValue = new StringBuilder();
    }
  }
  
  /**
   At the start of a new note header, or at the end of a file, take the 
   accumulated note text found, apply it to the last note, and add the 
   note to the event. 
  */
  private void setLastNoteFieldValue() {
    if (clubEvent != null
        && eventNote != null
        && noteFieldValue.length() > 0) {
      eventNote.setNote(noteFieldValue.toString());
      clubEventCalc.calcAll(eventNote);
      clubEvent.addEventNote(eventNote);
      noteFieldValue = new StringBuilder();
    }
  }
  
  /**
    Process the contents of the next line. 
   */
  private void processNextLine() {
    
    if (fieldNumber >= 0) {
      // A valid field has been identified on this or a prior line
      if (appendValue.length() == 0) {
        if (ClubEvent.isMarkdownFormat(fieldNumber)) {
          eventFieldValue.append(GlobalConstants.LINE_FEED);
        }
      } else {
        // This line is not blank
        if (eventFieldValue.length() > 0
            && eventFieldValue.charAt(eventFieldValue.length() - 1) 
              != GlobalConstants.LINE_FEED) {
          eventFieldValue.append(" ");
        }
        eventFieldValue.append(appendValue);
        if (ClubEvent.isMarkdownFormat(fieldNumber)) {
          eventFieldValue.append(GlobalConstants.LINE_FEED);
        }
        if (fieldNumber == ClubEvent.NOTES_COLUMN_INDEX) {
          // This line is part of the Notes field
          if (notesHeaderLine) {
            processNotesHeader(appendValue, notesHeaderDashStart);
          } else {
            if (appendValue.length() == 0) {
              noteFieldValue.append(GlobalConstants.LINE_FEED);
            } else {
              if (noteFieldValue.length() > 0
                  && noteFieldValue.charAt(noteFieldValue.length() - 1) 
                    != GlobalConstants.LINE_FEED) {
                noteFieldValue.append(" ");
              }
              noteFieldValue.append(appendValue);
              noteFieldValue.append(GlobalConstants.LINE_FEED);
            } // end if we have a non-blank append value
          } // end if we have a non-header notes line
        } // end if processing a notes line
      } // end if processing a line with a non-blank value
    } // end if we have found a valid field identifier for this line    
  }
  
  /**
  Process a notes header line identified by two leading hyphens. 
  
  @param header    The content of the notes line. 
  @param dashStart The starting location for the two dashes within the header. 
  */
  private void processNotesHeader(String header, int dashStart) {

    eventNote = new EventNote();
    int i = dashStart;
    i = i + 2;
    headerPosition = NOTES_MIN;
    char c;
    headerWord = new StringBuilder();
    headerElement = new StringBuilder();
    while (i < header.length()) {
      c = header.charAt(i);
      if (Character.isWhitespace(c)) {
        processHeaderWord();
      }
      else
      if (c == ',') {
        processHeaderWord();
        processHeaderElement();
        headerPosition++;
      } else {
        headerWord.append(c);
      }
      i++;
    } // end while more header components to process
    processHeaderWord();
    processHeaderElement();
  } // end processNotesHeader method
  
  /**
   Process the next word after running into a space or a comma or end of line. 
   */
  private void processHeaderWord() {

    if (headerWord.toString().equalsIgnoreCase("from")) {
      processHeaderElement();
      headerPosition = NOTES_FROM;
    }
    else
    if (headerWord.toString().equalsIgnoreCase("on")
        || headerWord.toString().equalsIgnoreCase("of")) {
      processHeaderElement();
      headerPosition = NOTES_FOR;
    }
    else
    if (headerWord.toString().equalsIgnoreCase("via")) {
      processHeaderElement();
      headerPosition = NOTES_VIA;
    }
    else
    if (headerWord.length() > 0) {
      if (headerElement.length() > 0) {
        headerElement.append(' ');
      }
      headerElement.append(headerWord);
    }
    headerWord = new StringBuilder();
  }
  
  private void processHeaderElement() {

    if (headerElement.length() > 0) {
      switch(headerPosition) {
        case NOTES_FROM:
          eventNote.setNoteFrom(headerElement.toString());
          break;
        case NOTES_FOR:
          eventNote.setNoteFor(headerElement.toString());
          clubEventCalc.getStringDate().parse(headerElement.toString());
          eventNote.setNoteForYmd (clubEventCalc.getStringDate().getYMD());
          break;
        case NOTES_VIA:
          eventNote.setNoteVia(headerElement.toString());
          break;
      }
    }
    headerElement = new StringBuilder();
  }

  /**
     Returns the record definition for the reader.
    
     @return Record definition.
   */
  public RecordDefinition getRecDef () {
    return recDef;
  }
  
  /**
     Returns the sequential record number of the last record returned.
    
     @return Sequential record number of the last record returned via 
             nextRecordIn, where 1 identifies the first record.
   */
  public int getRecordNumber () {
    return recordNumber;
  }
  
  /**
     Returns the reader as some kind of string.
    
     @return Name of the directory.
   */
  public String toString () {
    return ("Directory Name is "
      + super.toString ());
  }

  /**
     Indicates whether there are more records to return.
    
     @return True if no more records to return.
   */
  public boolean isAtEnd() {
    return atEnd;
  }
  
  /**
     Closes the reader.
   */
  public void close() {
    reader.close();
    ensureLog();
    // logEvent.setSeverity (LogEvent.NORMAL);
    // logEvent.setMessage (inPath + " closed successfully.");
    // log.recordEvent (logEvent);
  }
  
  /**
     Ensures that a log is available, by allocating a new one if
     one has not already been supplied.
   */
  protected void ensureLog () {
    if (log == null) {
      setLog (new Logger (new LogOutput()));
    }
  }
    
  /**
     Sets a log to be used by the reader to record events.
    
     @param  log A logger object to use.
   */
  public void setLog (Logger log) {
    this.log = log;
  }
  
  /**
     Indicates whether all data records are to be logged.
    
     @param  dataLogging True if all data records are to be logged.
   */
  public void setDataLogging (boolean dataLogging) {
    this.dataLogging = dataLogging;
  }
  
  /**
     Sets the debug instance to the passed value.
    
     @param debug Debug instance. 
   */
  public void setDebug (Debug debug) {
    this.debug = debug;
  }
  
  /**
     Sets the maximum directory explosion depth. The default is 1, meaning
     that only one level is returned (no explosion). If this is changed, it
     should be done after the reader is constructed, but before it is opened
     for input.
    
     @param maxDepth Desired directory/sub-directory explosion depth.
   */
  public void setMaxDepth (int maxDepth) {

  }
  
  /**
     Retrieves the path to the original source data (if any).
    
     @return Path to the original source data (if any).
   */
  public String getDataParent () {
    if (inPath == null) {
      return System.getProperty (GlobalConstants.USER_DIR);
    } else {
      return inPath;
    }
  }
  
  /**
     Sets a file ID to be used to identify this reader in the log.
    
     @param  fileId An identifier for this reader.
   */
  public void setFileId (String fileId) {
    this.fileId = fileId;
    logData.setSourceId (fileId);
  }
  
} 