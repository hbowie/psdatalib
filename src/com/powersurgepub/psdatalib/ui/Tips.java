/*
 * Tips.java
 *
 * Created on June 17, 2006, 8:57 AM
 */

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

import com.powersurgepub.psdatalib.markup.MarkupElement;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import java.io.*;
  import java.net.*;
  import java.util.*;
  import javax.swing.*;
  import javax.swing.event.*;
  import javax.xml.transform.*;
  import javax.xml.transform.stream.*;
  import org.xml.sax.*;
  import org.xml.sax.helpers.*;

/**
 *
 * @author  hbowie
 */
public class Tips 
    extends JFrame
      implements org.xml.sax.ContentHandler,
                 HyperlinkListener {
  
  public static final String              TIPS          = "tips";
  public static final String              TIP           = "tip";
  public static final String              TIPS_FILE_NAME = "tips.xml";
  public static final String              STARTUP_TIPS_KEY = "startuptips";
  
  public static final String TIP_LEFT      = "tip-left";
  public static final String TIP_TOP       = "tip-top";
  public static final String TIP_WIDTH     = "tip-width";
  public static final String TIP_HEIGHT    = "tip-height";
  public static final String TIP_NUMBER    = "tip-number";
  
  private             String              tipsKey;
  private             String              tipsFileNameString = "";
  private             ArrayList           tips          = new ArrayList();
  private             MarkupElement       tip           = new MarkupElement();
  private             XMLReader           parser;
  
  // Depth within XML structure, where 0 = no XML, 1 is within first, etc.
  private             int                 elementLevel  = 0;
  
  // tags within the tip should be passed along as is, for the most part
  private             boolean             withinTip     = false;
  
  private             int                 tipNumber     = 0;
  
  private             StringBuffer        text;
  
  private             UserPrefs           userPrefs = UserPrefs.getShared();
  private             Home                home      = Home.getShared();
  private             File                appFolder;
  private             Logger              log = Logger.getShared();
  
  /** Creates new form Tips */
  public Tips () {
    appFolder = home.getAppFolder();
    String tipsFileName = TIPS_FILE_NAME;
    if (appFolder != null) {
      File tipsFile = new File (appFolder, TIPS_FILE_NAME);
      tipsFileName = tipsFile.toString();
    } 
    this.tipsFileNameString = tipsFileName;
    this.setTitle (Home.getShared().getProgramName() + " Tips");
    this.tipsKey = STARTUP_TIPS_KEY;
    this.tipNumber = userPrefs.getPrefAsInt (TIP_NUMBER, 0);
    initComponents();
    tipCheckBox.setSelected
        (userPrefs.getPrefAsBoolean (tipsKey, true));
    setBounds (
        userPrefs.getPrefAsInt (TIP_LEFT, 80),
        userPrefs.getPrefAsInt (TIP_TOP,  80),
        userPrefs.getPrefAsInt (TIP_WIDTH, 360),
        userPrefs.getPrefAsInt (TIP_HEIGHT, 360));
    boolean ok = createParser();
    if (ok) {
      loadTips ();
    }
    if (tipNumber < 0 || tipNumber >= tips.size()) {
      tipNumber = 0;
    }
    if (tips != null && tipNumber >= 0 && tipNumber < tips.size()) {
      displayTip();
    }
  }

  public void noTipsAtStartupOption () {
    tipLabel.setVisible (false);
    tipCheckBox.setVisible (false);
  }
  
  public void savePrefs () {
    userPrefs.setPref (TIP_LEFT, this.getX());
    userPrefs.setPref (TIP_TOP, this.getY());
    userPrefs.setPref (TIP_WIDTH, this.getWidth());
    userPrefs.setPref (TIP_HEIGHT, this.getHeight());
    userPrefs.setPref (TIP_NUMBER, this.getNextTipNumber());
  }
  
  public int getTipNumber () {
    return tipNumber;
  }
  
  public int getNextTipNumber () {
    int i = tipNumber + 1;
    if (i >= tips.size()) {
      i = 0;
    }
    return i;
  }
  
  private void displayTip () {
    text = new StringBuffer();
    text.append ("<html>");
    text.append ("<body>");

    if (tips != null && tipNumber >= 0 && tipNumber < tips.size()) {
      tipLabel.setText ("Tip Number " + String.valueOf (tipNumber + 1) 
          + " of " + String.valueOf (tips.size()));
      tip = (MarkupElement)tips.get(tipNumber);
      text.append (tip.toString (MarkupElement.OLD_HTML, true));
    } 
    text.append ("</body>");
    text.append ("</html>");
    tipEditorPane.setText (text.toString());
    tipEditorPane.addHyperlinkListener (this);
  }
  
  /**
   Create XML Parser.
   */
  private boolean createParser () {
    
    boolean ok = true;
    try {
      parser = XMLReaderFactory.createXMLReader();
    } catch (SAXException e) {
      log.recordEvent (LogEvent.MINOR, 
          "Generic SAX Parser Not Found",
          false);
      try {
        parser = XMLReaderFactory.createXMLReader
            ("org.apache.xerces.parsers.SAXParser");
      } catch (SAXException eex) {
        log.recordEvent (LogEvent.MEDIUM, 
            "Xerces SAX Parser Not Found",
            false);
        ok = false;
      } // end catch specific sax parser not found
    } // end catch generic sax parser exception
    if (ok) {
      parser.setContentHandler (this);
    }
    return ok;
  } // end method createParser
  
  public void loadTips () {

    File tipsFile = new File (tipsFileNameString);
    FileName tipsFileName = new FileName (tipsFileNameString);

    elementLevel = 0;
    withinTip = false;
    try {
      parser.parse (tipsFileName.getURLString());
    } 
    catch (SAXException saxe) {
        System.out.println ("Encountered SAX error while reading XML file " 
            + tipsFileNameString 
            + saxe.toString());   
    } 
    catch (java.io.IOException ioe) {
        System.out.println ("Encountered I/O error while reading XML file " 
            + tipsFileNameString  
            + ioe.toString());   
    }
  }
  
  public void startDocument () {
    
  }
  
  public void startElement (
      String namespaceURI,
      String localName,
      String qualifiedName,
      Attributes attributes) {
    
    if (localName.equals (TIPS)) {
      elementLevel = 1;
      withinTip = false;
    }
    else
    if (localName.equals (TIP)) {
      elementLevel = 2;
      withinTip = true;
      tip = new MarkupElement ();
    }
    else
    if (elementLevel >= 0) {
      if (withinTip) {
        if (tip != null) {
          tip.startElement (
              namespaceURI,
              localName,
              qualifiedName,
              attributes);
        } // end if tip exists
      } // end if within tip
    } // end if elementLevel >= 0
  } // end method
  
  public void characters (char [] ch, int start, int length) {
    
    StringBuffer xmlchars = new StringBuffer();
    xmlchars.append (ch, start, length);
    
    if (withinTip) {
      if (tip != null) {
        tip.characters (ch, start, length);
      }
    } 
  } // end method characters
  
  public void ignorableWhitespace (char [] ch, int start, int length) {
    
  }
  
  public void endElement (
      String namespaceURI,
      String localName,
      String qualifiedName) {
    
    if (elementLevel >= 0) {
      if (localName.equals (TIPS)) {
        withinTip = false;
        elementLevel--;
      }
      else
      if (localName.equals (TIP)) {
        withinTip = false;
        elementLevel--;
        if (tip != null) {
          tips.add (tip);
        }
      } 
      else
      if (withinTip) {
        if (tip != null) {
          tip.endElement (
              namespaceURI,
              localName,
              qualifiedName);
        } // end if tip exists
      } // end if within a tip
    } // end if elementLevel is valid
  } // end method
  
  public void startPrefixMapping (String prefix, String uri) {
    
  }
  
  public void endPrefixMapping (String prefix) {
    
  }
  
  public void processingInstruction (String target, String data) {
    
  }
  
  public void setDocumentLocator (Locator locator) {
    
  }
  
  public void skippedEntity (String name) {
    
  }
  
  public void endDocument () {
    
  }
  
  public void hyperlinkUpdate (HyperlinkEvent e) {
    HyperlinkEvent.EventType type = e.getEventType();
    String url = StringUtils.cleanURLString(e.getURL().toString());
    if (type == HyperlinkEvent.EventType.ACTIVATED) {
      try {
        XOS.getShared().openURL (url);
      } catch (java.io.IOException exc) {
        Trouble.getShared().report 
            ("Trouble opening Web page " + url, "Web Browser Problem");
      } 
    }
  }

  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tipLabel = new javax.swing.JLabel();
        tipScrollPane = new javax.swing.JScrollPane();
        tipEditorPane = new javax.swing.JEditorPane();
        tipCheckBox = new javax.swing.JCheckBox();
        nextTipButton = new javax.swing.JButton();
        priorTipButton = new javax.swing.JButton();
        firstTipButton = new javax.swing.JButton();
        lastTipButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        tipLabel.setText("Tip");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(tipLabel, gridBagConstraints);

        tipScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tipEditorPane.setContentType("text/html");
        tipScrollPane.setViewportView(tipEditorPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        getContentPane().add(tipScrollPane, gridBagConstraints);

        tipCheckBox.setText("Show tips at startup?");
        tipCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tipCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        tipCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 12, 4);
        getContentPane().add(tipCheckBox, gridBagConstraints);

        nextTipButton.setText(">");
        nextTipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextTipButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 12, 4);
        getContentPane().add(nextTipButton, gridBagConstraints);

        priorTipButton.setText("<");
        priorTipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priorTipButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 12, 4);
        getContentPane().add(priorTipButton, gridBagConstraints);

        firstTipButton.setText("<<");
        firstTipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstTipButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 12, 4);
        getContentPane().add(firstTipButton, gridBagConstraints);

        lastTipButton.setText(">>");
        lastTipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastTipButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 12, 4);
        getContentPane().add(lastTipButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

  private void lastTipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastTipButtonActionPerformed
    tipNumber = tips.size() - 1;
    displayTip();
  }//GEN-LAST:event_lastTipButtonActionPerformed

  private void firstTipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstTipButtonActionPerformed
    tipNumber = 0;
    displayTip();
  }//GEN-LAST:event_firstTipButtonActionPerformed

  private void priorTipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priorTipButtonActionPerformed
    tipNumber--;
    if (tipNumber < 0) {
      tipNumber = tips.size() - 1;
    }
    displayTip();
  }//GEN-LAST:event_priorTipButtonActionPerformed

  private void nextTipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextTipButtonActionPerformed
    tipNumber++;
    if (tipNumber >= tips.size()) {
      tipNumber = 0;
    }
    displayTip();
  }//GEN-LAST:event_nextTipButtonActionPerformed

  private void tipCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipCheckBoxActionPerformed
    userPrefs.setPref (tipsKey, tipCheckBox.isSelected());
  }//GEN-LAST:event_tipCheckBoxActionPerformed
  
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton firstTipButton;
    private javax.swing.JButton lastTipButton;
    private javax.swing.JButton nextTipButton;
    private javax.swing.JButton priorTipButton;
    private javax.swing.JCheckBox tipCheckBox;
    private javax.swing.JEditorPane tipEditorPane;
    private javax.swing.JLabel tipLabel;
    private javax.swing.JScrollPane tipScrollPane;
    // End of variables declaration//GEN-END:variables
  
}
