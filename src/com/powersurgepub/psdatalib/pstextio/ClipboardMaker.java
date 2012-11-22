package com.powersurgepub.psdatalib.pstextio;

  import java.awt.*;
  import java.awt.datatransfer.*;
  import java.io.*;

/**
  Writes text lines to the System clipboard. Implements TextLineWriter.
 */
public class ClipboardMaker 
    implements ClipboardOwner, TextLineWriter {
  
  private StringMaker maker = new StringMaker();
  
  // System ClipBoard fields
  boolean             clipBoardOwned = false;
  Clipboard           clipBoard = null;
  Transferable        clipContents = null;
  
  /** Creates a new instance of StringMaker */
  public ClipboardMaker() {

  }
  
  public void lostOwnership (Clipboard clipBoard, Transferable contents) {
    clipBoardOwned = false;
  }
  
  public boolean openForOutput () {
    return maker.openForOutput();
  }
  
  public boolean writeLine (String s) {
    return maker.writeLine (s);
  }
  
  public boolean write (String s) {
    return maker.write(s);
  }
  
  public boolean newLine () {
    return maker.newLine();
  }
  
  public boolean flush () {
    return maker.flush();
  }
  
  public boolean close () {
    boolean ok = maker.close();
    StringSelection block = new StringSelection (maker.toString());
    clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipBoard.setContents(block, this);
    return ok;
  }
  
  public boolean isOK () {
    return maker.isOK();
  }
  
  public File getFile () {
    return null;
  }
  
  public String getDestination () {
    return ("System Clipboard");
  }
  
  public String toString () {
    return maker.toString();
  }
  
}
