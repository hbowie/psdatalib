package com.powersurgepub.psdatalib.txbmodel;

  import java.util.*;
  import javax.swing.tree.*;


/**
 This class represents a tree of text nodes. The class extends 
 DefaultTreeModel and leverages its basic tree structure. It may also be  
 used as a tree model for a JTree interface.

 @author Herb Bowie
 */
public class TextTree 
  extends DefaultTreeModel {
  
  private ArrayList<TocEntry>       tocEntries = new ArrayList();
  
  /** 
   Creates a new instance of TextTree. 
   */
  public TextTree (TextNode root) {
    super (root);
  }
  
  public TextNode getTextRoot () {
    return (TextNode)getRoot();
  }
  
  public void addTocEntry (TocEntry tocEntry) {
    tocEntries.add(tocEntry);
  }
  
  public boolean hasTocEntries () {
    return (tocEntries.size() > 0);
  }
  
  public int getTocSize() {
    return tocEntries.size();
  }
  
  public TocEntry getTocEntry(int index) {
    if (index < 0 || index >= tocEntries.size()) {
      return null;
    } else {
      return tocEntries.get(index);
    }
  }

}
