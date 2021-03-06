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

import com.powersurgepub.psdatalib.psdata.widgets.TextSelector;
  import java.awt.event.*;
  import javax.swing.*;
  import javax.swing.text.*;

/**
 A class that provides a popup list for TextSelector.
 
 com.powersurgepub.ui.TextSelector can be used to present the user with a
 popup list from which he or she can select a value.

 com.powersurgepub.ui.PopUpList provides the list that is displayed.

 com.powersurgepub.ui.TextHandler defines an interface for the class that is to
 be notified when text selection is complete.

 com.powersurgepub.psutils.ValueList is the class that provides the list
 from which the user will choose a value.

 @author Herb Bowie
 */

public class PopUpList 
    extends javax.swing.JPanel {
  
  private TextSelector textSelector;

    /** Creates new form PopUpList */
    public PopUpList() {
        initComponents();
    }
    
  /**
   Set the model to be used for the JList. 
   
   @param listModel Model to be used for the JList. 
   */
  public void setModel (ValueList listModel) {
    list.setModel (listModel);
    list.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
  }
  
  /**
   Set the prefix to be used to find the first matching list occurrence.
   
   @param prefix Prefix used to search through the list for the first partial
                 match.
   */
  public void setPrefix (String prefix) {
    if (prefix != null && list.getModel().getSize() > 0) {
      int match = list.getNextMatch (prefix, 0, Position.Bias.Forward);
      list.clearSelection();
      list.setSelectedIndex (match);
      list.ensureIndexIsVisible (match);
    }
  }
  
  /**
   Sets the TextSelector to be coordinated with this list.
   
   @param textSelector TextSelector to be coordinated with this list.
   */
  public void setTextSelector (TextSelector textSelector) {
    this.textSelector = textSelector;
  }
  
  public void announceSelection () {
    if (textSelector != null) {
      textSelector.setListSelection (getSelectedValue());
    }
  }
  
  public String getSelectedValue () {
    if (isSelectionEmpty()) {
      return "";
    } else {
      return (String)list.getSelectedValue();
    }
  }
  
  public boolean isSelectionEmpty() {
    return list.isSelectionEmpty();
  }

  public void nextItemOnList () {
    int index = list.getSelectedIndex();
    index++;
    if (index < list.getModel().getSize()) {
      list.setSelectedIndex (index);
      list.ensureIndexIsVisible (index);
    }
  }

  public void priorItemOnList () {
    int index = list.getSelectedIndex();
    if (index > 0) {
      index --;
      list.setSelectedIndex (index);
      list.ensureIndexIsVisible (index);
    }
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        listScrollPane = new javax.swing.JScrollPane();
        list = new javax.swing.JList();

        setMinimumSize(new java.awt.Dimension(120, 240));
        setPreferredSize(new java.awt.Dimension(480, 240));
        setLayout(new java.awt.BorderLayout());

        listScrollPane.setPreferredSize(new java.awt.Dimension(200, 140));

        list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list.setMaximumSize(new java.awt.Dimension(400, 800));
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMouseClicked(evt);
            }
        });
        list.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listKeyPressed(evt);
            }
        });
        listScrollPane.setViewportView(list);

        add(listScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

private void listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMouseClicked
  if (! list.isSelectionEmpty()) {
    announceSelection();
  }
}//GEN-LAST:event_listMouseClicked

private void listKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listKeyPressed

}//GEN-LAST:event_listKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList list;
    private javax.swing.JScrollPane listScrollPane;
    // End of variables declaration//GEN-END:variables

}
