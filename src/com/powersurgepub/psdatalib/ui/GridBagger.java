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

package com.powersurgepub.psdatalib.ui;

  import java.awt.*;
  import javax.swing.*;
           
/**
   A collection of methods that make it easier to add GUI components
   when using GridBagLayout. <p>
     
   This code is copyright (c) 2000 by Herb Bowie of PowerSurge Publishing. 
   All rights reserved. <p>
   
   Version History: <ul><li>
      2000/12/10 - Initially written for TDFCzar project.<li>
      </ul>
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing (<A href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
   @version 2000/12/15 - Added ability to work by columns instead of rows.
 */
public class GridBagger {

  /** One set of constraints is used for all layouts. */
  private   GridBagConstraints    constraints = new GridBagConstraints();
  
  /** Current Container to which components are being added. */
  private   Container             container;
  
  /** Proceed by row first or by column? */
  private   boolean               byRows = true;
  
  /** Number of columns in current container. */
  private   int                   columns;
  
  /** Number of rows in current container. */
  private   int                   rows;
  
  /** Next column into which a component is to be placed. */
  private   int                   column;
  
  /** Next row into which a component is to be placed. */
  private   int                   row;
  
  /** Next left inset to be used. */
  private   int                   nextLeftInset = 0;
  
  /** Next right inset to be used. */
  private   int                   nextRightInset = 0;
  
  /** Next Top inset to be used. */
  private   int                   nextTopInset = 0;
  
  /** Next Bottom inset to be used. */
  private   int                   nextBottomInset = 0;
  
  /** Last left inset that was used. */
  private   int                   lastLeftInset = 0;
  
  /** Last right inset that was used. */
  private   int                   lastRightInset = 0;
  
  /** Last Top inset that was used. */
  private   int                   lastTopInset = 0;
  
  /** Last Bottom inset that was used. */
  private   int                   lastBottomInset = 0;
  
  /** Insets object to use for layout. */
  private   Insets                insets = new Insets (0, 0, 0, 0);
  
  /** Width to use. */
  private   int                   width = 1;
  
  /** Height to use. */
  private   int                   height = 1;
  
  /** Default column weight to use. */
  private   double                defaultColumnWeight = 0.5;
  
  /** Default row weight to use. */
  private   double                defaultRowWeight = 0.5;
  
  /** X weight to use. */
  private   double                columnWeight = 0.5;
  
  /** Y weight to use. */
  private   double                rowWeight = 0.5;
  
  /** Fill value to use. */
  private   int                   fill = GridBagConstraints.BOTH;
  
  /** Anchor value to use. */
  private   int                   anchor = GridBagConstraints.CENTER;
  
  /** Layout manager to use. */
  private   GridBagLayout         layout;
    
  /**
     Constructor doesn't really do anything.
   */
  public GridBagger () {

  } // end constructor
  
  /**
     Begins layout of a new visual grid. Resets default column and row weights to
     0.5. Resets to construct layout one row at a time, from top to bottom. 
    
     @param  container Container into which components are to be 
                       placed, using GridBagLayout.
    
     @param  columns   Number of columns in grid.
    
     @param  rows      Number of rows in grid.
   */
  public void startLayout (Container container, 
      int columns, int rows) {
    this.container = container;
    this.columns = columns;
    this.rows = rows;
    column = 0;
    row = 0;
    layout = new GridBagLayout();
    container.setLayout (layout);
    fill = GridBagConstraints.BOTH;
    anchor = GridBagConstraints.CENTER;
    setDefaultColumnWeight (0.5);
    setDefaultRowWeight (0.5);
    setByRows (true);
  }
  
  /**
     Determines whether rows will be filled first or columns.
      
     @param  byRows  True if by rows first. 
   */
  public void setByRows (boolean byRows) {    
    this.byRows  = byRows;
  }
  
  /**
     Sets the next value to be used for all insets.
      
     @param  all  Next value to be used for all insets. 
   */
  public void setAllInsets (int all) {    
    nextLeftInset = all;
    nextRightInset = all;
    nextTopInset = all;
    nextBottomInset = all;
  }

  /**
     Sets the next value to be used for both left and right insets.
      
     @param  leftRight  Next value to be used for both left and right insets. 
   */
  public void setLeftRightInsets (int leftRight) {    
    nextLeftInset = leftRight;
    nextRightInset = leftRight;
  }
  
  /**
     Sets the next value to be used for both top and bottom insets.
      
     @param  topBottom  Next value to be used for both top and bottom insets. 
   */
  public void setTopBottomInsets (int topBottom) {    
    nextTopInset = topBottom;
    nextBottomInset = topBottom;
  }
  
