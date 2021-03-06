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

	import com.apple.mrj.*;
  import java.io.*;
  import javax.swing.*;
  
/**
 * A standard interface for a user interfacing program designed to
 * run on a Macintosh under OS X. <p>
 *
 * This code is copyright (c) 2002-2003 by Herb Bowie.
 * All rights reserved. <p>
 *
 * Version History: <ul><li>
 *     </ul>
 *
 * @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
 *         herb@powersurgepub.com</a>)<br>
 *         of PowerSurge Publishing 
 *         (<a href="http://www.powersurgepub.com">
 *         www.powersurgepub.com</a>)
 *
 * @version 2003/05/22 - Originally written.
 */

public interface MacUI {

  /**
     Standard way to respond to an About Menu Item Selection on a Mac.
   */
  public void handleAbout();
  
  /**      
    Standard way to respond to a document being passed to this application on a Mac.
   
    @param inFile File to be processed by this application, generally
                  as a result of a file or directory being dragged
                  onto the application icon.
   */
  public void handleOpenFile (File inFile);

  /**
     Standard way to respond to a Quit Menu Item on a Mac.
   */
  public void handleQuit();
    
}

