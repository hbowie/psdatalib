/*
 * Copyright 2013 - 2014 Herb Bowie
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
  import java.util.*;

/**
 One line of note data, following MultiMarkdown conventions. The line will be
 parsed, and its contents will be appropriately stored in the passed 
 Note object. 

 @author Herb Bowie
 */
public class NoteLine {
  
  private               NoteLineLeadingSymbol leadingSymbol 
      = new NoteLineLeadingSymbol();
  private               ArrayList<NoteLineLeadingSymbol> leadingSymbols 
      = new ArrayList<NoteLineLeadingSymbol>();

  private               String        line = null;
  private               boolean       breakAfter = false;
  private               int           first = 0;
  private               int           last = 0;
  private               int           colon = -1;
  private               int           field1First = 0;
  private               int           field1Last = -1;
  private               int           field2First = 0;
  private               int           field2Last = -1;
  
  public NoteLine (NoteParms noteParms, NoteBuilder builder, Note note, String line) {
    setLine(noteParms, builder, note, line);
  }
  
  public void setLine (NoteParms noteParms, NoteBuilder builder, Note note, String line) {
    this.line = line;
    parseStartAndEndOfLine(noteParms, builder, note);
  } // end setLine method
  
  private void parseStartAndEndOfLine(NoteParms noteParms, NoteBuilder builder, Note note) {
    
    if (line != null) {
      builder.incrementLineNumber();
      builder.addToFileSize(line.length() + 1);
      
      boolean contentStored = false;
      
      first = 0;
      
      // Look for last character on the line
      int trailingSpaceCount = 0;
      last = line.length() - 1;
      while (last >= 0 && Character.isWhitespace(line.charAt(last))) {
        trailingSpaceCount++;
        last--;
      }
      breakAfter = (trailingSpaceCount >= 2);
      
      // Let's analyze the front of the line for white space
      // and punctuation.
      int spaceCount = 0;
      char c = ' ';
      char c2 = ' ';
      char underlineChar = ' ';
      boolean whitespaceOrPunctuation = true;
      leadingSymbol = new NoteLineLeadingSymbol();
  
      int headingLevel = getHeadingLevel();
      if (first <= last) {
        c2 = line.charAt(first);
      } 
      while (first <= last && whitespaceOrPunctuation) {
        c = c2;
        if ((first + 1) <= last) {
          c2 = line.charAt(first + 1);
        } else {
          c2 = ' ';
        }
        
        // Check for white space
        if (Character.isWhitespace(c)) {
          if (line.charAt(first) == GlobalConstants.TAB) {
            leadingSymbol.incrementIndentLevels();
          } else {
            spaceCount++;
            if (spaceCount == 4) {
              leadingSymbol.incrementIndentLevels();
              spaceCount = 0;
            }
          }
          first++;
        }
        else
          
        // > indicates block quote
        if (c == '>') {
          leadingSymbol.setType(c);
          addNoteLineLeadingSymbol();
          first++;
        }
        else
          
        // # indicates a heading
        if (c == '#') {
          headingLevel++;
          if (line.charAt(last) == '#') {
            last--;
          }
          first++;
        }
        else
          
        // Check for unordered list
        if (c2 == ' '
            && (c == '*' || c == '-' || c == '+')) {
          leadingSymbol.setUnorderedListItem();
          addNoteLineLeadingSymbol();
          first++;
        }
        else
          
        // Check for ordered list
        if (c2 == '.' && Character.isDigit(c)) {
          leadingSymbol.setOrderedListItem();
          addNoteLineLeadingSymbol();
          first++;
        }
        else
          
        // Underlines may consist of dashes or equal signs  
        if ((c == '-' || c == '=')) {
          if (underlineChar == ' ') {
            underlineChar = c;
          }
          else
          if (c == underlineChar) {
            // keep the underline char
          } else {
            underlineChar = ' ';
          }
          first++;
          
        // Looks like we've reached the start of the actual line content
        } else {
          whitespaceOrPunctuation = false;
        }
      } // end of punctuation
      
      // If we've got nothing left to parse, then check for
      // a blank line or a line of underlines.
      if (first > last) {
        if (underlineChar == '-' || underlineChar == '=') {
          leadingSymbol.setType(underlineChar);
          addNoteLineLeadingSymbol();
        } else {
          leadingSymbol.setType(' ');
          addNoteLineLeadingSymbol();
        }
      }
      
      if (getFirstNoteLineLeadingSymbol().isH1() && builder.hasLastLine()) {
        note.setTitle(builder.getLastLine());
        builder.setLastStringBuilder(null);
        contentStored = true;
      }
      
      // Now that we've processed leading and trailing punctuation, let's 
      // skip past any more white space on the front or end of the line
      while (first <= last
          && Character.isWhitespace(line.charAt(first))) {
        first++;
      }
      
      while (first <= last
          && Character.isWhitespace(line.charAt(last))) {
        last--;
      }
      
      if (first <= last && headingLevel > 0) {
        setHeadingLevel(headingLevel);
      }
      
      int endOfFirstWord = first;
      
      String metaKey = "";
      
      if (builder.isFrontOfFile()
          && (! isBlankLine())
          && (! isUnderlines())
          && (! isHeading())) {
        colon = line.indexOf(":");
        if (colon >= 0) {
          field1First = first;
          field2Last = last;
          field1Last = colon - 1;
          while (field1Last >= field1First
              && Character.isWhitespace(line.charAt(field1Last))) {
            field1Last--;
          }
          field2First = colon + 1;
          while (field2First <= field2Last
              && Character.isWhitespace(line.charAt(field2First))) {
            field2First++;
          }
          while (field2First <= field2Last
              && Character.isWhitespace(line.charAt(field2Last))) {
            field2Last--;
          }
          
          DataFieldDefinition fieldDef = noteParms.checkForFieldName(getMetaKey());
          if (fieldDef != null) {
            leadingSymbol.setTypeToMetadata();
            addNoteLineLeadingSymbol();
            metaKey = fieldDef.getCommonName().getCommonForm();
            builder.setLastStringBuilder(null);
            if (metaKey.equals(NoteParms.TITLE_COMMON_NAME)) {
              note.setTitle(getMetaData());
              builder.setLastStringBuilder(null);
            }
            else
            if (metaKey.equals(NoteParms.AUTHOR_COMMON_NAME)
                || metaKey.equalsIgnoreCase(NoteParms.BY)
                || metaKey.equalsIgnoreCase(NoteParms.CREATOR)) {
              note.setAuthor(getMetaData());
              builder.setLastStringBuilder(null);
            }
            else
            if (metaKey.equals(NoteParms.DATE_COMMON_NAME)) {
              note.setDate(getMetaData());
              builder.setLastStringBuilder(null);
            }
            else
            if (metaKey.equals(NoteParms.LINK_COMMON_NAME)) {
              note.setLink(getMetaData());
              builder.setLastStringBuilder(null);
            }
            else
            if (metaKey.equals(NoteParms.TAGS_COMMON_NAME)) {
              note.setTags(getMetaData());
              builder.setLastStringBuilder(null);
            }
            else
            if (metaKey.equals(NoteParms.BODY_COMMON_NAME)) {
              note.setBody(getMetaData());
              builder.setLastStringBuilder(note.getBodyAsDataValue());
              builder.setBodyStarted(true);
            } else {
              fieldDef.setType(DataFieldDefinition.STRING_BUILDER_TYPE);
              DataValueStringBuilder dataValue = new DataValueStringBuilder(getMetaData());
              DataField dataField = new DataField (fieldDef, dataValue);
              note.storeField(note.getRecDef(), dataField);
              builder.setLastStringBuilder(dataValue);
            }
            contentStored = true;
          }  // end if valid metadata key found
        } // end if colon found
        else {
          // Find end of first word
          while (endOfFirstWord < line.length()
             && (! Character.isWhitespace(line.charAt(endOfFirstWord)))) {
            endOfFirstWord++;
          }
          if (line.substring(first, endOfFirstWord).trim().equalsIgnoreCase("by")) {
            note.setAuthor(line.substring(endOfFirstWord + 1, last + 1));
            builder.setLastStringBuilder(null);
            leadingSymbol.setTypeToByline();
            addNoteLineLeadingSymbol();
            contentStored = true;
          } 
        }
      } // end if metadata candidate
      builder.setLastLine(line);
      
      // If we haven't yet stored the contents of the line in the note,
      // then do it now.
      if (! contentStored) {
        if (! builder.hasLastStringBuilder()) {
          if (isBlankLine()) {
            // Just treat this as a spacer and not data
          } else {
            note.setBody("");
            builder.setLastStringBuilder(note.getBodyAsDataValue());
            builder.setBodyStarted(true);
          }
        }
        if (isBlankLine() && (! builder.appendingStarted())) {
          // Let's not put blank lines at the beginning of a multi-line field 
        } else {
          builder.appendLineToLastStringBuilder(line);
        }
      } // End if content not yet stored
    } // end if line not null
  } // end of parseStartAndEndOfLine method
  
