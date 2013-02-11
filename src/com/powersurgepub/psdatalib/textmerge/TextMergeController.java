package com.powersurgepub.psdatalib.textmerge;

/**
 The interface for an application calling the text merge modules. 

 @author Herb Bowie
 */
public interface TextMergeController {
  
  /**
   Indicate whether or not a list has been loaded. 
  
   @param listAvailable True if a list has been loaded, false if the list
                        is not available. 
  */
  public void setListAvailable (boolean listAvailable);
  
  /**
   Indicate whether or not a list has been loaded. 
  
   @return True if a list has been loaded, false if the list is not 
           available. 
  */
  public boolean isListAvailable();
  
}
