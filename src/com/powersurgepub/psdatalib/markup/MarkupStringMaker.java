package com.powersurgepub.psdatalib.markup;

  import com.powersurgepub.psdatalib.pstextio.TextLineWriter;

/**
 *
 * Writes characters and lines to a string field. 
 */
public class MarkupStringMaker 
    implements TextLineWriter {
  
  private String lineSep;
  
  StringBuffer str = new StringBuffer();
  
  /** Creates a new instance of StringMaker */
  public MarkupStringMaker() {
    lineSep = System.getProperty ("line.separator");
  }
  
  public boolean openForOutput () {
    str = new StringBuffer();
    return true;
  }
  
  public boolean writeLine (String s) {
    write (s);
    newLine();
    return true;
  }
  
  public boolean write (String s) {
    str.append (s);
    return true;
  }
  
  public boolean newLine () {
    str.append (lineSep);
    return true;
  }
  
  public boolean flush () {
    return true;
  }
  
  public boolean close () {
    return true;
  }
  
  public String toString () {
    return str.toString();
  }
  
  /**
   Return the file path, or other string identifying the output destination.

   @return The file path, or other string identifying the output destination.
   */
  public String getDestination () {
    return toString();
  }
  
  public boolean isOK () {
    return true;
  }
  
}