  private void addNoteLineLeadingSymbol() {
    leadingSymbols.add(leadingSymbol);
    leadingSymbol = new NoteLineLeadingSymbol();
  }
  
  public String getLine() {
    return line;
  }
  
  /* public int getBlockQuoteLevel() {
    return blockQuoteLevel;
  } */
  
  public boolean isBlankLine() {
    return (leadingSymbols.size() > 0
        && getLastNoteLineLeadingSymbol().isBlankLine());
  }
  
  public boolean isUnderlines() {
    return (getFirstNoteLineLeadingSymbol().isUnderlines());
  }
  
  public boolean isByLine() {
    return (getFirstNoteLineLeadingSymbol().isByLine());
  }
  
  public char getUnderlineChar() {
    return (getFirstNoteLineLeadingSymbol().getUnderlineChar());
  }
  
  public boolean isMetadata() {
    return (getFirstNoteLineLeadingSymbol().isMetadata());
  }
  
  public String getMetaKey() {
    if (field1Last >= field1First) {
      return line.substring(field1First, field1Last + 1);
    } else {
      return "";
    }
  }
  
  public String getMetaData() {
    if (field2Last >= field2First) {
      return line.substring(field2First, field2Last + 1);
    } else {
      return "";
    }
  }
  
  public void setHeadingLevel(int headingLevel) {
    if (leadingSymbols.size() > 0) {
      leadingSymbols.get(0).setHeadingLevel(headingLevel);
    } else {
      leadingSymbol = new NoteLineLeadingSymbol();
      leadingSymbol.setHeadingLevel(headingLevel);
      addNoteLineLeadingSymbol();
    }
  }
  
