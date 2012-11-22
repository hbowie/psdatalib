package com.powersurgepub.psdatalib.psdata;

  import java.lang.Character;
  import java.lang.String;
  import java.lang.StringBuffer;
  
/**
   Formats a string into all lower case letters. Good for formatting
   e-mail and WWW addresses. <p>
   
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
      </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
  
   @version 00/06/13 - First created.
 */
public class LowerCaseRule
  extends DataFormatRule {
  
  /** ID for this DataFormatRule descendent. */
  public static final String LOWER_CASE_RULE_CLASS_NAME = "LowerCaseRule";
  
  /**
     Constructor doesn't do anything.
   */
  public LowerCaseRule () {
    super ();
  }
  
  /**
     Transforms a string by converting all the letters to lower case.
    
     @return All lower-case letters.
    
     @param inData String to be transformed. Note that this is not 
                   stored as part of the LowerCaseRule object.
   */
  public String transform (String inData) {
    return inData.toLowerCase();
  }
  
  /**
     Identify the class.
    
     @return String identifying the class name.
   */
  public String toString () {
    return LOWER_CASE_RULE_CLASS_NAME;
  }

}