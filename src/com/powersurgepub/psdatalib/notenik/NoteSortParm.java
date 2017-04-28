/*
 * Copyright 2017 - 2017 Herb Bowie
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
package com.powersurgepub.psdatalib.notenik;

  import java.awt.event.*;
  import javax.swing.*;
  import com.powersurgepub.psdatalib.psdata.values.*;
import java.awt.*;

/**
 A parameter to indicate how the user would like to see the notes sorted. 

 @author Herb Bowie
 */
public class NoteSortParm 
    implements
        ActionListener {
  
  public static final int SORT_BY_TITLE         = 0;
  public static final int SORT_BY_SEQ_AND_TITLE = 1;
  public static final int SORT_TASKS_BY_DATE    = 2;
  public static final int SORT_TASKS_BY_SEQ     = 3;
  
  private int parm = 0;
  
  private JMenu menu = null;
  
  private String[] labels = {
    "Title",
    "Seq + Title",
    "Tasks by Date",
    "Tasks by Seq"
  };
  
  private NoteList noteList = null;
  
  private int maxPositionsToLeftOfDecimal = 0;
  private int maxPositionsToRightOfDecimal = 0;
  
  public NoteSortParm() {
    
  }
  
  public void setParm(int parm) {
    if (parm < 0 || parm >= labels.length) {
      // Do nothing
    } else {
      this.parm = parm;
      if (menu != null) {
        int i = 0;
        while (i < labels.length) {
          JCheckBoxMenuItem item = (JCheckBoxMenuItem)menu.getItem(i);
          if (i == parm) {
            item.setSelected(true);
            // System.out.println("Sort Parm set to " + evt.getActionCommand());
          } else {
            item.setSelected(false);
          }
          i++;
        } // end scan of possible parms
      }
      if (noteList != null) {
        noteList.sortParmChanged();
      }
    }
  }
  
  public void setList(NoteList noteList) {
    this.noteList = noteList;
    resetSeqStats();
  }
  
  public void resetSeqStats() {
    maxPositionsToLeftOfDecimal = 0;
    maxPositionsToRightOfDecimal = 0;
  }
  
  /**
   Keep track of longest string to left of the decimal and longest
   string to the right of the decimal. 
  
   @param seq A single Sequence Data Value. 
  
   @return True if stats were adjusted, false if no adjustment necessary.
  */
  public boolean maintainSeqStats(DataValueSeq seq) {
    boolean statsAdjusted = false;
    if (seq != null) {
      if (seq.getPositionsToLeftOfDecimal() > maxPositionsToLeftOfDecimal) {
        statsAdjusted = true;
        maxPositionsToLeftOfDecimal = seq.getPositionsToLeftOfDecimal();
      }
      if (seq.getPositionsToRightOfDecimal() > maxPositionsToRightOfDecimal) {
        statsAdjusted = true;
        maxPositionsToRightOfDecimal = seq.getPositionsToRightOfDecimal();
      }
    }
    return statsAdjusted;
  }
  
  public int getMaxPositionsToLeftOfDecimal() {
    return maxPositionsToLeftOfDecimal;
  }
  
  public int getMaxPositionsToRightOfDecimal() {
    return maxPositionsToRightOfDecimal;
  }
  
  public int getParm() {
    return parm;
  }
  
  public String getParmLabel() {
    return labels[parm];
  }
  
  public void populateMenu(JMenu sortMenu) {
    menu = sortMenu;
    sortMenu.removeAll();
    for (int i = 0; i < labels.length; i++) {
      JCheckBoxMenuItem sortOption = new JCheckBoxMenuItem(labels[i]);
      switch (i) {
        case 0:
          sortOption.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_1,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
          break;
        case 1:
          sortOption.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_2,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
          break;
        case 2:
          sortOption.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_3,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
          break;
        case 3:
          sortOption.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_4,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
          break;
        
      }
      sortOption.setActionCommand(labels[i]);
      sortOption.addActionListener(this);
      if (i == 0) {
        sortOption.setSelected(true);
      } else {
        sortOption.setSelected(false);
      }
      sortMenu.add(sortOption);
    }
    
  }
  
  public void actionPerformed(ActionEvent evt) {
    int i = 0;
    while (i < labels.length) {
      JCheckBoxMenuItem item = (JCheckBoxMenuItem)menu.getItem(i);
      if (evt.getActionCommand().equals(labels[i])) {
        parm = i;
        item.setSelected(true);
        // System.out.println("Sort Parm set to " + evt.getActionCommand());
      } else {
        item.setSelected(false);
      }
      i++;
    } // end scan of possible parms
    if (noteList != null) {
      noteList.sortParmChanged();
    }
  } // end method actionPerformed
  
  /**
   Reset to default values, preparing for a new list. 
  */
  public void resetToDefaults() {
    parm = 0;
    if (menu != null) {
      int i = 0;
      while (i < labels.length) {
        JCheckBoxMenuItem item = (JCheckBoxMenuItem)menu.getItem(i);
        if (i == 0) {
          item.setSelected(true);
        } else {
          item.setSelected(false);
        }
        i++;
      }
    }
    noteList = null;
    maxPositionsToLeftOfDecimal = 0;
    maxPositionsToRightOfDecimal = 0;
  }

}
