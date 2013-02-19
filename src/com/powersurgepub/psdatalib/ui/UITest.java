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
