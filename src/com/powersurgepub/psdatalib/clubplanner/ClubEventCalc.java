/*
 * Copyright 2012 - 2013 Herb Bowie
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

package com.powersurgepub.psdatalib.clubplanner;

  import com.powersurgepub.psdatalib.pstextio.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.math.*;
  import java.util.*;
  import org.pegdown.*;

/**
 Performs various calculations and transformations on club event data. 

 @author Herb Bowie
 */
public class ClubEventCalc {
  
  public static final String BLANK = "Blank";
  
  private ResourceList statusResource;
   
  private ArrayList statusList = new ArrayList();
  
  private ResourceList categoryResource;
  
  private ArrayList categoryList = new ArrayList();
  
  private    StringDate         strDate = new StringDate();
  private    PegDownProcessor   pegDown;
  
  private    String             status = "";
  private    String             category = "";
  private    boolean            statusFromFolder = false;
  private    boolean            categoryFromFolder = false;
  private    String             opYearFolder = "";
  private    String             year = "";
  private    boolean            opYearFound = false;
  
  private    boolean            blockComment = false;
  
  private    StringBuilder    headerWord;
  
  private    int              headerPosition;
  private static final int    NOTES_FROM = 1;
  private static final int    NOTES_FOR  = 2;
  private static final int    NOTES_VIA  = 3;
  private static final int    NOTES_MIN  = NOTES_FROM;
  private static final int    NOTES_MAX  = NOTES_VIA;
  
  private    StringBuilder    headerElement;
  
  public ClubEventCalc () {
    
    int pegDownOptions = 0;
    pegDownOptions = pegDownOptions + Extensions.SMARTYPANTS;
    pegDown = new PegDownProcessor(pegDownOptions);
    
    statusResource = new ResourceList(getClass(), "status");
    statusResource.load(statusList);
    
    categoryResource = new ResourceList (getClass(), "category");
    categoryResource.load(categoryList);
  }
  
  public void setStringDate (StringDate strDate) {
    this.strDate = strDate;
  }
  
  public StringDate getStringDate() {
    return strDate;
  }
  
  /**
   Gleans information from the club event's enclosing folders. 
  
   @param file The file containing the club event. 
  
   @return True if an operating year was found in one of the folders, 
           false if not. 
  */
  public boolean setFileName (File file) {
    
    FileName inPathFileName = new FileName (file);
    
    // Get information from the names of the folders containing this file
    int folderDepth = inPathFileName.getNumberOfFolders();

    // Get the category or status from the deepest folder
    categoryFromFolder = false;
    category = "";
    statusFromFolder = false;
    status = "";
    if (folderDepth > 0) {
      String categoryOrStatus = inPathFileName.getFolder(folderDepth);
      if (categoryOrStatus.equalsIgnoreCase(BLANK)) {
        categoryOrStatus = "";
      }
      if (categoryList.indexOf(categoryOrStatus) >= 0) {
        category = categoryOrStatus;
        categoryFromFolder = true;
      }
      else
      if (statusList.indexOf(categoryOrStatus) >= 0) {
        status = categoryOrStatus;
        setFuture(status);
        statusFromFolder = true;
      }
    }

    // Check higher folders to see if one of them identifies the club
    // operating year. Note that the year may be a pair of years, to
    // indicate an operating year starting in July and ending in June. 
    opYearFolder = "";
    if (categoryFromFolder || statusFromFolder) {
      folderDepth--;
    }
    opYearFound = false;
    while (folderDepth > 0 && (! opYearFound)) {
      String folder = inPathFileName.getFolder(folderDepth);
      opYearFound = parseOpYear(folder);
      if (opYearFound) {
        opYearFolder = folder;
      }
      folderDepth--;
    } // end while looking for a folder identifying the club year
    if (opYearFound) {
      year = getStringDate().getOpYear();
    }
    
    return opYearFound;
  }
  
  public boolean ifStatusFromFolder() {
    return statusFromFolder;
  }
  
  /**
   If the status was found in the file path, then return it.
  
   @return Status as found in file location, or null, if no status was found. 
   */
  public String getStatusFromFolder() {
    if (statusFromFolder) {
      return status;
    } else {
      return null;
    }
  }
  
  public boolean ifCategoryFromFolder() {
    return categoryFromFolder;
  }
  /**
   If the category was found in the file path, then return it. 
  
   @return Category as found in file location, or null, if no category was found. 
  */
  public String getCategoryFromFolder() {
    if (categoryFromFolder) {
      return category;
    } else {
      return null;
    }
  }
  
  public boolean ifOpYearFromFolder() {
    return opYearFound;
  }
  
