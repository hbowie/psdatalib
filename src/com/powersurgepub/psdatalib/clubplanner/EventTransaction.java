/*
 * Copyright 2012 - 2013 Herb Bowie
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

package com.powersurgepub.psdatalib.clubplanner;

  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.pslist.*;
  import com.powersurgepub.psdatalib.pstags.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.math.*;
  import java.util.*;
 
/**
 A single event, or other item to be tracked by the club. <p>
 
    This item class definition generated by PSTextMerge using: <p>
 
     template:  item-class.java <p>
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls

 @author Herb Bowie
 */
public class EventTransaction
    implements
      // Generated by PSTextMerge using template taggable-implements.java.
      // No taggable fields
            Comparable,
            PSItem
			 {

  private static final RecordDefinition recDef;


  /*
   Following code generated by PSTextMerge using:
 
     template:  variable-definitions.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 

  /**
   The date on which the financial transaction occurred.
   */
  private String date = null;
 
  public static final String DATE_FIELD_NAME = "Date";
 
  public static final String DATE_COLUMN_NAME = "Date";
 
  public static final String DATE_COMMON_NAME = "date";
 
  public static final int DATE_COLUMN_INDEX = 0;
 
  public static final int DATE_COLUMN_WIDTH = 10;
 

  /**
   Identified whether this is an income or an expense item.
   */
  private String incomeExpense = null;
 
  public static final String INCOME_EXPENSE_FIELD_NAME = "Income/Expense";
 
  public static final String INCOME_EXPENSE_COLUMN_NAME = "Inc/Exp";
 
  public static final String INCOME_EXPENSE_COMMON_NAME = "incomeexpense";
 
  public static final int INCOME_EXPENSE_COLUMN_INDEX = 1;
 
  public static final int INCOME_EXPENSE_COLUMN_WIDTH = 10;
 

  /**
   The person or company from which we received money, or to whom we paid money.
   */
  private String fromTo = null;
 
  public static final String FROM_TO_FIELD_NAME = "From/To";
 
  public static final String FROM_TO_COLUMN_NAME = "From/To";
 
  public static final String FROM_TO_COMMON_NAME = "fromto";
 
  public static final int FROM_TO_COLUMN_INDEX = 2;
 
  public static final int FROM_TO_COLUMN_WIDTH = 10;
 

  /**
   A description of the item.
   */
  private String paidFor = null;
 
  public static final String PAID_FOR_FIELD_NAME = "Paid For";
 
  public static final String PAID_FOR_COLUMN_NAME = "For";
 
  public static final String PAID_FOR_COMMON_NAME = "paidfor";
 
  public static final int PAID_FOR_COLUMN_INDEX = 3;
 
  public static final int PAID_FOR_COLUMN_WIDTH = 40;
 

  /**
   The amount of the transaction.
   */
  private String amount = null;
 
  public static final String AMOUNT_FIELD_NAME = "Amount";
 
  public static final String AMOUNT_COLUMN_NAME = "Amount";
 
  public static final String AMOUNT_COMMON_NAME = "amount";
 
  public static final int AMOUNT_COLUMN_INDEX = 4;
 
  public static final int AMOUNT_COLUMN_WIDTH = 40;
 

  public static final int COLUMN_COUNT = 5;


  private boolean modified = false;
 
  private String  diskLocation = "";
 
  private Comparator comparator = new EventTransactionDefaultComparator();

  /**
   Static initializer.
   */
  static {
    DataDictionary dict = new DataDictionary();
    recDef = new RecordDefinition (dict);
    for (int i = 0; i < COLUMN_COUNT; i++) {
      recDef.addColumn (getColumnName(i));
    }
  }

  /**
   A constructor without any arguments.
   */
  public EventTransaction() {

  }
 
  /**
   Get the comparator to be used;
   */
  public Comparator getComparator() {
    return comparator;
  }
 
  /**
   Set the comparator to be used.
   */
  public void setComparator (Comparator comparator) {
    this.comparator = comparator;
  }
 
  /**
   Determine if this item has a key that is equal to the passed
   item.

   @param  obj2        The second object to be compared to this one.
   @param  comparator  The comparator to be used to make the comparison.
   @return True if the keys are equal.
   */
  public boolean equals (Object obj2, Comparator comparator) {
    return (this.compareTo (obj2, comparator) == 0);
  }
 
  /**
   Determine if this item has a key that is equal to the passed
   item.

   @param  obj2  The second object to be compared to this one.
   @return True if the keys are equal.
   */
  public boolean equals (Object obj2) {
    return (this.compareTo (obj2) == 0);
  }
 
  /**
   Compare this ClubEvent object to another, using the key field(s) for comparison.
 
   @param The second object to compare to this one.
 
   @return A number less than zero if this object is less than the second,
           a number greater than zero if this object is greater than the second,
           or zero if the two item's keys are equal.
   */
  public int compareTo (Object obj2, Comparator comparator) {
    if (comparator == null) {
      return -1;
    }
    return comparator.compare (this, obj2);
  }
 
  /**
   Compare this ClubEvent object to another, using the key field(s) for comparison.
 
   @param The second object to compare to this one.
 
   @return A number less than zero if this object is less than the second,
           a number greater than zero if this object is greater than the second,
           or zero if the two item's keys are equal.
   */
  public int compareTo (Object obj2) {
    if (comparator == null) {
      return -1;
    }
    return comparator.compare (this, obj2);
  }
 
  public void resetModified() {
    setModified (false);
  }
 
  public void setModified (boolean modified) {
    this.modified = modified;
  }
 
  public boolean isModified() {
    return modified;
  }
 
  /**
   Set the disk location at which this item is stored.
 
   @param diskLocation The path to the disk location at which this item
                       is stored.
  */
  public void setDiskLocation (String diskLocation) {
    this.diskLocation = diskLocation;
  }
 
  /**
   Set the disk location at which this item is stored.
 
   @param diskLocationFile The disk location at which this item is stored.
  */
  public void setDiskLocation (File diskLocationFile) {
    try {
      this.diskLocation = diskLocationFile.getCanonicalPath();
    } catch (java.io.IOException e) {
      this.diskLocation = diskLocationFile.getAbsolutePath();
    }
  }
 
  /**
   Indicate whether the item has a disk location.
 
   @return True if we've got a disk location, false otherwise.
  */
  public boolean hasDiskLocation() {
    return (diskLocation != null
        && diskLocation.length() > 0);
  }
 
  /**
   Return the disk location at which this item is stored.
 
   @return The disk location at which this item is stored.
  */
  public String getDiskLocation () {
    return diskLocation;
  }
 
  /**
   Find a common name (no punctuation, all lower-case) that starts with
   the passed string, if one exists.
 
   @param possibleFieldName The potential field name we're looking for. This
                            will be converted to a common name before it's
                            compared to the common forms of the field names.
 
   @return The index pointing to the first matching common name that was found,
           or -1 if no match was found.
  */
  public static int commonNameStartsWith (String possibleFieldName) {
    int i = 0;
    boolean fieldMatch = false;
    String possibleCommonName = StringUtils.commonName (possibleFieldName);
    while (i < COLUMN_COUNT && (! fieldMatch)) {
      if (getCommonName(i).startsWith(possibleCommonName)) {
        fieldMatch = true;
      } else {
        i++;
      }
    } // end while looking for field name match
    if (fieldMatch) {
      return i;
    } else {
      return -1;
    }
  }

  /**
   Return a record definition for the ClubEvent.
 
   @return A record definition using a new dictionary.
  */
  public static RecordDefinition getRecDef() {
    return recDef;
  }
 
  /**
   Return a standard data rec using the variables belonging to this object.
 
   @return A generic data record.
  */
  public DataRecord getDataRec() {
    DataRecord dataRec = new DataRecord();
    for (int i = 0; i < COLUMN_COUNT; i++) {
      Object columnValue = getColumnValue(i);
      String columnValueStr = "";
      if (columnValue != null) {
        columnValueStr = columnValue.toString();
      }
      int dataRecFieldNumber = dataRec.addField(recDef, columnValueStr);
    }
    return dataRec;
  }
 

  /*
   Following code generated by PSTextMerge using:
 
     template:  duplicate.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 
  /**
     Duplicates this item, making a deep copy.
   */
  public EventTransaction duplicate () {
    EventTransaction newEventTransaction = new EventTransaction();
		String dateStr = new String(getDateAsString());
		newEventTransaction.setDate(dateStr);
		String incomeExpenseStr = new String(getIncomeExpenseAsString());
		newEventTransaction.setIncomeExpense(incomeExpenseStr);
		String fromToStr = new String(getFromToAsString());
		newEventTransaction.setFromTo(fromToStr);
		String paidForStr = new String(getPaidForAsString());
		newEventTransaction.setPaidFor(paidForStr);
		String amountStr = new String(getAmountAsString());
		newEventTransaction.setAmount(amountStr);
		return newEventTransaction;
  }
 
  /**
    Check for a search string within the given Club Event item.
 
    This method generated by PSTextMerge using template find.java.
 
    @param findLower   The search string in all lower case.
    @param findUpper   The search string in all upper case.
 
    @return True if this item contains the search string
            in one of its searchable fields.

   */
  public boolean find (String findLower, String findUpper) {

    boolean found = false;
    int fieldStart = -1;

    return found;
  }

  /**
    Return a string value representing the given item.
 
    This method generated by PSTextMerge using template toString.java.
 
    @return The string by which this item shall be known.

   */
  public String toString() {

    StringBuilder str = new StringBuilder();

    return str.toString();
  }

  /*
   Following code generated by PSTextMerge using:
 
     template:  merge.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 
  /**
     Merges the contents of a second item into this one.
   */
  public void merge (EventTransaction eventTransaction2) {
    Object obj2;
    String str2;
    obj2 = eventTransaction2.getDate();
    if (obj2 == null) {
      // No value available -- leave current value as-is
    } else {
      str2 = obj2.toString();
      if (str2.equals ("")) {
        // No value available -- leave current value as-is
      } else {
        setDate ((String)obj2);
      }
    }
    obj2 = eventTransaction2.getIncomeExpense();
    if (obj2 == null) {
      // No value available -- leave current value as-is
    } else {
      str2 = obj2.toString();
      if (str2.equals ("")) {
        // No value available -- leave current value as-is
      } else {
        setIncomeExpense ((String)obj2);
      }
    }
    obj2 = eventTransaction2.getFromTo();
    if (obj2 == null) {
      // No value available -- leave current value as-is
    } else {
      str2 = obj2.toString();
      if (str2.equals ("")) {
        // No value available -- leave current value as-is
      } else {
        setFromTo ((String)obj2);
      }
    }
    obj2 = eventTransaction2.getPaidFor();
    if (obj2 == null) {
      // No value available -- leave current value as-is
    } else {
      str2 = obj2.toString();
      if (str2.equals ("")) {
        // No value available -- leave current value as-is
      } else {
        setPaidFor ((String)obj2);
      }
    }
    obj2 = eventTransaction2.getAmount();
    if (obj2 == null) {
      // No value available -- leave current value as-is
    } else {
      str2 = obj2.toString();
      if (str2.equals ("")) {
        // No value available -- leave current value as-is
      } else {
        setAmount ((String)obj2);
      }
    }
  }

  /*
   Following code generated by PSTextMerge using:
 
     template:  setColumnValue.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 
 
 /**
  Sets the column value indicated by the given column index.
 
  @param columnIndex An integer indicating the desired column whose value is to
                     be set.
  @param columnValue A string representing the value to be set.
  */
  public void setColumnValue (int columnIndex, String columnValue) {
    switch (columnIndex) {
      case DATE_COLUMN_INDEX:
          setDate (columnValue);
          break;
      case INCOME_EXPENSE_COLUMN_INDEX:
          setIncomeExpense (columnValue);
          break;
      case FROM_TO_COLUMN_INDEX:
          setFromTo (columnValue);
          break;
      case PAID_FOR_COLUMN_INDEX:
          setPaidFor (columnValue);
          break;
      case AMOUNT_COLUMN_INDEX:
          setAmount (columnValue);
          break;
    }
  }

  /*
   Following code generated by PSTextMerge using:
 
     template:  getColumnValue.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 
  /**
     Returns the value at the given column index.
   */
  public Object getColumnValue (int columnIndex) {
    switch (columnIndex) {
      case DATE_COLUMN_INDEX:
          return date;
      case INCOME_EXPENSE_COLUMN_INDEX:
          return incomeExpense;
      case FROM_TO_COLUMN_INDEX:
          return fromTo;
      case PAID_FOR_COLUMN_INDEX:
          return paidFor;
      case AMOUNT_COLUMN_INDEX:
          return amount;
      default: return null;
    }
  }

  /*
   Following code generated by PSTextMerge using:
 
     template:  getColumnName.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 
  /**
     Returns the field name for the given column index.
   */
  public static String getColumnName (int columnIndex) {
    switch (columnIndex) {
      case DATE_COLUMN_INDEX:
          return DATE_COLUMN_NAME;
      case INCOME_EXPENSE_COLUMN_INDEX:
          return INCOME_EXPENSE_COLUMN_NAME;
      case FROM_TO_COLUMN_INDEX:
          return FROM_TO_COLUMN_NAME;
      case PAID_FOR_COLUMN_INDEX:
          return PAID_FOR_COLUMN_NAME;
      case AMOUNT_COLUMN_INDEX:
          return AMOUNT_COLUMN_NAME;
      default: return null;
    }
  }

  /*
   Following code generated by PSTextMerge using:
 
     template:  getCommonName.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 
  /**
     Returns the common name (all lower case, no word separators)
     for the given column index.
   */
  public static String getCommonName (int columnIndex) {
    switch (columnIndex) {
      case DATE_COLUMN_INDEX:
          return DATE_COMMON_NAME;
      case INCOME_EXPENSE_COLUMN_INDEX:
          return INCOME_EXPENSE_COMMON_NAME;
      case FROM_TO_COLUMN_INDEX:
          return FROM_TO_COMMON_NAME;
      case PAID_FOR_COLUMN_INDEX:
          return PAID_FOR_COMMON_NAME;
      case AMOUNT_COLUMN_INDEX:
          return AMOUNT_COMMON_NAME;
      default: return null;
    }
  }

  /*
   Following code generated by PSTextMerge using:
 
     template:  getColumnWidth.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 
  /**
     Returns the field name for the given column index.
   */
  public static int getColumnWidth (int columnIndex) {
    switch (columnIndex) {
      case DATE_COLUMN_INDEX:
          return DATE_COLUMN_WIDTH;
      case INCOME_EXPENSE_COLUMN_INDEX:
          return INCOME_EXPENSE_COLUMN_WIDTH;
      case FROM_TO_COLUMN_INDEX:
          return FROM_TO_COLUMN_WIDTH;
      case PAID_FOR_COLUMN_INDEX:
          return PAID_FOR_COLUMN_WIDTH;
      case AMOUNT_COLUMN_INDEX:
          return AMOUNT_COLUMN_WIDTH;
      default: return 20;
    }
  }

  /*
   Following code generated by PSTextMerge using:
 
     template:  getColumnClass.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 
  /**
     Returns the class of the field at the given column index.
   */
  public static Class getColumnClass (int columnIndex) {
    switch (columnIndex) {
      case DATE_COLUMN_INDEX:
          return String.class;
      case INCOME_EXPENSE_COLUMN_INDEX:
          return String.class;
      case FROM_TO_COLUMN_INDEX:
          return String.class;
      case PAID_FOR_COLUMN_INDEX:
          return String.class;
      case AMOUNT_COLUMN_INDEX:
          return String.class;
      default: return null;
    }
  }

  /*
   Following code generated by PSTextMerge using:
 
     template:  isMarkdownFormat.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 
  /**
     Indicates whether the field at the given column index should be in Markdown format.
   */
  public static boolean isMarkdownFormat (int columnIndex) {
    switch (columnIndex) {
      default: return false;
    }
  }

  /*
   Following code generated by PSTextMerge using:
 
     template:  variable-methods.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 
 
  /**
     Sets the date for this event transaction.
 
     @param  date The date for this event transaction.
   */
  public void setDate (String date) {
    this.date = date;
    setModified (true);
  }

  /**
    Returns the date for this event transaction as a string.
 
    @return The date for this event transaction as a string.
   */
  public String getDateAsString () {
    if (hasDate()) {
      return getDate().toString();
    } else {
      return "";
    }
  }

  /**
    Determines if the date for this event transaction is null.
 
    @return True if the date for this event transaction is not null.
   */
  public boolean hasDate () {
    return (date != null);
  }

  /**
    Determines if the date for this event transaction
    is null or is empty.
 
    @return True if the date for this event transaction
    is not null and not empty.
   */
  public boolean hasDateWithData () {
    return (date != null && date.length() > 0);
  }

  /**
    Returns the date for this event transaction.
 
    @return The date for this event transaction.
   */
  public String getDate () {
    return date;
  }
 
  /**
     Sets the income expense for this event transaction.
 
     @param  incomeExpense The income expense for this event transaction.
   */
  public void setIncomeExpense (String incomeExpense) {
    this.incomeExpense = incomeExpense;
    setModified (true);
  }

  /**
    Returns the income expense for this event transaction as a string.
 
    @return The income expense for this event transaction as a string.
   */
  public String getIncomeExpenseAsString () {
    if (hasIncomeExpense()) {
      return getIncomeExpense().toString();
    } else {
      return "";
    }
  }

  /**
    Determines if the income expense for this event transaction is null.
 
    @return True if the income expense for this event transaction is not null.
   */
  public boolean hasIncomeExpense () {
    return (incomeExpense != null);
  }

  /**
    Determines if the income expense for this event transaction
    is null or is empty.
 
    @return True if the income expense for this event transaction
    is not null and not empty.
   */
  public boolean hasIncomeExpenseWithData () {
    return (incomeExpense != null && incomeExpense.length() > 0);
  }

  /**
    Returns the income expense for this event transaction.
 
    @return The income expense for this event transaction.
   */
  public String getIncomeExpense () {
    return incomeExpense;
  }
 
  /**
     Sets the from to for this event transaction.
 
     @param  fromTo The from to for this event transaction.
   */
  public void setFromTo (String fromTo) {
    this.fromTo = fromTo;
    setModified (true);
  }

  /**
    Returns the from to for this event transaction as a string.
 
    @return The from to for this event transaction as a string.
   */
  public String getFromToAsString () {
    if (hasFromTo()) {
      return getFromTo().toString();
    } else {
      return "";
    }
  }

  /**
    Determines if the from to for this event transaction is null.
 
    @return True if the from to for this event transaction is not null.
   */
  public boolean hasFromTo () {
    return (fromTo != null);
  }

  /**
    Determines if the from to for this event transaction
    is null or is empty.
 
    @return True if the from to for this event transaction
    is not null and not empty.
   */
  public boolean hasFromToWithData () {
    return (fromTo != null && fromTo.length() > 0);
  }

  /**
    Returns the from to for this event transaction.
 
    @return The from to for this event transaction.
   */
  public String getFromTo () {
    return fromTo;
  }
 
  /**
     Sets the paid for for this event transaction.
 
     @param  paidFor The paid for for this event transaction.
   */
  public void setPaidFor (String paidFor) {
    this.paidFor = paidFor;
    setModified (true);
  }

  /**
    Returns the paid for for this event transaction as a string.
 
    @return The paid for for this event transaction as a string.
   */
  public String getPaidForAsString () {
    if (hasPaidFor()) {
      return getPaidFor().toString();
    } else {
      return "";
    }
  }

  /**
    Determines if the paid for for this event transaction is null.
 
    @return True if the paid for for this event transaction is not null.
   */
  public boolean hasPaidFor () {
    return (paidFor != null);
  }

  /**
    Determines if the paid for for this event transaction
    is null or is empty.
 
    @return True if the paid for for this event transaction
    is not null and not empty.
   */
  public boolean hasPaidForWithData () {
    return (paidFor != null && paidFor.length() > 0);
  }

  /**
    Returns the paid for for this event transaction.
 
    @return The paid for for this event transaction.
   */
  public String getPaidFor () {
    return paidFor;
  }
 
  /**
     Sets the amount for this event transaction.
 
     @param  amount The amount for this event transaction.
   */
  public void setAmount (String amount) {
    this.amount = amount;
    setModified (true);
  }

  /**
    Returns the amount for this event transaction as a string.
 
    @return The amount for this event transaction as a string.
   */
  public String getAmountAsString () {
    if (hasAmount()) {
      return getAmount().toString();
    } else {
      return "";
    }
  }

  /**
    Determines if the amount for this event transaction is null.
 
    @return True if the amount for this event transaction is not null.
   */
  public boolean hasAmount () {
    return (amount != null);
  }

  /**
    Determines if the amount for this event transaction
    is null or is empty.
 
    @return True if the amount for this event transaction
    is not null and not empty.
   */
  public boolean hasAmountWithData () {
    return (amount != null && amount.length() > 0);
  }

  /**
    Returns the amount for this event transaction.
 
    @return The amount for this event transaction.
   */
  public String getAmount () {
    return amount;
  }
  /**
    Returns the amount as a BigDecimal object.
 
    @return The amount for this event transaction as a BigDecimal.
   */
  public BigDecimal getAmountAsBigDecimal () {
    CalcParser parser = new CalcParser(amount);
    return parser.getResult();
  }

  /**
   Return the number of columns.
   */
  public static int getColumnCount() {
    return COLUMN_COUNT;
  }

  /*
   Following code generated by PSTextMerge using:
 
     template:  taggable-methods.java
     data file: /Users/hbowie/Java/projects/nbproj/clubplanner/javagen/fields.xls
   */
 

  /**
   Does this class have a Tags field?
 
   @return True if so, false if not.
   */
  public static boolean isClassTagged() {
    return false;
  }
 
  /**
   Return the tags assigned to this taggable item.
 
   @return The tags assigned.
   */
  public Tags getTags () {
    // No Tags field for this item
    return null;
  }
 
  /**
   Flatten all the tags for this item, separating each level/word into its own
   first-level tag.
   */
  public void flattenTags() {
    // No Tags field for this item
  }

  /**
   Convert the tags to all lower-case letters.
   */
  public void lowerCaseTags (){
    // No Tags field for this item
  }
 
  /**
   Set the first TagsNode occurrence for this Taggable item. This is stored
   in a TagsModel occurrence.

   @param tagsNode The tags node to be stored.
   */
  public void setTagsNode (TagsNode tagsNode) {
    // No Tags field for this item
  }

  /**
   Return the first TagsNode occurrence for this Taggable item. These nodes
   are stored in a TagsModel occurrence.

   @return The tags node stored.
   */
  public TagsNode getTagsNode () {
    // No Tags field for this item
    return null;
  }

}
