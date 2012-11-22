package com.powersurgepub.psdatalib.txbio;

  import java.io.*;
  import java.net.*;

/**
  An interface for parsing elements from documents. 

  @author Herb Bowie
 */
public interface ElementParser {
  
  public void setSource(String inPath);
  
  public void setSource(File inPathFile);
  
  public void setSource(URL inURL);
  
  /**
   Parse the designated structured text source and pass the parsed elements
   to the supplied element handler. 
  
   @param url     The URL pointing to the structured text source to be parsed. 
   @param handler The element handler that will consume the elements. 
  */
  public void parse (ElementHandler handler) 
      throws IOException;
  
}
