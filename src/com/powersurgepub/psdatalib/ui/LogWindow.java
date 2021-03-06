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
  import javax.swing.*;

/**
   LogWindow.java
  
   Created on August 5, 2007, 9:59 AM
 
   A panel to display information about the program's execution. <p>
  
   This code is copyright (c) 2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2004/03/01 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2004/03/01 - Originally written. 
 */

/**
 *
 * @author  hbowie
 */
public class LogWindow 
    extends javax.swing.JFrame
      implements WindowToManage {
  
  /** Creates new form LogWindow */
  public LogWindow() {
    initComponents();
    // this.setTitle (Home.getShared().getProgramName() + "Log");
    this.setBounds (100, 100, 600, 540);
  }
  
  public JTextArea getTextArea () {
    return logTextArea;
  }
    
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        logPanel1 = new javax.swing.JPanel();
        logScrollPane = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();

        setTitle("Log");

        logPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                logPanel1formComponentShown(evt);
            }
        });
        logPanel1.setLayout(new java.awt.GridBagLayout());

        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logScrollPane.setViewportView(logTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        logPanel1.add(logScrollPane, gridBagConstraints);

        getContentPane().add(logPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void logPanel1formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_logPanel1formComponentShown
    logScrollPane.requestFocus();
  }//GEN-LAST:event_logPanel1formComponentShown
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel logPanel1;
    private javax.swing.JScrollPane logScrollPane;
    private javax.swing.JTextArea logTextArea;
    // End of variables declaration//GEN-END:variables
  
}
