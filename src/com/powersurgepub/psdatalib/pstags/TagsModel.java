package com.powersurgepub.psdatalib.pstags;

  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.util.*;
  import javax.swing.tree.*;

/**
   A collection of objects that is sorted by tag/category, and
   provides the underlying data model for a JTree display of the
   objects. <p>
  
   This code is copyright (c) 2003-2009 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/11/16 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 
      2003/11/16 - Originally written.
 */
public class TagsModel {
          
  private DefaultTreeModel    tree;
  
  private TagsNode            root;
  
  private TagsNode            currentNode;
  
  private TagsNode            nextNode;
  
  private TagsNode            priorNode;

  public TagsModel() {
    File unknown = new File (System.getProperty (GlobalConstants.USER_DIR), "???");
    root = new TagsNode(unknown);
    tree = new DefaultTreeModel (root, true);
  }
  /** 
    Creates a new instance of TagsModel.
   */
  public TagsModel(File source) {
    if (source == null) {
      File unknown = new File (System.getProperty (GlobalConstants.USER_DIR), "???");
      root = new TagsNode(unknown);
    } else {    
      root = new TagsNode(source);
    }
    tree = new DefaultTreeModel (root, true);
  }

  /**
   Set the data source, stored in the root node of the tree. 
  
   @param source The file or folder from which the data is taken. 
  */
  public void setSource (File source) {
    root.setSource(source);
  }

  /**
   Get the data source, stored in the root node of the tree. 
  
   @return The file or folder from which the data is taken. 
  */
  public File getSource () {
    return (File)root.getUserObject();
  }
  
  public DefaultTreeModel getModel() {
    return tree;
  }
  
  /**
    Make sure this view is sorted in the proper sequence
    and contains only selected items.
   */
  public void sort (TaggableList list) {
    // Make sure each tagged is selected and in correct location
    for (int i = 0; i < list.size(); i++) {
      modify (list.get(i));
    }
  }
  
  /**
   *    Process a new Taggable that has just been modified within the Taggables collection.
   *   
   *    @param tagged   Taggable just modified.
   */
  public void modify(Taggable tagged) {
    remove (tagged);
    add (tagged);
  }
  
  /**
   *    Process a taggable item to be deleted from the collection.
   *   
   *    @param tagged   Item to be deleted.
   */
  public void remove (Taggable tagged) {
    // selectItem (tagged);
    TagsNode itemNode = tagged.getTagsNode();
    TagsNode nodeNext = null;
    tagged.setTagsNode (null);
    while (itemNode != null) { 
      nodeNext = itemNode.getNextNodeForItem();
      tree.removeNodeFromParent (itemNode);
      itemNode = nodeNext;
    }
  }

