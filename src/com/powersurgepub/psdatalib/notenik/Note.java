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

  import com.powersurgepub.psdatalib.pstags.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.urlvalidator.*;
  import java.io.*;
  import java.text.*;
  import java.util.*;

/**
 A Note.

 @author Herb Bowie
 */
public class Note 
    implements 
      Comparable, 
      Taggable, 
      ItemWithURL {
  
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
  
  public static final String  TITLE_FIELD_NAME  = "Title";
  public static final String  TITLE_COMMON_NAME = "title";
  public static final String  LINK_FIELD_NAME   = "Link";
  public static final String  LINK_COMMON_NAME  = "link";
  public static final String  TAGS_FIELD_NAME   = "Tags";
  public static final String  TAGS_COMMON_NAME  = "tags";
  public static final String  BODY_FIELD_NAME   = "Body";
  public static final String  BODY_COMMON_NAME  = "body";
  
  private String        fileName = "";
  private String        title    = "";
  private Tags          tags     = new Tags(SLASH_TO_SEPARATE);
  private Link          link     = new Link();
  private StringBuilder body     = new StringBuilder();
  
  private String        diskLocation = "";
  
  private Date          lastModDate;
  
  private TagsNode      tagsNode = null;

  
  public Note() {
    setLastModDateToday();
  }
  
  public Note(String title) {
    setTitle(title);
    setLastModDateToday();
  }
  
  public Note(String title, String body) {
    setTitle(title);
    setBody(body);
    setLastModDateToday();
  }
  
  public boolean equals (Object obj2) {
    boolean eq = false;
    if (obj2.getClass().getSimpleName().equals ("Note")) {
      Note note2 = (Note)obj2;
      eq = (this.getKey().equalsIgnoreCase (note2.getKey()));
    }
    return eq;
  }
  
  /**
   Compare this Note object to another, using the titles for comparison.
  
   @param The second object to compare to this one.
  
   @return A number less than zero if this object is less than the second,
           a number greater than zero if this object is greater than the second,
           or zero if the two titles are equal (ignoring case differences).
   */
  public int compareTo (Object obj2) {
    int comparison = -1;
    if (obj2.getClass().getSimpleName().equals ("Note")) {
      Note note2 = (Note)obj2;
      comparison = this.getKey().compareToIgnoreCase(note2.getKey());
    }
    return comparison;
  }
  
  public boolean hasKey() {
    return (getKey() != null
        && getKey().length() > 0);
  }
  
  public String getKey() {
    return getFileName();
  }
  
  public void merge (Note note2) {

    // Merge URLs
    if (note2.hasLink()) {
      setLink (note2.getLink());
    }

    // Merge titles
    if (note2.getTitle().length() > getTitle().length()) {
      setTitle (note2.getTitle());
    }

    // Merge tags
    tags.merge (note2.getTags());

    // Merge comments
    if (getBody().equals(note2.getBody())) {
      // do nothing
    }
    else
    if (note2.getBody().length() == 0) {
      // do nothing
    }
    else
    if (getBody().length() == 0) {
      setBody (note2.getBody());
    } else {
      setBody (getBody() + " " + note2.getBody());
    }
  }
  
  public void setTitle(String title) {
    this.title = title;
		if (title == null) {
			fileName = "";
		}
		else
		if (title.length() == 0) {
			fileName = "";
		} else {
			fileName = StringUtils.makeReadableFileName (title);
		}
  }
  
  public boolean equalsTitle (String title2) {
    return title.equals (title2.trim());
  }
  
  public String getTitle() {
    return title;
  }
  
	/**
	 Return the file name in which this item should be stored.
	
	 @return The file name to be used, without a file extension.
	 */
	public String getFileName() {
    return fileName;
	}
  
  public void setTags(String tags) {
    this.tags.set(tags);
  }
  
  public boolean hasTags() {
    return (! tags.areBlank());
  }
  
  public Tags getTags() {
    return tags;
  }
  
  public String getTagsAsString() {
    return tags.toString();
  }
  
  public void setTagsNode (TagsNode tagsNode) {
    this.tagsNode = tagsNode;
  }

  public TagsNode getTagsNode () {
    return tagsNode;
  }
  
  public void flattenTags () {
    tags.flatten();
  }

  public void lowerCaseTags () {
    tags.makeLowerCase();
  }
  
  public void setLink(String link) {
    this.link.setLink(link);
  }
  
  public void setLink(Link link) {
    this.link.setLink(link.toString());
  }
  
  public boolean hasLink() {
    return (link != null && link.hasLink());
  }
  
  public boolean blankLink () {
    return (link == null || (link.blankLink()));
  }
  
  public Link getLink() {
    return link;
  }
  
  public String getLinkAsString() {
    return link.getURLasString();
  }
  
  public boolean equalsTags (String tags2) {
    return tags.toString().equals (tags2.trim());
  }
  
  public String getURLasString () {
    return link.getURLasString();
  }
  
  public void setBody(String body) {
    this.body = new StringBuilder(body);
  }
  
  public void appendLineToBody(String line) {
    body.append(line);
    body.append(GlobalConstants.LINE_FEED);
  }
  
  public boolean hasBody() {
    return (body.length() > 0);
  }
  
  public String getBody() {
    return body.toString();
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
  
  public void setLastModDateStandard (String date) {
    setLastModDate (STANDARD_FORMAT, date);
  }
    
  public void setLastModDateYMD (String date) {
    setLastModDate (YMD_FORMAT, date);
  }
  
  /**
     Sets the last mod date for this item.
 
     @param  fmt  A DateFormat instance to be used to parse the following string.
     @param  date String representation of a date.
   */
  public void setLastModDate (DateFormat fmt, String date) {
    
    try {
      setLastModDate (fmt.parse (date));
    } catch (ParseException e) {
      System.out.println ("URLPlus.setLastModDate to " + date + " with " + fmt
          + " -- Parse Exception");
    }

  } // end method
  
  /**
    Sets the last mod date to today's date. 
   */
  public void setLastModDateToday () {
    setLastModDate (new GregorianCalendar().getTime());
  }
  
  /**
     Sets the due date for this item.
 
     @param  date Date representation of a date.
   */
  public void setLastModDate (Date date) {
    
    lastModDate = date;

  } // end method
  
  /**
     Gets the due date for this item, formatted as a string.
 
     @return  String representation of a date.
     @param   fmt  A DateFormat instance to be used to format the date as a string.

   */
  public String getLastModDate (DateFormat fmt) {
    
    return fmt.format (lastModDate);

  } // end method
  
  /**
     Gets the due date for this item, formatted as a string 
     in yyyy/mm/dd format.
 
     @return  String representation of a date in yyyy/mm/dd format.
   */
  public String getLastModDateYMD () {
    
    return YMD_FORMAT.format (lastModDate);

  } // end method
  
  public String getLastModDateStandard () {
    
    return STANDARD_FORMAT.format (lastModDate);
  }
  
  /**
     Gets the due date for this item.
 
     @return  date Date representation of a date.
   */
  public Date getLastModDate () {
    
    return lastModDate;

  } // end method
  
  public String toString() {
    return title;
  }

}
