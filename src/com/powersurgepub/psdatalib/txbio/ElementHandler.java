package com.powersurgepub.psdatalib.txbio;

  import java.io.*;

/**
 An interface for handling elements within documents. Such an interface would
 typically be used by a parser to pass data to an application. In fact, 
 this interface is a simplified version an XML content handler. Elements may
 be nested. 

 @author Herb Bowie
 */
public interface ElementHandler {
  
  public void setSource(File file);
  
  /**
   Indicate the start of a new document. 
   */
  public void startDocument();
  
  /**
   Indicate the start of a new element (aka field) within a document.
  
   @param name The name of the element. 
  
   @param isAttribute Attributes are treated as a special type of attribute,
                      so attributes are identified with this flag. 
   */
  public void startElement (String name, boolean isAttribute);
  
  /**
   Pass a data value contained within the last element started. This may be a 
   complete data value, or it may be incomplete, and only a part of the entire 
   data value. It is assumed that the handler will concatenate multiple data 
   occurrences within an element as needed. 
  
   @param str A string of data contained within the last element started. 
   */
  public void data (String str);
  
  /**
   Pass a data value contained within the last element started. This may be a 
   complete data value, or it may be incomplete, and only a part of the entire 
   data value. It is assumed that the handler will concatenate multiple data 
   occurrences within an element as needed. 
  
   @param str A string of data contained within the last element started.
   @param useMarkdown Use a Markdown parser to format the data being passed. 
   */
  public void data (String str, boolean useMarkdown);
  
  public void characters (char [] ch, int start, int length);
  
  /**
   Indicate the end of the named element. It is assumed that the handler will
   close lower-level elements as needed.
  
   @param name The name of the previously started element to be closed. 
   */
  public void endElement (String name);
  
  /**
   Indicates the end of a document.
   */
  public void endDocument();
  
}
