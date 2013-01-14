package com.powersurgepub.psdatalib.pslist;

  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psfiles.*;
  import com.powersurgepub.psdatalib.pslist.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.util.*;
  import javax.swing.table.*;

/**
 A list of data records that can be sorted and filtered. 

 @author Herb Bowie
 */
public class DataRecList 
    extends
      AbstractTableModel
    implements
        PSList {
  
  private     FileSpec            fileSpec = null;
  
  private     DataDictionary      dataDict = null;
  private     RecordDefinition    recDef = null;
    
  private     DataSet             completeDataSet;
	private     DataSet             filteredDataSet;  
  
  private     Comparator          comparator = new PSDefaultComparator();
  private     PSItemFilter        itemFilter = null;
  
  private     int                 findIndex = -1;
  private     boolean             findMatch = false;
  
  /**
   Constructor with no arguments.
  */
  public DataRecList() {
    super();
  }
  
  /**
   Initialize the list with no items in the list. 
  */
  public void initialize() {
    // fileNameToDisplay = "No Input File";
    // tabName = "";
    fileSpec = null;
    dataDict = new DataDictionary();
    dataDict.setLog (Logger.getShared());
    recDef = new RecordDefinition(dataDict);
    completeDataSet = new DataSet (recDef);
    completeDataSet.setLog (Logger.getShared());
    comparator = new PSDefaultComparator();
    itemFilter = null;
    reloadFilteredDataSet();
  }
  
  /**
   Set the data source, stored in the root node of the TagsModel tree.
 
   @param source The file or folder from which the data is taken.
  */
  public void setSource (FileSpec fileSpec) {
    this.fileSpec = fileSpec;
  }
  
  /**
     Adds a subsequent DataSource to an existing DataSet, merging the two
     RecordDefinitions into a new combined one, with an unlimited
     number of records.
    
     @param inData Data source that allows data records to be read.
    
     @throws IOException If the passed data source experiences an i/o error.
   */
  public void merge(DataSource inData) 
      throws IOException {
    completeDataSet.merge(inData);
    reloadFilteredDataSet();
  }
  
  /**
     Adds a subsequent DataSource to an existing DataSet, using the
     record definition for the existing DataSet as the definition
     for the new one as well.
    
     @param inData Data source that allows data records to be read.
    
     @throws IOException If the passed data source experiences an i/o error.
   */
  public void mergeSame (DataSource inData) 
      throws IOException {
    completeDataSet.mergeSame(inData);
    reloadFilteredDataSet();
  }
  
  public void load (DataDictionary dataDict, DataSource dataSource, Logger log) 
      throws IOException {
    completeDataSet = new DataSet (dataDict, dataSource, log);
    reloadFilteredDataSet();
  }
  
  public int getRecordsLoaded() {
    return completeDataSet.getRecordsLoaded();
  }
 
  /**
   Get the data source, stored in the root node of the TagsModel tree.
 
   @return The file or folder from which the data is taken.
  */
  public FileSpec getSource () {
    return fileSpec;
  }
  
  /**
   Return the record definition for this list 
   
   @return The record definition, or null if no record defined yet. 
   */
  public RecordDefinition getRecDef() {
    return recDef;
  }
  
  /**
   Sets a data filter to be used to select desired output records.
 
   @param inputFilter Desired output filter.
   */
  public void setInputFilter (PSItemFilter inputFilter) {
    this.itemFilter = inputFilter;
    reloadFilteredDataSet();
    fireTableDataChanged();
  }
  
  public void setComparator (Comparator comparator) {
    if (comparator == null) {
      this.comparator = new PSDefaultComparator();
    } 
    else
    if (comparator instanceof PSItemComparator) {
      PSItemComparator itemComparator = (PSItemComparator)comparator;
      if (itemComparator.getNumberOfFields() == 0) {
        this.comparator = new PSDefaultComparator();
      } else {
        this.comparator = comparator;
      }
    } else {
      this.comparator = new PSDefaultComparator();
    }
    reloadFilteredDataSet();
    fireTableDataChanged();
  }
  
  public void reloadFilteredDataSet() {
    filteredDataSet = new DataSet(completeDataSet.getRecDef());
    for (int i = 0; i < completeDataSet.size(); i++) {
      addToFilteredDataSet(i);
    }
  }
  
  private void addToFilteredDataSet (int i) {
    DataRecord dataRec = completeDataSet.getRecord(i);
    /* System.out.println (
        "=$itemclass$=List.addToFilteredDataSet " 
        + " # "
        + String.valueOf(i)
        + " : "
        + =$itemclass&clul$=.toString()); */
    if (itemSelected(dataRec)) {
      /* System.out.println("  item selected");
      System.out.println("  size of proxy list = " + String.valueOf(size()));
      if (size() > 0) {
        System.out.println ("  item at end of list = " + get(size() - 1).toString());
      } */
      findIndex = size();
      if (size() == 0) {
        // If this is the first =$itemclass$= being added to the proxy list,
        // simply add the proxy to the list
        filteredDataSet.add(dataRec);
      }
      else
      if (comparator.compare (get(size() - 1), dataRec) < 0) {
        // If the new item has a key higher than the highest item in the
        // collection, simply add the new item to the end
        // (more efficient if an input file happens to be pre-sorted).
        filteredDataSet.add (dataRec);
      } else {
        findInternal (dataRec);
        filteredDataSet.add (findIndex, dataRec);
      }
    }
  }
  
  /**
   Remove a record from the list, based on the record's position within the
   complete list. 
  
   @param index Index pointing to the record's position within the 
                complete list. 
  
   @return True if the index reference was valid, false otherwise. 
  */
  public boolean removeFromCompleteList (int index) {
    if (index < 0) {
      return false;
    }
    else
    if (index >= completeDataSet.size()) {
      return false;
    } else {
      DataRecord recToRemove = completeDataSet.getRecord(index);
      completeDataSet.removeRecord(index);
      findInternal (recToRemove);
      if (findMatch) {
        filteredDataSet.removeRecord(findIndex);
      }
      return true;
    }
  }
  
  public boolean itemSelected (DataRecord dataRec) {
    if (itemFilter == null) {
      return true;
    } else {
      return itemFilter.selects(dataRec);
    }
  }
  
  /**
   Find the appropriate insertion point or match point in the filtered and
   sorted data record list, and use findIndex and findMatch to 
   return the results.

   @param findDataRec Data record we are looking for.
   */
  private void findInternal (DataRecord findDataRec) {
    int low = 0;
    int high = size() - 1;
    findIndex = 0;
    findMatch = false;
    while (high >= low
        && findMatch == false
        && findIndex < size()) {
      int diff = high - low;
      int split = diff / 2;
      findIndex = low + split;
      int compare = comparator.compare (get(findIndex), findDataRec);
      if (compare == 0) {
        // found an exact match
        findMatch = true;
      }
      else
      if (compare < 0) {
        // =$itemclass$= from list is less than the one we're looking for
        findIndex++;
        low = findIndex;
      } else {
        // =$itemclass$= from list is greater than the one we're looking for
        if (high > findIndex) {
          high = findIndex;
        } else {
          high = findIndex - 1;
        }
      }
    } // end while looking for right position
  } // end find method
  
  /**
   Get the item at the specified index in the sorted, filtered list.
 
   @param index Indicates the desired position within the sorted, filtered list.
 
   @return The item at the desired index, or null if the index is out
           of bounds.
  */
  public DataRecord get (int index) {
    if (index >= 0 && index < size()) {
      return filteredDataSet.get(index);
    } else {
      return null;
    }
  } // end method get (int)
  
  /**
   Return the size of the total list, without any filtering.
   */
  public int totalSize() {
    return completeDataSet.size();
  }
 
  /**
   Return the size of the filtered list.
 
   @return The size of the filtered list.
  */
  public int size() {
    return filteredDataSet.size();
  }

  /**
   Return the number of rows in the table.
   */
  public int getRowCount() {
    return size();
  }
  
  /**
	 Returns a List containing the names of all the fields
	 stored in the data set.
	
	 @return Proper names of all the fields.
   */
  public List getNames() {
    ArrayList names = new ArrayList();
    for (int i = 0; i < getColumnCount(); i++) {
      names.add(getColumnName(i));
    }
    return names;
  }
  
  public String getColumnName (int columnIndex) {
    if (recDef == null) {
      return "";
    } else {
      return recDef.getDef(columnIndex).getProperName();
    }
  }
  
  /**
   Get the column number assigned to a particular field name. 
  
   @param columnName The name of the column. Case and word separation are
                     not significant. 
  
   @return The column number (with the first starting at zero), or -1
           if the field name could not be found.
  */
  public int getColumnNumber (String columnName) {
    if (recDef == null) {
      return -1;
    } else {
      return recDef.getColumnNumber(columnName);
    }
  }
  
  /**
   Return the column value of the item at the specified index.
   */
  public String getValueAt (int rowIndex, int columnIndex) {
    DataRecord dataRec = get(rowIndex);
    if (dataRec == null) {
      return "";
    } else {
      Object columnValue = dataRec.getColumnValue(columnIndex);
      if (columnValue == null) {
        return "";
      } else {
        return columnValue.toString();
      }
    }
  } // end method getValueAt
  
  /**
   Return the number of columns.
   */
  public int getColumnCount () {
    if (recDef == null) {
      return 0;
    } else {
      return recDef.getNumberOfFields();
    }
  }

}
