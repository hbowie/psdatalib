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

  import javax.swing.tree.*;

/**
  This class represents a single node in a tree. Each node represents a chunk
  of text and/or some information about the intended meaning of the text
  (i.e., markup).
 */
public class TextNode 
  extends DefaultMutableTreeNode {

  public static final int ATTRIBUTES_ONLY    = +1;
  public static final int TAGS_ONLY          = -1;
  public static final int ATTRIBUTES_OR_TAGS = 0;
  private TextTree tree;
  
  /** 
   Creates a new instance of TextNode, identifying the tree of which the node
   is a part.

   @param tree The tree to which this node belongs.
   */
  public TextNode(TextTree tree) {
    super();
    this.tree = tree;
  }

  /**
   Set the user data associated with the node.

   @param data The TextData instance associated with this node.
   */
  public void setData (TextData data) {
    setUserObject (data);
  }

  /**
   Set the text to the supplied value.

   @param text The text to replace whatever text is currently defined.
   */
  public void setText (String text) {
    getData().setText (text);
  }

  /**
   Append the passed character array to the existing text, reducing white space
   runs to single character occurrences.

   @param ch     The character array to be appended.
   @param start  The starting position within the array.
   @param length The length of the character sequence to be appended.
   */
  public void characters (char [] ch, int start, int length) {
    getData().characters (ch, start, length);
  } // end method characters

  /**
   Append the passed text to the current text, reducing white space runs to
   single character occurrences.

   @param more
   */
  public void characters (String more) {
    getData().characters (more);    
  }
  
  public void characters (String more, boolean preformatted) {
    getData().characters (more, preformatted);
  }

  /**
   Return the text associated with this node.

   @return The text string associated with this node.
   */
  public String getText () {
    return getData().getText();
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
    return getData().isTextTitleCase ();
  }

  /**
   Set the type of the node. This is a label identifying the type of node.
   In the case of HTML, this would be the tag or attribute.

   @param type The type of the node.
   */
  public void setType (String type) {
    getData().setType (type);
  }

  /**
   Return the type of the node.

   @return The type of the node.
   */
  public String getType () {
    return getData().getType();
  }

  public TextType getTextType () {
    return getData().getTextType();
  }

  /**
   Is this a break tag?

   @return True if the type is a break.
   */
  public boolean isBreak () {
    return (getData().isBreak());
  }

  /**
   Is this a block quote?

   @return True if the type is a block quote.
   */
  public boolean isBlockQuote () {
    return (getData().isBlockQuote());
  }

  /**
   Is this a paragraph?

   @return True if the type is a paragraph.
   */
  public boolean isParagraph () {
    return (getData().isParagraph());
  }

  /**
   Is this a heading type?

   @return True if the tag/type starts with 'h' and the second (and only other)
           character is a digit.
   */
  public boolean isHeading () {
    return (getData().isHeading());
  }

  /**
   Is this a comment type?

   @return True if the tag/type is a comment.
   */
  public boolean isComment() {
    return (getData().isComment());
  }

  /**
   Is this text without any identifying type?

   @return True if the type is null.
   */
  public boolean isNakedText () {
    return (getData().isNakedText());
  }

  /**
   Is this an italics type?

   @return True if the tag/type is italics.
   */
  public boolean isItalics () {
    return (getData().isItalics());
  }

  /**
   Is this a citation type?

   @return True if the tag/type represents a citation.
   */
  public boolean isCite () {
    return (getData().isCite());
  }

  /**
   Is this an anchor/hyperlink type?

   @return True if the tag/type represents an anchor.
   */
  public boolean isAnchor () {
    return (getData().isAnchor());
  }

  /**
   Is this an href attribute?

   @return True if this is an href attribute.
   */
  public boolean isAttributeHref () {
    return (getData().isAttributeHref());
  }

  /**
   Is this the target attribute for an anchor/hyperlink?

   @return True if this is a target attribute.
   */
  public boolean isAttributeTarget () {
    return (getData().isAttributeTarget());
  }

  /**
   Should we assume that the opening tag is also the closing tag?

   @return True if the opening tag is also the closing tag.
   */
  public boolean isSelfClosing () {
    if ((getData().isSelfClosingTag())) {
      return true;
    }
    for (int i = 0; i < this.getChildCount(); i++) {
      if (this.getTextChildAt(i).getType().equals(TextType.CLOSING)) {
        return true;
      }
    }
    return false;
  }

  /**
   Return the href attribute value for this node, if one exists.

   @return The href if one exists, otherwise return a string with zero length.
   */
  public String getAttributeHref () {
    String href = "";
    for (int i = 0; href.length() == 0 && i < this.getChildCount(); i++) {
      TextNode child = this.getTextChildAt (i);
      if (child.isAttributeHref()) {
        href = child.getText();
      }
    }
    return href;
  }

  /**
   Style is used as a euphemism for class (since Class is class name in Java).

   @param style The class of this tag.
   */
  public void setStyle (String style) {
    TextNode classNode = getChildOfType(TextType.CLASS, ATTRIBUTES_ONLY);
    if (classNode == null) {
      addAttribute (TextType.CLASS, style);
    } else {
      classNode.getData().setText(style);
    }
  }

  /**
   Does this tag already have a class attribute?

   @return True if a class attribute can be found for this tag.
   */
  public boolean hasStyle () {
    TextNode classNode = getChildOfType(TextType.CLASS, ATTRIBUTES_ONLY);
    return (classNode != null);
  }

  /**
   Returns the class associated with this tag.

   @return The class attribute value associated with this tag, if one
           exists, otherwise a zero-length string. 
   */
  public String getStyle () {
    TextNode classNode = getChildOfType(TextType.CLASS, ATTRIBUTES_ONLY);
    if (classNode == null) {
      return "";
    } else {
      return classNode.getText();
    }
  }

  /**
   Return the child node of the specified type, if it exists.
   @param type The type of child node desired.
   @param attribute A value greater than zero (+1) indicates that an attribute
                    is desired.
                    A value less than zero (-1) indicates that a tag
                    (non-attribute) is desired.
                    A value of zero indicates that we are indifferent to the
                    attribute status.
   @return The first node of the desired type, if one exists, otherwise null.
   */
  public TextNode getChildOfType (String type, int attribute) {
    TextNode child;
    for (int i = 0; i < this.getChildCount(); i++) {
      child = this.getTextChildAt(i);
      if (((child.isAttribute() && attribute >= 0)
            || (child.isTag() && attribute <= 0))
          && child.getType().equalsIgnoreCase(type)) {
        return child;
      } // end if found desired child
    } // end searching through children
    return null;
  }

  /**
   Set the flag indicating whether this data should be treated as an
   attribute.

   @param attribute True if an attribute.
   */
  public void setAttribute (boolean attribute) {
    getData().setAttribute (attribute);
  }

  /**
   Is this an attribute?

   @return True if this is an attribute.
   */
  public boolean isAttribute () {
    return getData().isAttribute();
  }

  /**
   Add a child node to this one with the given type and value.

   @param name  The name/type of the attribute.
   @param value The value of the attribute.
   */
  public void addAttribute (String name, String value) {
    if (name == null || name.length() == 0) {
      // do nothing
    } else {
      TextNode attrNode = new TextNode (tree);
      attrNode.setAttribute (true);
      attrNode.setType (name);
      attrNode.setText (value);
      this.add (attrNode);
    }
  }

  /**
   If this is not an attribute, then treat it as a tag.

   @return True if not an attribute.
   */
  public boolean isTag () {
    return getData().isTag();
  }

  /**
   Keep track of the number of children representing tags (as opposed to
   attributes). This may be useful to determine when text needs to be added
   as a separate node identified with a type of naked text.
   */
  public void incrementChildTagCount () {
    getData().incrementChildTagCount();
  }

  /**
   Find out how many non-attribute children we have. This may be useful to
   determine when text needs to be added as a separate node identified
   with a type of naked text.

   @return True if more than zero child tags.
   */
  public boolean hasChildTags () {
    return getData().hasChildTags();
  }

  /**
   Format the data as a single string.

   @return Type, followed by a colon, followed by the text.
   */
  public String toString() {
    return getData().toString();
  }

  /**
   Get the user data associated with this node.

   @return the TextData instance associated with this node.
   */
  public TextData getData () {
    TextData nodeData = (TextData)getUserObject();
    if (nodeData == null) {
      nodeData = new TextData ();
      setUserObject (nodeData);
    } 
    return nodeData;
  }

  /**
   Return this node's parent.

   @return This node's parent, as a TextNode.
   */
  public TextNode getTextParent () {
    return (TextNode)super.getParent();
  }

  /**
   Return this node's child at a specific location.

   @param index The index position of the desired child.
   @return The desired child, if one exists at the specified index.
   */
  public TextNode getTextChildAt (int index) {
    return (TextNode)getChildAt (index);
  }

  /**
   Add the specified node as a child of this one.

   @param child The node to be added. 
   */
  public void add (TextNode child) {
    super.add (child);
    if (child.isTag()) {
      incrementChildTagCount();
    }
  }

}
