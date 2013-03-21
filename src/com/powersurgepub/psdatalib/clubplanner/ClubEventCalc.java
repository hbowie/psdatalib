/*
 * Copyright 1999 - 2013 Herb Bowie
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
  
  private ResourceList statusResource;
   
  private ArrayList statusList = new ArrayList();
  
  private ResourceList typeResource;
  
  private ArrayList typeList = new ArrayList();
  
  private    StringDate         strDate = new StringDate();
  private    PegDownProcessor   pegDown;
  
  private    String             status = "";
  private    String             type = "";
  private    boolean            statusFromFolder = false;
  private    boolean            typeFromFolder = false;
  private    String             opYearFolder = "";
  private    String             year = "";
  private    boolean            opYearFound = false;
  
  public ClubEventCalc () {
    
    int pegDownOptions = 0;
    pegDownOptions = pegDownOptions + Extensions.SMARTYPANTS;
    pegDown = new PegDownProcessor(pegDownOptions);
    
    statusResource = new ResourceList(getClass(), "status");
    statusResource.load(statusList);
    
    typeResource = new ResourceList (getClass(), "type");
    typeResource.load(typeList);
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

    // Get the type or status from the deepest folder
    typeFromFolder = false;
    type = "";
    statusFromFolder = false;
    status = "";
    if (folderDepth > 0) {
      String typeOrStatus = inPathFileName.getFolder(folderDepth);
      if (typeList.indexOf(typeOrStatus) >= 0) {
        type = typeOrStatus;
        typeFromFolder = true;
      }
      else
      if (statusList.indexOf(typeOrStatus) >= 0) {
        status = typeOrStatus;
        setFuture(status);
        statusFromFolder = true;
      }
    }

    // Check higher folders to see if one of them identifies the club
    // operating year. Note that the year may be a pair of years, to
    // indicate an operating year starting in July and ending in June. 
    opYearFolder = "";
    if (typeFromFolder || statusFromFolder) {
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
  
  public boolean ifTypeFromFolder() {
    return typeFromFolder;
  }
  /**
   If the type was found in the file path, then return it. 
  
   @return Type as found in file location, or null, if no type was found. 
  */
  public String getTypeFromFolder() {
    if (typeFromFolder) {
      return type;
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
    
    calcType (clubEvent);
    calcBlurbAsHtml (clubEvent);
    calcNotesAsHtml (clubEvent);
    calcFinanceProjection (clubEvent);
    calcOverUnder (clubEvent);
    calcYmd (clubEvent);
    calcSeq (clubEvent);
    calcShortDate (clubEvent);

  }
  
  public void calcType (ClubEvent clubEvent) {

    StringBuilder type = new StringBuilder (clubEvent.getTypeAsString());
    int pipeIndex = type.indexOf("|");
    if (pipeIndex >= 0) {
      pipeIndex++;
      while (pipeIndex < type.length()
          && Character.isWhitespace(type.charAt(pipeIndex))) {
        pipeIndex++;
      }
      type.delete(0, pipeIndex);
    }
    clubEvent.setType (type.toString());
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
    String typeLower = clubEvent.getType().toString().toLowerCase();
    if (typeLower.indexOf("open meeting") >= 0) {
      clubEvent.setSeq("1");
    }
    else
    if (typeLower.indexOf("finance") >= 0) {
      clubEvent.setSeq("2");
    }
    else
    if (typeLower.indexOf("communication") >= 0) {
      clubEvent.setSeq("8");
    }
    else
    if (typeLower.indexOf("close meeting") >= 0) {
      clubEvent.setSeq("9");
    } 
    else
    if (clubEvent.hasWhen() && strDate.isInThePast()) {
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
  
  public void calcAll (EventNote eventNote) {
    calcNoteAsHtml (eventNote);
  }
  
  public void calcNoteAsHtml (EventNote eventNote) {
    if (eventNote.getNote() != null
        && eventNote.getNote().length() > 0) {
      eventNote.setNoteAsHtml(pegDown.markdownToHtml(eventNote.getNote()));
    }
  }

}
