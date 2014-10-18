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
  import java.text.*;

/**
 A factory class for making things related to notes. 

 @author Herb Bowie
 */
public class NoteFactory {
  
  public static final String  TITLE_FIELD_NAME  = "Title";
  public static final String  TITLE_COMMON_NAME = "title";
  public static final String  LINK_FIELD_NAME   = "Link";
  public static final String  LINK_COMMON_NAME  = "link";
  public static final String  TAGS_FIELD_NAME   = "Tags";
  public static final String  TAGS_COMMON_NAME  = "tags";
  public static final String  BODY_FIELD_NAME   = "Body";
  public static final String  BODY_COMMON_NAME  = "body";
  
  public static final DataFieldDefinition TITLE_DEF 
      = new DataFieldDefinition(TITLE_FIELD_NAME);
  public static final DataFieldDefinition LINK_DEF 
      = new DataFieldDefinition(LINK_FIELD_NAME);
  public static final DataFieldDefinition TAGS_DEF
      = new DataFieldDefinition(TAGS_FIELD_NAME);
  public static final DataFieldDefinition BODY_DEF
      = new DataFieldDefinition(BODY_FIELD_NAME);
  
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

  static {
    TITLE_DEF.setType (DataFieldDefinition.TITLE_TYPE);
    LINK_DEF.setType  (DataFieldDefinition.LINK_TYPE);
    TAGS_DEF.setType  (DataFieldDefinition.TAGS_TYPE);
    BODY_DEF.setType  (DataFieldDefinition.STRING_BUILDER_TYPE);
  }
}
