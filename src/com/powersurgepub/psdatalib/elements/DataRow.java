package com.powersurgepub.psdatalib.elements;

/**
  A row, as in a table, consisting of a fixed number of data elements.

 * @author Herb Bowie
 */
public interface DataRow {

  /**
   Obtain the number of columns, or data elements, contained in each row.

   @return The number of columns, or data elements, contained in each row.
   */
  public int getColumnCount ();

  /**
   Obtain the name of the designated column.

   @param columnIndex Indicates which column is of interest.
   
   @return The name of the specified column.
   */
  public String getColumnName (int columnIndex);

  /**
   Obtain the preferred class representation for the data values contained in
   the specified column.

   @param columnIndex Indicates the column of interest.

   @return The preferred class representation for the data values contained in
           the specified column.
   */
  public Class getColumnClass (int columnIndex);

  /**
   Obtain the value of the specified data element.

   @param columnIndex Indicates the column of interest.

   @return The value of the specified data element. 
   */
  public String getValueAt (int columnIndex);

  /**
   Obtain the data element at the specified column, or null if the
   column is outside the valid range.

   @param columnIndex Indicates the column of interest.

   @return The data element at the specified column, or null if the
           column is outside the valid range.
   */
  public DataElement getElementAt (int columnIndex);

}
