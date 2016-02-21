/*
 * Copyright 1999 - 2016 Herb Bowie
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

/**
 A data value interpreted as a sequence number. 

 @author Herb Bowie
 */
public class DataValueSeq

    implements
        DataValue {
  
  private String          value = null;
  
  // Pad the value on the left for compare purposes
  private StringBuilder   seq   = new StringBuilder();
  
  public DataValueSeq() {
    
  }
  
  /**
   Set the value from a String. 
  
   @param value The value as a string. 
  */
  public void set(String value) {
    this.value = value;
    seq = new StringBuilder (value);
    int i = 0;
    boolean numeric = true;
    boolean periodFound = false;
    int leftCount = 0;
    char c = ' ';
    while (i < seq.length()) {
      c = seq.charAt(i);
      if (c == '$' || c == ',' || Character.isWhitespace(c)) {
        // ignore non-significant characters
        seq.deleteCharAt(i);
      }
      else
      if (c == '.') {
        periodFound = true;
        i++;
      }
      else
      if (Character.isDigit(c)) {
        if (! periodFound) {
          leftCount++;
        }
        i++;
      }
      else {
        numeric = false;
        if (! periodFound) {
          leftCount++;
        }
        i++;
      } // end logic based on char type
    } // end of seq string
    char pad = ' ';
    if (numeric) {
      pad = '0';
    }
    while (leftCount < 8) {
      seq.insert(0, pad);
      leftCount++;
    }
  } // end of set method
  
  public int length() {
    if (hasData()) {
      return value.length();
    } else {
      return 0;
    }
  }
  
  /**
   Does this value have any data stored in it? 
  
   @return True if data, false if empty. 
  */
  public boolean hasData() {
    return (value != null && value.length() > 0);
  }
  
  /** 
   Converts the value to a String.
  
   @return the value as a string. 
  */
  public String toString() {
    if (value == null) {
      return "";
    } else {
      return value;
    }
  }
  
  /**
   Returns a padded string, for the purposes of comparison. 
  
   @return Padded on the left. 
  */
  public String toPaddedString() {
    return seq.toString();
  }
  
  /**
     Compares this data value to another and indicates which is greater.
    
     @return Zero if the fields are equal, negative if this field is less than value2,
             or positive if this field is greater than value2.
    
     @param  value2 Another data value to be compared to this one.
   */
  public int compareTo(DataValue value2) {
    
    if (value2 instanceof DataValueSeq) {
      DataValueSeq seq2 = (DataValueSeq)value2;
      return toPaddedString().compareTo(seq2.toPaddedString());
    } else {
      return toString().compareTo(value2.toString());
    }
  }

}
