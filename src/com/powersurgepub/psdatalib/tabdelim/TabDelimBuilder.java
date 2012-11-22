package com.powersurgepub.psdatalib.tabdelim;

  import com.powersurgepub.psutils.GlobalConstants;
  import java.lang.Object;
  import java.lang.String;
  import java.lang.StringBuffer;
  import java.lang.System;

/**
   A tab-delimited record built from a series of strings. <p>
   
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
      </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
  
   @version 00/05/19 - Modified to be consistent with "The Elements of Java Style".
 */
public class TabDelimBuilder {

  /** Number of tokens so far included in tab-delimited record. */
  private  int              tokenCount;
  
  /** Buffer used to build the tab-delimited record. */
  private  StringBuffer     record;
  
  /**
     Constructs a new builder.
   */
  public TabDelimBuilder () {
    record = new StringBuffer();
    tokenCount = 0;
  }
  
  /**
     Adds another token to the tab-delimited record being built.
    
     @parm inToken Next string to be added, with a tab between
                   adjacent tokens.
   */
  public void nextToken (String inToken) {
    if (tokenCount == 0) {
      record.append (inToken);
    } else {
      record.append (GlobalConstants.TAB_STRING + inToken);
    }
    tokenCount++;
  }

  /**
     Returns the finished tab-delimited record.
    
     @return Tab-delimited record containing all tokens passed so far.
   */
  public String toString () {
    return record.toString();
  }
}