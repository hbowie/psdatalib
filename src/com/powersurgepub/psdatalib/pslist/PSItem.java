package com.powersurgepub.psdatalib.pslist;

  import com.powersurgepub.psdatalib.pstags.*;

/**
 A single item from a list. 

 @author Herb Bowie
 */
public interface PSItem {
  
  /**
   Compare one item to another, for sequencing purposes.

   @param  The second object to be compared to this one.
   @return A negative number if this item is less than the passed
           item, zero if they are equal, or a positive number if
           this item is greater.
   */
  public int compareTo (Object obj2);

  /**
   Determine if this item has a key that is equal to the passed
   item.

   @param  The second object to be compared to this one.
   @return True if the keys are equal.
   */
  public boolean equals (Object obj2);
  
  /**
   Return the tags assigned to this taggable item. 
   
   @return The tags assigned. 
   */
  public Tags getTags ();

  /**
   Flatten all the tags for this item, separating each level/word into its own
   first-level tag.
   */
  public void flattenTags();

  /**
   Convert the tags to all lower-case letters.
   */
  public void lowerCaseTags ();

  /**
   Set the first TagsNode occurrence for this Taggable item. This is stored
   in a TagsModel occurrence.

   @param tagsNode The tags node to be stored.
   */
  public void setTagsNode (TagsNode tagsNode);

  /**
   Return the first TagsNode occurrence for this Taggable item. These nodes
   are stored in a TagsModel occurrence.

   @return The tags node stored. 
   */
  public TagsNode getTagsNode ();
  
  /**
   Return the value stored in the indicated column. 
  
   @param columnIndex An index to the desired column, with zero pointing
                      to the first column. 
  
   @return An Object stored at the indicated column. 
  */
  public Object getColumnValue (int columnIndex);
  
}
