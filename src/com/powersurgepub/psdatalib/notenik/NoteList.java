/*
 * Copyright 2009 - 2015 Herb Bowie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.powersurgepub.psdatalib.notenik;

  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.pstags.*;
  import java.io.*;
  import java.util.*;
  import javax.swing.table.*;

/**
 A list of notes.
 */
public class NoteList
    extends AbstractTableModel
    implements TaggableList {

  private String          title = "Note List";
  private RecordDefinition recDef = null;
  private List<Note>      notes = new ArrayList<Note>();

  private int             findIndex = -1;
  private boolean         findMatch = false;

  private TagsList        tagsList = new TagsList();
  private TagsModel       tagsModel = new TagsModel();

  public NoteList (RecordDefinition recDef) {
    this.recDef = recDef;
    tagsList.registerValue("");
  }
  
  public RecordDefinition getRecDef() {
    return recDef;
  }

  public TagsList getTagsList () {
    return tagsList;
  }

  public TagsModel getTagsModel () {
    return tagsModel;
  }

  public File getSource () {
    return tagsModel.getSource();
  }

  public void setSource (File source) {
    tagsModel.setSource(source);
  }

  /**
   Add a new note to the list. If a note with the same key (title) already
   exists in the list, the the note to be added will instead be merged with
   the existing note. 
   
   @param newNote
   @return A positioned note composed of the resulting note and an index
           pointing to its resulting position in the list.
   */
  public NotePositioned add (Note newNote) {
    // newNote.getTags().displayTags();
    Note resultingNote = newNote;
    boolean merged = false;
    if (notes.isEmpty()) {
      // If this is the first note being added to the collection, simply add it
      notes.add (newNote);
      findIndex = 0;
    }
    else
    if (get(notes.size() - 1).compareTo(newNote) < 0) {
      // If the new Note has a key higher than the highest item in the
      // collection, simply add the new Note to the end
      // (more efficient if an input file happens to be pre-sorted).
      findIndex = notes.size();
      notes.add (newNote);
    } else {
      findInternal (newNote);
      if (findMatch) {
        get(findIndex).merge(newNote);
        resultingNote = get(findIndex);
        merged = true;
      } else {
        notes.add (findIndex, newNote);
      }
    }
    
    if (merged) {
      tagsList.modify  (resultingNote);
      tagsModel.modify (resultingNote);
    } else {
      tagsList.add  (resultingNote);
      tagsModel.add (resultingNote);
    }

    return new NotePositioned (resultingNote, findIndex);
  } // end add method

  public NotePositioned modify (NotePositioned modNote) {
    tagsList.modify(modNote.getNote());
    tagsModel.modify(modNote.getNote());
    return modNote;
  }

  /**
   Removes the passed note, if it exists in the collection.

   @param position A position containing the note to be removed.
   
   @return A position for the next note following the one just removed.
   */
  public NotePositioned remove (NotePositioned position) {
    int oldIndex = find (position.getNote());
    NotePositioned newPosition = position;
    if (findMatch) {
      newPosition = next (position);
      tagsModel.remove (position.getNote());
      tagsList.remove (position.getNote());
      notes.remove(oldIndex);
    }
    return newPosition;
  }
  
  /**
   Removes the passed note, if it exists in the collection.

   @param noteToRemove The note to be removed.
   
   @return True if note found and removed; false otherwise.
   */
  public boolean remove (Note noteToRemove) {
    int oldIndex = find (noteToRemove);
    if (findMatch) {
      tagsModel.remove (noteToRemove);
      tagsList.remove (noteToRemove);
      notes.remove(oldIndex);
    }
    return findMatch;
  }

  public int find (Note findNote) {
    findInternal (findNote);
    if (findMatch) {
      return findIndex;
    } else {
      return -1;
    }
  }

  /**
   Find the appropriate insertion point or match point in the note list,
   and use findIndex and findMatch to return the results.

   @param findNote Note we are looking for.
   */
  private void findInternal (Note findNote) {
    int low = 0;
    int high = notes.size() - 1;
    findIndex = 0;
    findMatch = false;
    while (high >= low
        && findMatch == false
        && findIndex < notes.size()) {
      int diff = high - low;
      int split = diff / 2;
      findIndex = low + split;
      int compare = get(findIndex).compareTo(findNote);
      if (compare == 0) {
        // found an exact match
        findMatch = true;
      }
      else
      if (compare < 0) {
        // note from list is less than the one we're looking for
        findIndex++;
        low = findIndex;
      } else {
        // note from list is greater than the one we're looking for
        if (high > findIndex) {
          high = findIndex;
        } else {
          high = findIndex - 1;
        }
      }
    } // end while looking for right position
  } // end find method
  
  public NotePositioned first (NotePositioned position) {
    if (position.navigateUsingList()) {
      return firstUsingList ();
    } else {
      return firstUsingTree ();
    }
  }
  
  public NotePositioned last (NotePositioned position) {
    if (position.navigateUsingList()) {
      return lastUsingList ();
    } else {
      return lastUsingTree ();
    }
  }

  public NotePositioned next (NotePositioned position) {
    NotePositioned nextPosition;
    if (position.navigateUsingList()) {
      nextPosition = nextUsingList (position);
    } else {
      nextPosition = nextUsingTree (position);
    }
    if (nextPosition == null) {
      return first(position);
    } else {
      return nextPosition;
    }
  }

  public NotePositioned prior (NotePositioned position) {
    if (position.navigateUsingList()) {
      return priorUsingList (position);
    } else {
      return priorUsingTree (position);
    }
  }

  public NotePositioned firstUsingList () {
    return positionUsingListIndex (0);
  }

  public NotePositioned lastUsingList () {
    return positionUsingListIndex (size() - 1);
  }

  public NotePositioned nextUsingList (NotePositioned position) {
    return (positionUsingListIndex (position.getIndex() + 1));
  }

  public NotePositioned priorUsingList (NotePositioned position) {
    return (positionUsingListIndex (position.getIndex() - 1));
  }

  public NotePositioned positionUsingListIndex (int index) {
    if (index < 0) {
      index = 0;
    }
    if (index >= size()) {
      index = size() - 1;
    }
    NotePositioned position = new NotePositioned(recDef);
    position.setIndex (index);
    position.setNavigator (NotePositioned.NAVIGATE_USING_LIST);
    if (index >= 0) {
      position.setNote (get (index));
      position.setTagsNode (position.getNote().getTagsNode());
    }
    return position;
  }

  public NotePositioned firstUsingTree () {
    return positionUsingNode (tagsModel.firstItemNode());
  }

  public NotePositioned lastUsingTree () {
    return positionUsingNode (tagsModel.lastItemNode());
  }

  public NotePositioned nextUsingTree (NotePositioned position) {
    if (position.getTagsNode() == null) {
      return null;
    } else {
      return positionUsingNode
          (tagsModel.nextItemNode(position.getTagsNode()));
    }
  }

  public NotePositioned priorUsingTree (NotePositioned position) {
    if (position.getTagsNode() == null) {
      return null;
    } else {
      return positionUsingNode
          (tagsModel.priorItemNode(position.getTagsNode()));
    }
  }

  public NotePositioned positionUsingNode (TagsNode node) {
    if (node == null) {
      return null;
    } else {
      NotePositioned position = new NotePositioned(recDef);
      position.setNote ((Note)node.getTaggable());
      position.setTagsNode (node);
      findInternal (position.getNote());
      position.setIndex (findIndex);
      position.setNavigator (NotePositioned.NAVIGATE_USING_TREE);
      return position;
    }
  }

  public int getColumnCount () {
    return 3;
  }

  public String getColumnName (int columnIndex) {
    switch (columnIndex) {
      case 0: return "Title";
      case 1: return "Tags";
      case 2: return "Link"; 
      default: return "?";
    }
  }

  public Class getColumnClass (int columnIndex) {
    return String.class;
  }

  public String getValueAt (int rowIndex, int columnIndex) {
    Note row = get(rowIndex);
    if (row == null) {
      return "";
    } else {
      switch (columnIndex) {
        case 0:
          return row.getTitle();
        case 1:
          return row.getTagsAsString(); 
        case 2:
          return row.getLinkAsString();
        default:
          return "Column " + String.valueOf(columnIndex);
      } // end switch
    } // end if good row
  } // end method getValueAt

  public Note get (int index) {
    if (index >= 0 && index < notes.size()) {
      return (Note)notes.get(index);
    } else {
      return null;
    }
  } // end method get (int)
  
  public Note getUnfiltered (int index) {
    if (index >= 0 && index < notes.size()) {
      return (Note)notes.get(index);
    } else {
      return null;
    }
  } // end method get (int)
  
  public void setTitle (String title) {
    this.title = title;
  }

  public String getTitle () {
    return title;
  }

  public int size() {
    return notes.size();
  }
  
  public int totalSize() {
    return notes.size();
  }

  public int getRowCount() {
    return notes.size();
  }

} // end NoteList class