  /**
   Return the folder name containing the Operating Year. 
  
   @return Blanks if the operating year was not found, otherwise the name of 
           the folder in which the operating year was found. 
  */
  public String getOpYearFolder() {
    return opYearFolder;
  }
  
  /**
   If the operating year was found in the file path, then return it. 
  
   @return The Operating Year as found in the file path, or null, if no 
           year was found. 
  */
  public String getOpYearFromFolder() {
    if (opYearFound) {
      return year;
    } else {
      return null;
    }
  }
  
  /**
   If the status of an item is "Future", then adjust the year to be a future 
   year. 
  
   @param futureStr The status of an item. If it says "Future", then 
                    adjust the year to be a future year. 
  */
  public void setFuture(String futureStr) {
    strDate.setFuture(futureStr);
  }
  
  /**
   Parse a field containing an operating year.
  
   @param years This could be a single year, or a range containing two
                consecutive years. If a range, months of July or later
                will be assumed to belong to the earlier year, with months
                of June or earlier will be assumed to belong to the 
                later year. 
  
   @return True if an operating year was found in the passed String.
  */
  public boolean parseOpYear (String years) {
    return strDate.parseOpYear(years);
  }
  
  /**
   Calculate fields that are not stored in the text files, but derived from
   other fields. 
   */
  public void calcAll (ClubEvent clubEvent) {
    
    calcItemType (clubEvent);
    calcCategory (clubEvent);
    calcBlurbAsHtml (clubEvent);
    calcNotesAsHtml (clubEvent);
    calcFinanceProjection (clubEvent);
    calcOverUnder (clubEvent);
    calcYmd (clubEvent);
    calcSeq (clubEvent);
    calcShortDate (clubEvent);
    calcEventNotes (clubEvent);
  }
  
  public void calcItemType (ClubEvent clubEvent) {
    String itemType = clubEvent.getItemType();
    if (itemType == null
        || itemType.length() == 0) {
      String whatLower = "";
      if (clubEvent.hasWhat()) {
        whatLower = clubEvent.getWhat().toLowerCase();
      }
      String statusLower = clubEvent.getStatusAsString().toLowerCase();
      if (whatLower.contains("budget")
          || statusLower.contains("budget")) {
        clubEvent.setItemType("Budget");
      } else {
        clubEvent.setItemType("Member Event");
      }
    }
  }
  
  public void calcCategory (ClubEvent clubEvent) {

    StringBuilder category = new StringBuilder (clubEvent.getCategoryAsString());
    int pipeIndex = category.indexOf("|");
    if (pipeIndex >= 0) {
      pipeIndex++;
      while (pipeIndex < category.length()
          && Character.isWhitespace(category.charAt(pipeIndex))) {
        pipeIndex++;
      }
      category.delete(0, pipeIndex);
    }
    clubEvent.setCategory (category.toString());
  }
  
  public void calcBlurbAsHtml (ClubEvent clubEvent) {

    if (clubEvent.getBlurb() != null
        && clubEvent.getBlurb().length() > 0) {
      clubEvent.setBlurbAsHtml(pegDown.markdownToHtml(clubEvent.getBlurb()));
    }
  }
  
  public void calcNotesAsHtml (ClubEvent clubEvent) {
    if (clubEvent.getNotes() != null
        && clubEvent.getNotes().length() > 0) {
      clubEvent.setNotesAsHtml(pegDown.markdownToHtml(clubEvent.getNotes()));
    }
  }
  
  public void calcFinanceProjection (ClubEvent clubEvent) {

    BigDecimal financeProjection = BigDecimal.ZERO;

    boolean anyFinances = false;
    if (clubEvent.getActualIncome() != null
        && clubEvent.getActualIncome().length() > 0) {
      financeProjection = clubEvent.getActualIncomeAsBigDecimal();
      anyFinances = true;
    }
    else
    if (clubEvent.getPlannedIncome() != null
        && clubEvent.getPlannedIncome().length() > 0) {
      financeProjection = clubEvent.getPlannedIncomeAsBigDecimal();
      anyFinances = true;
    }

    if (clubEvent.getActualExpense() != null
        && clubEvent.getActualExpense().length() > 0) {
      financeProjection = financeProjection.subtract
          (clubEvent.getActualExpenseAsBigDecimal());
      anyFinances = true;
    }
    else
    if (clubEvent.getPlannedExpense() != null
        && clubEvent.getPlannedExpense().length() > 0) {
      financeProjection = financeProjection.subtract
          (clubEvent.getPlannedExpenseAsBigDecimal());
      anyFinances = true;
    }

    if (anyFinances) {
      clubEvent.setFinanceProjection(financeProjection.toPlainString());
    } else {
      clubEvent.setFinanceProjection("");
    }
  }
  
