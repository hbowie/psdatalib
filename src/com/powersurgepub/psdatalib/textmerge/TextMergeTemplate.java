/*
 * Copyright 1999 - 2014 Herb Bowie
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

package com.powersurgepub.psdatalib.textmerge;

  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.pslist.*;
  import com.powersurgepub.psdatalib.script.*;
  import com.powersurgepub.psdatalib.template.*;
  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psfiles.*;
  import com.powersurgepub.psutils.*;
  import java.awt.*;
  import java.awt.event.*;
  import java.io.*;
  import javax.swing.*;
  import javax.swing.border.*;

/**
 The template module used as part of PSTextMerge. 

 @author Herb Bowie
 */
public class TextMergeTemplate {
  
  private     PSList              psList = null;
  private     TextMergeScript     scriptRecorder = null;
  private     JTabbedPane         tabs = null;
  private     JMenuBar            menus = null;
  private     boolean             tabSet = false;
  private     boolean             menuSet = false;
  
  // Fields used for the User Interface
  private     Border              raisedBevel;
  private     Border              etched;
  
  // Template Panel Objects
  private     JPanel              templatePanel; 
  
  private     JButton             setWebRootButton;
  
  private     JButton             setTemplateLibraryButton;
  
  private     JButton             openTemplateButton;
  private     JButton             openTemplateFromLibraryButton;

  private     JButton             generateOutputButton;
  
  private     JLabel              setTemplateLibraryLabel;
  private     JLabel              openTemplateLabel;
  private     JLabel              generateOutputLabel;
  
  private     JLabel              templateLibraryName;
  private     JLabel              openTemplateName;
  private     JLabel              generateOutputName;
  
  private     JTextArea           templateText;
  
  private     GridBagger          gb = new GridBagger();
  
  // Template menu items
  private			JMenu								templateMenu;
  private     JMenuItem           setWebRoot;
  private			JMenuItem						templateOpen;
  private     JMenuItem           templateOpenFromLibrary;
  private			JMenuItem						templateGenerate;
  
  // Fields used for Template processing
  private     Template            template;
  private     File                templateFile;
  private     File                lastTemplateFile = null;
        
  private     boolean             templateCreated = false;
  private     boolean             templateFileReady = false;
  private     boolean             templateFileOK = false;
  private     boolean             generateOutputOK = false;
        
  private     String              templateFileName = "                         ";
  private     String              outputFileName   = "                         ";
  
  private static final String     TEMPLATE_LIBRARY_KEY = "templatelib";
  private     File                templateLibraryUserPref = null;
  private     File                templateLibraryAppFolder = null;
  private     File                templateLibraryFileSpec = null;
  private     File                templateLibraryButton = null;
  
  private     File                webRootFile = null;
  
  public TextMergeTemplate (PSList psList, TextMergeScript scriptRecorder) {
    this.psList = psList;
    this.scriptRecorder = scriptRecorder;
    
    // Initialize template library based on app preferences
    // These may be overridden later by list preferences
    templateLibraryUserPref = new File (UserPrefs.getShared().getPref (TEMPLATE_LIBRARY_KEY));
    templateLibraryAppFolder = new File 
          (Home.getShared().getAppFolder().getPath(),  "templates");
    
    setListOptions();
  }
  
  public void setPSList (PSList psList) {
    this.psList = psList;
    setListOptions();
  }
  
  private void setListOptions() {
    
    templateLibraryFileSpec = null;
    
    if (psList != null) {
      FileSpec source = psList.getSource();
      
      // Set template library
      String templateLibraryPath = null;
      if (source != null) {
        templateLibraryPath = source.getTemplatesFolder();
      }
      if (templateLibraryPath != null
          && templateLibraryPath.length() > 0) {
        templateLibraryFileSpec = new File (templateLibraryPath);
        if (! validLibrary (templateLibraryFileSpec)) {
          templateLibraryFileSpec = null;
        }
      } 
    }
  }
  
  public void setTemplateLibrary (File templateLibrary) {
    templateLibraryFileSpec = templateLibrary;
  }
  
  /**
   Do we have a valid template library?
  
   @return True if current template library variable points to a valid folder. 
  */
  public boolean validTemplateLibrary() {
    return (validLibrary (getTemplateLibrary()));
  }
  
