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

package com.powersurgepub.psdatalib.psdata.widgets;

  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.net.*;
  import java.util.*;
  import javax.swing.*;

/**
 An extended combo box with several additional convenience methods added. 

 @author Herb Bowie
 */
public class ComboBoxWidget 
    extends JComboBox 
      implements DataWidget {
  
  private ResourceList list = null;
  
  /**
   A constructor with no arguments. 
  */
  public ComboBoxWidget() {
    super();
    list = new ResourceList(this);
  }
  
  public ComboBoxWidget (Object[] items) {
    super(items);
    list = new ResourceList(this);
  }
  
  /**
   Load a list of items into the combo box list, reading the list of items
   from a text file containing one item per line. 
  
   @param klass The reference class from which the resource will be found. 
  
   @param name The name of the resource. If it lacks a file extension, then
               ".txt" will be added to the end. 
  
   @return True if the list was loaded successfully, false otherwise. 
  */
  public boolean load (Class klass, String name) {
    
    boolean ok = true;
    list = new ResourceList (klass, name);
    ok = list.isOK();
    if (ok) {
      ok = list.load(this);
    }
    return ok;
  }
  
  /**
   Load a list of items into the combo box list, reading the list of items
   from a text file containing one item per line. 
  
   @param url The url pointing to the list to be loaded. 
  
   @return True if the list was loaded successfully, false otherwise.
  */
  public boolean load (URL url) {
    boolean ok = true;
    list = new ResourceList (url);
    ok = list.isOK();
    if (ok) {
      ok = list.load(this);
    }
    return ok;
  }
  
  /**
   Load a list of items into the combo box list, reading the list of items
   from a text file containing one item per line. 
  
   @param stream The stream containing the list to be loaded. 
  
   @return True if the list was loaded successfully, false otherwise.
  */
  public boolean load (InputStream stream) {
    boolean ok = true;
    list = new ResourceList (stream);
    ok = list.isOK();
    if (ok) {
      ok = list.load(this);
    }
    return ok;
  }

  /**
   Load a list of items into the combo box list, reading the list of items
   from a provided list. 
  
   @param list The list from which to populate the combo box. 
  
   @param startFromScratch If true, then the combo box will be purged of any
                           existing items before the provided list is loaded. 
  
   @return True to indicate everything went ok, for consistency with other 
           load methods. 
  */
  public boolean load (List list, boolean startFromScratch) {
    boolean ok = true;
    if (getItemCount() > 0 && startFromScratch) {
      removeAllItems();
    }
    for (int i = 0; i < list.size(); i++) {
      addItem(list.get(i).toString());
    }
    return ok;
  } // end comboBoxLoad method
  
  /**
   Make sure the passed string is an item in the list, and make it the 
   selected item. 
  
   @param text 
  */
  public void setText (String text) {
    int i = addAlphabetical (text);
    setSelectedIndex(i);
  }
  
  /**
   Return the selected item from the list as a String. 
  
   @return The selected item, or an empty string if no item is selected. 
  */
  public String getText () {
    int i = getSelectedIndex();
    if (i >= 0 && i < getItemCount()) {
      return (String)getItemAt(i);
    } else {
      return "";
    }
  }
  
  /**
   Add another item to the list displayed in the combo box. Keep the list
   in alphabetical order, and do not allow duplicates. 
  
   @param anotherItem The item to be added. If it has leading or trailing
                      white space, then this will be removed. 
  
   @return An index pointing to the position in the list at which the given
           item was located, or added. 
  */
  public int addAlphabetical (String anotherItem) {
    
    return list.addAlphabetical(anotherItem);
  }

}