  public void calcOverUnder (ClubEvent clubEvent) {

    BigDecimal overUnder = BigDecimal.ZERO;
    
    boolean anyActuals = false;
    if (clubEvent.getActualIncome() != null
        && clubEvent.getActualIncome().length() > 0) {
      overUnder = clubEvent.getActualIncomeAsBigDecimal();
      anyActuals = true;
    }

    if (clubEvent.getPlannedIncome() != null
        && clubEvent.getPlannedIncome().length() > 0) {
      overUnder = overUnder.subtract
          (clubEvent.getPlannedIncomeAsBigDecimal());
    }

    if (clubEvent.getActualExpense() != null
        && clubEvent.getActualExpense().length() > 0) {
      overUnder = overUnder.subtract
          (clubEvent.getActualExpenseAsBigDecimal());
      anyActuals = true;
    }

    if (clubEvent.getPlannedExpense() != null
        && clubEvent.getPlannedExpense().length() > 0) {
      overUnder = overUnder.add
          (clubEvent.getPlannedExpenseAsBigDecimal());
    }

    if (anyActuals) {
      clubEvent.setOverUnder (overUnder.toPlainString());
    }
  }
  
  public void calcYmd (ClubEvent clubEvent) {
    
    // Now get the date in a predictable year-month-date format
    if (clubEvent.hasWhen()) {
      strDate.setFuture(clubEvent.getStatusAsString());
      strDate.parse(clubEvent.getWhen());
      clubEvent.setYmd(strDate.getYMD());
    }
  }
  
  public void calcSeq (ClubEvent clubEvent) {
    String categoryLower = clubEvent.getCategory().toString().toLowerCase();
    String statusLower = clubEvent.getStatusAsString().toLowerCase();
    if (categoryLower.indexOf("open meeting") >= 0) {
      clubEvent.setSeq("1");
    }
    else
    if (categoryLower.indexOf("finance") >= 0) {
      clubEvent.setSeq("2");
    }
    else
    if (categoryLower.indexOf("communication") >= 0) {
      clubEvent.setSeq("8");
    }
    else
    if (categoryLower.indexOf("close meeting") >= 0) {
      clubEvent.setSeq("9");
    } 
    else
    if (clubEvent.hasWhen() 
        && strDate.isInThePast()
        && statusLower.indexOf("future") < 0) {
      clubEvent.setSeq("4");
    } else {
      clubEvent.setSeq("5");
    }
  }
  
  public void calcShortDate (ClubEvent clubEvent) {
    
    // Now set a short, human readable date
    if (clubEvent.hasWhen()) {
      strDate.setFuture(clubEvent.getStatusAsString());
      strDate.parse(clubEvent.getWhen());
      clubEvent.setShortDate(strDate.getShort());
    }
    
  }
  
  
  /**
   Build all of the event's event objects from the notes text. 
  
   @param clubEvent 
  */
  public void calcEventNotes (ClubEvent clubEvent) {
    TextLineReader reader = new StringLineReader (clubEvent.getNotes());
    clubEvent.newEventNoteList();
    EventNote eventNote = new EventNote();
    StringBuilder noteFieldValue = new StringBuilder();
    reader.open();
    while (reader != null
        && reader.isOK()
        && (! reader.isAtEnd())) {
      readAndProcessNextLine(reader, clubEvent, eventNote, noteFieldValue);
    } // end while reader has more lines
    setLastNoteFieldValue(clubEvent, eventNote, noteFieldValue);
    reader.close();
  }
  
