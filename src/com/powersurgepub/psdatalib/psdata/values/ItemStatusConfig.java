/*
 * Copyright 1999 - 2015 Herb Bowie
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

package com.powersurgepub.psdatalib.psdata.values;

  import com.powersurgepub.psutils.*;
  import javax.swing.*;

/**
 Definition of an Item State field, as configured for a particular use case. 

 @author Herb Bowie
 */
public class ItemStatusConfig {
  
  public final static int OPEN      = 0;
  public final static int SUGGESTED = 0;
  public final static int IN_WORK   = 4;
  public final static int PENDING   = 5;
  public final static int COMPLETED = 6;
  public final static int CANCELED  = 8;
  public final static int CLOSED    = 9;
  
  private static ItemStatusConfig sharedConfig = null;
  
  private ItemStatusValue[] values = new ItemStatusValue[10];
  
  private int statusLow = 0;
  private int statusHigh = 9;
  
  /**
   An optional getter for a single, shared instantiation of this class.
  
   @return A single, shared instantiation of this class. 
  */
  public static ItemStatusConfig getShared() {
    if (sharedConfig == null) {
      sharedConfig = new ItemStatusConfig();
    }
    return sharedConfig;
  }
  
  public ItemStatusConfig() {
    // Populate the values array with the standard, default configuration. 
    values[0] = new ItemStatusValue("Suggested");
    values[1] = new ItemStatusValue("Proposed");
    values[2] = new ItemStatusValue("Approved");
    values[3] = new ItemStatusValue("Planned");
    values[4] = new ItemStatusValue("Active");
    values[5] = new ItemStatusValue("Held");
    values[6] = new ItemStatusValue("Completed");
    values[7] = new ItemStatusValue("Pending Recurs");
    values[8] = new ItemStatusValue("Canceled");
    values[9] = new ItemStatusValue("Closed");
  }
  
  /**
   Adjust the status values with the passed string. 
  
   @param statusValues A string consisting of one or more integer + value pairs.
  */
  public void set(String statusValues) {
    int i = 0;
    char c = ' ';
    int num = -1;
    String val = "";
    // Process all Integer + String pairs
    while (i < statusValues.length()) {
      // Process next Integer + String pair
      int startNum = i;
      int endNum = i;
      int startVal = i;
      int endVal = i;
      num = -1;
      val = "";
      c = statusValues.charAt(i);
      // Look for start of integer
      while (i < statusValues.length() && (! Character.isDigit(c))) {
        i++;
        if (i < statusValues.length()) {
          c = statusValues.charAt(i);
        } else {
          c = ' ';
        } 
      }
      startNum = i;
      
      // Now look for end of integer
      while (i < statusValues.length() && Character.isDigit(c)) {
        i++;
        if (i < statusValues.length()) {
          c = statusValues.charAt(i);
        } else {
          c = ' ';
        }
      }
      endNum = i;
      
      // Now look for start of value
      while (i < statusValues.length() && (! Character.isLetter(c))) {
        i++;
        if (i < statusValues.length()) {
          c = statusValues.charAt(i);
        } else {
          c = ' ';
        }
      }
      startVal = i;
      
      // Now look for end of value
      while (i < statusValues.length() && 
          (Character.isLetter(c) || Character.isWhitespace(c))) {
        i++;
        if (i < statusValues.length()) {
          c = statusValues.charAt(i);
        } else {
          c = ' ';
        }
      }
      endVal = i;
      
      // If everything looks good, then update our list of status values
      if (endNum > startNum && endVal > startVal) {
        val = statusValues.substring(startVal, endVal);
        try {
          num = Integer.parseInt(statusValues.substring(startNum, endNum));
          if (num < 0 || num > 9) {
            System.out.println(
                "ItemStatusConfig.set " +
                statusValues.substring(startNum, endNum) + 
                " is not in the range 0 - 9");
          } else {
            values[num] = new ItemStatusValue(val);
          }
        } catch (NumberFormatException ex) {
          System.out.println (
              "ItemStatusConfig.set " + 
              statusValues.substring(startNum, endNum) + 
              " is not a valid integer");
        }
      } // End if we have both a number and a string
    } // End while processing status values
  } // End of set methiod
  