  /**
   Add a taggable item to the tree model.

   @param tagged The taggable item to be added. 
   */
  public void add(Taggable tagged) {

    TagsNode lastNode = null;
    int tagIndex = 0;
    Tags tags;
    TagsIterator iterator;
    tags = tagged.getTags();
    iterator = new TagsIterator (tagged.getTags());
    int nodesStored = 0;
    while (iterator.hasNextTag() || nodesStored < 1) {
      nodesStored++;
      String nextTag = "";;
      if (iterator.hasNextTag()) {
        nextTag = iterator.nextTag();
      }
      // System.out.println ("  nextTag = " + nextTag);
      TagsNode itemNode = new TagsNode (tagged, tagIndex);
      /* System.out.println ("    TagsModel add " + String.valueOf(tagIndex)
          + " from " + tagged.getTags().toString());
      */

      // Store this node so we can find it later by its index
      if (tagIndex == 0) {
        tagged.setTagsNode (itemNode);
      } else {
        lastNode.setNextNodeForItem (itemNode);
      }
      lastNode = itemNode;

      // Now store it in the tree
      TagsNode currentNode = root;
      TagsNode parentNode = root;
      
      // levels = the number of levels in the new tagged item's tags
      int levels = tags.getLevels (tagIndex);
      // System.out.println ("      levels = " + String.valueOf(levels));
      boolean done = false;
      int compass = TagsNode.BELOW_THIS_NODE;
      // level is used to keep track of our current depth as we walk
      // through the tree. Note that the root level is considered -1, and
      // the first level with real keys is considered 0. This allows level
      // to be used to pull the appropriate tag level out of the tags,
      // where zero would be the first level. 
      int level = -1;
      // child is used to keep track of our position within the
      // current set of children we are traversing.
      int child = 0;
      // Walk through the tree until we find the right location
      // for the tagged item to be added
      do {
        compass = itemNode.compareToNode (currentNode);
        // if (currentNode == null) {
            // System.out.println ("        compass = " + String.valueOf(compass)
            //   + " for null node");
        // } else {
          // System.out.println ("        compass = " + String.valueOf(compass)
          //     + " for node type " + currentNode.toString());
        // }
        TagsNode newNode;
        switch (compass) {
          case (TagsNode.BELOW_THIS_NODE):
            level++;
            child = 0;
            if (currentNode.getChildCount() > 0) {
              // New tagged item should be below this one, and children already exist
              newNode = (TagsNode)currentNode.getFirstChild();
              parentNode = currentNode;
              currentNode = newNode;
              // level++;
              // child = 0;
            } else {
              // New tagged should be below this one, but no children yet exist
              // level++;
              if (levels > level) {
                // Not yet at desired depth -- create a new tags node
                String levelCat = tags.getLevel (tagIndex, level);
                // System.out.println ("          Adding new tags node for tag "
                //     + String.valueOf(tagIndex)
                //     + ", level " + String.valueOf(level)
                //     + ": " + levelCat);
                newNode = new TagsNode (levelCat);
              } else {
                // we're at desired depth -- add the new tagged node
                newNode = itemNode;
                done = true;
              }
              tree.insertNodeInto (newNode, currentNode, 0);
              /* System.out.println ("          Adding new node " + newNode.toString());
              System.out.println ("            Done? " + String.valueOf (done));
              */
              parentNode = currentNode;
              currentNode = newNode;
              // level++;
              // child = 0;
            } // end go below but no children
            break;
          case (TagsNode.AFTER_THIS_NODE):
            // new node should be after this one -- keep going
            newNode = (TagsNode)currentNode.getNextSibling();
            currentNode = newNode;
            child++;
            break;
          case (TagsNode.BEFORE_THIS_NODE):
            // Don't go any farther -- put it here or add a new tags
            if (levels > level) {
              String levelCat = tags.getLevel(tagIndex, level);
              newNode = new TagsNode (levelCat);
            } else {
              newNode = itemNode;
              done = true;
            }
            tree.insertNodeInto (newNode, parentNode, child);
            // System.out.println ("          Adding new node " +newNode.toString());
            // System.out.println ("            Done? " + String.valueOf (done));
            currentNode = newNode;
            break;
          case (TagsNode.EQUALS_THIS_NODE):
            newNode = itemNode;
            tree.insertNodeInto (newNode, parentNode, child);
            currentNode = newNode;
            done = true;
            // System.out.println ("          Adding new node " +newNode.toString());
            // System.out.println ("            Done? " + String.valueOf (done));
            break;
          default:
            Logger.getShared().recordEvent (LogEvent.MAJOR,
                "Tags Model add -- hit default in switch", false);
            Logger.getShared().recordEvent (LogEvent.NORMAL, "Compass value = "
                + String.valueOf (compass), false);
            Logger.getShared().recordEvent (LogEvent.NORMAL, "itemNode = "
                + String.valueOf (itemNode.getNodeType())  + " - "
                + itemNode.toString(), false);
            Logger.getShared().recordEvent (LogEvent.NORMAL, "Current node = "
                + String.valueOf (currentNode.getNodeType())  + " - "
                + currentNode.toString(), false);
                break;
        } // end of compass result switch
      } while (! done);
      // setNextPrior();
      tagIndex++;
    } // end for each category assigned to tagged
  }
  
  public Taggable firstItem () {
    currentNode = getNextItem (root, 1);
    if (currentNode.getNodeType() == TagsNode.ITEM) {
      setNextPrior();
      return ((Taggable)currentNode.getUserObject());
    } else {
      return null;
    }
  }


  
  public Taggable lastItem () {
    currentNode = getNextItem (root, -1);
    if (currentNode.getNodeType() == TagsNode.ITEM) {
      setNextPrior();
      return ((Taggable)currentNode.getUserObject());
    } else {
      return null;
    }
  }
  
  public Taggable nextItem () {
    if (nextNode.getNodeType() == TagsNode.ITEM) {
      currentNode = nextNode;
      setNextPrior();
      return ((Taggable)currentNode.getUserObject());
    } else {
      return null;
    }
  }

  public Taggable priorItem () {
    if (priorNode.getNodeType() == TagsNode.ITEM) {
      currentNode = priorNode;
      setNextPrior();
      return ((Taggable)currentNode.getUserObject());
    } else {
      return null;
    }
  }

  public TagsNode firstItemNode () {
    TagsNode desiredNode = getNextItem (root, 1);
    if (desiredNode != null
        && desiredNode.getNodeType() == TagsNode.ITEM) {
      return desiredNode;
    } else {
      return null;
    }
  }
  
  public TagsNode lastItemNode () {
    TagsNode desiredNode = getNextItem (root, -1);
    if (desiredNode.getNodeType() == TagsNode.ITEM) {
      return desiredNode;
    } else {
      return null;
    }
  }

  public TagsNode nextItemNode (TagsNode startingNode) {
    TagsNode desiredNode = getNextItem (startingNode, +1);
    if (desiredNode.getNodeType() == TagsNode.ITEM) {
      return desiredNode;
    } else {
      return null;
    }
  }

