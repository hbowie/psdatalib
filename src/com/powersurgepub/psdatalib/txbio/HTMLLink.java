package com.powersurgepub.psdatalib.txbio;

	import com.powersurgepub.psutils.*;
  
/**
   An object representing any sort of file reference embedded
   within an HTML file. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/06/04 - Originally written.
 */

public class HTMLLink {

  public  final static String   A_HREF_TYPE 		= "ahref";
  public  final static String   LINK_HREF_TYPE 	= "linkhref";
  public  final static String   MAILTO_TYPE 		= "mailto";
  public  final static String   IMG_SRC_TYPE 		= "imgsrc";
	
	/** The name/location of the linking file. */
  private String		from					= "";
  
  /** The type of link (see literals). */
  private String		type					= "";
  
  /** The name/location of the referenced file or resource. */
  private String		to						= "";
  
	/**
	   Constructor.
     
     @param from The name/location of the linking file.
     @param type The type of link.
     @param to   The name/location of the referenced file or resource. 
	 */
	public HTMLLink (String from, String type, String to) {
		this.from = from;
    this.type = type;
    this.to = to;
	}
  
	/**
	   Sets the from field.
     
     @param from The name/location of the linking file.
	 */
	public void setFrom (String from) {
		this.from = from;
	}
  
	/**
	   Get the from field.
     
     @return The name/location of the linking file.
	 */
	public String getFrom () {
		return from;
	}
  
	/**
	   Sets the type of link. 
     
     @param type The type of link.
	 */
	public void setType (String type) {
    this.type = type;
	}
  
	/**
	   Get the type field.
     
     @return The type of link. 
	 */
	public String getType () {
    return type;
	}
  
	/**
	   Sets the to field.
     
     @param to   The name/location of the referenced file or resource. 
	 */
	public void setTo (String to) {
    this.to = to;
	}  

	/**
	   Get the to field.
     
     @return The name/location of the referenced file or resource. 
	 */
	public String getTo () {
    return to;
	}
  	
	/**
	   Returns the object in string form.
	  
	   @return object formatted as a string
	 */
	public String toString() {
    return ("HTML Link from " + from
        + " (type " + type
        + ") to " + to);
	}
  
} // end of class

