package com.powersurgepub.psdatalib.pslist;

  import java.util.*;
  import com.powersurgepub.psutils.GlobalConstants;

/**
   A filter specification for a particular list, 
   made up of zero or more field fieldFilters. 

   @author Herb Bowie
 */

public class PSItemFilter {

  /** Does and logic apply? (If not, then "or" logic.) */
  private    boolean                  andLogic = false;

  /** The collection of field filters that make up this item filter. */
  private    ArrayList<PSFieldFilter> fieldFilters;

  /** 
     Constructs a new item filter specification.     

     @param andLogic True if and logic is to be used.
   */

  public PSItemFilter (boolean andLogic) {

    this.andLogic = andLogic;
    fieldFilters = new ArrayList();
  }

  /**
     Adds another data filter to the specification. The sequence in which
     fields are added determines their evaluation order.

     @param filter Next field filter for this specification.
   */
  public void addFilter (PSFieldFilter filter) {
    fieldFilters.add (filter);
  }

   /**
     Selects the item.

     @return Decision whether to select the record (true or false).

     @param psItem An item to evaluate.

     @throws IllegalArgumentException if the operator is invalid.
   */
  public boolean selects (PSItem psItem) 
      throws IllegalArgumentException {

    int size = fieldFilters.size();
    if (size == 0) {
      return true;
    } else {
      int i = 0;
      boolean selected = selectionAt (psItem, i);
      boolean endCondition;
      if (andLogic) {
        endCondition = false;
      } else {
        endCondition = true;
      }
      i = 1;
      while ((selected != endCondition) && (i < size)) {
        selected = selectionAt (psItem, i);
        i++;
      }
      return selected;
    } // end condition where number of fieldFilters is non-zero
  } // end selects method

  private boolean selectionAt (PSItem psItem, int index) 
      throws IllegalArgumentException {

    PSFieldFilter oneFilter = fieldFilters.get (index);
    return oneFilter.selects (psItem);
  }

  /**
     Sets the And logic flag.

     @param andLogic True if "and" logic is to be used, false if "or".
   */
  public void setAndLogic (boolean andLogic) {
    this.andLogic = andLogic;
  } 

  /**
     Returns this object as some kind of string.

     @return Concatenation of the string representation of all the
             field filters.
   */
  public String toString () {

    StringBuilder recordBuf = new StringBuilder ();
    for (int i = 0; i < fieldFilters.size (); i++) {
      if (i > 0) {
        recordBuf.append (GlobalConstants.LINE_FEED_STRING);
      }
      recordBuf.append (fieldFilters.get(i).toString());
    }
    return recordBuf.toString ();
  } // end toString method

} // end class PSItemFilter