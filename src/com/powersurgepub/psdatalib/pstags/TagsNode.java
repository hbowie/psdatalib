package com.powersurgepub.psdatalib.pstags;

  import java.io.*;
  import javax.swing.tree.*;

/**
   One node on a tree of objects that is organized by category. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/11/16 - Originally written. 
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/16 - Originally written. 
 */
public class TagsNode 
    extends DefaultMutableTreeNode {
      
  public static final int   BELOW_THIS_NODE = 10;
  public static final int   ABOVE_THIS_NODE = -10;
  public static final int   AFTER_THIS_NODE = 1;
  public static final int   BEFORE_THIS_NODE = -1;
  public static final int   EQUALS_THIS_NODE = 0;
      
  private int             nodeType;
  public static final int   ROOT = 0;
  public static final int   TAG  = 1;
  public static final int   ITEM = 2;
  
  private int             tagIndex = 0;
  
  private TagsNode        nextNodeForItem = null;
  
  // Show items before categories with identical category parents?
  private boolean         itemsBeforeCategories = true;
  
  /** 
    Creates a root node. 
   */
  public TagsNode(File source) {
    super (source, true);
    nodeType = ROOT;
  }
  
  /** 
    Creates a category node. 
   */
  public TagsNode (String category) {
    super (category, true);
    nodeType = TAG;
  }
  
  /**
   Creates a node that stores a taggable item.

   @param tagged   The taggable item to be stored within the node.

   @param tagIndex An index pointing to the tag that is to be stored
                   with this node.
   */
  public TagsNode (Taggable tagged, int tagIndex) {
    super (tagged, false);
    nodeType = ITEM;
    this.tagIndex = tagIndex;
  }

  public int getTagIndex () {
    return tagIndex;
  }

  public void setSource (File source) {
    if (nodeType == ROOT) {
      this.setUserObject(source);
    }
  }
  
  public void setNextNodeForItem (TagsNode nextNodeForItem) {
    this.nextNodeForItem = nextNodeForItem;
  }
  
  public boolean hasNextNodeForItem () {
    return nextNodeForItem != null;
  }
  
  public TagsNode getNextNodeForItem () {
    return nextNodeForItem;
  }
  
  /**
    Compare two tags nodes and determine their relative locations
    in the tree. Note that this method is only expected to be used
    on item nodes. 
   
    @return Value indicating relationship of the new node (this node) to the
            current node location in the tree. 
            ABOVE_THIS_NODE = The new node should be higher in the tree than
                              the current node.
            BELOW_THIS_NODE = The new node should be below the
                              current node. 
            AFTER_THIS_NODE = The new node should be at the same level as this node,
                              but come after it.
            BEFORE_THIS_NODE = The new node should be at the same level as this
                               node, but come before it.
            EQUALS_THIS_NODE = The two nodes have equal keys. 
   
    @param currNode Node already in the tree, to be compared to this one.

   */
  public int compareToNode (TagsNode currNode) {
    int levels = getLevels();
    /* if (currNode == null) {
      System.out.println ("Comparing " 
          + String.valueOf (this.getNodeType())
          + "-" + this.toString()
          +"(" + String.valueOf(levels)
          + ") to null");
    } else {
      System.out.println ("Comparing " 
          + String.valueOf (this.getNodeType())
          + "-" + this.toString()
          +"(" + String.valueOf(levels)
          + ") to " + String.valueOf (currNode.getNodeType())
          + "-" + currNode.toString()
          + "(" + String.valueOf (currNode.getLevel()) + ")");
    }
    */
    
    int result = 0;
    if (currNode == null) {
      result = BEFORE_THIS_NODE;
    } else {
      int currLevel = currNode.getLevel();
      // Since this new node is an item, it should go below its category node, hence
      // at its category level + 1.
      int newLevel = levels + 1;
      int currType = currNode.getNodeType();
      switch (currType) {
        case (ROOT):
          // we're at the top of the tree -- everything goes below this
          result = BELOW_THIS_NODE;
          break;
        case (TAG):
          if (currLevel > newLevel) {
            // Since this new node is an item, it should go below its category node, hence
            // at its category level + 1. If the category node we are now on is at a lower
            // level, then we need to go back up the tree to find the right position
            // for this new item. 
            result = ABOVE_THIS_NODE;
          }
          else
          if (currLevel == newLevel) { 
            // If the category node we are now on is at the same level
            // as the item we are trying to place, then this item should go
            // after it (if items go after sub-categories at the same level).
            if (itemsBeforeCategories) {
              result = BEFORE_THIS_NODE; 
            } else {
              result = AFTER_THIS_NODE;
            }
          }
          else {
            // We are currently on a category node with a number of levels
            // less than or equal to the number of levels of the item
            // we are trying to place. 
            String levelCat = this.getLevel (currLevel - 1);
            /* System.out.println ("levelCat for "
                + this.getTags().toString()
                + " @ " + String.valueOf(this.getTagIndex())
                + ", " + String.valueOf(currLevel - 1)
                + " = " + levelCat);
            */
            result = levelCat.compareToIgnoreCase(currNode.getUserObject().toString());
            if (result > 0) {
              // New item's sub-category at this level is greater than
              // this category node's sub-category value. 
              result = AFTER_THIS_NODE;
            } 
            else
            if (result < 0) {
              // New item's sub-category at this level is less than
              // this category node's sub-category value. 
              result = BEFORE_THIS_NODE;
            } else {
              // Sub-categories at this level are equal, so item
              // goes below the matching category node. 
              result = BELOW_THIS_NODE;
            }
          }
          break;
        case (ITEM):
          if (currLevel > newLevel) {
            // if this item is at a higher (deeper) level than the one
            // that this new one should go at, then we need to go back
            // up the tree. 
            result = ABOVE_THIS_NODE;
          }
          else
          if (currLevel == newLevel) { 
            // Item should go at this level
            result = getTaggable().compareTo (currNode.getTaggable());
            if (result > 0) {
              // New item sorts after the current item we are on
              result = AFTER_THIS_NODE;
            } 
            else
            if (result < 0) {
              // New item sorts before the current item we are on
              result = BEFORE_THIS_NODE;
            } else {
              // New item has an equal key with the current item we are on:
              // put the new item first. 
              result = BEFORE_THIS_NODE;
            }
          } else {
            // if this item is at a lower (shallower) level than the level
            // that this new one should go at, then put the new item
            // before this node.  
            if (itemsBeforeCategories) {
              result = AFTER_THIS_NODE;
            } else {
              result = BEFORE_THIS_NODE;
            }
          }
          break;
        default:
          // Unexpected node type -- should never arrive here
          break;
      }
    }
    // System.out.println ("compareToNode result = " + String.valueOf(result));
    return result;
  }
  
  public int getNodeType() {
    return nodeType;
  }

  /**
   Get the requested level within the specified tag for this node.

   @param levelIndex An index pointing to the requested level within the tag
                     for this node.
   
   @return The requested level within the specified tag for this node, if an
           item node, otherwise an empty string.
   */
  public String getLevel (int levelIndex) {
    if (nodeType == ITEM) {
      if (levelIndex >= 0 && levelIndex < getLevels()) {
        return getTags().getLevel(tagIndex, levelIndex);
      } else {
        return "";
      }
    } else {
      return "";
    }
  }

  /**
   Returns the number of levels for the given tag for an item node.
   @return The number of levels for the given tag at the index
           specified for this node, for an item node; returns 0 for
           any other type of node.
   */
  public int getLevels () {
    if (nodeType == ITEM) {
      return getTags().getLevels (tagIndex);
    } else {
      return 0;
    }
  }

  /**
   Returns the tags associated with a taggable item for an item node type.
   @return The tags associated with the taggable item for an item node,
           returns null for other node types.
   */
  public Tags getTags () {
    if (nodeType == ITEM) {
      return getTaggable().getTags();
    } else {
      return null;
    }
  }

  /**
   Return the tags as a String, whether the node type is an item or tags.

   @return Tags as a string.
   */
  public String getTagsAsString () {
    if (nodeType == ITEM) {
      return ((Taggable)getUserObject()).getTags().toString();
    }
    else
    if (nodeType == TAG) {
      StringBuilder tags = new StringBuilder();
      TagsNode tagNode = this;
      while (tagNode != null && tagNode.getNodeType() == TAG) {
        if (tags.length() > 0) {
          tags.insert(0, Tags.PREFERRED_LEVEL_SEPARATOR);
        }
        tags.insert(0, (String)tagNode.getUserObject());
        tagNode = (TagsNode)tagNode.getParent();
      }
      return tags.toString();
    } else {
      return "";
    }
  }

  /**
   Returns the taggable item for an item node.

   @return The taggable item for an item node, returns null for other
           node types.
   */
  public Taggable getTaggable () {
    if (nodeType == ITEM) {
      return (Taggable)getUserObject();
    } else {
      return null;
    }
  }

  public String toLongerString() {
    return ("Node type = " + String.valueOf (this.getNodeType())
        + " level = " + String.valueOf (this.getLevel())
        + " " + toString());
  }

  /**
   Returns the title of this node. This value is displayed in the Tree Model
   for the collection.

   @return Title of this node.
   */
  public String toString() {
    return (this.getUserObject().toString());
  }
  
}
