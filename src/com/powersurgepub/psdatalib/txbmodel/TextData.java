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

package com.powersurgepub.psdatalib.txbmodel;

/**
 Defines the data stored with each node in the tree. The tree can be
 used to represent HTML, or any other data structure.
 <p>
 Each TextData object has an optional string associated with it. All white '
 space characters are treated equally (no special significance for tabs,
 carriage returns, etc.) and runs of more than one white space characters
 are reduced to single character occurrences.
 <p>
 Each TextData object also has a type associated with it. For HTML, the type
 would be a tag or attribute.
 
 @author Herb Bowie
 */

public class TextData {
 
  private              TextType         type = new TextType();
  private              StringBuffer     text = new StringBuffer();
  private              int              textCase = CASE_NOT_EVALUATED;
  public  static final int              CASE_NOT_EVALUATED = -1;
  public  static final int              CASE_SENTENCE      = 0;
  public  static final int              CASE_TITLE         = 1;
  private              int              childTagCount = 0;

  /**
   Create a new TextData object.
   */
  public TextData () {
    
  }

  /**
   Create a new TextData object with the supplied text.

   @param text The text to associate with the data.
   */
  public TextData (String text) {
    this.text = new StringBuffer (text);
  }

  /**
   Set the text to the supplied value.

   @param text The text to replace whatever text is currently defined.
   */
  public void setText (String text) {
    this.text = new StringBuffer (text);
    textCase = CASE_NOT_EVALUATED;
  }

  /**
   Append the passed character array to the existing text, reducing white space
   runs to single character occurrences.

   @param ch     The character array to be appended.
   @param start  The starting position within the array.
   @param length The length of the character sequence to be appended. 
   */
  public void characters (char [] ch, int start, int length) {
    
    StringBuffer xmlchars = new StringBuffer();
    xmlchars.append (ch, start, length);
    characters (xmlchars.toString());
 
  } // end method characters
  
  public void characters (String more, boolean preformatted) {
    if (preformatted) {
      text.append(more);
    } else {
      characters (more);
    }
  }

  /**
   Append the passed text to the current text, reducing white space runs to
   single character occurrences.
   
   @param more
   */
  public void characters (String more) {

    // boolean lastCharWhiteSpace = (text.length() < 1 ||
    boolean lastCharWhiteSpace = (text.length() > 0 &&
        Character.isWhitespace (text.charAt (text.length() - 1)));
    char c;
    boolean charWhiteSpace;
    for (int i = 0; i < more.length(); i++) {
      c = more.charAt (i);
      charWhiteSpace = Character.isWhitespace (c);
      if (charWhiteSpace) {
        if (lastCharWhiteSpace) {
          // do nothing
        } else {
          lastCharWhiteSpace = true;
          text.append (" ");
        }
      } else {
        text.append (c);
        lastCharWhiteSpace = false;
      }
    } // end for each passed character
    textCase = CASE_NOT_EVALUATED;
  }

  /**
   Return the text associated with this node.

   @return The text string associated with this node. 
   */
  public String getText () {
    return text.toString();
  }

  /**
   If 60% or more of the words contained within the text begin with capital
   letters, then the text is considered to be a title. This may be useful,
   for example, to help determine if an italicized string should be treated
   as a citation or a simple case of emphasis.

   @return True if 60% or more of the words contained within the text begin
           with capital letters. 
   */
  public boolean isTextTitleCase () {
    if (textCase == CASE_NOT_EVALUATED) {
      int words = 0;
      int capitalized = 0;
      int i = 0;
      while (i < text.length()) {
        // skip white space and find beginning of next word
        while (i < text.length() && Character.isWhitespace (text.charAt (i))) {
          i++;
        }

        // See if we have another word and if it is capitalized
        if (i < text.length()) {
          words++;
          if (Character.isUpperCase (text.charAt (i))) {
            capitalized++;
          }
        } // end if another character available

        // Skip the rest of this word
        while (i < text.length() && (! Character.isWhitespace (text.charAt (i)))) {
          i++;
        }
        // Done with a word
      } // Done with all words
      if ((capitalized / words) > 0.6) {
        textCase = CASE_TITLE;
      } else {
        textCase = CASE_SENTENCE;
      }
    }
    return (textCase == CASE_TITLE);
  }

  /**
   Set the type of the node. This is a label identifying the type of node.
   In the case of HTML, this would be the tag or attribute.

   @param type The type of the node.
   */
  public void setType (String type) {
    this.type.setType(type);
  }

  /**
   Return the type of the node as a string.

   @return The type of the node as a string.
   */
  public String getType () {
    return type.getType();
  }

  /**
   Return the type of the node as a TextType object.

   @return The type of the node as a TextType object.
   */
  public TextType getTextType () {
    return type;
  }

  /**
   Is this a break tag?

   @return True if the type is a break.
   */
  public boolean isBreak () {
    return type.isBreak();
  }

  /**
   Is this a block quote?

   @return True if the type is a block quote.
   */
  public boolean isBlockQuote () {
    return type.isBlockQuote();
  }

  /**
   Is this a paragraph?

   @return True if the type is a paragraph.
   */
  public boolean isParagraph () {
    return type.isParagraph();
  }

