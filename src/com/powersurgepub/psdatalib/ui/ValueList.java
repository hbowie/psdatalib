package com.powersurgepub.psdatalib.ui;

  import java.util.*;
  import javax.swing.*;

/**
   A collection of string values. New values are added to the list. The
   list is maintained in alphabetical order. A JComboBox is maintained
   and kept synchronized with the list. The case (upper- or lower-) of the
   values is not considered significant. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/09 - Originally written.
 */
public class ValueList
    extends DefaultComboBoxModel {
      
  private boolean allValuesInt = true;
  
  /** 
    Creates a new instance of ValueList 
   */
  public ValueList() {
    super();
    // values = new Vector();
    // comboBox = new JComboBox(values);
    // comboBox = new JComboBox();
  }
  
  public void addAll (ValueList list2) {
    for (int i = 0; i < list2.size(); i++) {
      addElement (list2.getElementAt (i));
    }
    this.fireContentsChanged(this, 0, getSize());
  }
  
  /**
    Compares a value to a list of 
    all existing values for this field, and adds the new value to the
    list if it is not already there.
    
    @return The position in the list at which the passed value was found or added.
    @param  value  The assigned value to be checked.
   */
  public int registerValue (int value) {
    String valueStr = "n/a";
    if (value >= 0) {
      valueStr = String.valueOf(value).trim();
    }
    return (registerValue (valueStr));
  }
  
  /**
    Compares a value to a list of 
    all existing values for this field, and adds the new value to the
    list if it is not already there.
    
    @return The position in the list at which the passed value was found or added.
    @param  value  The assigned value to be checked.
   */
  public int registerValue (String value) {
    int i = 0;
    int result = 1;
    while (i < getSize() && (result > 0)) {
      result = value.compareToIgnoreCase ((String)getElementAt (i));
      if (result > 0) {
        i++;
      }
    }
    if (result != 0) {
      insertElementAt (value, i);
      this.fireContentsChanged(this, i, getSize());
    }
    return i;
  }
  
  /**
  Compare a String representation of a value to a list of all existing values
  for this field, and adds the new value to the list if it is not already
  there. 
  
  @param value The object to be added. 
  @return The resulting position of the value in the list.  
  */
  public int registerValue (Object value) {
    int i = 0;
    int result = 1;
    while (i < getSize() && result > 0) {
      result = value.toString().compareToIgnoreCase(getElementAt(i).toString());
      if (result > 0) {
        i++;
      }
    }
    if (result == 0) {
      // Already in list -- replace existing element with new element
      this.removeElementAt(i);
      this.insertElementAt(value, i);
      this.fireContentsChanged(this, i, i);
    } else {
      // Not yet in list -- add it
      insertElementAt (value, i);
      this.fireContentsChanged(this, i, getSize());
    }
    return i;
  }
  
  /**
  Remove an integer value from the list. 
  
  @param value The integer value to be removed. 
  @return The index position of the value removed, or -1 if the value
          could not be found in the list. 
  */
  public int removeValue (int value) {
    String valueStr = "n/a";
    if (value >= 0) {
      valueStr = String.valueOf(value).trim();
    }
    return (removeValue (valueStr));
  }
  
  /**
  Remove an element from the list. 
  
  @param value The value to be removed. 
  @return      The index of the element removed, or -1 if the value was not 
               found in the list. 
  */
  public int removeValue(String value) {
    int i = 0;
    int result = 1;
    while (i < getSize() && (result > 0)) {
      result = value.compareToIgnoreCase ((String)getElementAt (i));
      if (result > 0) {
        i++;
      }
    }
    if (result == 0) {
      removeElementAt(i);
      fireIntervalRemoved(this, i, i);
    } else {
      i = -1;
    }
    return i;
  }
  
  /**
  Remove an element from the list. 
  
  @param value The value to be removed. 
  @return      The index of the element removed, or -1 if the value was not 
               found in the list. 
  */
  public int removeValue(Object value) {
    int i = 0;
    int result = 1;
    while (i < getSize() && (result > 0)) {
      result = value.toString().compareToIgnoreCase (getElementAt(i).toString());
      if (result > 0) {
        i++;
      }
    }
    if (result == 0) {
      removeElementAt(i);
      fireIntervalRemoved(this, i, i);
    } else {
      i = -1;
    }
    return i;
  }
  
  /**
    Looks up a value in the values list and returns its index position.
    
    @return The position in the list at which the passed value was found,
            or -1 if not found.
    @param  value  The value to be looked up.
   */
  public int lookupValue (int value) {
    String valueStr = "n/a";
    if (value >= 0) {
      valueStr = String.valueOf(value).trim();
    }
    return (lookupValue (valueStr));
  }
  
  /**
    Looks up a value in the values list and returns its index position.
    
    @return The position in the list at which the passed value was found,
            or -1 if not found.
    @param  value  The value to be looked up.
   */
  public int lookupValue (String value) {
    int i = 0;
    int result = 1;
    while (i < getSize() && (result > 0)) {
      result = value.compareToIgnoreCase ((String)getElementAt (i));
      if (result > 0) {
        i++;
      }
    }
    if (result != 0) {
      i = -1;
    }
    return i;
  }
  
  /**
    Looks up a value in the values list and returns its index position.
    
    @return The position in the list at which the passed value was found,
            or -1 if not found.
    @param  value  The value to be looked up.
   */
  public int lookupValue (Object value) {
    int i = 0;
    int result = 1;
    while (i < getSize() && (result > 0)) {
      result = value.toString().compareToIgnoreCase (getElementAt(i).toString());
      if (result > 0) {
        i++;
      }
    }
    if (result != 0) {
      i = -1;
    }
    return i;
  }
  
  /**
     Passes in an optional JComboBox for the field.
 
     @param  comboBox A user interface element that allows a user to 
                      select a value from a list.
   */
  public void setComboBox (JComboBox comboBox) {
    comboBox.setModel (this);
  }
  
  /**
    Gets a vector containing all values.
    
    @return Vector containing all values.
   */
  public Vector getList () {
    Vector values = new Vector();
    for (int i = 0; i < getSize(); i++) {
      values.add (getElementAt (i));
    }
    return values;
  }
  
  public int size() {
    return getSize();
  }

  public String toString () {
    StringBuffer work = new StringBuffer();
    int i = 0;
    while (i < getSize()) {
      if (work.length() > 0) {
        work.append(", ");
      }
      work.append (getElementAt(i).toString());
      i++;
    }
    return work.toString();
  }
  
}
