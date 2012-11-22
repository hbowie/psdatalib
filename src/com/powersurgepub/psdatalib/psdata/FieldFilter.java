package com.powersurgepub.psdatalib.psdata;



  import com.powersurgepub.psdatalib.psdata.RecordDefinition;

  import java.lang.String;



/**

   One field used in a filter specification

   (used for record selection, etc.). <p>

   

   This code is copyright (c) 2000 by Herb Bowie of PowerSurge Publishing. 

   All rights reserved. <p>

   

   Version History: <ul><li>

      </ul>

  

   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">

           herb@powersurgepub.com</a>)<br>

           of PowerSurge Publishing (<A href="http://www.powersurgepub.com/software/">

           www.powersurgepub.com/software</a>)

  

   @version 2000/11/29 - First written for tabview project.

 */

public class FieldFilter 

    implements  DataFilter {

        

  /** The record definition containing the field to be used in a filter spec. */

  private    RecordDefinition   recDef;

  

  /** The column number of this field within the record definition. */

  private    int                column;

  

  /** Logical operator to use for comparison. */

  private    String             operator;

  

  /** Value to be compared to field data. */

  private    DataField          value;



/**

   Constructs the filter field.

  

   @param recDef        the Record Definition of the record to be selected.

  

   @param fieldName     the name of a field within the record definition.

  

   @param operator      the operator to use for comparison.

  

   @param valueString   the value to compare the field to, passed as a String.

 */

  public FieldFilter (RecordDefinition recDef, String fieldName, 

      String operator, String valueString) {

    this.recDef = recDef;

    this.column = recDef.getColumnNumber (fieldName);

    this.operator = operator;

    this.value = new DataField (recDef, column, valueString);

  }

  

   /**

     Selects the record.

    

     @return Decision whether to select the record (true or false).

    

     @param dataRec A data record to evaluate.

    

     @throws IllegalArgumentException if the operator is invalid.

   */

  public boolean selects (DataRecord dataRec) 

    throws IllegalArgumentException {

    DataField field = dataRec.getField (column);

    return field.operateLogically (operator, value);

  }

    

  /**

     Returns the record definition for the filter.

    

     @return Record definition.

   */

  public RecordDefinition getRecDef() {

    return recDef;

  }

  

  /**

     Returns the column number of this field within its record definition.

    

     @return Column number of this field.

   */

  public int getColumn () {

    return column;

  }

  

  /**

     Returns the operator for this field.

    

     @return Operator for this field.

   */

  public String getOperator () {

    return operator;

  }

  

  /**

     Returns the value to which this field is to be compared.

    

     @return Value to use in comparison.

   */

  public String getValue () {

    return value.getData();

  }

  

  /**

     Returns this object as some kind of string.

    

     @return Column number plus ascending or descending.

   */

  public String toString () {

    return ("Select record if field "

      + recDef.getDef(column).getProperName() + " " + operator + " " + value.getData());

  }



} // end FieldFilter Class

