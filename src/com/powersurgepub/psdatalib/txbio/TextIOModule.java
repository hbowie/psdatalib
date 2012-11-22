package com.powersurgepub.psdatalib.txbio;

  import com.powersurgepub.psdatalib.txbmodel.*;
  import com.powersurgepub.psdatalib.pstextio.TextWriter;
  import java.net.*;
  import java.util.*;
  import org.xml.sax.*;
  import org.xml.sax.helpers.*;

/**
 This abstract class defines the basics for any module that will perform
 input and/or output functions for TextBlocs. 
 */
public abstract class TextIOModule
    extends DefaultHandler {
  
  protected     TextTree              tree;
  protected     TextNode              currentNode;
  
  public abstract void registerTypes (List types);
  
  public abstract boolean load  (TextTree tree, URL url, TextIOType type, String parm);
  
  public abstract boolean store 
      (TextTree tree, TextWriter writer, TextIOType type, 
       boolean epub, String epubSite);

}
