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
  import java.io.*;
  import java.net.*;
  import javax.swing.*;
  import javax.swing.event.*;



/**
   A panel to display information about the version of the 
   software being executed. <p>
  
   This code is copyright (c) 2003-2007 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/08/31 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/09 - Added Category field. 
 */
public class AboutWindow 
    extends javax.swing.JFrame 
      implements HyperlinkListener {
  
  private String  fontBegin = "<font face=\"Arial\" size=\"4\">";
  private String  fontEnd   = "</font>";
  
  private Home    home       = Home.getShared();
  private File    appFolder  = null;
  private URL     pageURL;
  private URL     aboutURL;
  private String  aboutFileName = "about.html";
  private Trouble trouble = Trouble.getShared();
  private XOS     xos        = XOS.getShared();
  
  /** Creates new form AboutWindow */
  public AboutWindow() {
    setupWindow (true);
  }
  
  public AboutWindow (boolean loadFromDisk) {
    setupWindow (loadFromDisk);
  }
  
  private void setupWindow (boolean loadFromDisk) {
    initComponents();
    
    this.setTitle ("About " + Home.getShared().getProgramName());

    aboutJavaTextArea.setText
        (System.getProperty("java.vm.name") + 
        " version " + System.getProperty("java.vm.version") +
        " from " + StringUtils.removeQuotes(System.getProperty("java.vm.vendor")) +
        ", JRE version " + System.getProperty("java.runtime.version"));
    programNameAndVersionText.setText
        (home.getProgramName() 
        + " version " + home.getProgramVersion());
    
    boolean loadedFromDisk = loadFromDisk;
    appFolder = home.getAppFolder();
    if (loadFromDisk 
        && appFolder == null) {
      aboutFileError();
      loadedFromDisk = false;
    }
    if (loadedFromDisk) {
      try {
        URI pageURI = appFolder.toURI();
        pageURL = pageURI.toURL();
      } catch (MalformedURLException e) {
        loadedFromDisk = false;
        trouble.report ("Trouble forming pageURL from " + appFolder.toString(), 
            "URL Problem");
      }
    }
    if (loadedFromDisk) {
      try {
        aboutURL = new URL (pageURL, aboutFileName);
      } catch (MalformedURLException e) {
        loadedFromDisk = false;
        trouble.report ("Trouble forming aboutURL", "URL Problem");
      }
    }
    if (loadedFromDisk) {
      try {
        aboutTextPane.setPage (aboutURL);
      } catch (IOException e) {
        loadedFromDisk = false;
        aboutFileError();
      }
    }
    if (! loadedFromDisk) {
      StringBuilder t = new StringBuilder();
      t.append("<html>");
      
      t.append("<p>");
      t.append(fontBegin);
      t.append("Copyright &copy; ");
      t.append(home.getCopyrightYearFrom());
      t.append(" - 2013 Herb Bowie");
      t.append(fontEnd);
      t.append("</p>");
      
      t.append("<p>");
      t.append(fontBegin);
      t.append("Licensed under the ");
      t.append("<a href=\"http://www.apache.org/licenses/LICENSE-2.0\">");
      t.append("Apache License 2.0");
      t.append("</a>");
      t.append(fontEnd);
      t.append("</p>");
      
      t.append("<p>");
      t.append(fontBegin);
      t.append("To receive support, report bugs, request enhancements, ");
      t.append("or simply express unbridled enthusiasm for this product and its author, ");
      t.append("send an e-mail to the address below.");
      t.append(fontEnd);
      t.append("</p>");
      
      t.append("<p>");
      t.append(fontBegin);
      t.append(home.getProgramName());
      t.append(" is written in Java. It may be run on Windows, Macintosh and other Unix platforms. ");
      t.append(home.getProgramName());
      t.append(" requires a Java Virtual Machine (JVM/JRE) of 1.6 or later. ");
      t.append("You may wish to visit www.Java.com to download a compatible JVM. ");
      t.append(fontEnd);
      t.append("</p>");
      
      t.append("<br>");
      
      t.append("<table border=0 cellpadding=0 cellspacing=0>");
      
      t.append("<tr><td width=70 align=left valign=top>");
      t.append(fontBegin);
      t.append("E-mail: ");
      t.append(fontEnd);
      t.append("</td>");
      t.append("<td>");
      t.append(fontBegin);
      t.append("<a href=\"mailto:support@powersurgepub.com\">");
      t.append("support@powersurgepub.com");
      t.append("</a>");
      t.append(fontEnd);
      t.append("</td></tr>");
      
      t.append("<tr><td columns=2>&nbsp;</td></tr>");
      
      t.append("<tr><td width=70 align=left valign=top>");
      t.append(fontBegin);
      t.append("WWW: ");
      t.append(fontEnd);
      t.append("</td>");
      t.append("<td>");
      t.append(fontBegin);
      t.append("<a href=\"http://www.powersurgepub.com/\">");
      t.append("www.powersurgepub.com");
      t.append("</a>");
      t.append(fontEnd);
      t.append("</td></tr>");

      t.append("</table>");
      
      t.append("</html>");
      aboutTextPane.setText (t.toString());
    }

    this.setBounds (100, 100, 600, 540);
    aboutTextPane.addHyperlinkListener (this);
  }
  
  public void hyperlinkUpdate (HyperlinkEvent e) {
    HyperlinkEvent.EventType type = e.getEventType();
    if (type == HyperlinkEvent.EventType.ACTIVATED) {
      openURL (e.getURL());
    }
  }
  
  public void openURL (URL url) {
    openURL (url.toString());
  }
  
  public void openURL (String url) {
    try {
      xos.openURL (StringUtils.cleanURLString(url));
    } catch (java.io.IOException e) {
      trouble.report ("Trouble opening Web page " + url, "Web Browser Problem");
    }
  }
  
  private void aboutFileError () {
    JOptionPane.showMessageDialog (this, 
        "About File named "
        + aboutFileName
        + " could not be opened successfully",
        "About File Error",
        JOptionPane.ERROR_MESSAGE);
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    aboutPanel1 = new javax.swing.JPanel();
    programNameAndVersionText = new javax.swing.JLabel();
    aboutScrollPane = new javax.swing.JScrollPane();
    aboutTextPane = new javax.swing.JEditorPane();
    aboutJavaLabel = new javax.swing.JLabel();
    aboutJavaScrollPane = new javax.swing.JScrollPane();
    aboutJavaTextArea = new javax.swing.JTextArea();

    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosed(java.awt.event.WindowEvent evt) {
        formWindowClosed(evt);
      }
    });
    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentHidden(java.awt.event.ComponentEvent evt) {
        formComponentHidden(evt);
      }
    });

    aboutPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        aboutPanel1formComponentShown(evt);
      }
    });
    aboutPanel1.setLayout(new java.awt.GridBagLayout());

    programNameAndVersionText.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
    programNameAndVersionText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    programNameAndVersionText.setText("xxx version n.nn");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    aboutPanel1.add(programNameAndVersionText, gridBagConstraints);

    aboutTextPane.setEditable(false);
    aboutTextPane.setContentType("text/html"); // NOI18N
    aboutScrollPane.setViewportView(aboutTextPane);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    aboutPanel1.add(aboutScrollPane, gridBagConstraints);

    aboutJavaLabel.setText("About Java:");
    aboutJavaLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    aboutPanel1.add(aboutJavaLabel, gridBagConstraints);

    aboutJavaTextArea.setEditable(false);
    aboutJavaTextArea.setColumns(20);
    aboutJavaTextArea.setLineWrap(true);
    aboutJavaTextArea.setRows(3);
    aboutJavaTextArea.setWrapStyleWord(true);
    aboutJavaScrollPane.setViewportView(aboutJavaTextArea);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridheight = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    aboutPanel1.add(aboutJavaScrollPane, gridBagConstraints);

    getContentPane().add(aboutPanel1, java.awt.BorderLayout.CENTER);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void aboutPanel1formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_aboutPanel1formComponentShown
    aboutJavaTextArea.requestFocus();
  }//GEN-LAST:event_aboutPanel1formComponentShown

private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    WindowMenuManager.getShared().hide(this);
}//GEN-LAST:event_formWindowClosed

private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
  WindowMenuManager.getShared().hide(this);
}//GEN-LAST:event_formComponentHidden
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel aboutJavaLabel;
  private javax.swing.JScrollPane aboutJavaScrollPane;
  private javax.swing.JTextArea aboutJavaTextArea;
  private javax.swing.JPanel aboutPanel1;
  private javax.swing.JScrollPane aboutScrollPane;
  private javax.swing.JEditorPane aboutTextPane;
  private javax.swing.JLabel programNameAndVersionText;
  // End of variables declaration//GEN-END:variables
  
}
