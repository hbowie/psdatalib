package com.powersurgepub.psdatalib.ui;

/**
   An application that test the various ui classes.
   
   This code is copyright (c) 2003 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
      </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 
    2003/03/09 - Initially written.
 */

public class UITest 
                      implements  UIOwner {
                                  
  protected UIManager			uiMgr;
  
	/**
     Main method.
   */
  public static void main(String args[]) {
    new UITest();
  }
  
	/**
     Constructor.
   */
  public UITest() {

    uiMgr = new UIManager (this);
    uiMgr.activate();

  }
	
  public void handleQuit() {	

  }

}
