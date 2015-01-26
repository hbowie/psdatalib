/*
 * Copyright 2015 - 2015 Herb Bowie
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
 A value that can store a rating. A rating is assumed to be 
 a decimal integer greater than or equal to zero.

 @author Herb Bowie
 */
public class Rating 
      implements
        DataValue{
  
  public static final int DEFAULT_RATING = 3;
  
  private int rating = DEFAULT_RATING;
  
  public Rating() {
    
  }
  
  public Rating(String value) {
    set(value);
  }
  
  /**
   Set the value from a String. 
  
   @param value The value as a string. 
  */
  public void set(String value) {
    try {
      rating = Integer.parseInt(value);
    } catch (NumberFormatException e) {
      // No action necessary -- leave current rating unchanged. 
    }
  }
  
  public void set(int value) {
    rating = value;
  }
  
  /**
   Does this value have any data stored in it? 
  
   @return True if data, false if empty. 
  */
  public boolean hasData() {
    return (rating >= 0);
  }
  
  public int get() {
    return rating;
  }
  
  /** 
   Converts the value to a String.
  
   @return the value as a string. 
  */
  public String toString() {
    return String.valueOf(rating);
  }
  
  /**
     Compares this data value to another and indicates which is greater.
    
     @return Zero if the fields are equal, negative if this field is less than value2,
             or positive if this field is greater than value2.
    
     @param  value2 Another data value to be compared to this one.
   */
  public int compareTo(DataValue value2) {
    if (value2 instanceof Rating) {
      Rating rating2 = (Rating)value2;
      if (rating < rating2.get()) {
        return -1;
      } 
      else
      if (rating > rating2.get()) {
        return 1;
      } else {
        return 0;
      }
    } else {
      return (toString().compareTo(value2.toString()));
    }
  }

}
