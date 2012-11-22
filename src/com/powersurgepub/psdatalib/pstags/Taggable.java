package com.powersurgepub.psdatalib.pstags;

/**
 A class of objects assigned some tags. 
 */
public interface Taggable {

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
   Compare one taggable item to another, for sequencing purposes.

   @param  The second object to be compared to this one.
   @return A negative number if this taggable item is less than the passed
           taggable item, zero if they are equal, or a positive number if
           this item is greater.
   */
  public int compareTo (Object obj2);

  /**
   Determine if this taggable item has a key that is equal to the passed
   taggable item.

   @param  The second object to be compared to this one.
   @return True if the keys are equal.
   */
  public boolean equals (Object obj2);

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

}
