package com.powersurgepub.psdatalib.psdata;

  import com.powersurgepub.psdatalib.psdata.USPhoneRule;
  import com.powersurgepub.psutils.StringPattern;
  import java.lang.Character;
  import java.lang.String;
  import java.lang.StringBuffer;
  import java.util.Hashtable;
  
/**
   A rule for formatting a mobile telephone
   number field into a standard format. <p>
   
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
      </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
  
   @version 00/04/24 - Modified to be consistent with "The Elements of Java Style".
                       Also modified to use additional code from USPhoneRule.
 */
public class USMobileRule
  extends DataFormatRule {
  
  /** ID for this DataFormatRule descendent. */
  public static final String US_MOBILE_RULE_CLASS_NAME = "USMobileRule";
  
  /** An occurrence of USPhoneRule. This is used to do the real work. */
  private    USPhoneRule  phoneRule;
  
  /**
     Constructor requires an instance of USPhoneRule to be 
     passed.
    
     @param inPhoneRule An instance of USPhoneRule
   */
  public USMobileRule (USPhoneRule inPhoneRule) {
    super ();
    phoneRule = inPhoneRule;
  }
  
  /**
     Converts a mobile phone number string into a standard format
     and performs any applicable area code conversion.
    
     @return Standardized phone number.
    
     @param  inData Mobile phone number in any of a number 
                    of different formats.
   */
  public String transform (String inData) {
    return phoneRule.transformMobile (inData, true);
  }
  
  /**
     Return class name as a String.
    
     @return Class name as a String.
   */
  public String toString () {
    return US_MOBILE_RULE_CLASS_NAME;
  }
  
  /** 
     Tests the class with standard test cases. 
   */
  public static void test () {
    
    USPhoneRule phoneRule = new USPhoneRule();
    USMobileRule rule = new USMobileRule (phoneRule);
    System.out.println ("Testing " + rule.toString());
    testRule (rule, "4519732");
    testRule (rule, "451-9732");
    testRule (rule, "220-48-5752");
    testRule (rule, "6024519732");
    testRule (rule, "602-451-9732");
    testRule (rule, "891-7588");
    testRule (rule, "6027703527");
    testRule (rule, "6027913527");
    testRule (rule, "16027913527");
  }
  
  /**
     Tests one mobile phone conversion by printing input string and
     results of conversion.
    
     @param inRule   An instance of USMobileRule to use for test.
    
     @param inString The string to be converted for the test.
   */
  public static void testRule (USMobileRule inRule, String inString) {
  
    System.out.println 
      (inString + " becomes " + (String)inRule.transform(inString));
  }

}