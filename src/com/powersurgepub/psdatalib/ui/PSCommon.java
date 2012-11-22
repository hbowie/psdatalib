package com.powersurgepub.psdatalib.ui;
  
/**
   A common area holding data shared by all the many 
   PowerSurge classes. <p>
  
   This code is copyright (c) 2002 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2002/11/23 - Originally written.
 */
  import com.apple.mrj.*;
	import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import java.awt.event.*;
  import java.io.*;
  import java.lang.reflect.*;
  import java.net.*;
  import javax.swing.*;
  import java.util.*;

public abstract class PSCommon {
  
  // Specific Info about this program
  protected			String							programName = "*** unknown ***";
  protected			String							programHomePage;
  protected 		String							programRegPage;
  
  // System Properties and derived fields
  protected			String							mrjVersion;
  protected			String              userDirString;
  protected			String              fileSeparatorString;
  protected			File                userDirFile;
  protected			File                currentDirectory;
  protected			URL                 userDirURL; // was pageURL
                  
  // Logging info 
  protected			LogJuggler          logJuggler;
  protected			LogOutput           logout;
  protected			Logger              log;
  protected			LogEvent            logEvent;
  
  // User Preferences and Registration Info
  public static final String PREFS_FILE_NAME = "parms.txt";
  protected			FileInputStream     prefsIn;
  protected			FileOutputStream    prefsOut;
  protected			Properties					prefs;
  // protected			RegistrationCode		regCode;
  protected			String							registrationMessage = "** Unregistered **";
  
  // Collection of Modules
  protected			HashMap							modules;
  
  protected			PSModule						aboutModule;
  
  // Fields to use in building interface
  protected			File								helpHelpFile;
  protected			String							helpHelpName;
  protected			String							helpHelpString;
  
  // User Interface stuff
	protected			GridBagger          gb = new GridBagger();
  
  protected			JFrame							mainFrame;
	protected			JPanel              header;
	protected			JTabbedPane         tabs;
  
  protected			JMenuBar						menuBar;
  protected			JMenu								fileMenu;
  protected			JMenuItem						fileExit;
  protected			JMenu								helpMenu;
  protected			JMenuItem						helpAbout;
  protected			JMenuItem						helpHelp;
  protected			JMenuItem						helpHomePage;
  protected     JMenuItem						helpRegister;

	/**
	   Constructor.
	 */
	public PSCommon () {
  
    // Get needed System Properties
    mrjVersion = System.getProperty("mrj.version");
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
    
    // Load Preferences and determine Registration status
    prefs = new Properties (
        // RegistrationCode.getDefaultProperties()
        );
    try {
      prefsIn = new FileInputStream (PREFS_FILE_NAME);
      prefs.load (prefsIn);
    } catch (IOException e) {
        // no action needed -- unregistered
    }
    /*
    regCode = new RegistrationCode (prefs);
    if (regCode.isRegistered()) {
      registrationMessage = "Registered to: " + regCode.getUser();
    }
    else {
      registrationMessage = "** Unregistered **";
    }
     */
    
    modules = new HashMap();
    
	} // end constructor
  
  /**
     Start up operations that need initialization provided by child Class.
   */
  public void startUI () {
  
    // Set up logging stuff
    logJuggler = new LogJuggler(programName);
    logJuggler.setLog (LogJuggler.LOG_WINDOW_STRING);
    logout = logJuggler.getLog();
    log = logJuggler.getLogger();
    log.setLogAllData (false);
    log.setLogThreshold (LogEvent.MINOR);
    logEvent = new LogEvent();
    
  	// Create main frame for user interface
    mainFrame = new JFrame(programName);
	  mainFrame.getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
		mainFrame.addWindowListener (new WindowAdapter()
		  {
		    public void windowClosing (WindowEvent e) {
          handleQuit();
		    } // end ActionPerformed method
		  } // end action listener for filter fields combo box
		); 
    mainFrame.setSize(650, 450);
    mainFrame.setResizable (true);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setLocation( (d.width - mainFrame.getSize().width) / 2,
			(d.height - mainFrame.getSize().height) / 2);
    
    // Create Menu Bar
    menuBar = new JMenuBar();
    mainFrame.setJMenuBar (menuBar);
    fileMenu = new JMenu("File");
    menuBar.add (fileMenu);
    
    // Create basic components of main window
    header = new JPanel();
		tabs = new JTabbedPane();
  } // end startUI
  
