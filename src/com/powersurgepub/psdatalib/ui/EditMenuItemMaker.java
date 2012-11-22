/*
 * EditMenuItemMaker.java
 *
 * Created on June 24, 2004, 6:42 PM
 */

package com.powersurgepub.psdatalib.ui;

  import java.awt.*;
  import java.awt.event.*;
  import java.util.*;
  import javax.swing.*;
  import javax.swing.text.*;

/**
   A class to add standard actions to the Edit Menu. <p>
  
   This code is copyright (c) 2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2004/06/24 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 
      2004/06/24 - Originally written.
 */
public class EditMenuItemMaker {
  
  Hashtable actions;
  
  /** 
    Creates a new instance of EditMenuItemMaker 
   
    @param textComponent Representative text component that can
                         furnish a list of actions. 
   */
  public EditMenuItemMaker (JTextComponent textComponent) {
    actions = createActionTable (textComponent);
  }
  
  /**
     Add standard Cut, Copy and Paste actions to an Edit menu.
   
     @param editMenu The menu to have the standard actions added.
   */  
  public void addCutCopyPaste (JMenu editMenu) {
    JMenuItem cutMenuItem 
        = new JMenuItem (getActionByName (DefaultEditorKit.cutAction, actions));
    cutMenuItem.setText ("Cut");
    cutMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_X,
      Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    editMenu.add (cutMenuItem);
    
    JMenuItem copyMenuItem 
        = new JMenuItem (getActionByName (DefaultEditorKit.copyAction, actions));
    copyMenuItem.setText ("Copy");
    copyMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_C,
      Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));    
    editMenu.add (copyMenuItem);
    
    JMenuItem pasteMenuItem 
        = new JMenuItem (getActionByName (DefaultEditorKit.pasteAction, actions));
    pasteMenuItem.setText ("Paste");
    pasteMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_V,
      Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    editMenu.add (pasteMenuItem);
  }
  
  /**
     Create an action table for a given text component.
   
     @param textComponent The text component possessing the actions.
     @return A Hashtable containing all of the text component's actions.
   */  
  public static Hashtable createActionTable (JTextComponent textComponent) {
    Hashtable actions = new Hashtable();
    Action[] actionsArray = textComponent.getActions();
    for (int i = 0; i < actionsArray.length; i++) {
      Action a = actionsArray[i];
      actions.put (a.getValue(Action.NAME), a);
    } // end for each action
    return actions;
  } // end method
  
  /**
     Get the requested action.
   
     @param name The name identifying the desired action.
     @param actions The action table to be searched.
     @return The desired action.
   */  
  public static Action getActionByName (String name, Hashtable actions) {
    return (Action)(actions.get(name));
  }
  
} // end class
