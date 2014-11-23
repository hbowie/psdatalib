/*
 * Copyright 2014 Herb Bowie
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

package com.powersurgepub.psdatalib.notenik;

  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psutils.*;
  import java.text.*;

/**
 A set of parameters to specify how a particular Note Collection is configured. 

 @author Herb Bowie
 */
public class NoteParms {
  
  public static final String FILENAME = "notenik.parms";
  
  public static final String  TITLE_FIELD_NAME  = "Title";
  public static final String  TITLE_COMMON_NAME = "title";
  public static final String  LINK_FIELD_NAME   = "Link";
  public static final String  LINK_COMMON_NAME  = "link";
  public static final String  TAGS_FIELD_NAME   = "Tags";
  public static final String  TAGS_COMMON_NAME  = "tags";
  public static final String  BODY_FIELD_NAME   = "Body";
  public static final String  BODY_COMMON_NAME  = "body";
  public static final String  AUTHOR_FIELD_NAME = "Author";
  public static final String  AUTHOR_COMMON_NAME = "author";
  public static final String  DATE_FIELD_NAME   = "Date";
  public static final String  DATE_COMMON_NAME  = "date";
  public static final String  STATUS_FIELD_NAME = "Status";
  public static final String  STATUS_COMMON_NAME = "status";
  
  public static final String  COMPLETE_PATH     = "Complete Path";
  public static final String  BASE_PATH         = "Base Path";
  public static final String  LOCAL_PATH        = "Local Path";
  public static final String  PATH_TO_TOP       = "Path to Top";
  public static final String  DEPTH             = "Depth";
  public static final String  FILE_NAME         = "File Name";
  public static final String  FILE_NAME_BASE    = "File Name Base";
  public static final String  FILE_EXT          = "File Ext";
  public static final String  LAST_MOD_DATE     = "Last Mod Date";
  public static final String  FILE_SIZE         = "File Size";
  public static final String  BREADCRUMBS       = "Breadcrumbs";
  public static final String  LINKED_TAGS       = "Linked Tags";
  public static final String  SINGLE_TAG        = "Tag";
  
  public static final String  BY                = "By";
  public static final String  CREATOR           = "Creator";
  public static final String  KEYWORDS          = "Keywords";
  public static final String  CATEGORY          = "Category";
  public static final String  CATEGORIES        = "Categories";
  public static final String  URL               = "URL";
  
  public static final DataFieldDefinition TITLE_DEF 
      = new DataFieldDefinition(TITLE_FIELD_NAME);
  public static final DataFieldDefinition LINK_DEF 
      = new DataFieldDefinition(LINK_FIELD_NAME);
  public static final DataFieldDefinition TAGS_DEF
      = new DataFieldDefinition(TAGS_FIELD_NAME);
  public static final DataFieldDefinition BODY_DEF
      = new DataFieldDefinition(BODY_FIELD_NAME);
  public static final DataFieldDefinition AUTHOR_DEF
      = new DataFieldDefinition(AUTHOR_FIELD_NAME);
  public static final DataFieldDefinition DATE_DEF
      = new DataFieldDefinition(DATE_FIELD_NAME);
  public static final DataFieldDefinition STATUS_DEF
      = new DataFieldDefinition(STATUS_FIELD_NAME);
  
  public static final boolean SLASH_TO_SEPARATE = false;
  
  public final static String   YMD_FORMAT_STRING = "yyyy-MM-dd";
  public final static String   MDY_FORMAT_STRING = "MM-dd-yyyy";
  public final static String   STANDARD_FORMAT_STRING 
      = "yyyy-MM-dd'T'HH:mm:ssz";
  public final static String   
      COMPLETE_FORMAT_STRING = "EEEE MMMM d, yyyy KK:mm:ss aa zzz";
  
  public final static DateFormat YMD_FORMAT 
      = new SimpleDateFormat (YMD_FORMAT_STRING);
  public final static DateFormat MDY_FORMAT
      = new SimpleDateFormat (MDY_FORMAT_STRING);
  public final static DateFormat COMPLETE_FORMAT
      = new SimpleDateFormat (COMPLETE_FORMAT_STRING);
  public final static DateFormat STANDARD_FORMAT
      = new SimpleDateFormat (STANDARD_FORMAT_STRING);
  
  /** The type of data set to generate: planner or minutes. */
  private    int              noteType = NOTES_ONLY_TYPE;
  public static final int     NOTES_ONLY_TYPE     = 1;
  public static final int     NOTES_PLUS_TYPE     = 2;
  public static final int     NOTES_GENERAL_TYPE  = 3;
  public static final int     DEFINED_TYPE        = 4;
  public static final int     MARKDOWN_TYPE       = 5;
  public static final int     TAG_TYPE            = 6;
  
  
  private    boolean          metadataAsMarkdown = true;
  
  private    RecordDefinition recDef = new RecordDefinition();

  static {
    TITLE_DEF.setType (DataFieldDefinition.TITLE_TYPE);
    LINK_DEF.setType  (DataFieldDefinition.LINK_TYPE);
    TAGS_DEF.setType  (DataFieldDefinition.TAGS_TYPE);
    BODY_DEF.setType  (DataFieldDefinition.STRING_BUILDER_TYPE);
    AUTHOR_DEF.setType(DataFieldDefinition.STRING_TYPE);
    DATE_DEF.setType(DataFieldDefinition.STRING_TYPE);
    STATUS_DEF.setType(DataFieldDefinition.STRING_TYPE);
  }
  
  public NoteParms () {
    
  }
  
  public NoteParms (int noteType) {
    this.noteType = noteType;
  }
  
