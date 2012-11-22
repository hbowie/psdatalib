/*
 * MarkupUtils.java
 *
 * Created on April 18, 2007, 7:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.powersurgepub.psdatalib.markup;

/**
 *
 * @author hbowie
 */
public class MarkupUtils {
  
  public static final String ANCHOR   = "a";
  public static final String ITALICS  = "i";
  public static final String BOLD     = "b";
  public static final String CITE     = "cite";
  public static final String EMPHASIS = "em";
  public static final String BREAK    = "br";
  public static final String TEXT_FRAGMENT = "t";
  public static final String PARAGRAPH = "p";
  
  /** Creates a new instance of MarkupUtils */
  private MarkupUtils() {
  }
  
  public static boolean tagBreakBefore (String name) {
    boolean breakBefore = true;
    // Determine line breaks around tag
    if (name.equals (ANCHOR)
        || name.equals (ITALICS)
        || name.equals (BOLD)
        || name.equals (CITE)
        || name.equals (EMPHASIS)
        || name.equals (BREAK)
        || name.equals (TEXT_FRAGMENT)) {
      breakBefore = false;
    } 
    return breakBefore;
  }
  
  public static boolean tagBreakAfter (String name) {
    boolean breakAfter = true;
    // Determine line breaks around tag
    if (name.equals (ANCHOR)
        || name.equals (ITALICS)
        || name.equals (BOLD)
        || name.equals (CITE)
        || name.equals (EMPHASIS)
        || name.equals (TEXT_FRAGMENT)) {
      breakAfter = false;
    } 
    return breakAfter;
  }
  
  public static boolean tagEmpty (String name) {
    boolean emptyTag = false;
    if (name.equals (BREAK)
        || name.equals ("img")
        || name.equals ("hr")) {
      emptyTag = true;
    } 
    return emptyTag;
  }
  
  public static boolean tagIsTextFragment (String name) {
    return (name.equals (TEXT_FRAGMENT));
  }
  
  public static boolean tagIsAnchor (String name) {
    return (name.equals (ANCHOR));
  }
  
  public static boolean tagIsHeading (String name) {
    return (name.length() == 2 
        && name.charAt(0) == 'h'
        && name.charAt(1) >= '1'
        && name.charAt(1) <= '6');
  }

  public static boolean isBlockTag (String name) {
    return (name.equals(PARAGRAPH)
        || tagIsHeading(name));
  }
  
}
