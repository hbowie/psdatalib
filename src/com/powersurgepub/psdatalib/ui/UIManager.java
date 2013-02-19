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
   An object that will build and run a user interface. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/02/05 - Originally written.
 */
import com.powersurgepub.psdatalib.tabdelim.TabDelimFile;
import com.powersurgepub.psdatalib.psdata.DataRecord;
  import com.apple.mrj.*;
	import com.powersurgepub.psutils.*;
  import java.awt.*;
  import java.io.*;
  import java.lang.reflect.*;
  import java.net.*;
  import javax.swing.*;
  import java.util.*;

public class UIManager {

  public  static final String 			RESOURCE_BUNDLE_NAME 	= "com.powersurgepub.ui.UIRsrc";
  public  static final String 			DEFAULT_UI_FILE_NAME	= "uitest_ui.txt";
  public  static final String 			PROGRAM_NAME					= "program.name";
  public  static final String 			UI_FILE_NAME_KEY 			= "uidef.filename";
  
  protected String									programName						= "unknown";
  
  private	static final String				MRJ_VERSION = "mrj.version";
  
  // System Properties and derived fields
  protected			String							mrjVersion;
  protected			String              userDirString;
  protected			String              fileSeparatorString;
  protected			File                userDirFile;
  protected			File                currentDirectory;
  protected			URL                 userDirURL; 
  
  protected ResourceBundle					resources;
  
  protected String									uiFileName;
  
  protected TabDelimFile						uiFile;
  
  protected DataRecord 							uiRec;
  protected String									recType;
  private		UIWindow								uiWindow;
  private		UIWidget								uiWidget;
  private   ArrayList								windows = new ArrayList();
  private   HashMap									uiObjects = new HashMap();
  private   Object									oldValue;
  
  /** The owner of this interface. */
  protected UIOwner									uiOwner;

	/**
	   Constructor.
	 */
	public UIManager (UIOwner uiOwner) {
    System.out.println ("UIManager Starting Up");
    this.uiOwner = uiOwner;
	} // end constructor
  
  /**
     Activate the user interface.
   */
  public void activate() {
    
    // Get System properties and derived values
    mrjVersion = System.getProperty(MRJ_VERSION);
    userDirString = System.getProperty (GlobalConstants.USER_DIR);
    if ((userDirString != null) && (! userDirString.equals (""))) {
      userDirFile = new File (userDirString);
      currentDirectory = userDirFile;
    }
    fileSeparatorString = System.getProperty (GlobalConstants.FILE_SEPARATOR, "\\");
    try {
      userDirURL = new URL ("file:"
        + userDirString
        + fileSeparatorString); 
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    
    // Get Resource Bundle properties
    try {
      resources = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
    } catch (MissingResourceException e) {
      uiFileName = DEFAULT_UI_FILE_NAME;
      System.out.println (RESOURCE_BUNDLE_NAME + " Resource Bundle Not Found");
      System.out.println (e.getMessage() + e.getClassName());
    }
    if (resources != null) {
      try {
        programName = resources.getString(PROGRAM_NAME);
        uiFileName = resources.getString(UI_FILE_NAME_KEY);
      } catch (MissingResourceException e) {
        uiFileName = DEFAULT_UI_FILE_NAME;
        System.out.println (UI_FILE_NAME_KEY + " not found in resource bundle");
      }
    }
    
    // Build interface dynamically from tab-delimited file
    try {
      uiFile = new TabDelimFile (uiFileName);
      uiFile.openForInput ();
      do {
        uiRec = uiFile.nextRecordIn();
        if (uiRec != null) {
          recType = uiRec.getFieldData(UIObject.REC_TYPE);
          if (recType.equals (UIObject.WINDOW_TYPE)) {
            uiWindow = new UIWindow (uiRec, this);
            windows.add(uiWindow);
            oldValue = uiObjects.put (uiWindow.getHandle(), uiWindow);
          }
          else
          if (recType.equals (UIObject.WIDGET_TYPE)) {
            uiWidget = new UIWidget (uiRec, this);
            uiWindow.add(uiWidget);
            oldValue = uiObjects.put (uiWidget.getHandle(), uiWidget);
          }
        }
      } while (uiRec != null);
      uiFile.close();
    } catch (IOException e) {
      System.out.println ("I/O Exception on UI File " + uiFileName);
    }
    
    // If running on Mac, set standard Handlers
    if (mrjVersion != null) {
      System.setProperty 
        ("com.apple.macos.useScreenMenubar", "true");
      System.setProperty 
        ("com.apple.mrj.application.apple.menu.about.name", programName);
      try {
        Object [] args = { this };
        Class [] arglist = { UIManager.class };
        Class mac_class = Class.forName ("com.powersurgepub.ui.MacHandler");
        Constructor new_one = mac_class.getConstructor (arglist);
        new_one.newInstance (args);
      }
      catch (Exception e) {
        System.out.println ("Trouble invoking class" + e.getMessage());
      }
    }
    
    // Fire up main window
    UIWindow mainWindow = (UIWindow)windows.get(0);
    mainWindow.activate();
	} // end constructor
  
  /**
     Standard way to respond to a About action.
   */
  public void handleAbout() {	

  }
  
  /**
     Standard way to respond to a document being passed to this application.
   */
  public void handleOpenFile (File inFile) {
  
    /* 
    Collection modulesCollection = modules.values();
    Iterator modulesIterator = modulesCollection.iterator();
    boolean fileHandlerFound = false;
    PSModule nextModule;
    while (modulesIterator.hasNext() && (! fileHandlerFound)) {
      nextModule = (PSModule)modulesIterator.next();
      if (nextModule.isFileCapable(inFile)) {
        nextModule.handleOpenFile(inFile);
        fileHandlerFound = true;
      } // end if module capable of handling this file
    } // end while looking through list of modules
    if (! fileHandlerFound) {
      // notify user somehow
    }
    */
    
    /*
    FileName inFileName = new FileName (inFile);
    String inFileNameExt = inFileName.getExt();
    if (inFileNameExt.equals ("tcz")) {
      inScriptFile = inFile;
      inScript = new ScriptFile (inScriptFile);
      setCurrentDirectoryFromFile (inScriptFile);
      playScript();
  
      scriptReplayButton.setEnabled (true);
      scriptReplay.setEnabled (true);
      
      tabs.setSelectedComponent (scriptTab);
    } else
    if (inFileNameExt.equals ("txt")
        || inFileNameExt.equals ("tab")
        || inFileNameExt.equals ("csv")) {
      chosenFile = inFile;
      openFileOrDirectory();
      
      tabs.setSelectedComponent (inputTab); 
    } */
  } // end handleOpenFile method
  
  /**
     Standard way to respond to a Quit action.
   */
  public void handleQuit() {	
    // stopScriptRecording();
    uiOwner.handleQuit();
		System.exit(0);
  }
	
	/**
	   Returns the object in string form.
	  
	   @return object formatted as a string
	 */
	public String toString() {
    return "uiManager";
	}
  
}