  /**
   Return the first valid template library available, checking in this order
   of precedence:
   <ol>
     <li>the template library previously identified for the current source file;
     <li>the template library previously stored as a preference for this user;
     <li>the template library supplied with the application package. 
   </ol>
  @return A valid template library if one exists, otherwise null.
  */
  public File getTemplateLibrary() {
    if (validLibrary (templateLibraryFileSpec)) {
      return templateLibraryFileSpec;
    }
    else
    if (validLibrary (templateLibraryUserPref)) {
      return templateLibraryUserPref;
    } else {
      return templateLibraryAppFolder;
    }
  }
  
  private boolean validLibrary (File library) {
    return (library != null
        && library.exists()
        && library.isDirectory()
        && library.canRead());
  }
  
  private boolean fileAvailable() {
    return psList != null;
  }
  
  public void setMenus (JMenuBar menus) {
    
    menuSet = true;
    
    this.menus = menus;
    templateMenu = new JMenu("Template");
    
    // Equivalent Menu Item for Set Web Root
    setWebRoot = new JMenuItem ("Set Web Root...");
    templateMenu.add (setWebRoot);
    setWebRoot.addActionListener(new ActionListener()
      {
        public void actionPerformed (ActionEvent event) {
          setWebRoot();
        }
      }
    );
    
    // Equivalent Menu Item for Template Open
    templateOpen = new JMenuItem ("Open...");
    templateOpen.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_T,
        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    templateMenu.add (templateOpen);
    templateOpen.addActionListener (new ActionListener() 
      {
        public void actionPerformed (ActionEvent event) {
          // tabs.setSelectedComponent (templatePanel);
          openTemplateFile();		    
        } // end ActionPerformed method
      } // end action listener
    );
    
    // Equivalent Menu Item for Template Open
    templateOpenFromLibrary = new JMenuItem ("Open from Library...");
    templateMenu.add (templateOpenFromLibrary);
    templateOpenFromLibrary.addActionListener (new ActionListener() 
      {
        public void actionPerformed (ActionEvent event) {
          // tabs.setSelectedComponent (templatePanel);
          openTemplateFromLibrary();		    
        } // end ActionPerformed method
      } // end action listener
    );
    
    // Equivalent Menu Item for Template Generate
    templateGenerate = new JMenuItem ("Generate");
    templateGenerate.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_G,
        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    templateMenu.add (templateGenerate);
    templateGenerate.addActionListener (new ActionListener() 
      {
        public void actionPerformed (ActionEvent event) {
          // tabs.setSelectedComponent (templatePanel);
          generateTemplate();		    
        } // end ActionPerformed method
      } // end action listener
    );
    
    templateGenerate.setEnabled (false);
    
    menus.add(templateMenu);
  }
  
