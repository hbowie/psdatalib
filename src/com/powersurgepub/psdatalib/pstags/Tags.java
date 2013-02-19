/*
 * Copyright 1999 - 2013 Herb Bowie
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

package com.powersurgepub.psdatalib.pstags;

  import com.powersurgepub.psdatalib.elements.*;
  import com.powersurgepub.psutils.*;

/**
   An object representing the tags or categories assigned to an object. A
   tags field may consist of multiple levels, with the first
   level being the primary tags and subsequent levels being
   sub-categories. <p>
  
   This code is copyright (c) 2003-2009 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/11/17 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 
      2004/01/17 - Modified to allow sub-categories to be set
                   one level at a time. 
 */
public class Tags
      implements DataElement {

  public final static String  SIMPLE_NAME   = "tags";
  public final static String  DISPLAY_NAME  = "Tags";
  public final static String  BRIEF_NAME    = "Tags";
  public final static int     COLUMN_WIDTH  = 80;

  // The tags string is divided up into words by separators.
  
  /** Preferred separator character. */
  public static final char     PREFERRED_LEVEL_SEPARATOR = '.';
  
  /** Alternate separator character. */
  public static final char     ALTERNATE_LEVEL_SEPARATOR = '/';
  
  /** Preferred separator between multiple tags. */
  public static final char     PREFERRED_TAG_SEPARATOR = ',';
  
  /** Alternate separator between multiple tags. */
  public static final char     ALTERNATE_TAG_SEPARATOR = ';';
  
  /** 
    The normalized representation of zero or more tags,
    with periods separating each level, with commas separating
    distinct tags, and with one space following each comma.
   */
  private StringBuilder tags = new StringBuilder();
  
  /** 
    Creates a new instance of Tags with null values.
   */
  public Tags() {
    set ("");
  }
  
  /** 
    Creates a new instance of Tags with a particular value.
   
    @param tags     A string containing one or more tags.
                    Levels may be separated by periods or slashes, 
                    and spaces may separate the periords or slashes from
                    the words.
   */
  public Tags (String tags) {
    set (tags);
  }

  /**
   Sets the value of the data element from the element's preferred class form.

   @param value The value, as the element's preferred class.
   */
  public void setValue(Object value) {
    setValue(value.toString());
  }

  /**
   Sets the value of the data element from a string representation.

   @param value A string representation of the desired value for the element.
   */
  public void setValue(String value) {
    set (value);
  }

  /**
   Obtain the value of the element in the form of the element's preferred class.

   @return The value of the element in the form of the
           element's preferred class.
   */
  public Object getValue() {
    return tags.toString();
  }

  /**
   Get the class definition for the preferred form of the element.

   @return The class definition for the preferred form of the element.
   */
  public Class getElementClass() {
    return String.class;
  }

  /**
   Obtain the simplest form of the data element's name. This is typically a
   name in all lower-case, without any spaces or punctuation.

   @return The simplest form of the data element's name. This is typically a
           name in all lower-case, without any spaces or punctuation.
   */
  public String getSimpleName() {
    return SIMPLE_NAME;
  }


  /**
   Obtain the form of the name intended for display to humans. This would
   typically include spaces between words and capitalization of the first
   letter of each word.

   @return The form of the name intended for display to humans. This would
           typically include spaces between words and capitalization of the
           first letter of each word.
   */
  public String getDisplayName() {
    return DISPLAY_NAME;
  }


  /**
   Obtain a human-readable name, but intended for display in spaces where
   minimal space is available.

   @return A human-readable name, but intended for display in spaces where
           minimal space is available.
   */
  public String getBriefName() {
    return BRIEF_NAME;
  }


  /**
   Get the width of the column.

   @return The width of the column in a JTable.
   */
  public int getColumnWidth() {
    return COLUMN_WIDTH;
  }
  
  /** 
    Sets the tags to a particular value.
   
    @param inTags   A string containing one or more tags.
                    Levels may be separated by periods or slashes, 
                    and spaces may separate the periods or slashes from
                    the words.
   */
  public void set(String inTags) {
    tags.delete (0, tags.length());
    merge (inTags);
  }

  /**
   Flatten all the tags for this URL, separating each level/word into its own
   first-level tag.
   */
  public void flatten () {
    String tags2 = tags.toString();
    tags.delete (0, tags.length());
    int e2 = 0;
    int s2 = indexOfNextWordStart (tags2, e2);
    while (s2 < tags2.length()) {
      e2 = indexOfNextSeparator (tags2, s2, true, true);
      merge (tags2.substring (s2, e2));
      s2 = indexOfNextWordStart (tags2, e2);
    }
  }

  /**
   Perform a mass change operation, changing all occurrences of the
   from String to the To String.

   @param from
   @param to
   */
  public void replace (String from, String to) {
    boolean found = (from.length() == 0);

    // Go through tags, checking individual words
    /*
    int e = 0;
    int s = indexOfNextWordStart (tags, e);
    while (s < tags.length() && (! found)) {
      e = indexOfNextSeparator (tags, s, true, true);
      if (from.equalsIgnoreCase (tags.substring (s, e))) {
        found = true;
        removeTag (s, e);
      }
    }
    */
    int e = 0;
    int s = indexOfNextWordStart (tags, e);

    // Go through tags, looking for complete tags
    while (s < tags.length() && (! found)) {
      e = indexOfNextSeparator (tags, s, false, true);
      if (from.equalsIgnoreCase (tags.substring (s, e))) {
        found = true;
        removeTag (s, e);
      } else {
        s = indexOfNextWordStart (tags, e);
      }
    }
    if (found && to.length() > 0) {
      merge (to);
    }
  } // end method replace

  public void merge (Tags tags) {
    merge (tags.toString());
  }
  
  /** 
    Merges the passed tags, in string form, into whatever tags already exist.
   
    @param inTags   A string containing one or more tags.
                    Levels may be separated by periods or slashes, 
                    and spaces may separate the periods or slashes from
                    the words.
   */
  public void merge (String inTags) {

    // Remove any rubbish and drop leading and trailing spaces
    String tags2 = StringUtils.purify(inTags).trim();
    // System.out.println ("tags2 (purified) = " + tags2);
    int e2 = 0;
    int s2 = indexOfNextWordStart (tags2, e2);
    // System.out.println ("initial s2 = " + String.valueOf(s2));
    int wordEnd2 = e2;
    char sep = ' ';
    while (s2 < tags2.length()) {
      // Process next tag from input
      StringBuilder next2 = new StringBuilder();
      sep = ' ';
      while (s2 < tags2.length() && (! isTagSeparator (sep))) {
        // Process next level in text tag from input
        e2 = indexOfNextSeparator (tags2, s2, true, true);
        // System.out.println ("e2 and wordend both = " + String.valueOf(e2));
        wordEnd2 = e2;
        while (wordEnd2 > 0 && Character.isWhitespace (tags2.charAt(wordEnd2 - 1))) {
          wordEnd2--;
        }
        if (next2.length() > 0) {
          next2.append (PREFERRED_LEVEL_SEPARATOR);
        }
        next2.append (tags2.substring (s2, wordEnd2));
        // System.out.println ("appending " + String.valueOf(s2) + "-"
        //     + String.valueOf(e2) + " from " + tags2);
        if (e2 < tags2.length()) {
          sep = tags2.charAt (e2);
        }
        s2 = indexOfNextWordStart (tags2, e2);
        // System.out.println ("s2 = " + String.valueOf(s2));
      } // end while processing levels for next input tag

      // System.out.println ("next2 = " + next2.toString());

      // next2 now contains next tag from input string
      int s = indexOfNextWordStart (tags, 0);
      int e = 0;
      int compareResult = 1;
      while (s < tags.length() && (compareResult > 0)) {
        e = indexOfNextSeparator (tags, s, false, true);
        compareResult
            = (next2.toString().compareToIgnoreCase (tags.substring (s, e)));
        // System.out.println ("s = " + String.valueOf(s)
        //     + " e = " + String.valueOf(e)
        //     + " compareResult = " + String.valueOf(compareResult));
        if ((compareResult > 0) && (s < tags.length())) {
          s = indexOfNextWordStart (tags, e);
        }
      } // End while looking for a match or insertion point

      if (s >= tags.length()) {
        if (tags.length() > 0) {
          tags.append (PREFERRED_TAG_SEPARATOR);
          tags.append (' ');
        }
        tags.append (next2);
        // System.out.println ("At end of tags, appending with result "
        //     + tags.toString());
      }
      else
      if (compareResult < 0) {
        tags.insert (s, next2);
        tags.insert (s + next2.length(), PREFERRED_TAG_SEPARATOR);
        tags.insert (s + next2.length() + 1, ' ');
      }
      s2 = indexOfNextWordStart (tags2, e2);
    } // end while more tags from input
  } // end merge string method

  /**
   Remove the indicated tag, plus any associated separator and extra spacing.

   @param start The starting position of the tag to be deleted.
   @param end   The position of the first character following
                the tag to be deleted
   @return      The total number of characters deleted, including any
                associated separator and extra spacing.
   */
  public int removeTag (int start, int end) {
    int realEnd = end;
    if (end > tags.length()) {
      realEnd = tags.length();
    }
    tags.delete (start, end);
    int deletedLength = realEnd - start;
    boolean levelDeleted = false;
    boolean sepBefore = false;
    int sepLocation = -1;

    // If we have a period immediately before or after, then treat
    // the deleted tag as a level, and not a complete tag
    if (start > 0 && isLevelSeparator (tags.charAt (start - 1))) {
      levelDeleted = true;
      sepBefore = true;
      sepLocation = start - 1;
    }
    else
    if (start < tags.length() && isLevelSeparator (tags.charAt (start))) {
      levelDeleted = true;
      sepBefore = false;
      sepLocation = start;
    }

    // If we didn't find a level separator, then look for a tag separator
    if (! levelDeleted) {
      if (start > 0 && isTagSeparator (tags.charAt (start - 1))) {
        levelDeleted = false;
        sepBefore = true;
        sepLocation = start - 1;
      }
      else
      if (start < tags.length() && isTagSeparator (tags.charAt (start))) {
        levelDeleted = false;
        sepBefore = false;
        sepLocation = start;
      }
      else
      if ((start - 1) > 0
          && isTagSeparator (tags.charAt (start - 2))
          && Character.isWhitespace (tags.charAt (start - 1))) {
        levelDeleted = false;
        sepBefore = true;
        sepLocation = start - 2;
      }
    }

    // Delete the separator, if found
    if (sepLocation >= 0) {
      tags.delete (sepLocation, sepLocation + 1);
      deletedLength++;
    }

    // Delete associated space for a tag separator
    if (sepLocation >= 0 && (! levelDeleted)) {
      if (sepLocation < tags.length()
          && sepLocation >= 0
          && Character.isWhitespace (tags.charAt (sepLocation))) {
        tags.delete (sepLocation, sepLocation + 1);
        deletedLength++;
      }
      else
      if ((sepLocation - 1) < tags.length()
          && (sepLocation - 1) >= 0
          && Character.isWhitespace (tags.charAt (sepLocation - 1))) {
        tags.delete (sepLocation - 1, sepLocation);
        deletedLength++;
      }
    }

    return deletedLength;
  }

  /**
   Convert the tags to all lower-case letters.
   */
  public void makeLowerCase () {
    tags = new StringBuilder (tags.toString().toLowerCase());
  }

  /**
   Get the number of levels for the given tag number, with zero identifying
   the first tag.

   @param tagIndex A tag number, with zero identifying the first tag.

   @return The number of levels for the given tag number. 
   */
  public int getLevels (int tagIndex) {
    int levels = 0;
    int s = getTagStart (tagIndex);
    int e = indexOfNextSeparator (tags, s, true, true);
    if (s >= 0 && s < tags.length()) {
      levels = 1;
      while (s < tags.length()
          && e < tags.length()
          && (! isTagSeparator (tags.charAt(e)))) {
        levels++;
        s = indexOfNextWordStart (tags, e);
        e = indexOfNextSeparator (tags, s, true, true);
      }
    }
    return levels;
  }
  
  /**
   Get the word or phrase making up the given level, for the given tags
   number, within the entire tags string assigned to this item.
   
   @param tagIndex Number indicating which tags is desired (first is 0).
   @param levelIndex Number indicating which level is desired within given
                     tags (first is 0).
   @return Word or phrase making up this tags level.
   */
  public String getLevel (int tagIndex, int levelIndex) {
    if (tagIndex < 0 || levelIndex < 0) {
      return "";
    } else {
      int s = getTagStart (tagIndex);
      int e = indexOfNextSeparator (tags, s, true, true);
      int levelCount = 0;
      while (levelCount < levelIndex && s < tags.length()) {
        s = indexOfNextWordStart (tags, e);
        e = indexOfNextSeparator (tags, s, true, true);
        if (s < tags.length()) {
          levelCount++;
        }
      }
      if (s < tags.length() && levelCount == levelIndex) {
        return tags.substring (s, e);
      } else {
        return "";
      }
    }
  }

  /**
   Return the specified word.

   @param wordIndex The requested word number, where the first word is zero.

   @return The requested word.
   */
  public String getWord (int wordIndex) {
    if (wordIndex < 0 ) {
      return "";
    } else {
      int s = getWordStart (wordIndex);
      int e = indexOfNextSeparator (tags, s, true, true);
      if (e > s) {
        return tags.substring (s, e);
      } else {
        return "";
      }
    }
  }

  /**
   Return a concatenation of all requested words, identified by starting
   and ending word numbers.

   @param from The first word to be returned, where zero identifies the first.
   @param thru The last word to be returned, where zero identifies the first.
   @return The specified series of words.
   */
  public String getWordsFromThru (int from, int thru) {
    if (from < 0 || thru < 0 || thru < from) {
      return "";
    } else {
      int s = getWordStart (from);
      int e = getWordEnd (thru);
      if (s < e) {
        return tags.substring (s, e);
      } else {
        return "";
      }
    }
  } // end method
  
  /**
   Get all the tags embedded in html links. 
  
   @param parent The parent path to be prepended to the link.
   @return       The resulting string.
  */
  public String getLinkedTags (String parent) {
    StringBuilder linkedTags = new StringBuilder();
    int i = 0;
    int s = getTagStart(i);
    while (s >= 0 && s < tags.length()) {
      if (i > 0) {
        linkedTags.append(", ");
      }
      linkedTags.append(getLinkedTag (i, parent));
      i++;
      s = getTagStart(i);    
    }
    return linkedTags.toString();
  }
  
  /**
   Get the specified tag, with an html link surrounding it.
  
   @param categoryIndex The index identifying the desired tag, where
                        the first tag is at index 0.
   @param parent        The parent path to be prepended to the link.
   @return              The resulting string. 
  */
  public String getLinkedTag (int tagIndex, String parent) {
    StringBuilder tag = new StringBuilder();
    StringBuilder link = new StringBuilder();
    int levels = getLevels(tagIndex);
    for (int levelIndex = 0; levelIndex < levels; levelIndex++) {
      if (levelIndex > 0) {
        tag.append(PREFERRED_LEVEL_SEPARATOR);
        link.append("-");
      }
      String tagLevel = getLevel(tagIndex, levelIndex);
      tag.append(tagLevel);
      link.append(StringUtils.makeFileName(tagLevel, false));
    }
    return (
        "<a href='"
        + parent
        + link.toString()
        + ".html' rel='tag'>"
        + tag.toString()
        + "</a>");
  }
  
  /**
   Return the specified tag, including all levels. 
  
   @param tagIndex The index identifying the desired tag, where the 
                   first tag is at index 0.
  
   @return The specified tag, or an empty string, if the tag index does
           not exist. 
  */
  public String getTag(int tagIndex) {
    int s = getTagStart(tagIndex);
    if (s >= 0 && s < tags.length()) {
      int e = getTagEnd(tagIndex);
      return substring(s, e);
    } else {
      return "";
    }
  }

  /**
   Return the starting position of this tag in the tags string.

   @param tagIndex The index identifying the desired tag,
                   where the first word = 0.

   @return The starting position of this tag in the tags string.
   */
  public int getTagStart (int tagIndex) {
    int tagCount = -1;
    int i = 0;
    int start = 0;
    int end = 0;
    while (i < tags.length() && tagCount < tagIndex) {
      start = indexOfNextWordStart (tags, i);
      end   = indexOfNextSeparator (tags, start, false, true);
      tagCount++;
      if (tagCount < tagIndex) {
        i = end + 1;
      }
    }
    if (tagCount < tagIndex) {
      return tags.length();
    } else {
      return start;
    }
  }

  /**
   Returns the position immediately following the last character in this tag.

   @param tagIndex The index identifying the desired tag,
                   where the first tag = 0.

   @return The position immediately following the last character in this tag.
   */
  public int getTagEnd (int tagIndex) {
    int tagCount = -1;
    int i = 0;
    int start = 0;
    int end = 0;
    while (i < tags.length() && tagCount < tagIndex) {
      start = indexOfNextWordStart (tags, i);
      end   = indexOfNextSeparator (tags, start, false, true);
      tagCount++;
      if (tagCount < tagIndex) {
        i = end + 1;
      }
    }
    return end;
  }
  
  /**
   Return the starting position of this word in the tags string.
   
   @param wordIndex The index identifying the desired word,
                    where the first word = 0.
   
   @return The starting position of this word in the tags string.
   */
  public int getWordStart (int wordIndex) {
    int wordCount = -1;
    int i = 0;
    int start = 0;
    int end = 0;
    while (i < tags.length() && wordCount < wordIndex) {
      start = indexOfNextWordStart (tags, i);
      end   = indexOfNextSeparator (tags, start, true, true);
      wordCount++;
      if (wordCount < wordIndex) {
        i = end + 1;
      }
    }
    return start;
  }
  
  /**
   Returns the position immediately following the last character in this word.
   
   @param wordIndex The index identifying the desired word,
                    where the first word = 0.
   
   @return The position immediately following the last character in this word.
   */
  public int getWordEnd (int wordIndex) {
    int wordCount = -1;
    int i = 0;
    int start = 0;
    int end = 0;
    while (i < tags.length() && wordCount < wordIndex) {
      start = indexOfNextWordStart (tags, i);
      end   = indexOfNextSeparator (tags, start, true, true);
      wordCount++;
      if (wordCount < wordIndex) {
        i = end + 1;
      }
    }
    return end;
  }
  
  /**
    Determines if this tags is essentially equal to another
    tags. The two tagsCount may be different objects.
   
    @return True if the two tags strings are equal, ignoring upper-
            or lower- case considerations.
   
    @param  tags2 Second Category to be compared to this one.
   */
  public boolean equals (Tags tags2) {
    return (compareTo(tags2) == 0);
  }
  
  /**
    Compares this tags to another tags, using a non-case-sensitive
    comparison of their two normalized tags strings as the basis
    for the comparison. 
   
    @return Zero if the two tagsCount are equal;
            < 0 if this tags is less than the second tags; or
            > 0 if this tags is greater than the second tags.
   
    @param  cat2 Second Category to be compared to this one.
   */
  public int compareTo (Tags cat2) {
    return tags.toString().compareToIgnoreCase (cat2.toString());
  }

  /**
   Return the length of the tags string.

   @return The length of the tags string.
   */
  public int length () {
    return tags.length();
  }

  /**
   Is the tags string empty (length of zero)?

   @return True if length of tags string is zero, otherwise false.
   */
  public boolean isBlank() {
    return (tags.length() == 0);
  }

  /**
   Return the tags string in its native state, as a StringBuilder.

   @return The tags string as a StringBuilder. 
   */
  public StringBuilder getTags () {
    return tags;
  }

  /**
   Return the requested substring of the tags string.

   @param s The starting position.
   @param e The position just past the end of the string to be returned.
   @return  The requested substring. 
   */
  public String substring (int s, int e) {
    return tags.substring (s, e);
  }

  /**
   Return the character at the specified index position within
   the tags string.

   @param i The index position at which the character is desired.

   @return The character at the requested position, or a space
           if the requested position does not point to a valid position.
   */
  public char charAt (int i) {
    if (i < 0 || i >= tags.length()) {
      return ' ';
    } else {
      return tags.charAt (i);
    }
  }

  /**
   Display all of the tags, for debugging
   */
  public void displayTags() {
    System.out.println ("tags = " + tags);
  }
  
  /**
    Return the normalized tags string for this object.
   
    @return The normalized tags string, with periods separating levels,
            and with no spaces surrounding the periods.
   */
  @Override
  public String toString() {
    return tags.toString();
  }

  /**
   Append the appropriate separator, with appropriate spacing, to the
   passed string buffer.

   @param str     The string buffer to which the separator is to be appended.
   @param catSep  Is this the beginning of a new tag, or just another
                  level/word within an existing tag?
   @return        The number of characters appended to the passed string buffer.
   */
  private static int appendSeparator (StringBuilder str, boolean catSep) {
    int added = 0;
    if (catSep) {
      str.append (PREFERRED_TAG_SEPARATOR);
      str.append (" ");
      added = 2;
    } else {
      str.append (PREFERRED_LEVEL_SEPARATOR);
      added = 1;
    }
    return added;
  }
  
  /**
   Return the position of the start of the next word, following an optional
   separator and zero or more spaces.
   
   @param str       The StringBuilder to be scanned. 
   @param fromIndex The starting point for the scan.
   @return          The position of the start of the next word, or the length
                    of the StringBuilder, if not found.
   */
  public static int indexOfNextWordStart (StringBuilder str, int fromIndex) {
    int i = fromIndex;
    if (i < str.length() && 
        (isLevelSeparator (str.charAt(i)) || isTagSeparator (str.charAt(i)))) {
      i++;
    }
    while (i < str.length() && Character.isWhitespace (str.charAt(i))) {
      i++;
    }
    return i;
  }

  /**
   Return the position of the start of the next word, following an optional
   separator and zero or more spaces.

   @param str       The String to be scanned.
   @param fromIndex The starting point for the scan.
   @return          The position of the start of the next word, or the length
                    of the String, if not found.
   */
  public static int indexOfNextWordStart (String str, int fromIndex) {
    int i = fromIndex;
    if (i < str.length() &&
        (isLevelSeparator (str.charAt(i)) || isTagSeparator (str.charAt(i)))) {
      i++;
    }
    while (i < str.length() && Character.isWhitespace (str.charAt(i))) {
      i++;
    }
    return i;
  }

  /**
   Return the position of the next separator. User may elect to stop for
   one separator type or the other, or either.

   @param str       The StringBuilder to be scanned.
   @param fromIndex The starting point for the scan.
   @param levelSep  Stop at level separators?
   @param tagSep    Stop at tag separators?
   @return          The position of the next separator of the specified type,
                    or the length of the StringBuilder if nothing found.
   */
  public static int indexOfNextSeparator
      (StringBuilder str, int fromIndex, boolean levelSep, boolean tagSep) {
    int i = fromIndex;
    boolean found = false;
    char c = ' ';
    while (i < str.length() && (! found)) {
      c = str.charAt (i);
      if (isLevelSeparator (c) && levelSep) {
        found = true;
      }
      else
      if (isTagSeparator (c) && tagSep) {
        found = true;
      }
      if (! found) {
        i++;
      }
    }
    return i;
  }

  /**
   Return the position of the next separator. User may elect to stop for
   one separator type or the other, or either.

   @param str       The String to be scanned.
   @param fromIndex The starting point for the scan.
   @param levelSep  Stop at level separators?
   @param tagSep    Stop at tag separators?
   @return          The position of the next separator of the specified type,
                    or the length of the String if nothing found.
   */
  public static int indexOfNextSeparator
      (String str, int fromIndex, boolean levelSep, boolean tagSep) {
    int i = fromIndex;
    boolean found = false;
    char c = ' ';
    while (i < str.length() && (! found)) {
      c = str.charAt (i);
      if (isLevelSeparator (c) && levelSep) {
        found = true;
      }
      else
      if (isTagSeparator (c) && tagSep) {
        found = true;
      }
      if (! found) {
        i++;
      }
    }
    return i;
  }

  /**
   Static method to evaluate a character and see if it qualifies as a level
   separator.

   @param sepChar The character to be evaluated.

   @return True if this is a level separator (a period or a slash).
   */
  public static boolean isLevelSeparator (char sepChar) {
    return (sepChar == PREFERRED_LEVEL_SEPARATOR
          || sepChar == ALTERNATE_LEVEL_SEPARATOR);
  }

  /**
   Static method to evaluate a character and see if it qualifies as a
   tag separator.

   @param sepChar The character to evaluated.

   @return True if this is a tag separator (comma or a semi-colon).
   */
  public static boolean isTagSeparator (char sepChar) {
    return (sepChar == PREFERRED_TAG_SEPARATOR
          || sepChar == ALTERNATE_TAG_SEPARATOR);
  }


  
}
