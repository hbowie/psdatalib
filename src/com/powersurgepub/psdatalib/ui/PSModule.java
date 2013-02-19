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
   A object representing a major collection of functions and associated
   user interface componenets. <p>
  
   This code is copyright (c) 2002 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2002/11/24 - Originally written.
 */

	import com.powersurgepub.psutils.*;
  import java.awt.*;
  import java.awt.event.*;
  import java.io.*;
  import java.net.*;
  import javax.swing.*;
  import java.util.*;

public abstract class PSModule {
  
  // Common area
  private							PSCommon						common;
  
  // Name of module
  protected						String							name;
  
  // Will this module tell the user about the program?
  protected						boolean							aboutCapable = false;
  
  // Collection of file extensions handled by this module
  protected						HashSet							fileExtensions;
  
	protected			      JPanel              panel;
  
	/**
	   Constructor.
	 */
	public PSModule (PSCommon common) {
    this.common = common;
    fileExtensions = new HashSet();
    panel = new JPanel();
	}
  
  /**
     Start up operations that need initialization provided by child Class.
   */
  public void startup () {
  
  }
  
  public boolean isAboutCapable () {
    return aboutCapable;
  }
  
  /**
     Standard way to respond to a document being passed to this module.
   */
  public void handleAbout() {
  }
  
  protected void addFileExtension (String ext) {
    String lowerExt;
    if (ext.length() > 3 && ext.charAt  (0) == '.') {
      lowerExt = ext.substring(1, ext.length()-1).toLowerCase();
    } else {
      lowerExt = ext.toLowerCase();
    }
    fileExtensions.add (lowerExt);
  }
  
  public boolean isFileCapable (File inFile) {
    FileName inFileName = new FileName (inFile);
    String inExt = inFileName.getExt();
    return fileExtensions.contains (inExt);
  }
  
  public void handleOpenFile (File inFile) {
  
  } // end handleOpenFile method
  
  public String getName() {
    return name;
  }
  
  public JPanel getPanel() {
    return panel;
  }
  
}

