package com.powersurgepub.psdatalib.pstags;

/**
 A list of Taggable objects.
 */
public interface TaggableList {

  /**
   Returns the size of the list.

   @param  The size of the list.
   */
  public int size();

  /**
   Return a taggable object from the list.

   @param  An index identifying which item from the list to be returned.
   @return The taggable item requested, or null if the passed index is
           not a valid reference to a taggable item in the list.
   */
  public Taggable get (int i);

}