  /**
   Is this a heading type?

   @return True if the tag/type starts with 'h' and the second (and only other)
           character is a digit.
   */
  public boolean isHeading () {
    return type.isHeading();
  }

  /**
   Is the passed type a heading type?

   @param tag The tag/type to be evaluated.
   @return True if the tag/type starts with 'h' and the second (and only other)
           character is a digit.
   */
  public static boolean isHeading (String tag) {
    return TextType.isHeading(tag);
  }

  /**
   Return the heading level for a heading tag.

   @return Second character of the heading tag, if it is one, otherwise zero. 
   */
  public int getHeadingLevel () {
    return type.getHeadingLevel();
  }

  /**
   Return the heading level for a heading tag.

   @return Second character of the heading tag, if it is one, otherwise zero.
   */
  public static int getHeadingLevel (String tag) {
    return TextType.getHeadingLevel(tag);
  }

  /**
   Is this a comment type?

   @return True if the tag/type is a comment.
   */
  public boolean isComment() {
    return type.isComment();
  }

  /**
   Is this text without any identifying type?

   @return True if the type is null.
   */
  public boolean isNakedText () {
    return type.isNakedText();
  }

  /**
   Is this an anchor/hyperlink type?

   @return True if the tag/type represents an anchor. 
   */
  public boolean isAnchor () {
    return type.isAnchor();
  }

  /**
   Is this an italics type?

   @return True if the tag/type is italics.
   */
  public boolean isItalics () {
    return type.isItalics();
  }

  /**
   Is this a citation type?

   @return True if the tag/type represents a citation.
   */
  public boolean isCite () {
    return type.isCite();
  }

  /**
   Should we start a new line before the opening tag?

   @return True if we should start a new line.
   */
  public boolean breakBeforeOpeningTag () {
    return type.breakBeforeOpeningTag();
  }

  /**
   Should we start a new line before the opening tag?

   @return True if we should start a new line.
   */
  public static boolean breakBeforeOpeningTag (String tag) {
    return TextType.breakBeforeOpeningTag(tag);
  }

  /**
   Should we start a new line after the opening tag?

   @return True if we should start a new line.
   */
  public boolean breakAfterOpeningTag () {
    return type.breakAfterOpeningTag();
  }

  /**
   Should we start a new line after the opening tag?

   @return True if we should start a new line.
   */
  public static boolean breakAfterOpeningTag (String tag) {
    return TextType.breakAfterOpeningTag(tag);
  }

  /**
   Should we start a new line before the closing tag?

   @return True if we should start a new line.
   */
  public boolean breakBeforeClosingTag () {
     return type.breakBeforeClosingTag();
  }

  /**
   Should we start a new line before the closing tag?

   @return True if we should start a new line.
   */
  public static boolean breakBeforeClosingTag (String tag) {
     return TextType.breakBeforeClosingTag(tag);
  }

  /**
   Should we start a new line after the closing tag?

   @return True if we should start a new line.
   */
  public boolean breakAfterClosingTag () {
    return type.breakAfterClosingTag();
  }

  /**
   Should we start a new line after the closing tag?

   @return True if we should start a new line.
   */
  public static boolean breakAfterClosingTag (String tag) {
    return TextType.breakAfterClosingTag(tag);
  }

  /**
   Should we assume that the opening tag is also the closing tag?

   @return True if the opening tag is also the closing tag.
   */
  public boolean isSelfClosingTag () {
    return type.isSelfClosingTag();
  }

  /**
   Should we assume that the opening tag is also the closing tag?

   @return True if the opening tag is also the closing tag.
   */
  public static boolean isSelfClosingTag (String tag) {
    return (TextType.isSelfClosingTag(tag));
  }

  /**
   Is this an href attribute?

   @return True if this is an href attribute.
   */
  public boolean isAttributeHref () {
    return (type.isAttributeHref());
  }

  /**
   Is this the target attribute for an anchor/hyperlink?

   @return True if this is a target attribute.
   */
  public boolean isAttributeTarget () {
    return (type.isAttributeTarget());
  }

  /**
   Set the flag indicating whether this data should be treated as an
   attribute.

   @param attribute True if an attribute.
   */
  public void setAttribute (boolean attribute) {
    type.setAttribute(attribute);
  }

  /**
   Is this an attribute?

   @return True if this is an attribute.
   */
  public boolean isAttribute () {
    return type.isAttribute();
  }

  /**
   If this is not an attribute, then treat it as a tag.

   @return True if not an attribute.
   */
  public boolean isTag () {
    return (type.isTag());
  }

  /**
   Keep track of the number of children representing tags (as opposed to
   attributes). This may be useful to determine when text needs to be added
   as a separate node identified with a type of naked text.
   */
  public void incrementChildTagCount () {
    childTagCount++;
  }

  /**
   Find out how many non-attribute children we have. This may be useful to
   determine when text needs to be added as a separate node identified
   with a type of naked text.

   @return True if more than zero child tags. 
   */
  public boolean hasChildTags () {
    return (childTagCount > 0);
  }

  /**
   Format the data as a single string.

   @return Type, followed by a colon, followed by the text. 
   */
  public String toString() {
    StringBuffer work = new StringBuffer();
    if (type.getType().length() > 0) {
      work.append (type);
      if (text.length() > 0) {
        work.append (": ");
      }
    }
    work.append (text);
    return work.toString();
  }

}
