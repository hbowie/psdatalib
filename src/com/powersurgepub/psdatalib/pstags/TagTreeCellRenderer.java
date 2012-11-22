package com.powersurgepub.psdatalib.pstags;

  import com.powersurgepub.psutils.*;
  import java.awt.*;
  import javax.swing.*;
  import javax.swing.tree.*;

/**
   An object capable of rendering a cell in a category tree of to do items. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/12/06 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/12/06 - Originally written. 
 */
public class TagTreeCellRenderer 
    extends DefaultTreeCellRenderer 
        implements TreeCellRenderer {
  
  /** 
    Creates a new instance of ItemTreeCellRenderer.
   */
  public TagTreeCellRenderer () {

  }
  
  public Component getTreeCellRendererComponent (JTree jTree, Object obj, 
      boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    
    super.getTreeCellRendererComponent(jTree, obj, sel, expanded, leaf, 
        row, hasFocus);
    
    if (leaf) {
      try {
        TagsNode node = (TagsNode)obj;
        if (node.getNodeType() == node.ITEM) {
          Taggable tagged = (Taggable)node.getUserObject();
        }
      } catch (ClassCastException e) {
        // If not a CategoryNode, then no override
      }
    } // end if leaf
    return this;
  }
  
}
