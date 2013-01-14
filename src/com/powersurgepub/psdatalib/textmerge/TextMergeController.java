package com.powersurgepub.psdatalib.textmerge;

/**
 The interface for an application calling the text merge modules. 

 @author Herb Bowie
 */
public interface TextMergeController {
  
  /**
   Is the user registered?
  
   @return True if we should treat the user with unrestricted access rights, 
           false if we should limit their usage in some way. 
  */
  public boolean isRegistered();
  
  /**
   Handle the condition of not saving all user input due to the application
   not being registered.
   */
  public void handleRegistrationLimitation ();
  
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
