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

package com.powersurgepub.psdatalib.textmerge;

  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.pslist.*;
  import com.powersurgepub.psdatalib.script.*;
  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psdatalib.textmerge.input.*;
  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psfiles.*;
  import com.powersurgepub.psutils.*;
  import java.awt.*;
  import java.awt.event.*;
  import java.io.*;
  import java.net.*;
  import java.util.*;
  import java.util.zip.*;
  import javax.swing.*;
  import javax.swing.border.*;

/**
 The Text Merge module for writing output. 

 @author Herb Bowie
 */
public class TextMergeOutput {
  
  private     PSList              psList = null;
  private     DataRecList         dataRecList = null;
  
  private     TextMergeController textMergeController = null;
  private     TextMergeScript     textMergeScript = null;
  
  private     JTabbedPane         tabs = null;
  private     JMenuBar            menus = null;
  
  private     boolean             quietMode = true;
  private     boolean             tabSet = false;
  private     boolean             menuSet = false;
  
  // Fields used for the User Interface
  private     Border              raisedBevel;
  private     Border              etched;
  private     GridBagger          gb = new GridBagger();
  
  // Output Panel objects
  private     JPanel              outputTab;
  
  // Open Output Button
  private     JButton             openOutputDataButton;
  private     JLabel              openOutputDataLabel;
  private     JLabel              openOutputDataName;
  
  // Data Dictionary Check Box
  private    JLabel               outputDictionaryLabel;
  private    JCheckBox            outputDictionaryCkBox;
  
  // Place holder for Third Column
  private    JLabel								outputPlaceHolder;
  
  private     JTextArea           outputText;
  
  private			JMenuItem						fileSave;
  
  // Fields used for Output processing
  private     File                chosenOutputFile;
  private     String              tabNameOutput = "";
  private     FileName            tabFileName;
  private			TabDelimFile				tabFileOutput;
  
  // Data Dictionary Fields
	private			TabDelimFile				dictFile;
	private     DataDictionary      dataDict;
  private			boolean							usingDictionary = false;
  private     String              usingDictionaryValue = "No";
  public static final String      DICTIONARY_EXT = "dic";
  
  // Fields used for logging
  
  /** Log used to record events. */
  private    Logger             log = Logger.getShared();
  
  /** Should all data be logged (or only data preceding significant events(? */
  private    boolean            dataLogging = false;
  
  public TextMergeOutput (
      PSList psList, 
      TextMergeScript textMergeScript) {
    
    this.psList = psList;
    
    setListOptions();
    
    this.textMergeController = textMergeController;
    this.textMergeScript = textMergeScript;
  }
  


  public void setPSList (PSList psList) {
    this.psList = psList;
    setListOptions();
    setListAvailable(true);
  }
  
  public void setListAvailable (boolean listAvailable) {
    if (listAvailable) {
      
    } else {
      
    }
    if (openOutputDataButton != null) {
      openOutputDataButton.setEnabled (listAvailable);
    }
    if (fileSave != null) {
      fileSave.setEnabled (listAvailable);
    }
  }
  
  private void setListOptions() {
    if (psList instanceof DataRecList) {
      dataRecList = (DataRecList)psList;
    } else {
      dataRecList = null;
      throw new IllegalArgumentException("List must be a DataRecList");
    }
  }
  
  public void setMenus(JMenuBar menus) {
    quietMode = false;
    menuSet = true;
    
    this.menus = menus;
    
    JMenu fileMenu = null;
    boolean fileMenuFound = false;
    int i = 0;
    while (i < menus.getMenuCount() && (! fileMenuFound)) {
      Component menuElement = menus.getComponent(i);
      if (menuElement instanceof JMenu) {
        fileMenu = (JMenu)menuElement;
        if (fileMenu.getText().equals("File")) {
          fileMenuFound = true;
        }
      }
    }
    
    // Equivalent Menu Item
    if (fileMenuFound) {
      fileSave = new JMenuItem ("Save...");
      fileSave.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_S,
          Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      fileMenu.add (fileSave);
      fileSave.addActionListener (new ActionListener() 
        {
          public void actionPerformed (ActionEvent event) {
            tabs.setSelectedComponent (outputTab);
            saveOutputFile();		    
          } // end ActionPerformed method
        } // end action listener
      );
      fileSave.setEnabled (false);
    }
  }
  
