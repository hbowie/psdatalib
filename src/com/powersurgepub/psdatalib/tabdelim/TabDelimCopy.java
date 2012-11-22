package com.powersurgepub.psdatalib.tabdelim;

  import java.io.FileNotFoundException;
  import java.io.IOException;
  import java.lang.Integer;
  import java.lang.Object;
  import java.lang.String;
  import java.lang.System;
  import com.powersurgepub.psutils.LogOutput;
  import com.powersurgepub.psutils.LogData;
  import com.powersurgepub.psutils.LogEvent;
  import com.powersurgepub.psutils.Logger;
  import com.powersurgepub.psdatalib.psdata.DataDictionary;
  import com.powersurgepub.psdatalib.psdata.DataField;
  import com.powersurgepub.psdatalib.psdata.DataRecord;
  import com.powersurgepub.psdatalib.psdata.InitialCapsRule;
  import com.powersurgepub.psdatalib.tabdelim.TabDelimFile;
  
/**
   An application that copies one tab-delimited file to another.
   
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
      </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
  
   @version 00/05/21 - Modified to be consistent with "The Elements of Java Style".
 */
public class TabDelimCopy {
  
  public static void main (String [] parms) 
      throws IOException {
    Logger log = new Logger (new LogOutput());
    log.setLogAllData (false);
    log.recordEvent 
      (new LogEvent 
        (LogEvent.NORMAL, "TabDelimCopy main method beginning"));
    DataDictionary dict = new DataDictionary ();
    if (parms.length > 2) {
      try {
        TabDelimFile dictFile = new TabDelimFile (parms[2]);
        dict.load (dictFile);
      } catch (FileNotFoundException e) {
      }
    }
    TabDelimFile inFile1 = new TabDelimFile (parms[0]);
    inFile1.setLog (log);
    inFile1.setDataLogging (false);
    inFile1.openForInput (dict);
    System.out.println (inFile1.toString ());
    TabDelimFile outFile1 = new TabDelimFile (parms[1]);
    outFile1.setLog (log);
    outFile1.setDataLogging (false);
    outFile1.openForOutput (inFile1.getRecDef());
    DataRecord inRec;
    do {
      inRec = inFile1.nextRecordIn ();
      if (inRec != null) {
        outFile1.nextRecordOut (inRec);
      }
    } while (inRec != null);
    inFile1.close();
    outFile1.close();
    if (parms.length > 2) {
      TabDelimFile dictFile = new TabDelimFile (parms[2]);
      dict.store (dictFile);
    }
    log.recordEvent 
      (new LogEvent 
        (LogEvent.NORMAL, "TabDelimCopy main method ending"));
  } // end main method
} // end TabDelimCopy Class
