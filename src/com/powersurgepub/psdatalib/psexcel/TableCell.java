package com.powersurgepub.psdatalib.psexcel;

  import jxl.*; 

/**
  Creates a table cell from a cell.
 
  @author Herb Bowie
 */
public class TableCell {
  
  private Cell        cell;
  private Hyperlink   link;
  private boolean     merged = false;
  private boolean     mergedAnchor = false;
  private int         colspan = 1;
  private int         rowspan = 1;
  
  /** Creates a new instance of TableCell */
  public TableCell(Cell cell) {
    this.cell = cell;
  }
  
  public void setHyperlink (Hyperlink link) {
    this.link = link;
  }
  
  public void setMerged (boolean merged) {
    this.merged = merged;
  }
  
  public void setMergedAnchor (boolean mergedAnchor) {
    this.mergedAnchor = mergedAnchor;
    if (isMergedAnchor()) {
      setMerged (true);
    }
  }
  
  public void setColspan (int colspan) {
    this.colspan = colspan;
  }
  
  public void setRowspan (int rowspan) {
    this.rowspan = rowspan;
  }
  
  public Cell getCell () {
    return cell;
  }
  
  public Hyperlink getHyperlink () {
    return link;
  }
  
  public boolean isMerged () {
    return merged;
  }
  
  public boolean isMergedAnchor () {
    return mergedAnchor;
  }
  
  public int getColspan () {
    return colspan;
  }
  
  public int getRowspan () {
    return rowspan;
  }
  
}
