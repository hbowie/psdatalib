/*
 * Copyright 1999 - 2013 Herb Bowie
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
