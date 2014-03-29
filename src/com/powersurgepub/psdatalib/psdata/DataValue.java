/*
 * Copyright 1999 - 2014 Herb Bowie
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

package com.powersurgepub.psdatalib.psdata;

/**
 A data value that can be converted from and to a string. 

 @author Herb Bowie
 */
public interface DataValue {
  
  /**
   Set the value from a String. 
  
   @param value The value as a string. 
  */
  public void set(String value);
  
  /** 
   Converts the value to a String.
  
   @return the value as a string. 
  */
  public String toString();
  
  /**
     Compares this data value to another and indicates which is greater.
    
     @return Zero if the fields are equal, negative if this field is less than value2,
             or positive if this field is greater than value2.
    
     @param  value2 Another data value to be compared to this one.
   */
  public int compareTo(DataValue value2);
  
}
