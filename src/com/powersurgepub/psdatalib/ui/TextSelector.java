package com.powersurgepub.psdatalib.ui;

  import java.awt.*;
  import java.awt.event.*;
  import javax.swing.*;
  import javax.swing.event.*;

/**
 A class that can be used to present the user with a popup list from which
 to choose a value. 
 
 com.powersurgepub.ui.TextSelector can be used to present the user with a
 popup list from which he or she can select a value.

 com.powersurgepub.ui.PopUpList provides the list that is displayed.

 com.powersurgepub.ui.TextHandler defines an interface for the class that is to
 be notified when text selection is complete.

 com.powersurgepub.psutils.ValueList is the class that provides the list
 from which the user will choose a value.
 */
public class TextSelector
    extends javax.swing.JTextField {
  
  private PopupFactory    popupFactory  = PopupFactory.getSharedInstance();
  private Popup           popup         = null;
  private PopUpList       popUpList     = new PopUpList();
  
  private TextHandler     handler       = null;
  
  private ValueList       listModel;
  
  private StringBuffer    text;
  private int             semicolon = 0;
  private int             comma = 0;
  private int             start = 0;
  
  public TextSelector () {
    super();
    addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained (java.awt.event.FocusEvent evt) {
        if ((! evt.isTemporary())
            && (listModel.size() > 0)
            && popup == null) {
          JTextField field = (JTextField)evt.getComponent();
          if (field.getText().length() == 0) {
            displayList();
          }
        }
      }
      public void focusLost (java.awt.event.FocusEvent evt) {
        if ((! evt.isTemporary())
            && popup != null) {
          hideList();
          if (handler != null) {
            handler.textSelectionComplete();
          }
        }
      }
    });

    addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        if (popup != null) {
          if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            popUpList.nextItemOnList();
          }
          else
          if (evt.getKeyCode() == KeyEvent.VK_UP) {
            popUpList.priorItemOnList();
          }
        }
      }
    });
    
    addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
          if ((listModel.size() > 0)
            && popup == null) {
          displayList();
        }
        char c = evt.getKeyChar();
        if (c == '\r' || c == '\n') {
          if (! popUpList.isSelectionEmpty()) {
            setListSelection();
          }
        }
      }
    });
    
    DocumentListener documentListener = new DocumentListener() {
      public void changedUpdate(DocumentEvent documentEvent) {
        setPrefix();
      }
      public void insertUpdate(DocumentEvent documentEvent) {
        setPrefix();
      }
      public void removeUpdate(DocumentEvent documentEvent) {
        setPrefix();
      }
    };
    this.getDocument().addDocumentListener(documentListener);
  }
  
  public void addTextHandler (TextHandler handler) {
    this.handler = handler;
  }
  
  public void displayList() {

    Point location = this.getLocationOnScreen();
    Dimension dimension = getSize();
    // popUpList.setLocation (location.x, location.y + dimension.height);
    // popUpList.setBounds (
    //     location.x, 
    //     location.y + dimension.height,
    //     dimension.width / 2,
    //     dimension.height * 12);
    // popUpList.setMinimumSize (new Dimension (dimension.width / 2, dimension.height * 12));
    // popUpList.setSize (dimension.width / 2, dimension.height * 12);
    // popUpList.doLayout();
    Dimension popUpListSize = new Dimension(dimension.width, 240);
    popUpList.setPreferredSize (popUpListSize);
    popup = popupFactory.getPopup (this, popUpList,
        location.x, location.y + dimension.height);
    popup.show();
    // popUpList.showList();
    // popUpList.setAlwaysOnTop (true);
    // popUpList.setFocusable (false);
    // this.requestFocusInWindow();
    
    // displayingList = false;
  }
  
  public void hideList() {
    popup.hide();
    popup = null;
  }
  
  public void setPrefix () {
    checkText();
    String cat;
    if (start < text.length()) {
      cat = text.substring (start);
    } else {
      cat = "";
    }
    popUpList.setPrefix (cat);
  }
  
  public void setListSelection () {
    String value = popUpList.getSelectedValue();
    setListSelection (value);
  }
  
  public void setListSelection (String value) {
    checkText();
    if (start < text.length()) {
      text.replace (start, text.length(), value);
    } else {
      text.append (value);
    }
    setText (text.toString());
  }
  
  public void checkText () {
    text = new StringBuffer (getText());
    semicolon = text.lastIndexOf (";");
    comma = text.lastIndexOf (",");
    start = comma;
    if (semicolon > comma) {
      start = semicolon;
    }
    if (start < 0) {
      start = 0;
    } 
    while (start < text.length()
        && (! Character.isLetter (text.charAt (start)))) {
      start++;
    }
  }
  
  public void setValueList (ValueList listModel) {
    this.listModel = listModel;
    popUpList.setTextSelector (this);
    popUpList.setModel (listModel);
  }

}
