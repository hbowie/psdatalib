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

  import com.powersurgepub.psdatalib.ui.ValueList;

/**
   A collection of values that are used as tags or categories.
   New values are added to the list. The
   list is maintained in alphabetical order. A JComboBox is maintained
   and kept synchronized with the list.<p>
  
   This code is copyright (c) 2003-2009 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/18 - Originally written.
 */
public class TagsList 
    extends ValueList  {
  
  /** Creates a new instance of TagsList */
  public TagsList() {
  }
  
  public void add(Taggable tagged) {
    TagsIterator iterator = new TagsIterator (tagged.getTags());
    while (iterator.hasNextTag()) {
      registerValue (iterator.nextTag());
    }
  }
  
  public void modify(Taggable tagged) {
    TagsIterator iterator = new TagsIterator (tagged.getTags());
    while (iterator.hasNextTag()) {
      registerValue (iterator.nextTag());
    }
  }
  
  public void remove(Taggable tagged) {
    // No need to do anything
  }
  
}
