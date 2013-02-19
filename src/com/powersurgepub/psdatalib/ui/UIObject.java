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
  
/**
   An object representing a UI Object of some sort. <p>
  
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

public class UIObject {

  public  static final String REC_TYPE							= "record type";
  public  static final String WINDOW_TYPE						= "window";
  public  static final String WIDGET_TYPE						= "widget";
  public  static final String HANDLE								= "handle";
  public  static final String SUB_TYPE							= "subtype";
  public  static final String COLUMN_WIDTH					= "columnwidth";
  public  static final String ROW_HEIGHT						= "rowheight";
  public  static final String ROW										= "row";
  public  static final String COLUMN								= "column";
  public  static final String TITLE									= "title";
  public  static final String TEXT									= "text";
  

	/** General type of user interface object. */
  protected String						recType								= "";
  	
	/** Handle for the object. */
  protected String						handle								= "";
  
	/** Sub-Type of the object. */
  protected String						subType								= "";
  
  /** Width of the object in terms of GridBag columns. */
  protected int								columnWidth 					= 0;
  
  /** Height of the object in terms of GridBag rows. */
  protected int								rowHeight							= 0;
  
	/** Text associated with the object. */
  protected String						text									= "";
  
	/** Title of the object. */
  protected String						title									= "";
  
  protected UIManager 				uiMgr;

	/**
	   Constructor.
	 */
	public UIObject (DataRecord uiRec, UIManager uiMgr) {
    recType			= uiRec.getFieldData (REC_TYPE);
		handle 		 	= uiRec.getFieldData (HANDLE);
    subType 	 	= uiRec.getFieldData (SUB_TYPE);
    columnWidth = uiRec.getFieldAsInteger (COLUMN_WIDTH);
    rowHeight		= uiRec.getFieldAsInteger (ROW_HEIGHT);
    title 		 	= uiRec.getFieldData (TITLE);
    text				= uiRec.getFieldData (TEXT);
    this.uiMgr 	= uiMgr;
	}

	/**
	   Returns the handle.
	  
	   @return handle.
	 */
	public String getHandle () {
		return handle;
	}
  
	/**
	   Returns the Sub-Type.
	  
	   @return subType.
	 */
	public String getSubType () {
		return subType;
	}
  
	/**
	   Returns the Title.
	  
	   @return title.
	 */
	public String getTitle () {
		return title;
	}
  
	/**
	   Returns the Text.
	  
	   @return text.
	 */
	public String getText () {
		return text;
	}
	
	/*
	   Returns the object in string form.
	  
	   @return object formatted as a string
	 */
	public String toString() {
    return handle;
	}
  
}

