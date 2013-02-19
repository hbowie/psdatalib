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

package com.powersurgepub.psdatalib.psdata;

  import com.powersurgepub.psutils.StringUtils;
  import java.lang.Character;
  import java.lang.String;
  import java.lang.StringBuffer;
  
/**
   Formats a string by ensuring that the first letter of each word
   is capitalized, but other letters are lower-case. Good for formatting names
   and addresses, for example. <p>
   
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
      </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
  
   @version 00/04/21 - Modified to be consistent with "The Elements of Java Style".
 */
public class InitialCapsRule
  extends DataFormatRule {
  
  /** ID for this DataFormatRule descendent. */
  public static final String INITIAL_CAPS_RULE_CLASS_NAME = "InitialCapsRule";
  
  /**
     Constructor doesn't do anything.
   */
  public InitialCapsRule () {
    super ();
  }
  
  /**
     Transforms a string by converting initial letters of each word to upper-case,
     and other letters to lower-case.
    
     @return String with first letter of each word in upper-case, and
                    other letters in lower-case.
    
     @param inData String to be transformed. Note that this is not 
                   stored as part of this object.
   */
  public String transform (String inData) {
    return StringUtils.initialCaps (inData);
  }
    
  public String toString () {
    return INITIAL_CAPS_RULE_CLASS_NAME;
  }

}