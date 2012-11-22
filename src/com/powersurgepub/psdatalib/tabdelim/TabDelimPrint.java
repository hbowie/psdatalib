package com.powersurgepub.psdatalib.tabdelim;

  import java.io.IOException;
  import java.lang.Integer;
  import java.lang.Object;
  import java.lang.String;
  import java.lang.System;
  import com.powersurgepub.psdatalib.psdata.DataDictionary;
  import com.powersurgepub.psdatalib.psdata.DataField;
  import com.powersurgepub.psdatalib.psdata.DataRecord;
  import com.powersurgepub.psdatalib.psdata.InitialCapsRule;
  import com.powersurgepub.psdatalib.tabdelim.TabDelimFile;
  
/**
   An application that prints a file of tab-delimited records.
   
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
public class TabDelimPrint {
  
  /**
     Prints a tab-delimited file whose name is passed as the first run-time
     parameter. 
   */
  public static void main (String [] parms) 
      throws IOException {
    DataDictionary dict = new DataDictionary ();
    System.out.println (parms[0]);
    TabDelimFile inFile1 = new TabDelimFile (parms[0]);
    System.out.println (inFile1.getPath());
    inFile1.openForInput (dict);
    System.out.println (inFile1.toString ());
    DataRecord inRec;
    do {
      inRec = inFile1.nextRecordIn ();
      if (inRec != null) {
        System.out.println ("Record number " 
          + Integer.toString(inFile1.getRecordNumber()));
        inRec.startWithFirstField ();
        while (inRec.hasMoreFields ()) {
          DataField field = inRec.nextField ();
          System.out.println (field.toString ());
        }
      }
    } while (inRec != null);
  }
}