  public void setNoteType (int noteType) {
    this.noteType = noteType;
  }
  
  public int getNoteType() {
    return noteType;
  }
  
  public boolean notesOnly() {
    return (noteType == NOTES_ONLY_TYPE);
  }
  
  public boolean notesPlus() {
    return (noteType == NOTES_PLUS_TYPE);
  }
  
  public boolean notesGeneral() {
    return (noteType == NOTES_GENERAL_TYPE);
  }
  
  public boolean definedType() {
    return (noteType == DEFINED_TYPE);
  }
  
  public boolean markdownType() {
    return (noteType == MARKDOWN_TYPE);
  }
  
  public boolean tagType() {
    return (noteType == TAG_TYPE);
  }
  
  public void newRecordDefinition (DataDictionary dict) {
    recDef = new RecordDefinition(dict);
  }
  
  public void setRecDef(RecordDefinition recDef) {
    setRecordDefinition(recDef);
  }
  
  public void setRecordDefinition(RecordDefinition recDef) {
    this.recDef = recDef;
  }
  
  public RecordDefinition getRecDef() {
    return getRecordDefinition();
  }
  
  public RecordDefinition getRecordDefinition() {
    if (recDef == null) {
      buildRecordDefinition();
    }
    return recDef;
  }
  
  public RecordDefinition buildRecordDefinition(RecordDefinition recDef) {
    this.recDef = recDef;
    return buildRecordDefinition();
  }
  
  public RecordDefinition buildRecordDefinition() {
    if (recDef == null) {
      recDef = new RecordDefinition();
    }
    if (noteType == NOTES_ONLY_TYPE 
        || noteType == NOTES_PLUS_TYPE) {
      recDef.addColumn(TITLE_DEF);
      recDef.addColumn(TAGS_DEF);
      recDef.addColumn(LINK_DEF);
      recDef.addColumn(BODY_DEF);
    }
    if (noteType == MARKDOWN_TYPE 
        || noteType == TAG_TYPE) {
      recDef.addColumn(TITLE_DEF);
      recDef.addColumn (COMPLETE_PATH);
      recDef.addColumn (BASE_PATH);
      recDef.addColumn (LOCAL_PATH);
      recDef.addColumn (PATH_TO_TOP);
      recDef.addColumn (DEPTH);
      recDef.addColumn (FILE_NAME);
      recDef.addColumn (FILE_NAME_BASE);
      recDef.addColumn (FILE_EXT);
      recDef.addColumn (LAST_MOD_DATE);
      recDef.addColumn (FILE_SIZE);
      recDef.addColumn (AUTHOR_DEF);
      recDef.addColumn (DATE_DEF);
      recDef.addColumn (STATUS_DEF);
      recDef.addColumn (BREADCRUMBS);
      switch (noteType) {
        case MARKDOWN_TYPE:
          recDef.addColumn(TAGS_DEF);
          recDef.addColumn (LINKED_TAGS);
          break;
        case TAG_TYPE:
          recDef.addColumn (SINGLE_TAG);
          break;
      }
    }
    return recDef;
  }
  
  /**
   Check to see if the passed field name is valid for this type of note. 
  
   @param fieldName A potential field name. 
  
   @return A DataFieldDefinition for the field, or null, if the field name
           is not valid. 
  */
  public DataFieldDefinition checkForFieldName(String fieldName) {
    
    String fn = StringUtils.commonName (fieldName);
    
    // If this is a url, then don't confuse it with a field name.
    if (fn.endsWith("http")
         || fn.endsWith("ftp")
         || fn.endsWith("mailto")) {
      return null;
    }
    
    // Check for most basic note fields. 
    if (fn.equals(TITLE_COMMON_NAME)) {
      return TITLE_DEF;
    }
    if (fn.equals(TAGS_COMMON_NAME)
        || fn.equalsIgnoreCase(KEYWORDS)
        || fn.equalsIgnoreCase(CATEGORY)
        || fn.equalsIgnoreCase(CATEGORIES)) {
      return TAGS_DEF;
    }
    if (fn.equals(LINK_COMMON_NAME)
        || fn.equalsIgnoreCase(URL)) {
      return LINK_DEF;
    }
    if (fn.equals(BODY_COMMON_NAME)) {
      return BODY_DEF;
    }
    if (notesOnly()) {
      return null;
    }
    
    if (fn.equals(AUTHOR_COMMON_NAME)
        || fn.equalsIgnoreCase(BY)
        || fn.equalsIgnoreCase(CREATOR)) {
      return AUTHOR_DEF;
    }
    if (fn.equals(DATE_COMMON_NAME)) {
      return DATE_DEF;
    }
    if (fn.equals(STATUS_COMMON_NAME)) {
      return STATUS_DEF;
    }
    
    int columnNumber = recDef.getColumnNumber(fn);
    if (columnNumber < 0) {
      if (definedType()) {
        return null;
      } else {
        DataFieldDefinition newDef = new DataFieldDefinition(fieldName);
        recDef.addColumn(newDef);
        return newDef;
      }
    } else {
      return recDef.getDef(columnNumber);
    }
    
  }
  
  /**
   Pass any metadata lines to the markdown parser as well. 
  
   @param metadataAsMarkdown True if metadata lines should appear as part
                             of output HTML, false otherwise. 
  */
  public void setMetadataAsMarkdown (boolean metadataAsMarkdown) {
    this.metadataAsMarkdown = metadataAsMarkdown;
  }
  
  public boolean treatMetadataAsMarkdown() {
    return metadataAsMarkdown;
  }

}