  public boolean isHeading() {
    return (getFirstNoteLineLeadingSymbol().isHeading());
  }
  
  public int getHeadingLevel() {
    return (getFirstNoteLineLeadingSymbol().getHeadingLevel());
  }
  
  /**
   Get the content of the line, bypassing any leading and trailing 
   whitespace and punctuation.

   @return The data contained on the line. 
  */
  public String getLineContent() {
    if (first > last) {
      return "";
    } else {
      return line.substring(first, last + 1);
    }
  }
  
  public boolean isTableOfContentsHeading() {
    String id = getID();
    return (getHeadingLevel() > 0
        &&  id.length() > 0
        && (id.equals("table-of-contents")
         || id.equals("contents")));
  }
  
  public String getID() {
    return makeID (line, first, last);
  }
  
  public static String makeID (String line, int first, int last) {
    StringBuilder id = new StringBuilder();
    for (int h = first; h <= last; h++) {
      char ch = line.charAt(h);
      if (Character.isWhitespace(ch)
          && id.length() > 0
          && (h + 1) <= last
          && (! Character.isWhitespace(line.charAt(h + 1)))) {
        id.append("-");
      }
      if (Character.isLetter(ch)
          || Character.isDigit(ch)) {
        id.append(Character.toLowerCase(ch));
      } // end if we have a letter or a digit in the heading
    } // end while we have more heading characters to convert to an id
    return id.toString();
  }
  
  public NoteLineLeadingSymbol getFirstNoteLineLeadingSymbol() {
    if (leadingSymbols.size() < 1) {
      return new NoteLineLeadingSymbol();
    } else {
      return leadingSymbols.get(0);
    }
  }
  
  public NoteLineLeadingSymbol getLastNoteLineLeadingSymbol() {
    if (leadingSymbols.size() < 1) {
      return new NoteLineLeadingSymbol();
    } else {
      return leadingSymbols.get(leadingSymbols.size() - 1);
    }
  }
  
  public NoteLineLeadingSymbol getNoteLineLeadingSymbol (int i) {
    if (i < 0 || i >= leadingSymbols.size()) {
      return null;
    } else {
      return leadingSymbols.get(i);
    }
  }
  
  /**
   Display internal data for testing purposes. 
  */
  public void display() {
    System.out.println("MarkdownLine.display");
    System.out.println("  line: " + line);
    for (int i = 0; i < leadingSymbols.size(); i++) {
      leadingSymbols.get(i).display();
    }

    if (breakAfter) {
      System.out.println("  break after");
    }
    System.out.println("  first position of info: " + String.valueOf(first));
    System.out.println("  last position of info: " + String.valueOf(last));
    if (colon >= 0) {
      System.out.println("  colon position: " + String.valueOf(colon));
    }
    if (isMetadata()) {
      System.out.println("  metakey: " + getMetaKey());
      System.out.println("  metadata: " + getMetaData());
    }
    System.out.println(" ");
  }

}