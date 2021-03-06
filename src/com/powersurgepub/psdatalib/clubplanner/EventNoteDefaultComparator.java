/*
 * Copyright 2012 - 2013 Herb Bowie
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

package com.powersurgepub.psdatalib.clubplanner;

  import java.util.*;

/**
  The default comparator for event notes. 

  @author Herb Bowie.
 */
public class EventNoteDefaultComparator 
    implements
      Comparator {
  
  private final static String noteClassName
      = "com.powersurgeppub.psdatalib.clubplanner.EventNote";
  
  public EventNoteDefaultComparator() {
    
  }
  
  /**
   Compare the two events and return the result. 
  
   @param note1 The first event note to be compared. 
   @param note2 The second event note to be compared. 
  
   @return -1 if the first event is lower than the second, 
           +1 of the first event is higher than the second, or
           zero if the two events have the same keys. 
  */
  public int compare (Object obj1, Object obj2) {
    
    int result = 0;
    
    if (! obj1.getClass().getName().equals(noteClassName)) {
      result = 1;
    } 
    else
    if (! obj2.getClass().getName().equals(noteClassName)) {
      result = -1;
    }
    
    if (result == 0) {
      EventNote note1 = (EventNote)obj1;
      EventNote note2 = (EventNote)obj2;
      result = note1.getNoteForYmd().compareTo(note2.getNoteForYmd());
      if (result == 0) {
        result = note1.getNoteFrom().compareTo(note2.getNoteFrom());
      }
      if (result == 0) {
        result = note1.getNoteVia().compareTo(note2.getNoteVia());
      }
    } 
    return result;
  }
  
  /**
   Determine whether the two events have equal keys.
  
   @param note1 The first event note to be compared. 
   @param note2 The second event note to be compared. 
  
   @return True if the two events have equal keys. 
  */
  public boolean equal (Object obj1, Object obj2) {
    return (compare(obj1, obj2) == 0);
  }

}
