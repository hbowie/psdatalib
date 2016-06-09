/*
 * Copyright 2012 - 2016 Herb Bowie
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

  import com.powersurgepub.psdatalib.pslist.*;
  import com.powersurgepub.psdatalib.script.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;
  import javax.swing.*;

/**
 A shell window to contain the various PSTextMerge tabs. 

 @author Herb Bowie
 */
public class TextMergeHarness 
  extends javax.swing.JFrame
    implements 
      ScriptExecutor,
      TextMergeController,
      WindowToManage{
  
  private static      TextMergeHarness    sharedHarness = null;  
  
  private             PSList              pslist = null;
  private             boolean             listAvailable = false;
  
  private             TextMergeController controller = null;
  
  private             ScriptExecutor      executor = null;
  private             boolean             allow = false;
  private             boolean             combineAllowed = false;
  private             boolean             inputModule = true;
  private             boolean             outputModule = false;
  
  private             TextMergeInput      textMergeInput = null;
  private             TextMergeScript     textMergeScript = null;
  private             TextMergeFilter     textMergeFilter = null;
  private             TextMergeSort       textMergeSort = null;
  private             TextMergeTemplate   textMergeTemplate = null;
  private             TextMergeOutput     textMergeOutput = null;
  
  private             int                 filterTabIndex = 0;
  private             int                 sortTabIndex = 1;
  private             int                 templateTabIndex = 2;
  
	/**
     Play a script.
   
     @param script    Name of script file to be played.
     @param logOutput Output destination for log messages.
   */
	public static void playScript (String script) {
    TextMergeHarness textMerge = TextMergeHarness.getShared();
    textMerge.initTextMergeModules();
    File scriptFile = new File(script);
    textMerge.playScript(scriptFile);
	} // end execScript method

  public static TextMergeHarness getShared() {
    if (sharedHarness == null) {
      sharedHarness = new TextMergeHarness();
    }
    return sharedHarness;
  }
  
  private TextMergeHarness() {
    pslist = new DataRecList();
    initComponents();
  }
  
  public void setList(PSList pslist) {
    this.pslist = pslist;
    if (textMergeInput != null
        && (pslist instanceof DataRecList)) {
      textMergeInput.setPSList(pslist);
    }
    if (textMergeFilter != null) {
      textMergeFilter.setPSList(pslist);
    }
    if (textMergeSort != null) {
      textMergeSort.setPSList(pslist);
    }
    if (textMergeTemplate != null) {
      textMergeTemplate.setPSList(pslist);
    }
    if (textMergeScript != null) {
    textMergeScript.setPSList(pslist);
    }
    if (textMergeOutput != null
        && (pslist instanceof DataRecList)) {
      textMergeOutput.setPSList(pslist);
    }
  }
  
  public void setController(TextMergeController controller) {
    this.controller = controller;
  }
  
  public void setExecutor(ScriptExecutor executor) {
    this.executor = executor;
    if (textMergeScript != null) {
      textMergeScript.setScriptExecutor(executor);
    }
  }
  
  public void enableInputModule(boolean inputModule) {
    this.inputModule = inputModule;
  }
  
  public void enableOutputModule(boolean outputModule) {
    this.outputModule = outputModule;
  }
  
  public void initTextMergeModules() {
    
    if (textMergeScript == null) {
      textMergeScript   = new TextMergeScript(pslist);

      if (controller == null) {
        textMergeInput  = new TextMergeInput (pslist, this, textMergeScript);
      } else {
        textMergeInput  = new TextMergeInput (pslist, controller, textMergeScript);
      }
      textMergeFilter   = new TextMergeFilter(pslist, textMergeScript);
      textMergeSort     = new TextMergeSort  (pslist, textMergeScript);
      textMergeTemplate = new TextMergeTemplate (pslist, textMergeScript);
      textMergeOutput   = new TextMergeOutput   (pslist, textMergeScript);

      textMergeScript.allowAutoplay(allow);

      textMergeScript.setInputModule(textMergeInput);
      textMergeScript.setFilterModule(textMergeFilter);
      textMergeScript.setSortModule(textMergeSort);
      textMergeScript.setTemplateModule(textMergeTemplate);
      textMergeScript.setOutputModule(textMergeOutput);

      if (executor == null) {
        textMergeScript.setScriptExecutor(this);
      } else {
        textMergeScript.setScriptExecutor(executor);
      }

      int tabCount = 0;
      
      if (inputModule) {
        textMergeInput.setTabs(this.getTabs());
        tabCount++;
      }
      
      textMergeScript.setTabs(this.getTabs());
      filterTabIndex = tabCount;
      tabCount++;
      
      textMergeFilter.setTabs(this.getTabs());
      sortTabIndex = tabCount;
      tabCount++;
      
      textMergeSort.setTabs(this.getTabs(), combineAllowed);
      templateTabIndex = tabCount;
      tabCount++;
      
      textMergeTemplate.setTabs(this.getTabs());
      tabCount++;
      
      if (outputModule) {
        textMergeOutput.setTabs(this.getTabs());
        tabCount++;
      }
      
      textMergeScript.selectEasyTab();
    }
  }
  
  public void allowAutoplay(boolean allow) {
    this.allow = allow;
    if (textMergeScript != null) {
      textMergeScript.allowAutoplay(allow);
    }
  }
  
  public void setCombineAllowed(boolean combineAllowed) {
    this.combineAllowed = combineAllowed;
    if (textMergeSort != null) {
      textMergeSort.setTabs(this.getTabs(), combineAllowed);
    }
  }
  
  public void setMenus(JMenuBar menus, String menuText) {
    if (textMergeScript != null) {
      textMergeScript.setMenus(menus, menuText);
    }
  }
  
  public TextMergeScript getTextMergeScript() {
    return textMergeScript;
  }
  
  public void playScript (File sFile) {
    textMergeScript.playScript(sFile);
  }
  
  public String getOutputFileName() {
    return textMergeTemplate.getOutputFileName();
  }
  
  /**
   Get the tabs containing all the TextMerge panels. 
  
   @return the tabs containing all the TextMerge panels. 
  */
  public JTabbedPane getTabs() {
    return jTabbedPane1;
  }
  
  /**
   Add to Window Menu. 
  */
  public void addToWindowMenuManager() {
    WindowMenuManager.getShared().add(this);
  }
  
  public void addScriptsToMenuBar(JMenuBar menuBar) {
    textMergeScript.setMenus(menuBar, "Scripts");
  }
  
  public void selectEasyTab() {
    textMergeScript.selectEasyTab();
  }
  
  public void clearSortAndFilterSettings() {
    if (textMergeScript != null) {
      textMergeScript.clearSortAndFilterSettings();
    }
  }
  
  /**
   A method provided to PSTextMerge 
  
   @param operand
   */
  public void scriptCallback(String operand) {

  }
  
  /**
   Indicate whether or not a list has been loaded. 
  
   @param listAvailable True if a list has been loaded, false if the list
                        is not available. 
  */
  public void setListAvailable (boolean listAvailable) {
    this.listAvailable = listAvailable;
  }
  
  /**
   Indicate whether or not a list has been loaded. 
  
   @return True if a list has been loaded, false if the list is not 
           available. 
  */
  public boolean isListAvailable() {
    return listAvailable;
  }
  
  /**
   This method is called from within the constructor to initialize the form.
   WARNING: Do NOT modify this code. The content of this method is always
   regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jTabbedPane1 = new javax.swing.JTabbedPane();

    setTitle("TextMerge");
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
      public void windowActivated(java.awt.event.WindowEvent evt) {
        formWindowActivated(evt);
      }
    });
    getContentPane().setLayout(new java.awt.GridBagLayout());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    getContentPane().add(jTabbedPane1, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    
  }//GEN-LAST:event_formWindowClosing

  private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

  }//GEN-LAST:event_formWindowActivated


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTabbedPane jTabbedPane1;
  // End of variables declaration//GEN-END:variables
}