  /**
    Read the next line and see what we've got. 
   */
  private void readAndProcessNextLine (
      TextLineReader reader,
      ClubEvent clubEvent,
      EventNote eventNote,
      StringBuilder noteFieldValue) {
    
    String line = reader.readLine();
    int lineStart = 0;
    int lineEnd = line.length();

    // Check for comments
    int blockCommentStart = -1;
    int blockCommentEnd = -1;
    int lineCommentStart = -1;
    
    boolean notesHeaderLine = false;

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
    
    // Check for markdown heading
    int headingLevel = 0;
    while ((lineStart + headingLevel) < lineEnd
        && line.charAt(lineStart + headingLevel) == '#') {
      headingLevel++;
    }
    if (headingLevel > 0
        && (lineStart + headingLevel) < lineEnd
        && line.charAt(lineStart + headingLevel) == ' ') {
      // Looks like a markdown heading
      lineEnd = 0;
    } else {
      headingLevel = 0;
    }
    
    String appendValue = line.substring(lineStart, lineEnd).trim();
    
    int notesHeaderDashStart = 0;
    while (notesHeaderDashStart < appendValue.length() 
        && Character.isWhitespace(appendValue.charAt(notesHeaderDashStart))) {
      notesHeaderDashStart++;
    }
    int k = notesHeaderDashStart + 1;
    if (notesHeaderDashStart < appendValue.length() 
        && appendValue.charAt(notesHeaderDashStart) == '-'
        && k < appendValue.length() && appendValue.charAt(k) == '-') {
      notesHeaderLine = true;
      setLastNoteFieldValue(clubEvent, eventNote, noteFieldValue);        
    } // end if notes line starts with two hyphens

    if (notesHeaderLine) {
      processNotesHeader(eventNote, appendValue, notesHeaderDashStart);
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
  
  } // end method readAndProcessNextLine
  
  /**
  Process a notes header line identified by two leading hyphens. 
  
  @param header    The content of the notes line. 
  @param dashStart The starting location for the two dashes within the header. 
  */
  private void processNotesHeader(
      EventNote eventNote, 
      String header, 
      int dashStart) {

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
        processHeaderWord(eventNote);
      }
      else
      if (c == ',') {
        processHeaderWord(eventNote);
        processHeaderElement(eventNote);
        headerPosition++;
      } else {
        headerWord.append(c);
      }
      i++;
    } // end while more header components to process
    processHeaderWord(eventNote);
    processHeaderElement(eventNote);
  } // end processNotesHeader method
  
  /**
   Process the next word after running into a space or a comma or end of line. 
   */
  private void processHeaderWord(EventNote eventNote) {

    if (headerWord.toString().equalsIgnoreCase("from")) {
      processHeaderElement(eventNote);
      headerPosition = NOTES_FROM;
    }
    else
    if (headerWord.toString().equalsIgnoreCase("on")
        || headerWord.toString().equalsIgnoreCase("of")) {
      processHeaderElement(eventNote);
      headerPosition = NOTES_FOR;
    }
    else
    if (headerWord.toString().equalsIgnoreCase("via")) {
      processHeaderElement(eventNote);
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
  
  private void processHeaderElement(EventNote eventNote) {

    if (headerElement.length() > 0) {
      switch(headerPosition) {
        case NOTES_FROM:
          eventNote.setNoteFrom(headerElement.toString());
          break;
        case NOTES_FOR:
          eventNote.setNoteFor(headerElement.toString());
          getStringDate().parse(headerElement.toString());
          eventNote.setNoteForYmd (getStringDate().getYMD());
          break;
        case NOTES_VIA:
          eventNote.setNoteVia(headerElement.toString());
          break;
      }
    }
    headerElement = new StringBuilder();
  }
  
  /**
   At the start of a new note header, or at the end of a file, take the 
   accumulated note text found, apply it to the last note, and add the 
   note to the event. 
  */
  private void setLastNoteFieldValue(
      ClubEvent clubEvent, 
      EventNote eventNote, 
      StringBuilder noteFieldValue) {
    if (clubEvent != null
        && eventNote != null
        && noteFieldValue.length() > 0) {
      eventNote.setNote(noteFieldValue.toString());
      calcAll(eventNote);
      clubEvent.addEventNote(eventNote);
      noteFieldValue = new StringBuilder();
    }
  }
  
  public void calcAll (EventNote eventNote) {
    calcNoteAsHtml (eventNote);
  }
  
  public void calcNoteAsHtml (EventNote eventNote) {
    if (eventNote.getNote() != null
        && eventNote.getNote().length() > 0) {
      eventNote.setNoteAsHtml(pegDown.markdownToHtml(eventNote.getNote()));
    }
  }
  
  public String calcNoteHeaderLine (EventNote eventNote) {
    StringBuilder header = new StringBuilder();
    header.append ("-- ");
    if (eventNote.hasNoteFrom() && eventNote.getNoteFrom().length() > 0) {
      header.append("from " + eventNote.getNoteFrom());
    }
    if (eventNote.hasNoteFor() && eventNote.getNoteFor().length() > 0) {
      if (header.length() > 3) {
        header.append(", ");
      }
      header.append("on " + eventNote.getNoteFor());
    }
    if (eventNote.hasNoteVia() && eventNote.getNoteVia().length() > 0) {
      if (header.length() > 3) {
        header.append(", ");
      }
      header.append("via " + eventNote.getNoteVia());
    }
    return header.toString();
  }

}
