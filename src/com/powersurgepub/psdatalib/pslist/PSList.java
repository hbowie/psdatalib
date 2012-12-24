package com.powersurgepub.psdatalib.pslist;

  import com.powersurgepub.psdatalib.psdata.*;
  import java.util.*;

/**
 Standard interface for a list of items that can be sorted, filtered, searched,
 tagged, etc. 

 @author Herb Bowie
 */
public interface PSList {
  
  /**
   Returns the size of the list.

   @param  The size of the list.
   */
  public int size();
  
  /**
   Returns the total size of the list, disregarding any filtering. 
  
   @return The total number of items in the list, disregarding any filtering.
  */
  public int totalSize();
  
  public RecordDefinition getRecDef();
  
  /**
     Returns a List containing the names of all the fields
     stored in the data set.
    
     @return Proper names of all the fields.
   */
  public List getNames();

  /**
   Return An item from the list.

   @param  An index identifying which item from the list to be returned.
   @return The item requested, or null if the passed index is
           not a valid reference to an item in the list.
   */
  public PSItem get (int i);
  
  /**
   Get the number of rows in the list/table.
  
   @return The number of rows in the list/table. 
  */
  public int getRowCount();
  
  /**
   Get the number of columns in the list/table.
  
   @return The number of columns in the list/table. 
  */
  public int getColumnCount();
  
  /**
   Get the value stored in one cell of the list/table. 
  
   @param row    The row index, with the first row denoted by zero.
  
   @param column The column index, with the first column denoted by zero. 
  
   @return The value stored at the identified cell.
  */
  public Object getValueAt(int row, int column);
  
  /**
   Get the column number for a given column name. 
  
   @param columnName The name of the column. 
  
   @return The index for the given column name, starting at 0, or -1
           if the column name could not be found. 
  */
  public int getColumnNumber (String columnName);
  
  /**
     Sets a data filter to be used to select desired output records.
    
     @param inputFilter Desired output filter. 
   */
  public void setInputFilter (PSItemFilter inputFilter);

  
}
