package com.powersurgepub.psdatalib.tabdelim;

import com.powersurgepub.psdatalib.psdata.DataDictionary;
import com.powersurgepub.psdatalib.psdata.DataRecord;
  import java.io.*;
  import com.powersurgepub.psutils.*;
  
/**
   An application that copies one tab-delimited file to another,
   normalizing the data in the process.
   
   This code is copyright (c) 1999-2003 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
      </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003-03-14 -- Created anew. 
 */
public class TabDelimNormalize {
  
  public static void main (String [] parms) 
      throws IOException {
    Logger log = new Logger (new LogOutput());
    log.setLogAllData (false);
    log.recordEvent 
      (new LogEvent 
        (LogEvent.NORMAL, "TabDelimNormalize main method beginning"));
    DataDictionary dict = new DataDictionary ();
    if (parms.length > 2) {
      try {
        TabDelimFile dictFile = new TabDelimFile (parms[2]);
        dict.load (dictFile);
      } catch (FileNotFoundException e) {
      }
    }
    TabDelimFile inFiles = new TabDelimFile (parms[0]);
    BoeingDocsNormalizer inFile1 = new BoeingDocsNormalizer (inFiles);
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
        (LogEvent.NORMAL, "TabDelimNormalize main method ending"));
  } // end main method
} // end TabDelimNormalize Class