  public void setTabs(JTabbedPane tabs) {
    
    tabSet = true;
    
    this.tabs = tabs;
		templatePanel = new JPanel();
		
    // Create Open Button for Template File
    openTemplateButton = new JButton ("Open Template");
    openTemplateButton.setToolTipText
      ("Specify the Template File to be used for the Merge");
    openTemplateButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
          openTemplateFile();
		    } // end ActionPerformed method
		  } // end action listener
		);
    
    // Create Set Web Root Button
    setWebRootButton = new JButton ("Set Web Root");
    setWebRootButton.setToolTipText("Not yet set");
    setWebRootButton.addActionListener(new ActionListener ()
      {
        public void actionPerformed (ActionEvent event) {
          setWebRoot();
        }
      });
    
    // Create SetTemplate Library Button 
    setTemplateLibraryButton = new JButton ("Set Template Library");
    if (validTemplateLibrary()) {
      setTemplateLibraryButton.setToolTipText
        (getTemplateLibrary().toString());
    }
    setTemplateLibraryButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
          setTemplateLibrary();
		    } // end ActionPerformed method
		  } // end action listener
		);
    
    // Create Open from Library Button for Template File
    openTemplateFromLibraryButton = new JButton ("Open from Library");
    openTemplateFromLibraryButton.setToolTipText
      ("Specify the Template File to be used for the Merge");
    openTemplateFromLibraryButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
          openTemplateFromLibrary();
		    } // end ActionPerformed method
		  } // end action listener
		);
           
    // Create Generate Button for Template
    generateOutputButton = new JButton ("Generate Output");
    generateOutputButton.setToolTipText
      ("Merge the data file with the template and "
      + "generate the output file(s) specified by the Template");
    generateOutputButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
          generateTemplate();
		    } // end ActionPerformed method
		  } // end action listener
		);
    
    // Set Off Initially
    generateOutputButton.setEnabled (false);
          
    // Create Descriptive Labels Beside Buttons
    setTemplateLibraryLabel = new JLabel ("Template Library", JLabel.CENTER);
    setTemplateLibraryLabel.setBorder (etched);
    
    openTemplateLabel = new JLabel ("Input Template File", JLabel.CENTER);
    openTemplateLabel.setBorder (etched);
        
    generateOutputLabel = new JLabel ("Merged Output File(s)", JLabel.CENTER);
    generateOutputLabel.setBorder (etched);
    
    // Create Dynamic Labels to hold Current Values
    templateLibraryName = new JLabel();
    templateLibraryName.setAlignmentX(JLabel.CENTER);
    if (validTemplateLibrary()) {
      templateLibraryName.setText
        (getTemplateLibrary().toString());
    }
    templateLibraryName.setBorder (etched);
    
    openTemplateName = new JLabel (templateFileName, JLabel.CENTER);
    openTemplateName.setBorder (etched);
        
    generateOutputName = new JLabel (outputFileName, JLabel.CENTER);
    generateOutputName.setBorder (etched);
    
    // Bottom of Screen
		templateText = new JTextArea("");
		templateText.setLineWrap (true);
		templateText.setEditable (false);
		templateText.setWrapStyleWord (true);
		templateText.setVisible (true);

    // Finish up the Template Pane
		gb.startLayout (templatePanel, 3, 4);
		gb.setByRows (false);
		gb.setAllInsets (2);
		gb.setDefaultRowWeight (0.0);
    
		// Column 0
    gb.setRow (0);
    gb.setBottomInset(6);
    gb.add (setWebRootButton);
    gb.add (setTemplateLibraryButton);
    gb.setTopInset(6);
    gb.setBottomInset(2);
    gb.add (setTemplateLibraryLabel);
    gb.setAllInsets(2);
    gb.add (templateLibraryName);   
		
		// Column 1
    gb.nextColumn();
    gb.setBottomInset(6);
    gb.add (openTemplateButton);
    gb.add (openTemplateFromLibraryButton);
    gb.setTopInset(6);
    gb.setBottomInset(2);
    gb.add (openTemplateLabel);
    gb.setAllInsets(2);
    gb.add (openTemplateName);
    
    // Column 2
    gb.nextColumn();
    gb.setBottomInset(6);
    gb.add (generateOutputButton); 
    gb.setTopInset(6);
    gb.setBottomInset(2);
    gb.setRow (2);
    gb.add (generateOutputLabel);
    gb.setAllInsets(2);
    gb.add (generateOutputName);
    
    /* Column 2
    gb.nextColumn();
    gb.setRow(1);
    gb.setTopInset(6);
    gb.add (templatePlaceHolder);
    gb.setAllInsets(2);
     */
    
    // Bottom of Panel
    gb.setColumn (0);
    gb.setRow (4);
    gb.setWidth (3);
    gb.setRowWeight (1.0);
    gb.add (templateText);
    
    tabs.add("Template", templatePanel);
  
  }
    
  
  
  /**
   Select the tab for this panel. 
  */
  public void selectTab() {
    if (tabs != null) {
      tabs.setSelectedComponent (templatePanel);
    }
  }
  
  /**
     Play one recorded action in the Template module.
   */
  public void playTemplateModule (
      String inActionAction,
      String inActionModifier,
      String inActionObject,
      String inActionValue) {

    if (inActionAction.equals (ScriptConstants.OPEN_ACTION)) {
      templateFile = new File (inActionValue);
      if (templateFile == null) {
        Logger.getShared().recordEvent (LogEvent.MEDIUM, 
          inActionValue + " is not a valid " + inActionModifier 
          + " for a Template Open Action", true);
      }
      else {
        templateOpen();
      } // end file existence selector
    }
    else 
    if (inActionAction.equals (ScriptConstants.WEB_ROOT_ACTION)) {
      webRootFile = new File (inActionValue);
      if (webRootFile == null) {
        Logger.getShared().recordEvent (LogEvent.MEDIUM,
            inActionValue + " is not a valid local directory", true);
      } else {
        setWebRootWithFile();
      }
    }
    else
    if (inActionAction.equals (ScriptConstants.GENERATE_ACTION)) {
      checkTemplateRepeat();
      templateGenerate();
    } // end valid action
    else {
      Logger.getShared().recordEvent (LogEvent.MEDIUM, 
        inActionAction + " is not a valid Scripting Action for the Template Module",
        true);
    } // end Action selector
  } // end playTemplateModule method
  
  /**
     Open a Template File
   */
  private void openTemplateFile () {
    templateFileOK = false;
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (validTemplateLibrary()) {
      fileChooser.setCurrentDirectory(getTemplateLibrary());
    }
    int fileChooserReturn 
      = fileChooser.showOpenDialog (openTemplateButton);
    if (fileChooserReturn 
      == JFileChooser.APPROVE_OPTION) {
      templateFile = fileChooser.getSelectedFile();
      templateOpen();
      if (! templateFileOK) {
        JOptionPane.showMessageDialog (templatePanel, 
          "Error occurred while opening template file",
          "Template File Error",
          JOptionPane.ERROR_MESSAGE);
      } // end if error opening template file
    } // end if user performed a valid file selection
  }
  
  /**
     Opens the template file for input.
   */
  private void templateOpen() {
    // setCurrentDirectoryFromFile (templateFile);
    if (! templateCreated) {
      createNewTemplate();
    }
    templateFileReady = true;
    templateFileName = templateFile.getName();
    openTemplateName.setText (templateFileName);
    templateFileOK = template.openTemplate (templateFile);
    if (templateFileOK) {
      scriptRecorder.recordScriptAction (
          ScriptConstants.TEMPLATE_MODULE, 
          ScriptConstants.OPEN_ACTION, 
          ScriptConstants.TEXT_MODIFIER, 
          ScriptConstants.NO_OBJECT, 
          templateFile.getAbsolutePath());
      // setTemplateDirectoryFromFile (templateFile);
    } else {
      Logger.getShared().recordEvent (LogEvent.MEDIUM, 
        templateFileName + " could not be opened as a valid Template File",
        true);
    }
    
    // Set appropriate value for Generate Button
    if (generateOutputButton != null) {
      if (templateFileOK && fileAvailable() && templateCreated) {
        if (tabSet) {
          generateOutputButton.setEnabled (true);
        }
        if (menuSet) {
          templateGenerate.setEnabled (true);
        }
      } else {
        if (tabSet) {
          generateOutputButton.setEnabled (false);
        }
        if (menuSet) {
          templateGenerate.setEnabled (false);
        }
      }
    }
  } // end method templateOpen()
  
  /**
     Creates a new template object, to perform a new merge operation.
   */
  private void createNewTemplate () {
    template = new Template (Logger.getShared());
    templateCreated = true;
    outputFileName = "";
    generateOutputName.setText (outputFileName);
  }
  
  /**
     Set the location of the web root.
   */
  private void setWebRoot () {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
    int fileChooserReturn 
      = fileChooser.showOpenDialog (openTemplateFromLibraryButton);
    if (fileChooserReturn
        == JFileChooser.APPROVE_OPTION) {
      webRootFile = fileChooser.getSelectedFile();
      setWebRootButton.setToolTipText(webRootFile.getAbsolutePath());
      setWebRootWithFile();
    }
  }
  
  /**
     Sets the web root directory.
   */
  private void setWebRootWithFile () {

    if (webRootFile != null) {
      scriptRecorder.recordScriptAction (
          ScriptConstants.TEMPLATE_MODULE, 
          ScriptConstants.WEB_ROOT_ACTION, 
          ScriptConstants.TEXT_MODIFIER, 
          ScriptConstants.NO_OBJECT, 
          webRootFile.getAbsolutePath());
        // setTemplateDirectoryFromFile (templateFile);
    } else {
      Logger.getShared().recordEvent (LogEvent.MEDIUM, 
        webRootFile.getAbsolutePath() 
          + " could not be opened as a valid Web Root Directory",
        true);
    }
  } // end method setWebRootWithFile()
  
  /**
     Set the location of the template library.
   */
  private void setTemplateLibrary () {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
    int fileChooserReturn 
      = fileChooser.showOpenDialog (openTemplateFromLibraryButton);
    if (fileChooserReturn
        == JFileChooser.APPROVE_OPTION) {
      templateLibraryFileSpec = fileChooser.getSelectedFile();
      
      templateLibraryName.setText 
        (templateLibraryFileSpec.getName());
      setTemplateLibraryButton.setToolTipText
        (templateLibraryFileSpec.toString());
      saveTemplateDirectory();
      if (psList != null) {
        FileSpec source = psList.getSource();
        if (source != null) {
          source.setTemplatesFolder(templateLibraryFileSpec);
        }
      }
    }
  }
  
  /**
     Open a Template File from the Template Library
   */
  private void openTemplateFromLibrary () {
    templateFileOK = false;
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (validTemplateLibrary()) {
      fileChooser.setCurrentDirectory (getTemplateLibrary());
    } 
    int fileChooserReturn 
      = fileChooser.showOpenDialog (openTemplateButton);
    if (fileChooserReturn 
      == JFileChooser.APPROVE_OPTION) {
      templateFile = fileChooser.getSelectedFile();
      templateOpen();
      if (! templateFileOK) {
        JOptionPane.showMessageDialog (templatePanel, 
          "Error occurred while opening template file",
          "Template File Error",
          JOptionPane.ERROR_MESSAGE);
      } // end if error opening template file
    } // end if user performed a valid file selection
  }
  
  /**
     Generate the template results
   */
  private void generateTemplate () {
    checkTemplateRepeat();
    boolean repeatOK = false;
    boolean ok = true;
    if (templateFileOK && fileAvailable() && templateCreated) {
      if (
        // (! lastTabNameOutput.equals (""))
        (lastTemplateFile != null)
        // && (lastTabNameOutput.equals (tabName))
        && (lastTemplateFile.equals (templateFile))) {
        int userResponse = JOptionPane.showConfirmDialog 
          (templatePanel, 
          "Are you sure you want to repeat\n the last merge operation?",
          "Repeat Confirmation",
          JOptionPane.OK_CANCEL_OPTION,
          JOptionPane.WARNING_MESSAGE);
        repeatOK = (userResponse != JOptionPane.CANCEL_OPTION);
        ok = repeatOK;
      } // end if repeating a merge operation
      if (ok) {
        templateGenerate();
        if (! generateOutputOK) {
          Trouble.getShared().report(
            "Error occurred while generating output file",
            "Output File Error");
        } // end if output not OK 
      } // end if all necessary files ready
    } else {
      Trouble.getShared().report(
        "One or Both Input files Not Ready",
        "Input File Error");
    } // end input files not ready
  }
  
  /**
     Checks to see if a template file is to be reused.
   */
  private void checkTemplateRepeat () {
    if ((lastTemplateFile != null)
      && (! templateFileReady)) {
      templateFile = lastTemplateFile;
      templateOpen ();
    }
  } // end method checkTemplateRepeat
  
  /**
     Generates the output file specified in the template.
   */
  private void templateGenerate() {

    if (templateFileOK 
        && fileAvailable() 
        && templateCreated
        && psList instanceof DataSource) {
      DataSource source = (DataSource)psList;
      template.setWebRoot (webRootFile);
      if (psList.getSource() != null) {
        template.openData (source, psList.getSource().toString());
      }
      try {
        generateOutputOK = template.generateOutput();
      } catch (IOException e) {
        generateOutputOK = false;
      }
      lastTemplateFile = templateFile;
      // lastTabNameOutput = tabName;
      templateCreated = false;
      templateFileReady = false;
      if (generateOutputOK) {
        FileName textFileOutName = template.getTextFileOutName();
        if (textFileOutName == null) {
          outputFileName = null;
        } else {
          outputFileName = textFileOutName.toString();
        }
        if (outputFileName != null) {
          FileName outputFN = new FileName (outputFileName);
          generateOutputName.setText (outputFN.getFileName());
          generateOutputName.setToolTipText (outputFileName);
        }
        scriptRecorder.recordScriptAction (
          ScriptConstants.TEMPLATE_MODULE, 
          ScriptConstants.GENERATE_ACTION, 
          ScriptConstants.NO_MODIFIER, 
          ScriptConstants.NO_OBJECT, 
          ScriptConstants.NO_VALUE);
      } else {
        Logger.getShared().recordEvent (LogEvent.MEDIUM, 
          "Error occurred while generating output file from template "
          + templateFileName,
          true);
      }
    } else {
      Logger.getShared().recordEvent (LogEvent.MEDIUM, 
        "One or Both Input files (Template and/or Data) Not Ready",
        true);
    }
  } // end method templateGenerate
  
  private void setTemplateDirectoryFromFile (File inFile) {
    templateLibraryFileSpec = new File(inFile.getParent());
    saveTemplateDirectory();
  }
  
  private void setTemplateDirectoryFromDir (File inFile) {
    templateLibraryFileSpec = inFile;
    saveTemplateDirectory();
  }
  
  private void saveTemplateDirectory() {
    if (psList != null) {
      FileSpec source = psList.getSource();
      if (source != null) {
        source.setTemplatesFolder(templateLibraryFileSpec);
      }
    }
  }
  
  public void savePrefs() {
    // UserPrefs.getShared().setPref 
    //     (TEMPLATE_LIBRARY_KEY, templateLibrary.toString());
  }

}
