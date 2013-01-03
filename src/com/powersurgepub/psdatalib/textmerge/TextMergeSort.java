package com.powersurgepub.psdatalib.textmerge;

  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.pslist.*;
  import com.powersurgepub.psdatalib.script.*;
  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psutils.*;
  import java.awt.event.*;
  import java.util.*;
  import javax.swing.*;
  import javax.swing.border.*;

/**
 The sorting module used as part of PSTextMerge. 

 @author Herb Bowie. 
 */
public class TextMergeSort {
  
  private     PSList              psList = null;
  private     TextMergeScript     scriptRecorder = null;
  private     boolean             combineAllowed = true;
  private     JTabbedPane         tabs = null;
  private     JMenuBar            menus = null;
  
  // Fields used for the User Interface
  private     Border              raisedBevel;
  private     Border              etched;
  
  // Sort Panel Objects
  private     JPanel              sortPanel;
  private     JLabel              sortFieldsLabel   = new JLabel();
  private     PSComboBox          sortFieldsBox;
  private     String[]            sortDirections    = {
                                    PSFieldComparator.ASCENDING,
                                    PSFieldComparator.DESCENDING};
  private     PSComboBox          sortDirectionBox  = new PSComboBox (sortDirections);
  private     JButton             sortAddButton     = new JButton();
  private     JButton             sortClearButton   = new JButton();
  private     JButton             sortSetButton     = new JButton();
  
  private     JLabel              combineFieldsLabel   = new JLabel();
  
  // Combine Column 1 - Combine now using following parameters
  private     JButton             combineButton  = new JButton();
  
  // Combine Column 2 - Data Loss Tolerance
  private    JLabel               combineToleranceLabel;
  private    ButtonGroup          combineToleranceGroup;
  private    JRadioButton         combineToleranceNoLossButton;
  private    static final String  NO_DATA_LOSS_STRING 
                = "No Data Loss";
  private    JRadioButton         combineToleranceLaterButton;
  private		 static final String  LATER_OVERRIDES_STRING 
                = "Later Records Override Earlier";
  private		 JRadioButton					combineToleranceEarlierButton;
  private    static final String  EARLIER_OVERRIDES_STRING 
                = "Earlier Records Override Later";
  private    JRadioButton         combineToleranceAppendButton;
  private    static final String  COMBINED_STRING 
                = "Combine Fields Where Allowed";
  
  // Combine Column 3 - Minimum Lossless Fields
  private    JLabel               combineMinNoLossLabel;
  private    JTextField           combineMinNoLossValue;
  private    JButton              combineMinNoLossUpButton;
  private    JButton              combineMinNoLossDownButton;
  
  private     JLabel              sortTextLabel     = new JLabel();
  private     JScrollPane         sortTextScrollPane;
  private     JTextArea           sortText = new JTextArea("");
  
  private     GridBagger          gb = new GridBagger();
  
  // Fields used for sorting
  private			boolean							sorted = false;    
	private     PSItemComparator    itemComparator;
	private     String              currentSortField;
	private     String              currentSortDirection;
  
  // Fields used for combining
  private     int                 dataLossTolerance = 0;
  private			int                 precedence = +1;
  private			int									minNoLoss = 0;
  private			int									totalCombinations = -1;
  
  public TextMergeSort (PSList psList, TextMergeScript scriptRecorder) {
    this.psList = psList;
    this.scriptRecorder = scriptRecorder;
    initSortSpec();
  }
  
  public void setPSList (PSList psList) {
    this.psList = psList;
  }
  
