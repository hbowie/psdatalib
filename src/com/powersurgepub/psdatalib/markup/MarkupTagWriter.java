/*
 * MarkupTagWriter.java
 *
 * Created on March 2, 2007, 5:59 PM
 *
 * An interface for writing markup. 
 */

package com.powersurgepub.psdatalib.markup;

  import org.xml.sax.*;
  import org.xml.sax.helpers.*;

/**
 *
 * @author hbowie
 */
public interface MarkupTagWriter {
  
  public void writeStartTag (
      String namespaceURI,
      String localName,
      String qualifiedName,
      Attributes attributes,
      boolean emptyTag);
  
  public void writeContent (
      String s);
  
  public void writeEndTag (
      String namespaceURI,
      String localName,
      String qualifiedName);
  
}

