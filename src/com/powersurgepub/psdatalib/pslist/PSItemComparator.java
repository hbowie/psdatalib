package com.powersurgepub.psdatalib.pslist;
  
  import com.powersurgepub.psutils.*;
  import java.util.*;

/**

   A comparator for an item, made up of zero or more field comparators. <p>

   @author Herb Bowie
 */
public class PSItemComparator 
    implements Comparator {
  
  /** The list containing the field to be compared. */
  private    PSList             psList;

  /** The collection of field comparators that make up this item comparator. */
  private    ArrayList<PSFieldComparator> fieldComparators;

  /** 
     Constructs a new item comparator. Field comparators must be added
     separately. 
   */
  public PSItemComparator (PSList psList) {
    this.psList = psList;
    fieldComparators = new ArrayList();
  }

  /**
     Adds another sequence field to the specification. The sequence in which
     fields are added determines their sort significance, going from major
     to minor.

     @param fieldComparator Next sequence field for this specification.
   */
  public void addField (PSFieldComparator fieldComparator) {
    fieldComparators.add (fieldComparator);
  }

  /**
     Adds another sequence field to the specification. The sequence in which
     fields are added determines their sort significance, going from major
     to minor.

     @param fieldName Name of next sequence field for this specification.
                      The desired sequence is assumed to be ascending.
   */

  public void addField (String fieldName) {
    fieldComparators.add (new PSFieldComparator (psList, fieldName));
  }

  /**
     Adds another sequence field to the specification. The sequence in which
     fields are added determines their sort significance, going from major
     to minor.

     @param fieldName Name of next sequence field for this specification.

     @param ascendingStr Something starting with 'D', 'd', 'F' or 'f' for 
                         descending, anything else for ascending.
   */
  public void addField (String fieldName, String ascendingStr) {
    fieldComparators.add 
      (new PSFieldComparator (psList, fieldName, ascendingStr));
  }

  /**
     Adds another sequence field to the specification. The sequence in which
     fields are added determines their sort significance, going from major
     to minor.

     @param fieldName Name of next sequence field for this specification.

     @param ascendingChar Character of 'D', 'd', 'F' or 'f' for 
                          descending, anything else for ascending.
   */
  public void addField (String fieldName, char ascendingChar) {
    fieldComparators.add
      (new PSFieldComparator (psList, fieldName, ascendingChar));
  }
  
  public int getNumberOfFields() {
    return fieldComparators.size();
  }
  
   /**
    Compares the given field within the two passed items.

    @param item1 The first item.
    @param item2 The second item. 

    @return -1 if the first field is lower than the second, 
            +1 of the first field is higher than the second, or
            zero if the two fields are equal.
   */
  public int compare (Object obj1, Object obj2) {
    
    int result = 0;
    if (obj1 instanceof PSItem && obj2 instanceof PSItem) {
      PSItem item1 = (PSItem)obj1;
      PSItem item2 = (PSItem)obj2;
      for (int i = 0; 
           i < fieldComparators.size() && result == 0; 
           i++) {
        PSFieldComparator fieldComparator = fieldComparators.get(i);
        result = fieldComparator.compare(obj1, obj2);
      }
    }
    return result;
  }

  /**
     Returns this object as some kind of string.

     @return Concatenation of the string representation of all the
             specification's sequence fields.
   */
  public String toString () {

    StringBuilder recordBuf = new StringBuilder ();
    for (int i = 0; i < fieldComparators.size (); i++) {
      if (i > 0) {
        recordBuf.append (GlobalConstants.LINE_FEED_STRING);
      }
      recordBuf.append (fieldComparators.get(i).toString());
    }
    return recordBuf.toString ();

  }

} // end class PSItemComparator