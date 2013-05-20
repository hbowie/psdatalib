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
    
    calcCategory (clubEvent);
    calcBlurbAsHtml (clubEvent);
    calcNotesAsHtml (clubEvent);
    calcFinanceProjection (clubEvent);
    calcOverUnder (clubEvent);
    calcYmd (clubEvent);
    calcSeq (clubEvent);
    calcShortDate (clubEvent);

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
