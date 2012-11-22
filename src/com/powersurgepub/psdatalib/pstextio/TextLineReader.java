package com.powersurgepub.psdatalib.pstextio;

/**
 The most basic interface for reading lines from somewhere.

  <p>The following classes implement this interface. </p>

 <ul>
   <li>FileLineReader - Reads lines from a file. </li>
 </ul>
 */
public interface TextLineReader {
  
  public boolean open ();
  
  public String readLine ();
  
  public boolean close();
  
  public boolean isOK ();
  
  public boolean isAtEnd();
  
  /**
   Return the file path or URL in the form of a string.
  
   @return The file path or URL. 
  */
  public String toString();

}
