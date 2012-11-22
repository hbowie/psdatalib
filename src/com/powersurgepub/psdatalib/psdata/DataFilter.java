package com.powersurgepub.psdatalib.psdata;



  import com.powersurgepub.psdatalib.psdata.RecordDefinition;

  import com.powersurgepub.psdatalib.psdata.DataRecord;

  import java.lang.String;



/**

   A means of logically evaluating a data record. <p>

   

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

public interface DataFilter {

    

  /**

     Selects the record.

    

     @return Decision whether to select the record (true or false).

    

     @param dataRec A data record to evaluate.

    

     @throws IllegalArgumentException if the operator is invalid.

   */

  public boolean selects (DataRecord dataRec)

    throws IllegalArgumentException;

    

  /**

     Returns the record definition for the selecter.

    

     @return Record definition.

   */

  public RecordDefinition getRecDef();

    

  /**

     Returns the selecter as some kind of string.

    

     @return String identification of the selecter.

   */

  public String toString ();



} // end of DataFilter Interface

