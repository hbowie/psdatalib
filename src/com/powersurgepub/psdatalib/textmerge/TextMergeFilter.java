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

  import com.powersurgepub.psdatalib.script.*;
  import com.powersurgepub.psdatalib.pslist.*;
  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psutils.*;
  import java.awt.event.*;
  import javax.swing.*;

/**
 The filtering module used as part of PSTextMerge. 

 @author Herb Bowie
 */
public class TextMergeFilter {
  
  private     PSList              psList = null;
  private     TextMergeScript     scriptRecorder = null;
  private     JTabbedPane         tabs = null;
  private     JMenuBar            menus = null;
  
  // Filter panel objects    
  private     JPanel              filterPanel;
  private     JLabel              filterFieldsLabel   = new JLabel();
  private     PSComboBox          filterFieldsBox;
  private     PSComboBox          filterOperandBox;
  private     String[]            defaultFilterValues = {" "};
  private     PSComboBox          filterValueBox;
  private     JRadioButton        filterAndButton     = new JRadioButton("And");
  private     JRadioButton        filterOrButton      = new JRadioButton("Or");
  private     ButtonGroup         filterAndOrGroup;
  private     JButton             filterAddButton     = new JButton();
  private     JButton             filterClearButton   = new JButton();
  private     JButton             filterSetButton     = new JButton();
  private     JLabel              filterTextLabel     = new JLabel();
  private     JScrollPane         filterTextScrollPane;
  private     JTextArea           filterText = new JTextArea("");
  
	private     GridBagger          gb = new GridBagger();
  
  // Fields using for filtering
  private     boolean             filterTabBuilt      = false;
  private     TextMergeFilter     textMergeFilter;
	private     PSFieldFilter       fieldFilter;
	private     PSItemFilter        itemFilter;
	private     String              currentFilterField;
	private     int                 currentFilterColumn;
	private     String              currentFilterOperand;
	private     String              currentFilterValue = " ";
	private     boolean             currentAndLogic = true;
  
  public TextMergeFilter(PSList psList, TextMergeScript scriptRecorder) {
    this.psList = psList;
    this.scriptRecorder = scriptRecorder;
    initItemFilter();
  }
  
  public void setPSList (PSList psList) {
    this.psList = psList;
    loadFilterFields();
  }
  
