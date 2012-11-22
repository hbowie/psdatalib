package com.powersurgepub.psdatalib.pstextio;

  import java.io.*;

/**
 
 Writes characters and lines to a string field. Implements TextLineWriter.
 */
public class StringMaker 
    implements TextLineWriter {
  
  private String lineSep;
  
  StringBuffer str = new StringBuffer();
  
  /** Creates a new instance of StringMaker */
  public StringMaker() {
    lineSep = System.getProperty ("line.separator");
  }
  
  public void setLineSep(String lineSep) {
    this.lineSep = lineSep;
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
    // no need to do anything
    return true;
  }
  
  public boolean close () {
    // no need to do anything
    return true;
  }
  
  public boolean isOK () {
    return true;
  }
  
  public File getFile () {
    return null;
  }
  
  public String toString () {
    return str.toString();
  }
  
  public String getDestination () {
    return ("String");
  }
  
}