  public void setTabs(JTabbedPane tabs) {
    quietMode = false;
    tabSet = true;
    
    this.tabs = tabs;
    
    // Create common interface components
    raisedBevel = BorderFactory.createRaisedBevelBorder();
    etched      = BorderFactory.createEtchedBorder();
    
		outputTab = new JPanel();
		
    // Button to Specify the Output Source and Open it
    openOutputDataButton = new JButton ("Save Output");
    openOutputDataButton.setToolTipText
      ("Specify the Output File Name and Location");
    openOutputDataButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
          saveOutputFile();
		    } // end ActionPerformed method
		  } // end action listener
		);
    
    // Set Off Initially
    openOutputDataButton.setEnabled (false);
        
    openOutputDataLabel       = new JLabel ("Output Data Destination", JLabel.CENTER);
    openOutputDataLabel.setBorder (etched);
        
    openOutputDataName    = new JLabel (tabNameOutput, JLabel.CENTER);
    openOutputDataName.setBorder (etched);
    
    // Create Check Box for Data Dictionary Output
    outputDictionaryLabel = new JLabel ("Data Dictionary Output", JLabel.LEFT);
    outputDictionaryLabel.setBorder (etched);
    
    outputDictionaryCkBox = new JCheckBox ("Save Companion Dictionary?");
    outputDictionaryCkBox.setSelected (false);
    usingDictionary = false;
    outputDictionaryCkBox.addItemListener (new ItemListener()
		  {
		    public void itemStateChanged (ItemEvent event) {
          usingDictionary 
              = (event.getStateChange() != ItemEvent.DESELECTED);
          setDictionaryImplications();
		    } // end itemStateChanged method
		  } // end action listener
		);
    
    // Create place holder for third column
    outputPlaceHolder       
        = new JLabel ("                            ", JLabel.CENTER);
    
    // Bottom of Screen
		outputText = new JTextArea("");
		outputText.setLineWrap (true);
		outputText.setEditable (false);
		outputText.setWrapStyleWord (true);
		outputText.setVisible (true);

    // Finish up the Input Pane
    setDictionaryImplications();
		gb.startLayout (outputTab, 3, 4);
		gb.setByRows (false);
		gb.setAllInsets (2);
		gb.setDefaultRowWeight (0.0);
		
		// Column 0
    gb.setBottomInset(6);
    gb.add (openOutputDataButton); 
    gb.setTopInset(6);
    gb.setBottomInset(2);
    gb.add (openOutputDataLabel);
    gb.setAllInsets(2);
    gb.add (openOutputDataName);
    
    // Column 1
    gb.nextColumn();
    gb.setRow(1);
    gb.setTopInset(6);
    gb.add (outputDictionaryLabel);
    gb.setAllInsets(2);
    gb.add (outputDictionaryCkBox);
    
    // Column 2
    gb.nextColumn();
    gb.setRow(1);
    gb.setTopInset(6);
    gb.add (outputPlaceHolder);
    gb.setAllInsets(2);
       
    // Bottom of Panel
    gb.setColumn (0);
    gb.setRow (3);
    gb.setWidth (3);
    gb.setRowWeight (1.0);
    gb.add (outputText);

		tabs.addTab ("Output", outputTab);
  }
  
  /**
     Open and save output file.
   */
  private void saveOutputFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (textMergeScript.hasCurrentDirectory()) {
      fileChooser.setCurrentDirectory (textMergeScript.getCurrentDirectory());
    } 
    int fileChooserReturn 
      = fileChooser.showSaveDialog (openOutputDataButton);
    if (fileChooserReturn 
      == JFileChooser.APPROVE_OPTION) {
      chosenOutputFile = fileChooser.getSelectedFile();
      createOutput();
      openOutputDataName.setText (tabNameOutput);
    }
  }
  
  /**
     Create output file of tab-delimited records.
   */
  private void createOutput() {
    textMergeScript.setCurrentDirectoryFromFile (chosenOutputFile);
    tabFileOutput = new TabDelimFile (chosenOutputFile);
    tabFileOutput.setLog (log);
    tabFileOutput.setDataLogging (false);
    boolean outputOK = true;
    try {
      tabFileOutput.openForOutput (dataRecList.getRecDef());
    } catch (IOException e) {
      outputOK = false;
      log.recordEvent (LogEvent.MEDIUM, 
        "Problem opening Output File",
        false);
    }
    if (outputOK) {
      dataRecList.openForInput();
      DataRecord inRec;
      do {
        inRec = dataRecList.nextRecordIn ();
        if (inRec != null) {
          try {
            tabFileOutput.nextRecordOut (inRec);
          } catch (IOException e) {
            log.recordEvent (LogEvent.MEDIUM, 
              "Problem writing to Output File",
              true);
          }
        }
      } while (dataRecList.hasMoreRecords());
      dataRecList.close();
      try {
        tabFileOutput.close();
      } catch (IOException e) {
      }
      tabNameOutput = chosenOutputFile.getName();
      openOutputDataName.setText (tabNameOutput);
      if (usingDictionary) {
        tabFileName = 
          new FileName (chosenOutputFile.getAbsolutePath());
        dictFile = 
          new TabDelimFile (textMergeScript.getCurrentDirectory(),
            tabFileName.replaceExt(DICTIONARY_EXT));
        dictFile.setLog (log);
        try {
          dataDict.store (dictFile);
        } catch (IOException e) {
          log.recordEvent (LogEvent.MEDIUM, 
              "Problem writing Output Dictionary",
              true);
        }
      } 
      textMergeScript.recordScriptAction (
          ScriptConstants.OUTPUT_MODULE, 
          ScriptConstants.OPEN_ACTION, 
          ScriptConstants.NO_MODIFIER,
          ScriptConstants.NO_OBJECT, 
          chosenOutputFile.getAbsolutePath());
    }
  }
  
  /**
     Depending on we are using a data dictionary, sets other
     appropriate values.
   */
  private void setDictionaryImplications() {
    if (usingDictionary) {
      usingDictionaryValue = "Yes";
    } else {
      usingDictionaryValue = "No";
    }
    textMergeScript.recordScriptAction 
     (ScriptConstants.OUTPUT_MODULE, 
      ScriptConstants.SET_ACTION, 
      ScriptConstants.NO_MODIFIER, 
      ScriptConstants.USING_DICTIONARY_OBJECT, 
      String.valueOf(usingDictionary));
  }

}
