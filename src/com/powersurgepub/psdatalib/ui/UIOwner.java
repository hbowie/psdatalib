package com.powersurgepub.psdatalib.ui;
  
/**
   An object representing the owner/customer for a User Interface
   managed by UIManager. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/02/09 - Originally written.
 */

public interface UIOwner {

  /**
	   Handles a Quit Command. This method should execute any
     cleanup activities that need to be performed before the 
     program execution ends. This method need not actually terminate
     processing, since this will be done by the UIManager.
	 */
  public void handleQuit ();
    
}