  /**
   Look for an Item Status Value that matches the passed label, using only
   the first two letters to match on, and ignoring case. 
  
   @param label The label we're looking for. 
  
   @return The position of the matching entry, if one was found; otherwise -1.
  */
  public int lookup(String label) {
    int index = statusLow;
    boolean matched = false;
    char firstChar = ' ';
    if (label.length() > 0) {
      firstChar = label.toLowerCase().charAt(0);
    }
    if (label.length() > 1) {
      String lookFor = label.substring(0, 2).toLowerCase();
      while (index <= statusHigh && (! matched)) {
        if (values[index].isAvailable() 
            && lookFor.equals(values[index].getFirstTwo())) {
          matched = true;
        } else {
          index++;
        }
      } // End of search for matching status value
    } // end if we have something to match
    if (matched) {
      // ok
    } 
    else
    if (firstChar == 'o') {
      index = OPEN;
    }
    else
    if (firstChar == 'i') {
      index = IN_WORK;
    }
    else
    if (firstChar == 'p') {
      index = PENDING;
    }
    else
    if (firstChar == 'c') {
      index = CLOSED;
    }
    
    if (statusValid(index)) {
      return index;
    } else {
      return -1;
    }
  }
  
  public ComboBoxModel getComboBoxModel() {
    DefaultComboBoxModel cbm = new DefaultComboBoxModel();
    return populateComboBoxModel(cbm);
  }
  
  public ComboBoxModel populateComboBoxModel(MutableComboBoxModel cbm) {
    for (int i = statusLow; i <= statusHigh; i++) {
      if (statusValid(i)) {
        ItemStatus status = new ItemStatus(this, i);
        cbm.addElement(status.toString());
      }
    }
    return cbm;
  }
  
  public void populateComboBox(JComboBox box) {
    box.removeAllItems();
    for (int i = statusLow; i <= statusHigh; i++) {
      if (statusValid(i)) {
        ItemStatus status = new ItemStatus(this, i);
        box.addItem(status.toString());
      }
    }
  }
  
  /**
   Return the corresponding label for the passed status value. 
  
   @param status The status value. 
  
   @return The corresponding label. 
  */
  public String getLabel(int status) {
    if (statusValid(status)) {
      return values[status].getLabel();
    } else {
      return "";
    }
  }
  
  /**
   Is the passed status valid?
  
   @param status The status to be tested. 
  
   @return True if OK; false if bad. 
  */
  public boolean statusValid(int status) {
    if (status >= statusLow
        && status <= statusHigh
        && values[status].isAvailable()) {
      return true;
    } else {
      return false;
    }
  }
  
  public int getStatusLow() {
    return statusLow;
  }
  
  public int getStatusHigh() {
    return statusHigh;
  }
  
  public String toString() {
    StringBuilder str = new StringBuilder();
    for (int i = statusLow; i <= statusHigh; i++) {
      str.append(String.valueOf(i));
      str.append(" - ");
      str.append(values[i].getLabel());
      str.append(" - Available? ");
      str.append(String.valueOf(values[i].isAvailable()));
      str.append(GlobalConstants.LINE_FEED);
    }
    return str.toString();
  }
  
  /**
   Get the highest status value that is available. 
  
   @return The integer, followed by a dash, followed by the label. 
  */
  public String getClosedString() {
    int closed = statusHigh;
    while (closed > 0 && (! values[closed].isAvailable())) {
      closed--;
    }
    StringBuilder str = new StringBuilder();
    String chars = String.valueOf(closed).trim();
    if (chars.length() > 0) {
      str.append (chars.charAt(0));
    } else {
      str.append('0');
    }
    str.append(" - ");
    str.append(values[closed].toString());
    return str.toString();
  }

}
