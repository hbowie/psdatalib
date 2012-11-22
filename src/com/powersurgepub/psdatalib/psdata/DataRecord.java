package com.powersurgepub.psdatalib.psdata;

  import java.util.*;
  import com.powersurgepub.psutils.*;
  
/**
   A record or row, consisting of one or more
   Data Fields. Since each field contains its own
   definition, a record definition is not contained
   within each data record. <p>
   
   This code is copyright (c) 1999-2001 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
   	 
     2003/07/28 - Added support for field calculation.
     2003/02/10 - Added convenience methods getFieldAsInteger and
                  getFieldAsBoolean. <li>
     2002/09/02 - Added combine method, to combine two records
                  whose keys are equal. <br>
                  Modified getField methods to pass back 
                  DataField objects with requested field name 
                  and null data. <li>
     2002/08/27 - Added record sequence number, to preserve sequence in 
                  which records were created/added, for use as a precedence 
                  indicator in combine processing. <li>
     2001/02/25 - Corrected containsField method to check for new UNKNOWN_FIELD
                  constant, in addition to null. <li>
     2001/02/05 - Removed ArrayIndexOutOfBoundsException from getField method
                  when bad column number is passed. Now return unknown field
                  instead. <li>
     2000/11/05 - Added ArrayIndexOutOfBoundsException when bad column
                         number is passed. <li>
     2000/05/08 - Modified to be consistent with "The Elements of Java Style".
      </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">
           www.powersurgepub.com/software</a>)
  
   @version 
     2004/06/01 - Modified toString method to include header literal. 
 */

