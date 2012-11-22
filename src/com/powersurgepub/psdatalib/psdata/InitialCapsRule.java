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