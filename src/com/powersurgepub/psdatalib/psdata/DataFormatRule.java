package com.powersurgepub.psdatalib.psdata;

/**
   A rule for formatting one or more strings into a standard format. 
   This class is meant to be extended by others. <p>
  
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
      00/04/21 - Modified to be consistent with "The Elements of Java Style".</ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
   @version 00/05/22 - Added code to handle CountryRule.
 */
public class DataFormatRule {

  /** ID for this DataFormatRule class. */
  public static final String DATA_FORMAT_RULE_CLASS_NAME = "DataFormatRule";
  
  /**
     Constructor doesn't do anything.
   */
  public DataFormatRule () {
    super ();
  }
  
  /**
     Return the string as input.
   */
  public String transform (String inData) {
    return inData;
  }
  
  /**
     Identify the class.
    
     @return String identifying the class name.
   */
  public String toString () {
    return DATA_FORMAT_RULE_CLASS_NAME;
  }
  
  /**
     Constructs and returns any one of a number of DataFormatRule objects,
     depending on the class name passed as a String value.
    
     @return New DataFormatRule object.
    
     @param className Name of the desired class.
    
     @throws IllegalArgumentException If the className is unknown.
   */
  public static DataFormatRule constructRule (String className) 
      throws IllegalArgumentException {
    if (className.equals (DataFormatRule.DATA_FORMAT_RULE_CLASS_NAME)
        || className.equals ("")) {
      return new DataFormatRule ();
    } else
    if (className.equals (AllCapsRule.ALL_CAPS_RULE_CLASS_NAME)) {
      return new AllCapsRule ();
    } else
    if (className.equals (LowerCaseRule.LOWER_CASE_RULE_CLASS_NAME)) {
      return new LowerCaseRule ();
    } else
    if (className.equals (InitialCapsRule.INITIAL_CAPS_RULE_CLASS_NAME)) {
      return new InitialCapsRule ();
    } else
    if (className.equals (CountryRule.COUNTRY_RULE_CLASS_NAME)) {
      return new CountryRule ();
    } else
    if (className.equals (USPhoneRule.US_PHONE_RULE_CLASS_NAME)) {
      return new USPhoneRule ();
    } else
    if (className.equals (USMobileRule.US_MOBILE_RULE_CLASS_NAME)) {
      return new USMobileRule (new USPhoneRule());
    } else
    if (className.equals (DateRule.DATE_RULE_CLASS_NAME)) {
      return new DateRule ();
    } else 
    if (className.equals (HyperlinkRule.HYPERLINK_RULE_CLASS_NAME)) {
      return new HyperlinkRule(); 
    } else {
      throw new IllegalArgumentException (className);
    }
  }

} // end of class DataFormatRule