public class DataRecord 
    extends DataField {
  
  /** An index pointing to a particular field number within the record. */
  private   int           fieldNumber;
  
  /** A number indicating sequence in which records were created/added. */
  private		int						recordSequence = 0;
  
  /**
     Constructs a new data record with no fields. 
   */
  public DataRecord () {
    
  }
  

  
  /**
     Adds a new field to this record, if one with this name does not 
     already exist. If one with this name does exist, then updates the
     data portion of the field to reflect the new data value. Maintains
     field length statistics. 
    
     @return Column number of field added or updated.
    
     @param  recDef Definition to be used for this record.
     @param  name   Name of the field to be added or updated.
     @param  data   Data value to be added or updated.
   */
  public int storeField (RecordDefinition recDef, String name, String data) {
    int columnNumber = getColumnNumber (name);
    DataField targetField = getField (columnNumber);
    DataFieldDefinition workDef = null;
    if (targetField == null
        || targetField == UNKNOWN_FIELD) {
      columnNumber = recDef.getColumnNumber(name);
      if (columnNumber >= 0) {
        workDef = recDef.getDef(columnNumber);
      } else {
        workDef = new DataFieldDefinition (name);
        columnNumber = recDef.addColumn(workDef);
      }
      targetField = new DataField (workDef, data);
      while (getNumberOfFields() < columnNumber) {
        DataFieldDefinition missingDef = recDef.getDef(getNumberOfFields());
        DataField missingField = new DataField(missingDef, "");
        addField(missingField);
      }
      return addField (targetField);
    } 
    else {
      targetField.setData (data);
      recDef.anotherField (data, columnNumber);
      return columnNumber;
    }
  }
  

  
  /**
    For calculated fields, calculate the field values.
   */
  public void calculate () {
    int column = 0;
    DataField field;
    while (column < fields.size()) {
      field = (DataField)fields.get (column);
      if (field.isCalculated()) {
        field.calculate (this);
      }
      column++;
    }
  }
  
  /**
     Compares the keys of two data records to see if they
     are equal.
     
     @return zero if records have identical keys, 
             a positive number if this record is greater than rec2, or
             a negative number if this record is less than rec2.
    
     @param  rec2 Second data record to compare to this one.
    
     @param  seq  A sequence specification that defines the key
                  fields for both records.
   */
  public int compareTo (DataRecord rec2, SequenceSpec seq) {
    SequenceField fieldSeq;
    int columnNumber, compareResult = 0;
    boolean ascending;
    DataField field1, field2;
    seq.startWithFirstField();
    while ((seq.hasMoreFields()) && (compareResult == 0)) {
      fieldSeq = seq.nextField();
      columnNumber = fieldSeq.getColumnNumber();
      field1 = this.getField (columnNumber);
      field2 = rec2.getField (columnNumber);
      compareResult = field1.compareTo (field2);
      if (! fieldSeq.isAscending()) {
        compareResult = compareResult * -1;
      }
    }
      return compareResult;
  }
    
  /**
     Tries to combine the two records.
     
     @return true if records were combined successfully, false if they were not. 
    
     @param  rec2   Second data record to compare to this one.
     @param  recDef The record definition for the two records.
     @param  precedence Indicator of whether earlier or later record
                        take precedence.
     @param  maxAllowed The maximum return value allowed as a result of the 
                        combination of the two records' fields. 
     @param  minNoLoss  If maxAllowed permits data to be overwritten,
                        then this parameter specifies the minimum number
                        of fields that must be without data loss.
   */
  public boolean combine (DataRecord rec2, RecordDefinition recDef, 
      int precedence, int maxAllowed, int minNoLoss) {
    Iterator columns = recDef.iterator();
    int result = 0;
    int maxResult = 0;
    int noLossFields = 0;
    Column column;
    DataField field1, field2;
    boolean combineSuccess;
    int columnIndex = 0;
    while (columns.hasNext()) {
      column = (Column)columns.next();
      field1 = getField (recDef, columnIndex);
      field2 = rec2.getField (recDef, columnIndex);
      result = field1.combine (field2, precedence, 
          getRecordSequence(), rec2.getRecordSequence());
      if (result > maxResult) {
        maxResult = result;
      }
      if (result == DataField.NO_DATA_LOSS) {
        noLossFields++;
      }
      columnIndex++;
    } // end while more columns
    if (maxResult > maxAllowed) {
      combineSuccess = false;
    }
    else
    if (maxResult == maxAllowed
        && maxAllowed == DataField.DATA_OVERRIDE
        && noLossFields < minNoLoss) {
      combineSuccess = false;
    }
    else {
      combineSuccess = true;
    }
    columns = recDef.iterator();
    columnIndex = 0;
    while (columns.hasNext()) {
      column = (Column)columns.next();
      field1 = getField (recDef, columnIndex);
      field1.finalizeCombining (combineSuccess);
      columnIndex++;
    } // end while more columns
    return combineSuccess;
  } // end combine method
  
  /**
     Indicates whether this records has more fields to process,
     using an internal index.
    
     @return True if there are more fields to process.
   */
  public boolean hasMoreFields () {
    return (fieldNumber < fields.size());
  }
  
  /**
     Returns the first field in the record, and positions the internal
     index for subsequent calls to nextField.
    
     @return First data field within this record.
   */
  public DataField firstField () {
    startWithFirstField ();
    return nextField ();
  }
  
  /**
     Positions the intenal index so that nextField will return the 
     first field the next time it is called.
   */
  public void startWithFirstField () {
    fieldNumber = 0;
  }
  
  /**
     Returns the next field in the record.
    
     @return Next field within record, using internal index
             to keep track of position within record.
   */
  public DataField nextField () {
    return getField (fieldNumber++);
  }
  
  /**
     Indicates whether this record contains the given field.
    
     @return True if the field is already contained within this
                  record, with a non-empty data value.
    
     @param  inName Name of the field of interest.
   */
  public boolean containsField (String inName) {
    DataField workField = getField (inName);
    if ((workField == null) || (workField == UNKNOWN_FIELD)) {
      return false;
    }
    Object workObject = workField.getData();
    if (workObject == null) {
      return false;
    }
    String workString = (String)workObject;
    if (workString.equals ("")) {
      return false;
    }
    return true;
  }
  



  
  /** 
     Sets the record number.
    
     param recordSequence A sequentially assigned record number.
   */
  public void setRecordSequence (int recordSequence) {
    this.recordSequence = recordSequence;
  }
  
  /** 
     Gets the record number.
    
     @return Sequentially assigned record number.
   */
  public int getRecordSequence () {
    return recordSequence;
  }
  
  public String getData () {
    return toString();
  }
  
  /**
     Returns this record as some kind of string.
    
     @return Concatenation of all the fields within this record.
   */
  public String toString () {
    StringBuffer recordBuf = new StringBuffer ("DataRecord -- ");
    for (int i = 0; i < fields.size (); i++) {
      if (i > 0) {
        recordBuf.append ("; ");
      }
      recordBuf.append (((DataField)fields.get(i)).toString());
    }
    return recordBuf.toString ();
  }
  
}