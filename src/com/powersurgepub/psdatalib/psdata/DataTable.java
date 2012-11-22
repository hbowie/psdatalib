package com.powersurgepub.psdatalib.psdata;

  import javax.swing.table.AbstractTableModel;
  import com.powersurgepub.psutils.*;
  
/**
   A collection of DataRecord objects that can be used as a Swing Table Model. <p>
   
   This code is copyright (c) 2000-2002 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
           11/05/2000 - Initially written.
      </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
  
   @version 09/07/2002 - Changed getValueAt to return better data from
                         short records. 
 */
public class DataTable 
    extends AbstractTableModel {
    
  /** Collection of data records. */
  private		 DataSet						dataSet;
  
  /** Definition of the records being stored in the data set. */
  private    RecordDefinition   recDef;
  
  /** 
     Constructs an empty DataTable.
   */
  public DataTable () {
    this.recDef = new RecordDefinition ();
    this.dataSet = new DataSet(recDef);
  }
  
  /** 
     Constructs a DataTable with a data set.
    
     @param dataSet Collection of data to be accessed in table format.
   */
  public DataTable (DataSet dataSet) {
  	setDataSet (dataSet);
  }
  
  /**
     Sets the data set to be manipulated by the Data Table.
    
     @param dataSet Collection of data to be accessed in table format.
   */
  public void setDataSet (DataSet dataSet) {
  	this.dataSet = dataSet;
  	this.recDef  = dataSet.getRecDef();
  }
  
  /**
     Returns the name of the column, in English-like format.
    
     @return Name of the column, in English-like format 
             with spaces between presumed words.
    
     @param  column Column number whose name is to be returned.
   */
  public String getColumnName (int column) {
  	DataFieldDefinition workDef = recDef.getDef (column);
  	String name = workDef.getProperName();
  	return StringUtils.wordSpace (name, false);
  }
  
  /**
     Returns the number of columns in the table.
    
     @return Number of columns in table.
   */
  public int getColumnCount () {
    return dataSet.getNumberOfFields ();
  }
  
  /** 
     Returns the number of rows in the table.
    
     @return Number of rows (records) in the table.
   */
  public int getRowCount () {
    return dataSet.getNumberOfRecords();
  }
  
  /**
     Returns the value at a given row and column.
    
     @return Data stored in the given cell.
    
     @param  row The number of the row (record) within the table.
    
     @param  column The number of the column within the table.
   */
  public Object getValueAt(int row, int column) 
  		throws ArrayIndexOutOfBoundsException {
    if ((row < 0) 
    	|| (row >= getRowCount())
    	|| (column < 0)
    	|| (column >= getColumnCount())) {
    	throw new ArrayIndexOutOfBoundsException();
    }
    DataRecord workRec = dataSet.getRecord (row);
    DataField workField = workRec.getField (recDef, column);
    return workField.getData();
  }
  
} // end class DataTable
