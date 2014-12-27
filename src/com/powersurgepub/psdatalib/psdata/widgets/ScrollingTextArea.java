/*
 * Copyright 2014 - 2015 Herb Bowie
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

package com.powersurgepub.psdatalib.psdata.widgets;

import com.powersurgepub.psdatalib.psdata.widgets.DataWidget;
  import javax.swing.*;

/**
 
 @author Herb Bowie
 */
public class ScrollingTextArea
    extends JScrollPane 
      implements
        DataWidget {
  
  private JTextArea textArea = new JTextArea();
  
  /**
   Constructor with all possible parameters.
  
   @param columns   Number of columns for the text area. 
   @param rows      Number of rows for the text area. 
   @param lineWrap  Should the text area wrap?
   @param wrapStyleWord Should the text area wrap at words?
  */
  public ScrollingTextArea (
      int columns,
      int rows,
      boolean lineWrap,
      boolean wrapStyleWord) {
    textArea.setColumns(columns);
    textArea.setRows(rows);
    textArea.setLineWrap(lineWrap);
    textArea.setWrapStyleWord(wrapStyleWord);
    textArea.setTabSize(2);
    this.setVerticalScrollBarPolicy
        (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    setViewportView(textArea);
  }
  
  /**
   Constructor with all possible parameters.
  
   @param columns   Number of columns for the text area. 
   @param rows      Number of rows for the text area. 
  */
  public ScrollingTextArea (
      int columns,
      int rows) {
    textArea.setColumns(columns);
    textArea.setRows(rows);
    setViewportView(textArea);
  }

  /**
   Default constructor with no 
  */
  public ScrollingTextArea() {
    textArea.setColumns(60);
    textArea.setRows(5);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    setViewportView(textArea);
  }
  
  public void setColumns(int columns) {
    textArea.setColumns(columns);
  }
  
  public void setRows(int rows) {
    textArea.setRows(rows);
  }
  
  public void setLineWrap(boolean lineWrap) {
    textArea.setLineWrap(lineWrap);
  }
  
  public void setWrapStyleWord(boolean wrapStyleWord) {
    textArea.setWrapStyleWord(wrapStyleWord);
  }
  
  public void setText(String t) {
    textArea.setText(t);
    textArea.setCaretPosition(0);
  }
  
  public String getText() {
    return textArea.getText();
  }
  
  public JTextArea getTextArea() {
    return textArea;
  }

}
