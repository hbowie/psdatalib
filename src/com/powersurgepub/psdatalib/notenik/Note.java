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
  import com.powersurgepub.psdatalib.psdata.values.*;
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
    extends
      DataRecord
    implements 
      Comparable, 
      Taggable, 
      ItemWithURL {
  
  private RecordDefinition recDef;
  
  private String        fileName = "";
  
  private String        diskLocation = "";
  
  private Date          lastModDate;
  private String        lastModDateStr;
  
  private boolean       synced = false;
  
  private TagsNode      tagsNode = null;
  
  private DataValueString     titleValue = null;
  private DataField           titleField = null;
  
  private Author              authorValue = null;
  private DataField           authorField = null;
  private boolean             authorAdded = false;
  
  private StringDate          dateValue = null;
  private DataField           dateField = null;
  private boolean             dateAdded = false;
  
  private Link                linkValue = null;
  private DataField           linkField = null;
  private boolean             linkAdded = false;
  
  private Tags                tagsValue = null;
  private DataField           tagsField = null;
  private boolean             tagsAdded = false;
  
  private DataValueStringBuilder bodyValue = null;
  private DataField           bodyField;
  private boolean bodyAdded = false;
  
  public static final String    UP_ONE_FOLDER   = "../";
  
  private    SimpleDateFormat   dateFormat 
      = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
  
  public Note(RecordDefinition recDef) {
    this.recDef = recDef;
    initNoteFields();
    setLastModDateToday();
  }
  
  public Note(RecordDefinition recDef, String title) {
    this.recDef = recDef;
    initNoteFields();
    setTitle(title);
    setLastModDateToday();
  }
  
  public Note(RecordDefinition recDef, String title, String body) {
    this.recDef = recDef;
    initNoteFields();
    setTitle(title);
    setBody(body);
    setLastModDateToday();
  }
  
  private void initNoteFields() {
    
    // Build the Title field
    titleValue = new DataValueString();
    titleField = new DataField(NoteParms.TITLE_DEF, titleValue);
    storeField (recDef, titleField);
    // addField(titleField);
    
    // Build the Author field
    authorValue = new Author();
    authorField = new DataField(NoteParms.AUTHOR_DEF, authorValue);
    authorAdded = false;
    
    // Build the Date field
    dateValue = new StringDate();
    dateField = new DataField(NoteParms.DATE_DEF, dateValue);
    dateAdded = false;
    
    // Build the Link field
    linkValue = new Link();
    linkField = new DataField(NoteParms.LINK_DEF, linkValue);
    linkAdded = false;
    
    // Build the Tags field
    tagsValue = new Tags();
    tagsField = new DataField(NoteParms.TAGS_DEF, tagsValue);
    tagsAdded = false;
    
    // Build the body field
    bodyValue = new DataValueStringBuilder();
    bodyField = new DataField(NoteParms.BODY_DEF, bodyValue);
    bodyAdded = false;
  }
  
  public RecordDefinition getRecDef() {
    return recDef;
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
  
  /**
   Compare this Note object to another, using the titles for comparison.
  
   @param The second object to compare to this one.
  
   @return A number less than zero if this object is less than the second,
           a number greater than zero if this object is greater than the second,
           or zero if the two titles are equal (ignoring case differences).
   */
  public int compareTo (Note note2) {
    int comparison = -1;
      comparison = this.getKey().compareToIgnoreCase(note2.getKey());

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
    getTags().merge (note2.getTags());

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
  
  /**
     Duplicates this item, making a deep copy.
   */
  public Note duplicate () {
    Note newNote = new Note(recDef);
    
    String titleStr = new String(getTitle());
    newNote.setTitle(titleStr);
    
    String tagsStr = new String(getTagsAsString());
    newNote.setTags(tagsStr);
    
    String linkStr = new String(getLinkAsString());
    newNote.setLink(linkStr);
    
    String bodyStr = new String(this.getBody());
    newNote.setBody(bodyStr);
	
		return newNote;
  }
  
  public void setTitle(String title) {
    titleValue.set(title);
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

    return titleValue.toString().equals (title2.trim());
  }
  
  public boolean hasTitle() {
    return titleValue.hasData();
  }
  
  public String getTitle() {
    return titleValue.toString();
  }
  
	/**
	 Return the file name in which this item should be stored.
	
	 @return The file name to be used, without a file extension.
	 */
	public String getFileName() {
    return fileName;
	}
  
  public void setAuthor(String author) {

    authorValue.set(author);
    if (! authorAdded) {
      storeField (recDef, authorField);
      authorAdded = true;
    }
  }
  
  public void setAuthor(Author author) {
    authorValue.set(author.toString());
    if (! authorAdded) {
      storeField (recDef, authorField);
      authorAdded = true;
    }
  }
  
  public boolean hasAuthor() {
    return (authorAdded && authorValue != null && authorValue.hasData());
  }
  
  public Author getAuthor() {
    return authorValue;
  }
  
  public String getAuthorAsString() {
    if (authorAdded && authorValue != null) {
      return authorValue.toString();
    } else {
      return "";
    }
  }
  
  public void setDate(String date) {

    dateValue.set(date);
    if (! dateAdded) {
      storeField (recDef, dateField);
      dateAdded = true;
    }
  }
  
  public void setDate(StringDate date) {
    dateValue.set(date.toString());
    if (! dateAdded) {
      storeField (recDef, dateField);
      dateAdded = true;
    }
  }
  
  public boolean hasDate() {
    return (dateAdded && dateValue != null && dateValue.hasData());
  }
  
  public StringDate getDate() {
    return dateValue;
  }
  
  public String getDateAsString() {
    if (dateAdded && dateValue != null) {
      return dateValue.toString();
    } else {
      return "";
    }
  }
  
  public void setTags(String tags) {
    tagsValue.set(tags);
    if (! tagsAdded) {
      storeField (recDef, tagsField);
      // addField(tagsField);
      tagsAdded = true;
    }
  }
  
  public boolean hasTags() {
    if (tagsAdded && tagsValue != null) {
      return (! tagsValue.areBlank());
    } else {
      return false;
    }
  }
  
  public Tags getTags() {
    return tagsValue;
  }
  
  public String getTagsAsString() {
    if (tagsAdded && tagsValue != null) {
      return tagsValue.toString();
    } else {
      return "";
    }
  }
  
  public void setTagsNode (TagsNode tagsNode) {
    this.tagsNode = tagsNode;
  }

  public TagsNode getTagsNode () {
    return tagsNode;
  }
  
  public void flattenTags () {
    tagsValue.flatten();
  }

  public void lowerCaseTags () {
    tagsValue.makeLowerCase();
  }
  
  public void setLink(String link) {

    linkValue.set(link);
    if (! linkAdded) {
      storeField (recDef, linkField);
      // addField(linkField);
      linkAdded = true;
    }
  }
  
  public void setLink(Link link) {
    linkValue.set(link.toString());
    if (! linkAdded) {
      storeField (recDef, linkField);
      // addField(linkField);
      linkAdded = true;
    }
  }
  
  public boolean hasLink() {
    return (linkAdded && linkValue != null && linkValue.hasLink());
  }
  
  public boolean blankLink () {
    return ((! linkAdded) || linkValue == null || (linkValue.blankLink()));
  }
  
  public Link getLink() {
    return linkValue;
  }
  
  public String getLinkAsString() {
    if (linkAdded && linkValue != null) {
      return linkValue.getURLasString();
    } else {
      return "";
    }
  }
  
  public boolean equalsTags (String tags2) {
    return tagsValue.toString().equals (tags2.trim());
  }
  
  public String getURLasString () {
    if (linkAdded && linkValue != null) {
      return linkValue.getURLasString();
    } else {
      return "";
    }
  }
  
  public void setBody(String body) {
    bodyValue.set(body);
    if (! bodyAdded) {
      storeField (recDef, bodyField);
      // addField(bodyField);
      bodyAdded = true;
    }
  }
  
  public void appendLineToBody(String line) {
    bodyValue.appendLine(line);
    if (! bodyAdded) {
      storeField (recDef, bodyField);
      // addField(bodyField);
      bodyAdded = true;
    }
  }
  
  public boolean hasBody() {
    if (bodyAdded && bodyValue != null) {
      return (bodyValue.toString().length() > 0);
    } else {
      return false;
    }
  }
  
  public String getBody() {
    if (bodyAdded && bodyValue != null) {
      return bodyValue.toString();
    } else {
      return "";
    }
  }
  
  public DataValueStringBuilder getBodyAsDataValue() {
    return bodyValue;
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
  
  public void extractDiskLocationInfo(String homePath) {
    
    // Let's populate some fields based on the file name 
    String fileNameBase;
    String  fileExt = "";
    String localPath;
    int depth = 0;
    StringBuilder tagsPath = new StringBuilder();
    StringBuilder pathToTop = new StringBuilder();
    ArrayList<String> parents = new ArrayList();
    StringBuilder breadcrumbs = new StringBuilder();
    
    // Get location of file extension and file name
    int period = diskLocation.length();
    int slash = -1;
    int i = diskLocation.length() - 1;
    while (i >= 0 && slash < 0) {
      if (diskLocation.charAt(i) == '.'
          && period == diskLocation.length()) {
        period = i;
      } 
      else
      if (diskLocation.charAt(i) == '/' ||
          diskLocation.charAt(i) == '\\') {
        slash = i;
      }
      i--;
    }
    int localPathStart = 0;
    if (diskLocation.startsWith(homePath)) {
      localPathStart = homePath.length();
    }
    if (diskLocation.charAt(localPathStart) == '/' ||
        diskLocation.charAt(localPathStart) == '\\') {
      localPathStart++;
    }
    // Let's get as much info as we can from the file name or URL
    if (slash > localPathStart) {
      localPath = diskLocation.substring(localPathStart, slash) + '/';
    } else {
      localPath = "";
    }
    
    int lastSlash = 0;
    if (lastSlash < localPath.length()
        && (localPath.charAt(0) == '/'
          || localPath.charAt(0) == '\\')) {
      lastSlash++;
    }
    while (lastSlash < localPath.length()) {
      depth++;
      tagsPath.append(UP_ONE_FOLDER);
      pathToTop.append(UP_ONE_FOLDER);
      int nextSlash = localPath.indexOf("/", lastSlash);
      if (nextSlash < 0) {
        nextSlash = localPath.indexOf("\\", lastSlash);
      }
      if (nextSlash < 0) {
        nextSlash = localPath.length();
      }
      parents.add(localPath.substring(lastSlash, nextSlash));
      lastSlash = nextSlash;
      lastSlash++;
    }
    tagsPath.append("tags/");
    
    fileName = diskLocation.substring(slash + 1);
    fileNameBase = diskLocation.substring(slash + 1, period);
    fileExt = diskLocation.substring(period + 1);
    
    // Now let's build breadcrumbs to higher-level index pages
    int parentIndex = 0;
    int parentStop = parents.size() - 1;
    while (parentIndex < parentStop) {
      addBreadcrumb (breadcrumbs, parents, parents.size() - parentIndex, parentIndex);
      parentIndex++;
    }
    if (! fileNameBase.equalsIgnoreCase("index")) {
      addBreadcrumb (breadcrumbs, parents, 0, parentIndex);
    } 
    
    if (! hasTitle()) {
      setTitle(diskLocation.substring(slash + 1, period));
    }
    
    File diskLocationFile = new File(diskLocation);
    if (diskLocationFile != null && diskLocationFile.exists()) {
      lastModDate = new Date (diskLocationFile.lastModified());
      lastModDateStr = dateFormat.format(lastModDate);
    }
    
  }
  
  /**
   Add another bread crumb level. 
  
   @param breadcrumbs The starting bread crumbs, to which the latest will be added. 
   @param levels      The number of levels upwards to point to. 
   @param parentIndex The parent to point to.
  
   @return The bread crumbs after the latest addition. 
  */
  private StringBuilder addBreadcrumb (
      StringBuilder breadcrumbs, 
      ArrayList<String> parents,
      int levels, 
      int parentIndex) {
    
    if (breadcrumbs.length() > 0) {
      breadcrumbs.append(" &gt; ");
    }
    breadcrumbs.append("<a href=\"");
    for (int i = 0; i < levels; i++) {
      breadcrumbs.append(UP_ONE_FOLDER);
    }
    breadcrumbs.append("index.html");
    breadcrumbs.append("\">");
    if (parentIndex < 0 || parentIndex >= parents.size()) {
      breadcrumbs.append("Home");
    } else {
      breadcrumbs.append(
          StringUtils.wordDemarcation(parents.get(parentIndex), " ", 1, 1, -1));
    }
    breadcrumbs.append("</a>");
    return breadcrumbs;
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
    setLastModDate (NoteParms.STANDARD_FORMAT, date);
  }
    
  public void setLastModDateYMD (String date) {
    setLastModDate (NoteParms.YMD_FORMAT, date);
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
      System.out.println ("Note.setLastModDate to " + date + " with " + fmt
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
    
    return NoteParms.YMD_FORMAT.format (lastModDate);

  } // end method
  
  public String getLastModDateStandard () {
    
    return NoteParms.STANDARD_FORMAT.format (lastModDate);
  }
  
  /**
     Gets the due date for this item.
 
     @return  date Date representation of a date.
   */
  public Date getLastModDate () {
    
    return lastModDate;

  } // end method
  
  public void setSynced(boolean synced) {
    this.synced = synced;
  }
  
  public boolean isSynced() {
    return synced;
  }
  
  public String toString() {
    return titleValue.toString();
  }

}
