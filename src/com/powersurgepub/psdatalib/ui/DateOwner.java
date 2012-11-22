/*
 * DateOwner.java
 *
 * Created on September 21, 2005, 4:58 AM
 *
 * An interface for using DatePanel. 
 */

package com.powersurgepub.psdatalib.ui;

  import java.text.*;
  import java.util.*;

/**
 *
 * @author hbowie
 */
public interface DateOwner {
  
  /**
   To be called whenever the date is modified by DatePanel.
   */
  public void dateModified (Date date);
  
  /**
   Does this date have an associated rule for recurrence?
   */
  public boolean canRecur();
  
  /**
   Provide a text string describing the recurrence rule, that can
   be used as a tool tip.
   */
  public String getRecurrenceRule();
  
  /**
   Apply the recurrence rule to the date.
   
   @param date Date that will be incremented. 
   */
  public void recur (GregorianCalendar date);
  
}