  /**
     Sets the next value to be used for the top inset.
      
     @param  i  Next value to be used for the top inset. 
   */
  public void setTopInset (int i) {    
    nextTopInset = i;
  }
  
  /**
     Sets the next value to be used for the bottom inset.
      
     @param  i  Next value to be used for the bottom inset. 
   */
  public void setBottomInset (int i) {    
    nextBottomInset = i;
  }
  
  /**
     Sets the next value to be used for the left inset.
      
     @param  i  Next value to be used for the left inset. 
   */
  public void setLeftInset (int i) {    
    nextLeftInset = i;
  }
  
  /**
     Sets the next value to be used for the right inset.
      
     @param  i  Next value to be used for the right inset. 
   */
  public void setRightInset (int i) {    
    nextRightInset = i;
  }
  
  /**
     Sets the x (column) position to use for next component. 
    
     @param column Next column (x) position to use.
   */
  public void setColumn (int column) {
    this.column = column;
  }
  
  /**
     Sets the y (row) position to use for next component. 
    
     @param row Next row (y) position to use.
   */
  public void setRow (int row) {
    this.row = row;
  }
  
  /**
     Sets the width to use for next component. 
    
     @param width Next width to use.
   */
  public void setWidth (int width) {
    this.width = width;
  }
  
  /**
     Sets the height to use for next component. 
    
     @param height Next height to use.
   */
  public void setHeight (int height) {
    this.height = height;
  }
  
  /** 
     Sets the column (x) weight to use by default for all following
     components.
    
     @param defaultColumnWeight Default x weight for following components.
   */
  public void setDefaultColumnWeight (double defaultColumnWeight) {
    this.defaultColumnWeight = defaultColumnWeight;
    columnWeight = defaultColumnWeight;
  }
  
  /** 
     Sets the row (y) weight to use by default for all following
     components.
    
     @param defaultRowWeight Default y weight for following components.
   */
  public void setDefaultRowWeight (double defaultRowWeight) {
    this.defaultRowWeight = defaultRowWeight;
    rowWeight = defaultRowWeight;
  }
  
  /** 
     Sets the column (x) weight to use for the next
     component only.
    
     @param columnWeight X weight for next component.
   */
  public void setColumnWeight (double columnWeight) {
    this.columnWeight = columnWeight;
  }
  
  /** 
     Sets the row (y) weight to use for the next
     component only.
    
     @param rowWeight Y weight for next component.
   */
  public void setRowWeight (double rowWeight) {
    this.rowWeight = rowWeight;
  }
  
  /**
     Sets the fill value to use for next and following components.
    
     @param fill Value to use as fill for constraints.
   */
  public void setFill (int fill) {
    this.fill = fill;
  }
  
  /**
     Sets the anchor value to use for next and following components.
    
     @param anchor Value to use as anchor for constraints.
   */
  public void setAnchor (int anchor) {
    this.anchor = anchor;
  }
  
  /**
     Adds a component to the container, using the current values for
     constraints, and performing some automatic calculations to prepare
     for the next component. Specifically, the next x and y values are 
     calculated assuming that components will be added starting at the
     0, 0 coordinate and proceeding left to right and top to bottom.
    
     @param component Component to be added.
   */
  public void add (Component component) {
    if ((nextLeftInset != lastLeftInset)
      || (nextRightInset != lastRightInset)
      || (nextTopInset != lastTopInset)
      || (nextBottomInset != lastBottomInset)) {
      insets = new Insets (nextTopInset, nextLeftInset, 
        nextBottomInset, nextRightInset);
      lastLeftInset = nextLeftInset;
      lastRightInset = nextRightInset;
      lastTopInset = nextTopInset;
      lastBottomInset = nextBottomInset;
    }
    constraints.insets = insets;
    constraints.gridx = column;
    constraints.gridy = row;
    constraints.gridwidth = width;
    constraints.gridheight = height;
    constraints.weightx = columnWeight;
    constraints.weighty = rowWeight;
    constraints.fill = fill;
    constraints.anchor = anchor;
    layout.setConstraints (component, constraints);
    container.add (component);
    if (byRows) {
      column += width;
      if (column >= columns) {
        nextRow();
      } 
    } else { // by columns
      row += height;
      if (row >= rows) {
        nextColumn();
      }
    }
    width = 1;
    height = 1;
    columnWeight = defaultColumnWeight;
    rowWeight = defaultRowWeight;
  } // end add method
  
  /**
     Bumps pointer to next column.
   */
  public void nextColumn () {
    if (row > 0) {
      column++;
      row = 0;
    }
  }
  
  /**
     Bumps pointer to next row.
   */
  public void nextRow () {
    if (column > 0) {
      row++;
      column = 0;
    }
  }
  
} // end of class GridBagger
