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

  import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import javax.swing.*;

/**
   User preferences that are generally applicable to all PowerSurge Publishing
   applications.

   @author Herb Bowie.
 */
public class CommonPrefs 
    extends javax.swing.JPanel {

  public static final String SPLIT_HORIZONTAL             = "splithorizontal";
  public static final String DIVIDER_LOCATION             = "divider-location";
  public static final String CONFIRM_DELETES              = "confirm-deletes";
  public static final String CHECK_FOR_SOFTWARE_UPDATES   = "check-updates";
  public static final String CHECK_VERSION_AUTO           = "versioncheckauto";
  
  public static final String PREFS_LEFT    = "left";
  public static final String PREFS_TOP     = "top";
  public static final String PREFS_WIDTH   = "width";
  public static final String PREFS_HEIGHT  = "height";

  /** Single shared occurrence of CommonPrefs. */
  private static  CommonPrefs       commonPrefs;

  private         ProgramVersion    programVersion = ProgramVersion.getShared();
  private         JSplitPane        splitPane = null;
  private javax.swing.UIManager.LookAndFeelInfo[] lookAndFeelList;
  private         Component         mainWindow = null;
  
  private int               autoSaveInterval = 0;

  /**
   Returns a single instance of CommonPrefs that can be shared by many classes.
   This is the only way to obtain an instance of CommonPrefs, since the
   constructor is private.

  @return A single, shared instance of CommonPrefs.
 */
  public static CommonPrefs getShared() {
    if (commonPrefs == null) {
      commonPrefs = new CommonPrefs();
    }
    return commonPrefs;
  }

  /** Creates new form CommonPrefs */
  private CommonPrefs() {
    
    initComponents();

    splitPaneCheckBox.setSelected
        (UserPrefs.getShared().getPrefAsBoolean (SPLIT_HORIZONTAL, false));

    confirmDeletesCheckBox.setSelected
        (UserPrefs.getShared().getPrefAsBoolean (CONFIRM_DELETES, true));
    
    boolean oldValue
      = UserPrefs.getShared().getPrefAsBoolean
        (CHECK_VERSION_AUTO, true);
    softwareUpdatesCheckBox.setSelected
      (UserPrefs.getShared().getPrefAsBoolean
        (CHECK_FOR_SOFTWARE_UPDATES, oldValue));
    enableAutoSave(false);
  }
  
  public void enableAutoSave (boolean autoSaveOn) {
    prefsAutoSaveLabel.setEnabled(autoSaveOn);
    prefsAutoSaveLabel.setVisible(autoSaveOn);
    prefsAutoSaveSlider.setEnabled(autoSaveOn);
    prefsAutoSaveSlider.setVisible(autoSaveOn);
    prefsAutoSaveEvery.setEnabled(autoSaveOn);
    prefsAutoSaveEvery.setVisible(autoSaveOn);
    prefsAutoSaveMinutesNumber.setEnabled(autoSaveOn);
    prefsAutoSaveMinutesNumber.setVisible(autoSaveOn);
    prefsAutoSaveMinutesWord.setEnabled(autoSaveOn);
    prefsAutoSaveMinutesWord.setVisible(autoSaveOn);
  }

  private void populateFileChooserList () {
    String chooser = UserPrefs.getShared().getPref (
        XFileChooser.FILE_CHOOSER_KEY,
        XFileChooser.FILE_CHOOSER_SWING);
    prefsFileChooserComboBox.addItem (XFileChooser.FILE_CHOOSER_SWING);
    prefsFileChooserComboBox.setSelectedIndex (0);
    if (XOS.getShared().isRunningOnMacOS()) {
      prefsFileChooserComboBox.addItem (XFileChooser.FILE_CHOOSER_AWT);
      if (chooser.equalsIgnoreCase (XFileChooser.FILE_CHOOSER_AWT)) {
        prefsFileChooserComboBox.setSelectedIndex (1);
      } // end if user pref is AWT
    } // end if running on a Mac
  } // end method

  private void populateLookAndFeelList () {
    lookAndFeelList = javax.swing.UIManager.getInstalledLookAndFeels();
    LookAndFeel defaultLookAndFeel = javax.swing.UIManager.getLookAndFeel();
    String defaultName = defaultLookAndFeel.getName();

    for (int i = 0; i < lookAndFeelList.length; i++) {
      javax.swing.UIManager.LookAndFeelInfo info = lookAndFeelList [i];
      String name = info.getName();
      int j = 0;
      while (j < prefsLookAndFeelComboBox.getItemCount()
          && (! name.equals ((String)prefsLookAndFeelComboBox.getItemAt(j)))) {
        j++;
      }
      if (j >= prefsLookAndFeelComboBox.getItemCount()) {
        prefsLookAndFeelComboBox.addItem (name);
        if (defaultName.equals (name)) {
          prefsLookAndFeelComboBox.setSelectedItem (name);
        }
      } // end if name not already in list
    } // end of look and feel list
  } // end of method

  private void populateMenuLocationList () {

    String menuloc = UserPrefs.getShared().getPref (
        XOS.MENU_LOCATION_KEY,
        XOS.MENU_AT_TOP_OF_SCREEN);
    prefsMenuLocationComboBox.addItem ("Top of Window");
    prefsMenuLocationComboBox.setSelectedIndex (0);
    if (XOS.getShared().isRunningOnMacOS()) {
      prefsMenuLocationComboBox.addItem ("Top of Screen");
      if (menuloc.equalsIgnoreCase (XOS.MENU_AT_TOP_OF_SCREEN)) {
        prefsMenuLocationComboBox.setSelectedIndex (1);
      }
    }
  }

  public void appLaunch() {
    checkVersionIfAuto();
  }

  public void checkVersionIfAuto () {
    if (programVersion == null) {
      System.out.println("programVersion == null");
    }
    if (checkForSoftwareUpdates()) {
      programVersion.informUserIfNewer();
    }
  }

  public boolean confirmDeletes () {
    return confirmDeletesCheckBox.isSelected();
  }

  public boolean checkForSoftwareUpdates () {
    return softwareUpdatesCheckBox.isSelected();
  }

  public void setSplitPane(JSplitPane splitPane) {
    this.splitPane = splitPane;
    setSplit(splitPaneCheckBox.isSelected());
    splitPane.setDividerLocation
        (UserPrefs.getShared().getPrefAsInt (DIVIDER_LOCATION, 240));
  }

  public boolean splitPaneHorizontal () {
    return splitPaneCheckBox.isSelected();
  }

  public void setSplit (boolean splitPaneHorizontal) {
    int splitOrientation = JSplitPane.VERTICAL_SPLIT;
    if (splitPaneHorizontal) {
      splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
    }
    if (splitPane != null) {
      splitPane.setOrientation (splitOrientation);
    }
  }

  /**
   Provides the main window for the application.

   @param mainWindow The main window for the application. Normally a JFrame, but
   only required to be a Component. 
   */
  public void setMainWindow (Component mainWindow) {
    this.mainWindow = mainWindow;
  }

  private void warnRelaunch() {
    JOptionPane.showMessageDialog (
        this,
        "You may need to Quit and relaunch "
            + XOS.getShared().getProgramName()
            + " for your preferences to take effect.",
        "Relaunch Warning",
        JOptionPane.WARNING_MESSAGE);
  }
  
  public int getAutoSave () {
    return autoSaveInterval;
  }
  
  public void setAutoSave (int autoSaveInterval) {
    if (autoSaveInterval >= 0
        && autoSaveInterval <= 60) {
      prefsAutoSaveSlider.setValue(autoSaveInterval);
      this.autoSaveInterval = autoSaveInterval;
      setAutoSaveText (autoSaveInterval);
    } // end if interval within acceptable range
  } // end method
  
  private void setAutoSaveText (int autoSaveInterval) {
    if (autoSaveInterval == 0) {
      prefsAutoSaveEvery.setText("Off");
      prefsAutoSaveMinutesNumber.setText("     ");
      prefsAutoSaveMinutesWord.setText("          ");
    } else {
      prefsAutoSaveEvery.setText("Every");
      prefsAutoSaveMinutesNumber.setText(String.valueOf(autoSaveInterval));
      prefsAutoSaveMinutesWord.setText("Minutes");
    }
  }

  public void savePrefs() {
    UserPrefs.getShared().setPref
        (DIVIDER_LOCATION, splitPane.getDividerLocation());
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        splitPaneLabel = new javax.swing.JLabel();
        splitPaneCheckBox = new javax.swing.JCheckBox();
        confirmDeletesLabel = new javax.swing.JLabel();
        confirmDeletesCheckBox = new javax.swing.JCheckBox();
        softwareUpdatesLabel = new javax.swing.JLabel();
        softwareUpdatesCheckBox = new javax.swing.JCheckBox();
        softwareUpdatesCheckNowButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        prefsFileChooserLabel = new javax.swing.JLabel();
        prefsFileChooserComboBox = new javax.swing.JComboBox();
        prefsLookAndFeelLabel = new javax.swing.JLabel();
        prefsLookAndFeelComboBox = new javax.swing.JComboBox();
        prefsMenuLocationLabel = new javax.swing.JLabel();
        prefsMenuLocationComboBox = new javax.swing.JComboBox();
        prefsAutoSaveSeparator = new javax.swing.JSeparator();
        prefsAutoSaveLabel = new javax.swing.JLabel();
        prefsAutoSaveSlider = new javax.swing.JSlider();
        prefsAutoSaveEvery = new javax.swing.JLabel();
        prefsAutoSaveMinutesNumber = new javax.swing.JLabel();
        prefsAutoSaveMinutesWord = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        splitPaneLabel.setText("Split Pane:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(splitPaneLabel, gridBagConstraints);

        splitPaneCheckBox.setSelected(true);
        splitPaneCheckBox.setText("Horizontal Split?");
        splitPaneCheckBox.setToolTipText("Split the screen horizontally or vertically?");
        splitPaneCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        splitPaneCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                splitPaneCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(splitPaneCheckBox, gridBagConstraints);

        confirmDeletesLabel.setText("Deletion:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(confirmDeletesLabel, gridBagConstraints);

        confirmDeletesCheckBox.setSelected(true);
        confirmDeletesCheckBox.setText("Confirm Deletes?");
        confirmDeletesCheckBox.setToolTipText("Give the user a chance to confirm deletes, or delete immediately?");
        confirmDeletesCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        confirmDeletesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmDeletesCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(confirmDeletesCheckBox, gridBagConstraints);

        softwareUpdatesLabel.setText("Software Updates:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(softwareUpdatesLabel, gridBagConstraints);

        softwareUpdatesCheckBox.setSelected(true);
        softwareUpdatesCheckBox.setText("Check Automatically?");
        softwareUpdatesCheckBox.setToolTipText("Automatically check for software updates whenever URL Union starts up?");
        softwareUpdatesCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        softwareUpdatesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                softwareUpdatesCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(softwareUpdatesCheckBox, gridBagConstraints);

        softwareUpdatesCheckNowButton.setText("Check Now");
        softwareUpdatesCheckNowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                softwareUpdatesCheckNowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        add(softwareUpdatesCheckNowButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jSeparator1, gridBagConstraints);

        prefsFileChooserLabel.setText("File Chooser:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(prefsFileChooserLabel, gridBagConstraints);

        populateFileChooserList();
        prefsFileChooserComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prefsFileChooserComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(prefsFileChooserComboBox, gridBagConstraints);

        prefsLookAndFeelLabel.setText("Look and Feel:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(prefsLookAndFeelLabel, gridBagConstraints);

        populateLookAndFeelList();
        prefsLookAndFeelComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prefsLookAndFeelComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(prefsLookAndFeelComboBox, gridBagConstraints);

        prefsMenuLocationLabel.setText("Menu Location:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(prefsMenuLocationLabel, gridBagConstraints);

        populateMenuLocationList();
        prefsMenuLocationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prefsMenuLocationComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(prefsMenuLocationComboBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(prefsAutoSaveSeparator, gridBagConstraints);

        prefsAutoSaveLabel.setText("AutoSave:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 2, 2);
        add(prefsAutoSaveLabel, gridBagConstraints);

        prefsAutoSaveSlider.setMajorTickSpacing(10);
        prefsAutoSaveSlider.setMaximum(60);
        prefsAutoSaveSlider.setPaintLabels(true);
        prefsAutoSaveSlider.setPaintTicks(true);
        prefsAutoSaveSlider.setSnapToTicks(true);
        prefsAutoSaveSlider.setValue(0);
        prefsAutoSaveSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                prefsAutoSaveSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(prefsAutoSaveSlider, gridBagConstraints);

        prefsAutoSaveEvery.setText("Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(prefsAutoSaveEvery, gridBagConstraints);

        prefsAutoSaveMinutesNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(prefsAutoSaveMinutesNumber, gridBagConstraints);

        prefsAutoSaveMinutesWord.setText("       ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        add(prefsAutoSaveMinutesWord, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

private void softwareUpdatesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_softwareUpdatesCheckBoxActionPerformed
  UserPrefs.getShared().setPref
      (CHECK_FOR_SOFTWARE_UPDATES, softwareUpdatesCheckBox.isSelected());
}//GEN-LAST:event_softwareUpdatesCheckBoxActionPerformed

private void softwareUpdatesCheckNowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_softwareUpdatesCheckNowButtonActionPerformed
    programVersion.informUserIfNewer();
    programVersion.informUserIfLatest();
}//GEN-LAST:event_softwareUpdatesCheckNowButtonActionPerformed

private void confirmDeletesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmDeletesCheckBoxActionPerformed
  UserPrefs.getShared().setPref
      (CONFIRM_DELETES, confirmDeletesCheckBox.isSelected());
}//GEN-LAST:event_confirmDeletesCheckBoxActionPerformed

private void splitPaneCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_splitPaneCheckBoxActionPerformed
  UserPrefs.getShared().setPref
      (SPLIT_HORIZONTAL, splitPaneCheckBox.isSelected());
  setSplit(splitPaneCheckBox.isSelected());
}//GEN-LAST:event_splitPaneCheckBoxActionPerformed

private void prefsFileChooserComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefsFileChooserComboBoxActionPerformed
    UserPrefs.getShared().setPref(XFileChooser.FILE_CHOOSER_KEY,
        (String)prefsFileChooserComboBox.getSelectedItem());
}//GEN-LAST:event_prefsFileChooserComboBoxActionPerformed

private void prefsLookAndFeelComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefsLookAndFeelComboBoxActionPerformed
    int index = prefsLookAndFeelComboBox.getSelectedIndex();
    if (index >= 0) {
      javax.swing.UIManager.LookAndFeelInfo info = lookAndFeelList [index];
      try {
        javax.swing.UIManager.setLookAndFeel (info.getClassName());
        if (mainWindow != null) {
          SwingUtilities.updateComponentTreeUI (mainWindow);
        }
      } catch (Exception e) {
        Trouble.getShared().report
            ("Problems Setting New Look and Feel", "UI Problem");
      }
      XOS.getShared().setLookAndFeelClassName(info.getClassName());
    }


}//GEN-LAST:event_prefsLookAndFeelComboBoxActionPerformed

private void prefsMenuLocationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefsMenuLocationComboBoxActionPerformed
    int index = prefsMenuLocationComboBox.getSelectedIndex();
    if (index > 0) {
      XOS.getShared().setMenuAtTopOfScreen(true);
    } else {
      XOS.getShared().setMenuAtTopOfScreen(false);
    }
    warnRelaunch();
}//GEN-LAST:event_prefsMenuLocationComboBoxActionPerformed

  private void prefsAutoSaveSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_prefsAutoSaveSliderStateChanged
    if (! prefsAutoSaveSlider.getValueIsAdjusting()) {
      autoSaveInterval = 0;
      autoSaveInterval = prefsAutoSaveSlider.getValue();
      setAutoSaveText(autoSaveInterval);
      // td.setAutoSave(autoSaveInterval);
    }
  }//GEN-LAST:event_prefsAutoSaveSliderStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox confirmDeletesCheckBox;
    private javax.swing.JLabel confirmDeletesLabel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel prefsAutoSaveEvery;
    private javax.swing.JLabel prefsAutoSaveLabel;
    private javax.swing.JLabel prefsAutoSaveMinutesNumber;
    private javax.swing.JLabel prefsAutoSaveMinutesWord;
    private javax.swing.JSeparator prefsAutoSaveSeparator;
    private javax.swing.JSlider prefsAutoSaveSlider;
    private javax.swing.JComboBox prefsFileChooserComboBox;
    private javax.swing.JLabel prefsFileChooserLabel;
    private javax.swing.JComboBox prefsLookAndFeelComboBox;
    private javax.swing.JLabel prefsLookAndFeelLabel;
    private javax.swing.JComboBox prefsMenuLocationComboBox;
    private javax.swing.JLabel prefsMenuLocationLabel;
    private javax.swing.JCheckBox softwareUpdatesCheckBox;
    private javax.swing.JButton softwareUpdatesCheckNowButton;
    private javax.swing.JLabel softwareUpdatesLabel;
    private javax.swing.JCheckBox splitPaneCheckBox;
    private javax.swing.JLabel splitPaneLabel;
    // End of variables declaration//GEN-END:variables

}