  public void setTabs (JTabbedPane tabs, boolean combineAllowed) {
    
    this.tabs = tabs;
    this.combineAllowed = combineAllowed;
    
		sortPanel = new JPanel();
    
    // Create common interface components
    raisedBevel = BorderFactory.createRaisedBevelBorder();
    etched      = BorderFactory.createEtchedBorder();
		
		sortFieldsLabel.setVisible(true);
		sortFieldsLabel.setText ("Add desired sort fields then Set the result:");
		
		// Combo box for sort field
		sortFieldsBox = new PSComboBox ();
		loadSortFields();
		sortFieldsBox.setEditable (false);
		sortFieldsBox.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      PSComboBox cb = (PSComboBox)event.getSource();
		      currentSortField = (String)cb.getSelectedItem();
		    } // end ActionPerformed method
		  } // end action listener for sort fields combo box
		); 
		
		// Combo box for sort direction
		sortDirectionBox.setSelectedIndex (0);
		currentSortDirection = (String)sortDirectionBox.getSelectedItem();
		sortDirectionBox.setEditable (false);
		sortDirectionBox.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      PSComboBox cb = (PSComboBox)event.getSource();
		      currentSortDirection = (String)cb.getSelectedItem();
		    } // end ActionPerformed method
		  } // end action listener for sort direction combo box
		); 
		
		// Button to add the current sort field to the parameters being built
		sortAddButton.setText("Add");
		sortAddButton.setVisible(true);
		sortAddButton.setToolTipText("Add Field and Direction to Sort Parameters");
		sortAddButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      sortAdd();
		    } // end ActionPerformed method
		  } // end action listener for sort add button
		);

    // Button to clear the current sort parameters
		sortClearButton.setText("Clear");
		sortClearButton.setVisible(true);
		sortClearButton.setToolTipText("Clear all Sort Parameters");
		sortClearButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      sortClear();
		    } // end ActionPerformed method
		  } // end action listener for sort add button
		);

    // Button to set the current sort parameters
		sortSetButton.setText("Set");
		sortSetButton.setVisible(true);
		sortSetButton.setToolTipText("Set Table Sort Parameters as Specified Below");
		sortSetButton.addActionListener (new ActionListener()
		  {
		    public void actionPerformed (ActionEvent event) {
		      sortSetParams();
		    } // end ActionPerformed method
		  } // end action listener for sort add button
		); 
    
    if (combineAllowed) {
      combineFieldsLabel.setText ("After setting a sort sequence, optionally combine records with duplicate keys");

      // Create combine column 1 - Combine Button
      combineButton.setText("Combine");
      combineButton.setVisible(true);
      combineButton.setToolTipText("Combine Records Using Parameters Shown");
      combineButton.addActionListener (new ActionListener()
        {
          public void actionPerformed (ActionEvent event) {
            if (sorted) {
              combineSet();
            }
            else {
              JOptionPane.showMessageDialog (sortPanel, 
                "Data must be sorted before it can be combined",
                "Sort/Combine Error",
                JOptionPane.ERROR_MESSAGE);
            } // end if not sorted 
          } // end ActionPerformed method
        } // end action listener for filter add button
      );

      // create combine column 2 - Data Loss Tolerance
      combineToleranceLabel = new JLabel ("Tolerance for Data Loss", JLabel.LEFT);
      combineToleranceLabel.setBorder (etched);

      combineToleranceGroup = new ButtonGroup();

      combineToleranceNoLossButton = new JRadioButton (NO_DATA_LOSS_STRING);
      combineToleranceNoLossButton.setActionCommand (NO_DATA_LOSS_STRING);
      combineToleranceNoLossButton.setSelected (true);
      dataLossTolerance = DataField.NO_DATA_LOSS;
      combineToleranceGroup.add (combineToleranceNoLossButton);
      combineToleranceNoLossButton.addActionListener  (new ActionListener()
        {
          public void actionPerformed (ActionEvent event) {
            dataLossTolerance = DataField.NO_DATA_LOSS;
          } // end ActionPerformed method
        } // end action listener
      );

      combineToleranceLaterButton = new JRadioButton (LATER_OVERRIDES_STRING);
      combineToleranceLaterButton.setActionCommand (LATER_OVERRIDES_STRING);
      combineToleranceGroup.add (combineToleranceLaterButton);
      combineToleranceLaterButton.addActionListener  (new ActionListener()
        {
          public void actionPerformed (ActionEvent event) {
            dataLossTolerance = DataField.DATA_OVERRIDE;
            precedence = DataField.LATER_OVERRIDES;
          } // end ActionPerformed method
        } // end action listener
      );

      combineToleranceEarlierButton = new JRadioButton (EARLIER_OVERRIDES_STRING);
      combineToleranceEarlierButton.setActionCommand (EARLIER_OVERRIDES_STRING);
      combineToleranceGroup.add (combineToleranceEarlierButton);
      combineToleranceEarlierButton.addActionListener  (new ActionListener()
        {
          public void actionPerformed (ActionEvent event) {
            dataLossTolerance = DataField.DATA_OVERRIDE;
            precedence = DataField.EARLIER_OVERRIDES;
          } // end ActionPerformed method
        } // end action listener
      );

      combineToleranceAppendButton = new JRadioButton (COMBINED_STRING);
      combineToleranceAppendButton.setActionCommand (COMBINED_STRING);
      combineToleranceGroup.add (combineToleranceAppendButton);
      combineToleranceAppendButton.addActionListener  (new ActionListener()
        {
          public void actionPerformed (ActionEvent event) {
            dataLossTolerance = DataField.DATA_COMBINED;
          } // end ActionPerformed method
        } // end action listener
      );

      // create combine column 3 - Minimum number of lossless fields
      combineMinNoLossLabel = new JLabel ("Minimum Number of Lossless Fields", JLabel.LEFT);
      combineMinNoLossLabel.setBorder (etched);

      combineMinNoLossValue = new JTextField (String.valueOf(minNoLoss));
      combineMinNoLossValue.setEditable (false);
      combineMinNoLossValue.setHorizontalAlignment (JTextField.RIGHT);

      combineMinNoLossUpButton = new JButton ("Increment (+)");
      combineMinNoLossUpButton.setBorder (raisedBevel);
      combineMinNoLossUpButton.setToolTipText
        ("Increase Minimum Number of Lossless Fields");
      combineMinNoLossUpButton.addActionListener (new ActionListener()
        {
          public void actionPerformed (ActionEvent event) {
            minNoLoss++;
            combineMinNoLossValue.setText (String.valueOf(minNoLoss));
          } // end ActionPerformed method
        } // end action listener
      );

      combineMinNoLossDownButton = new JButton ("Decrement (-)");
      combineMinNoLossDownButton.setBorder (raisedBevel);
      combineMinNoLossDownButton.setToolTipText
        ("Decrease Minimum Number of Lossless Fields");
      combineMinNoLossDownButton.addActionListener (new ActionListener()
        {
          public void actionPerformed (ActionEvent event) {
            if (minNoLoss > 0) {
              minNoLoss--;
            }
            combineMinNoLossValue.setText (String.valueOf(minNoLoss));
          } // end ActionPerformed method
        } // end action listener
      );
    }
    
		// Text area to display the current sort parameters
		sortTextLabel.setVisible(true);
		sortTextLabel.setText ("Resulting sort criteria will appear below:");
		
		sortText.setLineWrap (true);
		sortText.setEditable (false);
		sortText.setWrapStyleWord (true);
		sortText.setVisible (true);
    
    sortTextScrollPane = new JScrollPane(sortText, 
		  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sortTextScrollPane.setVisible(true);

    // Now layout the screen
		gb.startLayout (sortPanel, 3, 11);
		gb.setDefaultRowWeight (0.0);
		gb.setLeftRightInsets (1);
		gb.setTopBottomInsets (3);
		gb.setWidth (3);
    gb.setTopInset (10);
		gb.add (sortFieldsLabel);
		
		gb.setAllInsets (1);
		gb.setWidth (2);
		gb.add (sortFieldsBox);
		gb.add (sortDirectionBox);
		
		gb.add (sortAddButton);
		gb.add (sortClearButton);
		gb.add (sortSetButton);
    
    if (combineAllowed) {
      gb.setLeftRightInsets (1);
      gb.setTopBottomInsets (3);
      gb.setWidth (3);
      gb.setTopInset (10);
      gb.add (combineFieldsLabel);

      gb.setRowWeight (0.0);
      gb.setAllInsets (1);
      gb.add (combineButton);
      gb.add (combineToleranceLabel);
      gb.add (combineMinNoLossLabel);

      gb.setColumn(1);
      gb.add (combineToleranceNoLossButton);
      gb.add (combineMinNoLossValue);

      gb.setColumn(1);
      gb.add (combineToleranceLaterButton);
      gb.add (combineMinNoLossUpButton);

      gb.setColumn(1);
      gb.add (combineToleranceEarlierButton);
      gb.add (combineMinNoLossDownButton);

      gb.setColumn(1);
      gb.add (combineToleranceAppendButton);
    }
    
    gb.nextRow();
    gb.setTopBottomInsets (3);
		gb.setWidth (3);
    gb.setTopInset (10);
		gb.add (sortTextLabel);
    
    gb.setWidth (3);
		gb.setRowWeight (1.0);
		gb.add (sortTextScrollPane);
    
    tabs.add("Sort", sortPanel);
    
  }
  
  /**
   Select the tab for this panel. 
  */
  public void selectTab() {
    if (tabs != null) {
      tabs.setSelectedComponent (sortPanel);
    }
  }
  
  public boolean isCombineAllowed() {
    return combineAllowed;
  }
  
  /**
     Play one recorded action in the Sort module.
   */
  public void playSortModule (
      String inActionAction,
      String inActionModifier,
      String inActionObject) {
    
    if (inActionAction.equals (ScriptConstants.ADD_ACTION)) {
      currentSortDirection = inActionModifier;
      currentSortField = inActionObject;
      sortAdd();
    }
    else
    if (inActionAction.equals (ScriptConstants.CLEAR_ACTION)) {
      sortClear();
    }
    else
    if ((inActionAction.equals (ScriptConstants.SET_ACTION))
      && (inActionObject.equals (ScriptConstants.PARAMS_OBJECT))) {
      sortSetParams ();
    } // end valid actions
    else {
      Logger.getShared().recordEvent (LogEvent.MEDIUM, 
        inActionAction + " " + inActionObject +
        " is not a valid Scripting Action for the Sort Module",
        true);
    } // end Action selector
  } // end playSortModule method
  
  /**
     Play one recorded action in the Combine module.
   */
  public void playCombineModule (
      String inActionAction,
      String inActionModifier,
      String inActionObject,
      String inActionValue,
      int    inActionValueAsInt,
      boolean inActionValueValidInt) {
    if (inActionAction.equals (ScriptConstants.ADD_ACTION)) {
      if (inActionObject.equals (ScriptConstants.DATA_LOSS_OBJECT)) {
        dataLossTolerance = inActionValueAsInt;
        if (! inActionValueValidInt) {
          Logger.getShared().recordEvent (LogEvent.MEDIUM, 
            inActionValue +
            " is not a valid Combine Data Loss Tolerance Value",
            true);
        } // end if invalid int
      } // end if data loss value
      else
      if (inActionObject.equals (ScriptConstants.PRECEDENCE_OBJECT)) {
        precedence = inActionValueAsInt;
        if (! inActionValueValidInt) {
          Logger.getShared().recordEvent (LogEvent.MEDIUM, 
            inActionValue +
            " is not a valid Combine Precedence Value",
            true);
        } // end if invalid int
      } // end if precedence value
      else
      if (inActionObject.equals (ScriptConstants.MIN_NO_LOSS_OBJECT)) {
        minNoLoss = inActionValueAsInt;
        if (! inActionValueValidInt) {
          Logger.getShared().recordEvent (LogEvent.MEDIUM, 
            inActionValue +
            " is not a valid Combine Minimum Records with No Loss Value",
            true);
        } // end if invalid int
      } // end if Minimum No Loss value
      else {
        Logger.getShared().recordEvent (LogEvent.MEDIUM, 
          inActionObject +
          " is not a valid Combine Parameter",
          true);
      } // end no known combine object
    } // end if add action
    else
    if ((inActionAction.equals (ScriptConstants.SET_ACTION))
      && (inActionObject.equals (ScriptConstants.PARAMS_OBJECT))) {
      combineSet ();
    } // end valid actions
    else {
      Logger.getShared().recordEvent (LogEvent.MEDIUM, 
        inActionAction + " " + inActionObject +
        " is not a valid Scripting Action for the Combine Module",
        true);
    } // end Action selector
  } // end playCombineModule method
  
  /**
     Load potential sort fields into the JComboBox.
   */
  private void loadSortFields() {
		sortFieldsBox.load (psList.getNames(), true);
		if (psList.totalSize() > 0
        && sortFieldsBox.getItemCount() > 0) {
		  sortFieldsBox.setSelectedIndex (0);
		  currentSortField = (String)sortFieldsBox.getSelectedItem();
		} else {
		  currentSortField = "";
		}
  }
  
  private void sortAdd() {
    itemComparator.addField (currentSortField, currentSortDirection);
    sortText.append (currentSortField + " " + currentSortDirection
      + GlobalConstants.LINE_FEED_STRING);
    scriptRecorder.recordScriptAction (
      ScriptConstants.SORT_MODULE, 
      ScriptConstants.ADD_ACTION, 
      currentSortDirection,
      currentSortField, 
      ScriptConstants.NO_VALUE);
  }
  
  private void sortClear() {
    initSortSpec();
    scriptRecorder.recordScriptAction (
      ScriptConstants.SORT_MODULE, 
      ScriptConstants.CLEAR_ACTION, 
      ScriptConstants.NO_MODIFIER, 
      ScriptConstants.NO_OBJECT, 
      ScriptConstants.NO_VALUE);
  }
  
  private void sortSetParams() {
    psList.setComparator(itemComparator);
    sorted = true;
    sortText.append ("The sort parameters listed above have been set." 
      + GlobalConstants.LINE_FEED_STRING);
    scriptRecorder.recordScriptAction (
      ScriptConstants.SORT_MODULE, 
      ScriptConstants.SET_ACTION, 
      ScriptConstants.NO_MODIFIER, 
      ScriptConstants.PARAMS_OBJECT, 
      ScriptConstants.NO_VALUE);
  }
  
  private void combineSet() {
    String msg = "";
    
    if (sorted) {
      if (dataLossTolerance == 0) {
        msg = "No data loss tolerated";
      }
      else
      if (dataLossTolerance == 1) {
        msg = "One field may override another";
      }
      else {
        msg = "Some fields may be combined";
      }
      sortText.append (msg + GlobalConstants.LINE_FEED_STRING);
      scriptRecorder.recordScriptAction (
        ScriptConstants.COMBINE_MODULE, 
        ScriptConstants.ADD_ACTION, 
        ScriptConstants.NO_MODIFIER,
        ScriptConstants.DATA_LOSS_OBJECT, 
        String.valueOf (dataLossTolerance));
      
      if (dataLossTolerance > 0) {
        if (precedence > 0) {
          msg = "Later fields override earlier ones";
        }
        else
        if (precedence < 0) {
          msg = "Earlier fields override later ones";
        }
        else {
          msg = "No precedence established";
        }
        sortText.append (msg + GlobalConstants.LINE_FEED_STRING);
      }
      scriptRecorder.recordScriptAction (
        ScriptConstants.COMBINE_MODULE, 
        ScriptConstants.ADD_ACTION, 
        ScriptConstants.NO_MODIFIER,
        ScriptConstants.PRECEDENCE_OBJECT, 
        String.valueOf (precedence));
      
      if (dataLossTolerance > 0) {
        sortText.append ("At least "
            + String.valueOf (minNoLoss)
            + " fields must suffer no data loss"
            + GlobalConstants.LINE_FEED_STRING);
      }
      scriptRecorder.recordScriptAction (
        ScriptConstants.COMBINE_MODULE, 
        ScriptConstants.ADD_ACTION, 
        ScriptConstants.NO_MODIFIER,
        ScriptConstants.MIN_NO_LOSS_OBJECT, 
        String.valueOf (minNoLoss));
      
      // combineDataSet ();
      // dataTable.fireTableDataChanged();
      
      sortText.append (String.valueOf (totalCombinations) 
        + " records combined." 
        + GlobalConstants.LINE_FEED_STRING);
      scriptRecorder.recordScriptAction (
        ScriptConstants.COMBINE_MODULE, 
        ScriptConstants.SET_ACTION, 
        ScriptConstants.NO_MODIFIER, 
        ScriptConstants.PARAMS_OBJECT, 
        ScriptConstants.NO_VALUE);
    } // end if sorted
  }
  
  private void initSortSpec () {
    itemComparator = new PSItemComparator (psList);
    sortText.setText ("");
    sorted = false;
  }

}
