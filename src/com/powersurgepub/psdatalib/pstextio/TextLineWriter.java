package com.powersurgepub.psdatalib.pstextio;

/**

 The most basic interface for writing characters and lines somewhere. 
 
 <p>The following classes implement this interface. </p>

 <ul>
   <li>ClipboardMaker - Writes lines to the System clipboard. </li>
   <li>Writes text lines to a local file. </li>
   <li>Writes characters and lines to a string field. </li>
 </ul>
 
 */
public interface TextLineWriter {  
  
  public boolean openForOutput ();
  
  public boolean write (String s);
  
  public boolean writeLine (String s);
  
  public boolean newLine ();
  
  public boolean flush ();
  
  public boolean close();
  
  public boolean isOK ();

  /**
   Return the file path, or other string identifying the output destination.

   @return The file path, or other string identifying the output destination.
   */
  public String getDestination ();

}
