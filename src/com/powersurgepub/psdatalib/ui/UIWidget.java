package com.powersurgepub.psdatalib.ui;
  
/**
   An object representing a widget (JButton, etc.). <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/02/08 - Originally written.
 */

import com.powersurgepub.psdatalib.psdata.DataRecord;
	import com.powersurgepub.psutils.*;
  import java.awt.*;
  import java.io.*;
  import javax.swing.*;
  import java.util.*;

public class UIWidget 
    extends UIObject {
    
  public final static String LABEL			= "label";
  public final static String TEXT_FIELD = "textfield";
  
  /** The actual Swing component. */
  protected JComponent 			widget;
  
  /** The component if a label. */
  protected JLabel					label;
  
  /** The component if a text field. */
  protected JTextField			textField;

	/**
	   Constructor.
	 */
	public UIWidget (DataRecord uiRec, UIManager uiMgr) {
		super (uiRec, uiMgr);
    if (subType.equalsIgnoreCase(LABEL)) {
      widget = new JLabel();
    	label = (JLabel)widget;
      label.setText (text);
    }
    else
    if (subType.equalsIgnoreCase(TEXT_FIELD)) {
      widget = new JTextField();
    	textField = (JTextField)widget;
      if (text.length() > 0) {
        textField.setText (text);
      }
    }
  }
  
  /**
     Return Swing component.
    
	   @return a Swing component (JComponent)
   */
  public JComponent getComponent() {
    return widget;
  }
  
}

