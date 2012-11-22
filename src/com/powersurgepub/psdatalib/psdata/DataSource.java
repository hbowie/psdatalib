package com.powersurgepub.psdatalib.psdata;

  import com.powersurgepub.psutils.*;
  import java.io.IOException;

/**
   A source of DataRecord objects. <p>
   
   This code is copyright (c) 1999-2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
    2002/10/12 - Added setDebug method. <li>
    2000/05/18 - Changed interface name from DataRecordReadable to DataSource. <li>
    2000/05/08 - Modified to be consistent with "The Elements of Java Style".</ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
  
   @version 
    2003/07/29 - Added getDataParent method.
 */
public interface DataSource {
    
  /**
     Opens the reader for input.
    
     @param inDict A data dictionary to use.
    
     @throws IOException If there is trouble opening a disk file.
   */
  public void openForInput (DataDictionary inDict)
      throws IOException;
      
  /**
     Opens the reader for input.
    
     @param inRecDef A record definition to use.
    
     @throws IOException If there is trouble opening a disk file.
   */
  public void openForInput (RecordDefinition inRecDef)
      throws IOException;
  
  /**
     Opens the reader for input.
    
     @throws IOException If there is trouble opening a disk file.
   */
  public void openForInput ()
      throws IOException;
  
  /**
     Returns the next input data record.
    
     @return Next data record.
    
     @throws IOException If reading from a source that might generate
                         these.
   */
  public DataRecord nextRecordIn ()
      throws IOException;
    
  /**
     Returns the record definition for the reader.
    
     @return Record definition.
   */
  public RecordDefinition getRecDef ();
  
  /**
     Returns the sequential record number of the last record returned.
    
     @return Sequential record number of the last record returned via 
             nextRecordIn, where 1 identifies the first record.
   */
  public int getRecordNumber ();
  
  /**
     Returns the reader as some kind of string.
    
     @return String identification of the reader.
   */
  public String toString ();

  /**
     Indicates whether there are more records to return.
    
     @return True if no more records to return.
   */
  public boolean isAtEnd();
  
  /**
     Closes the reader.
    
     @throws IOException If there is trouble closing the file.
   */
  public void close () 
      throws IOException ;
    
  /**
     Sets a log to be used by the reader to record events.
    
     @param  log A logger object to use.
   */
  public void setLog (Logger log);
  
  /**
     Sets the debug instance to the passed value.
    
     @param debug Debug instance. 
   */
  public void setDebug (Debug debug);
  
  /**
     Indicates whether all data records are to be logged.
    
     @param  dataLogging True if all data records are to be logged.
   */
  public void setDataLogging (boolean dataLogging);
  
  /**
     Sets a file ID to be used to identify this reader in the log.
    
     @param  fileId An identifier for this reader.
   */
  public void setFileId (String fileId);
  
  /**
     Sets the maximum directory explosion depth.
    
     @param maxDepth Desired directory/sub-directory explosion depth.
   */
  public void setMaxDepth (int maxDepth);
  
  /**
     Retrieves the path to the original source file (if any).
    
     @return Path to the original source file (if any).
   */
  public String getDataParent ();
}
