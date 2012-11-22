package com.powersurgepub.psdatalib.ui;
  
/**
   An object representing a window (JFrame, etc.). <p>
  
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
  import java.awt.event.*;
  import java.io.*;
  import javax.swing.*;
  import java.util.*;

public class UIWindow 
    extends UIObject {
    
  public 		final static String 	MAIN = "main";
  public		final static String		PIXEL_WIDTH = "pixelwidth";
  public		final static String		PIXEL_HEIGHT = "pixelheight";
  public		final static String		RESIZE = "resize";
  
  protected	int										pixelWidth = 0;
  protected int										pixelHeight = 0;
  protected boolean								resize = false;
	
	/** The frame used. */
  private 		JFrame							uiFrame;
  
  private     GridBagger          gb = new GridBagger();
  
  private			ArrayList						widgets = new ArrayList();

	/**
	   Constructor.
	 */
	public UIWindow (DataRecord uiRec, UIManager uiMgr) {
    super (uiRec, uiMgr);
    pixelWidth = uiRec.getFieldAsInteger(PIXEL_WIDTH);
    pixelHeight = uiRec.getFieldAsInteger(PIXEL_HEIGHT);
    resize = uiRec.getFieldAsBoolean(RESIZE);
		uiFrame = new JFrame();
    uiFrame.setTitle(title);
    if (pixelWidth > 20 && pixelHeight > 10) {
      uiFrame.setSize(pixelWidth, pixelHeight);
    }
    uiFrame.setResizable (resize);
    if (handle.equals (MAIN)) {
      uiFrame.getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
      uiFrame.addWindowListener (new WindowAdapter()
        {
          public void windowClosing (WindowEvent e) {
            handleQuit();
          } // end ActionPerformed method
        } // end action listener for filter fields combo box
      ); 
    }
	}
  
  public void add(UIWidget uiWidget) {
    widgets.add (uiWidget);
  }
  
  /**
     Finish up window layout and activate it.
   */
  public void activate() {
    gb.startLayout (uiFrame.getContentPane(), columnWidth, rowHeight);
    for (int i = 0; i < widgets.size(); i++) {
      UIWidget widget = (UIWidget)widgets.get(i);
      JComponent component = widget.getComponent();
      gb.add (component);
    }
    uiFrame.setVisible(true);
  }
  
  /**
     Standard way to respond to a Quit action.
   */
  public void handleQuit() {	
    // stopScriptRecording();
    uiMgr.handleQuit();
  }
  
}

