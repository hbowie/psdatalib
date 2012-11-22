package com.powersurgepub.psdatalib.psdata;

  import java.lang.Character;
  import java.lang.Object;
  import java.lang.String;
  import java.lang.System;
  import com.powersurgepub.psdatalib.psdata.USPhoneRule;
  import com.powersurgepub.psutils.StringPattern;
  import com.powersurgepub.psutils.StringUtils;
  import com.powersurgepub.psutils.FileName;
  
/**
   Method to test the other classes in the package.
  
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
     00/04/24 - Consolidated all the test routines for this package 
                into this class.</ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
  
   @version 00/05/22 - Added test for CountryRule.
 */

public class PsdataTest {  
  
  /** 
     Tests all the psdata classes.
   */
  public static void main (String args[]) {
    
    CountryRule.test();
    USPhoneRule.test();
    USMobileRule.test();
    DateRule.test();
    HyperlinkRule.test();
  }
} // end of class PsdataTest