package com.powersurgepub.psdatalib.pstags;

/**
 A filtering mechanism. 

 @author Herb Bowie
 */
public interface TaggableSelector {
  
  /**
   Determine whether taggable item should be selected. 
  
   @param taggedItem A Taggable item. 
  
   @return True if selected, false otherwise. 
  */
  public boolean selected (Taggable taggedItem);
  
}