  public void finishUI () {
  
    // Finish off File Menu
    if (mrjVersion == null) {
      fileExit = new JMenuItem ("Exit/Quit");
      fileExit.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_Q,
        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      fileMenu.add (fileExit);
      fileExit.addActionListener (new ActionListener ()
        {
          public void actionPerformed (ActionEvent event) {
            handleQuit();
          } // end actionPerformed method
        } // end action listener
      );
    }
    
    // Help Menu 
    helpMenu = new JMenu("Help");
    menuBar.add (helpMenu);
    
    if (mrjVersion == null && aboutModule != null) {
      helpAbout = new JMenuItem ("About " + programName);
      helpMenu.add (helpAbout);
      helpAbout.addActionListener (new ActionListener ()
        {
          public void actionPerformed (ActionEvent event) {
            handleAbout();
          } // end actionPerformed method
        } // end action listener
      );
    }
    
    if (helpHelpString != null) {
      helpHelp = new JMenuItem (helpHelpName);
      helpMenu.add (helpHelp);
      helpHelp.addActionListener (new ActionListener() 
        {
          public void actionPerformed (ActionEvent event) {
            openURL (helpHelpString);
          } // end ActionPerformed method
        } // end action listener
      );
    }

    if (programHomePage != null) {
      helpHomePage = new JMenuItem (programName + " Home Page");
      helpMenu.add (helpHomePage);
      helpHomePage.addActionListener (new ActionListener() 
        {
          public void actionPerformed (ActionEvent event) {
            openURL (programHomePage);
          } // end ActionPerformed method
        } // end action listener
      );
    }
    
    if (programRegPage != null) {
      helpRegister = new JMenuItem ("Purchase Registration Code");
      helpMenu.add (helpRegister);
      helpRegister.addActionListener (new ActionListener() 
        {
          public void actionPerformed (ActionEvent event) {
            openURL (programRegPage);
          } // end ActionPerformed method
        } // end action listener
      );
    }

    // If running on Mac, set Handlers
    if (mrjVersion != null) {
      System.setProperty 
        ("com.apple.macos.useScreenMenubar", "true");
      System.setProperty 
        ("com.apple.mrj.application.apple.menu.about.name", programName);
      try {
        Object [] args = { this };
        Class [] arglist = { PSCommon.class };
        Class mac_class = Class.forName ("com.powersurgepub.ui.MacHandler");
        Constructor new_one = mac_class.getConstructor (arglist);
        new_one.newInstance (args);
      }
      catch (Exception e) {
        System.out.println ("Trouble invoking class" + e.getMessage());
      }
    }
    
    // Add the Tabbed Pane to the main frame
		mainFrame.getContentPane().add (tabs, BorderLayout.CENTER);
    
    // Show the results to the user
		mainFrame.setVisible (true);

  } // end finishUI method
  
  public void addModule (PSModule module) {
    PSModule result = (PSModule)modules.put (module.getName(), module);
    if (module.isAboutCapable()) {
      aboutModule = module;
    }
    tabs.addTab (module.getName(), module.getPanel());
  }
  
  /**
     Standard way to respond to an About Menu Item Selection.
   */
  public void handleAbout() {
    if (aboutModule != null) {
      aboutModule.handleAbout();
    }
  }
  
  /**
     Standard way to respond to a document being passed to this application.
   */
  public void handleOpenFile (File inFile) {
  
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
     Standard way to respond to a Quit Menu Item on a Mac.
   */
  public void handleQuit() {	
    // stopScriptRecording();
		System.exit(0);
  } // end handleQuit method
  
  /**
     Throw a URL to the local Web Browser for display.
   */
  private void openURL (String inURL) {
    try {
      BrowserLauncher.openURL (inURL);
    } catch (IOException e) {
      JOptionPane.showMessageDialog (tabs, 
        e.getMessage(),
        "Browser Error",
        JOptionPane.ERROR_MESSAGE);
    }
  } // end openURL method
  
}