  public void setTabs(JTabbedPane tabs) {

    this.tabs = tabs;
		filterPanel = new JPanel();
		
		// General Instructions for use of the Filter Tab
		filterFieldsLabel.setVisible(true);
		filterFieldsLabel.setText ("Add desired filter fields then Set the result:");
		
		filterValueBox = new PSComboBox (defaultFilterValues);
		filterValueBox.setEditable (true);
		
		// Combo box to select a field to filter on
		filterFieldsBox = new PSComboBox();
		loadFilterFields();
		filterFieldsBox.setEditable (false);
		filterFieldsBox.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      PSComboBox cb = (PSComboBox)event.getSource();
		      currentFilterField = (String)cb.getSelectedItem();
		      loadFilterValues();
		    } // end ActionPerformed method
		  } // end action listener for filter fields combo box
		); 
		
		// Combo box to select a logical operand for comparison to a value
		filterOperandBox = new PSComboBox (PSField.WORD_LOGICAL_OPERANDS);
		filterOperandBox.setSelectedIndex (0);
		currentFilterOperand = (String)filterOperandBox.getSelectedItem();
		filterOperandBox.setEditable (false);
		filterOperandBox.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      PSComboBox cb = (PSComboBox)event.getSource();
		      currentFilterOperand = (String)cb.getSelectedItem();
		    } // end actionPerformed method
		  } // end action listener for filter operand combo box
		); 
		
		// Combo box to select or enter a value for comparison
		filterValueBox.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      PSComboBox cb = (PSComboBox)event.getSource();
		      currentFilterValue = (String)cb.getSelectedItem();
		    } // end actionPerformed method
		  } // end action listener for filter value combo box
		);
		
		// Radio button to select and logic
		filterAndButton.setActionCommand ("and");
		filterAndButton.setSelected (true);
		currentAndLogic = true;
		filterAndButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      filterAndOr (true);
		    } // end actionPerformed method
		  } // end action listener for filter and radio button
		);
		      
		// radio button to select or logic
		filterOrButton.setActionCommand ("or");
		filterOrButton.setSelected (false);
		filterOrButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      filterAndOr (false);
		    } // end actionPerformed method
		  } // end action listener for filter or radio button
		);
		
		filterAndOrGroup = new ButtonGroup();
		filterAndOrGroup.add(filterAndButton);
		filterAndOrGroup.add(filterOrButton);
		
		// Button to add current filter field, operand and value to current parameters
		filterAddButton.setText("Add");
		filterAddButton.setVisible(true);
		filterAddButton.setToolTipText("Add Field Filter to Parameter List");
		filterAddButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      filterAdd();
		    } // end ActionPerformed method
		  } // end action listener for filter add button
		);

    // Button to clear current filter parameters
		filterClearButton.setText("Clear");
		filterClearButton.setVisible(true);
		filterClearButton.setToolTipText("Clear all Filter Parameters");
		filterClearButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      filterClear();
		    } // end ActionPerformed method
		  } // end action listener for filter add button
		);

    // Button to set the current parameters
		filterSetButton.setText("Set");
		filterSetButton.setVisible(true);
		filterSetButton.setToolTipText("Set Table Filter Parameters as Specified Below");
		filterSetButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      filterSetParams();
		    } // end ActionPerformed method
		  } // end action listener for filter add button
		); 
		
		filterTextLabel.setVisible(true);
		filterTextLabel.setText ("Resulting filter criteria will appear below:");
		
    filterText = new JTextArea("");
		filterText.setLineWrap (true);
		filterText.setEditable (false);
		filterText.setWrapStyleWord (true);
		filterText.setVisible (true);
    
    filterTextScrollPane = new JScrollPane(filterText, 
		  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		filterTextScrollPane.setVisible(true);

		gb.startLayout (filterPanel, 3, 6);
		gb.setDefaultRowWeight (0.0);
		gb.setLeftRightInsets (1);
		gb.setTopBottomInsets (3);
		gb.setWidth (3);
		gb.add (filterFieldsLabel);
		
		gb.setAllInsets (1);
		gb.add (filterFieldsBox);
		gb.add (filterOperandBox);
		gb.add (filterValueBox);
		
		gb.add (filterAddButton);
		gb.add (filterClearButton);
		gb.add (filterSetButton);
		
		gb.add (filterAndButton);
		gb.setWidth (2);
		gb.add (filterOrButton);
		
		gb.setTopBottomInsets (3);
		gb.setWidth (3);
		gb.add (filterTextLabel);
		
		gb.setTopBottomInsets (1);
		gb.setWidth (3);
		gb.setRowWeight (1.0);
		gb.add (filterTextScrollPane);
    tabs.add("Filter", filterPanel);
  }
  
  /**
   Select the tab for this panel. 
  */
  public void selectTab() {
    if (tabs != null) {
      tabs.setSelectedComponent (filterPanel);
    }
  }
  
  /**
     Play one recorded action in the Filter module.
   */
  public void playScript (
      String inActionAction,
      String inActionModifier,
      String inActionObject,
      String inActionValue) {
    
    if ((inActionAction.equals (ScriptConstants.SET_ACTION)) 
      && (inActionObject.equals (ScriptConstants.AND_OR_OBJECT))) {
      filterAndOr (Boolean.valueOf(inActionValue).booleanValue());
    }
    else
    if (inActionAction.equals (ScriptConstants.ADD_ACTION)) {
      currentFilterOperand = inActionModifier;
      currentFilterField = inActionObject;
      currentFilterValue = inActionValue;
      filterAdd();
    }
    else
    if (inActionAction.equals (ScriptConstants.CLEAR_ACTION)) {
      filterClear();
    }
    else
    if ((inActionAction.equals (ScriptConstants.SET_ACTION))
      && (inActionObject.equals (ScriptConstants.PARAMS_OBJECT))) {
      filterSetParams ();
    } // end valid actions
    else {
      Logger.getShared().recordEvent (LogEvent.MEDIUM, 
        inActionAction + " " + inActionObject +
        " is not a valid Scripting Action for the Filter Module",
        true);
    } // end Action selector
  }
  
  /** 
     Load potential filter fields into the PSComboBox.
   */
  private void loadFilterFields() {
		filterFieldsBox.load (psList.getNames(), true);
		if (psList.totalSize() > 0
        && filterFieldsBox.getItemCount() > 0) {
		  filterFieldsBox.setSelectedIndex (0);
		  currentFilterField = (String)filterFieldsBox.getSelectedItem();
		  loadFilterValues();
		} else {
		  currentFilterField = "";
		  if (filterValueBox.getItemCount() > 0) {
        filterValueBox.removeAllItems();
      }
		}
  }
  
  /**
   Load values for the current filter field. 
  */
  private void loadFilterValues() {
    if (currentFilterField != null) {
      currentFilterColumn = psList.getColumnNumber (currentFilterField);
      if (filterValueBox.getItemCount() > 0) {
        filterValueBox.removeAllItems();
      }
      for (int row = 0; row < psList.getRowCount(); row++) {
        filterValueBox.addAlphabetical
            (psList.getValueAt(row, currentFilterColumn).toString());
      }
      if (filterValueBox.getItemCount() > 0) {
        filterValueBox.setSelectedIndex(0);
        currentFilterValue = (String)filterValueBox.getSelectedItem();
      } else {
        currentFilterValue = "";
      }
    } // end if currentFilterField not null
  } // end method loadFilterValues
  
  private void filterAndOr (boolean currentAndLogic) {
    this.currentAndLogic = currentAndLogic;
    scriptRecorder.recordScriptAction (
        ScriptConstants.FILTER_MODULE, 
        ScriptConstants.SET_ACTION, 
        ScriptConstants.NO_MODIFIER, 
        ScriptConstants.AND_OR_OBJECT, 
        String.valueOf(currentAndLogic));
  }
  
  private void filterAdd () {
    fieldFilter = new PSFieldFilter (psList, currentFilterField,
      currentFilterOperand, currentFilterValue);
    itemFilter.addFilter (fieldFilter);
    filterText.append (currentFilterField + " " + currentFilterOperand
      + " " + currentFilterValue + GlobalConstants.LINE_FEED_STRING);
    scriptRecorder.recordScriptAction (
        ScriptConstants.FILTER_MODULE, 
        ScriptConstants.ADD_ACTION, 
        currentFilterOperand,
        currentFilterField, 
        currentFilterValue);
  }
  
  private void filterClear() {
    initItemFilter();
    scriptRecorder.recordScriptAction (
        ScriptConstants.FILTER_MODULE, 
        ScriptConstants.CLEAR_ACTION, 
        ScriptConstants.NO_MODIFIER, 
        ScriptConstants.NO_OBJECT, 
        ScriptConstants.NO_VALUE);
  }
  
  private void filterSetParams() {
    psList.setInputFilter (itemFilter);
    // filterDataSet();
    // dataTable.setDataSet (filteredDataSet);
    // dataTable.fireTableDataChanged();
    filterText.append ("The filter parameters listed above have been set." 
      + GlobalConstants.LINE_FEED_STRING);
    scriptRecorder.recordScriptAction (
        ScriptConstants.FILTER_MODULE, 
        ScriptConstants.SET_ACTION, 
        ScriptConstants.NO_MODIFIER, 
        ScriptConstants.PARAMS_OBJECT, 
        ScriptConstants.NO_VALUE);
	}
  
  private void initItemFilter () {
    itemFilter = new PSItemFilter (currentAndLogic);
    psList.setInputFilter (itemFilter);
    filterText.setText ("");
  }

}
