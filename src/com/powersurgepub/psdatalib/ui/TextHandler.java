package com.powersurgepub.psdatalib.ui;

/**
 An interface used by TextSelector to indicate that the user's text
 selection has been completed.

 com.powersurgepub.ui.TextSelector can be used to present the user with a
 popup list from which he or she can select a value.

 com.powersurgepub.ui.PopUpList provides the list that is displayed.

 com.powersurgepub.ui.TextHandler defines an interface for the class that is to
 be notified when text selection is complete.

 com.powersurgepub.psutils.ValueList is the class that provides the list
 from which the user will choose a value.
 
 @author Herb Bowie
 */
public interface TextHandler {
  
  public void textSelectionComplete ();

}