  public TagsNode priorItemNode (TagsNode startingNode) {
    TagsNode desiredNode = getNextItem (startingNode, -1);
    if (desiredNode.getNodeType() == TagsNode.ITEM) {
      return desiredNode;
    } else {
      return null;
    }
  }
  
  /*
   private void checkCurrentNode (Taggable tagged) {
    if (currentNode == null) {
      selectItem (tagged);
    }
  } */
  
  public void selectItem(Taggable tagged) {
    currentNode = tagged.getTagsNode();
    setNextPrior();
  }
  
  private void setNextPrior () {
    if (currentNode != null) {
      nextNode = getNextItem (currentNode, 1);
      priorNode = getNextItem (currentNode, -1);
    }
  }

  /**
   Gets the next/prior item.
   @param startNode The reference node from which we're starting.
   @param increment A positive number will get the next node; a negative
                    number will get the prior node. Tag nodes are skipped. 
   @return          The next/prior item node.
   */
  private TagsNode getNextItem (TagsNode startNode, int increment) {
    TagsNode currNode = startNode;
    TagsNode nextNode;
    boolean childrenExhausted = false;
    // Keep grabbing the next node until we have one that is an Item
    // or the Root (not a Category).
    do {
      if ((currNode.getNodeType() == TagsNode.ITEM)
          || (childrenExhausted)
          || (currNode.getChildCount() == 0)) {
        if (increment >= 0) {
          nextNode = (TagsNode)currNode.getNextSibling();
        } else {
          nextNode = (TagsNode)currNode.getPreviousSibling();
        }
        if (nextNode == null) {
          nextNode = (TagsNode)currNode.getParent();
          if (nextNode == null) {
            Logger.getShared().recordEvent (LogEvent.MAJOR, "Current node "
                + String.valueOf (currNode.getNodeType())  + " - "
                + currNode.toString()
                + " has no parent!", false);
          }
          childrenExhausted = true;
        } else {
          childrenExhausted = false;
        }
      } else {
        // look for children 
        if (increment >= 0) {
          nextNode = (TagsNode)currNode.getFirstChild();
        } else {
          nextNode = (TagsNode)currNode.getLastChild();
        }
      }
      if (nextNode == null) {
        Logger.getShared().recordEvent (LogEvent.MAJOR,
            "TagsModel  getNextItem -- null tags node",
            false);
        Logger.getShared().recordEvent (LogEvent.NORMAL, "Starting node = "
            + String.valueOf (startNode.getNodeType())  + " - "
            + startNode.toString(), false);
        Logger.getShared().recordEvent (LogEvent.NORMAL, "Current node = "
            + String.valueOf (currNode.getNodeType())  + " - "
            + currNode.toString(), false);
      }
      currNode = nextNode;
    } while (currNode != null
        && currNode.getNodeType() == TagsNode.TAG);
    return currNode;
  } // end method

  /**
   Get the root of the tree.

   @return The root node.
   */
  public TagsNode getRoot() {
    return (TagsNode)tree.getRoot();
  }

  /**
   Get the next node following the current one, in a depth-first progression
   through the tree.

   @param startingNode The node we're starting with.
   @return The next node, or null if we've traversed the entire tree.
   */
  public TagsNode getNextNode (TagsNode startingNode) {

    boolean noMoreSiblings = true;
    TagsNode currNode = startingNode;
    TagsNode nextUp = null;
    int level = currNode.getLevel();

    if (currNode.getChildCount() > 0) {
      nextUp = (TagsNode)currNode.getFirstChild();
    } else {
      nextUp = (TagsNode)currNode.getNextSibling();
      noMoreSiblings = (nextUp == null);
      while (level > 0 && noMoreSiblings) {
        nextUp = (TagsNode)currNode.getParent();
        currNode = nextUp;
        level = currNode.getLevel();
        nextUp = (TagsNode)currNode.getNextSibling();
        noMoreSiblings = (nextUp == null);
      } // While looking for next sibling at a higher level
    } // end if node has no children
    return nextUp;
  }
  
	/**
	   Returns the object in string form.
	  
	   @return Name of this class.
	 */
	public String toString() {
    StringBuffer work = new StringBuffer();
    TagsNode onDeckCircle = getRoot();
    TagsNode node = null;
    boolean noMoreSiblings = true;
    int level = 0;
    while (onDeckCircle != null) {
      node = onDeckCircle;

      // Add the current node's information to the concatenated output string
      level = node.getLevel();
      for (int i = 0; i < level; i++) {
        work.append ("  ");
      }
      work.append (node.getUserObject().toString());
      work.append ("\n");

      onDeckCircle = getNextNode (node);
    } // end while more nodes
    return work.toString();
	}
  
} // end class
