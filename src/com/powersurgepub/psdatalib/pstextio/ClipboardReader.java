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

package com.powersurgepub.psdatalib.pstextio;

  import com.powersurgepub.psutils.*;
  import java.awt.*;
  import java.awt.datatransfer.*;
  import java.io.*;

/**
 Implements the TextLineReader interface with input from the system clipboard.

 @author Herb Bowie
 */
public class ClipboardReader 
    implements 
      ClipboardOwner, 
      TextLineReader {
  
  private             StringLineReader    reader = null;
  
  private             boolean             ok = true;
  
  private             boolean             clipBoardOwned = false;
  private             Clipboard           clipBoard = null;
  private             Transferable        clipContents = null;
  
  /**
   Constructor.
  
   @param s The string to be used as input. 
  */
  public ClipboardReader() {
    reader = new StringLineReader("");
    clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable contents = clipBoard.getContents(null);
    boolean hasTransferableText = (contents != null) 
        && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
    if (hasTransferableText) {
      try {
        reader = new StringLineReader
            ((String)contents.getTransferData(DataFlavor.stringFlavor));
      } // end trying to get text from clipboard
      catch (UnsupportedFlavorException ex){
        //highly unlikely since we are using a standard DataFlavor
        ok = false;
        Trouble.getShared().report ("Trouble getting data from Clipboard",
            "Clipboard Problem");
      }
      catch (IOException ex) {
        ok = false;
        Trouble.getShared().report ("Trouble getting data from Clipboard",
            "Clipboard Problem");
      } // end catch io exception
    }
  }
  
  public void lostOwnership (Clipboard clipBoard, Transferable contents) {
    clipBoardOwned = false;
  }
 
  /**
   Ready the input source to be read. 
   
   @return 
  */
  public boolean open () {
    return reader.open();
  }
  
  /**
   Read the next line from the input String. End of line characters or Strings
   are not returned. Line endings are denoted by a Line Feed or Carriage Return
   (each optionally followed by the other), or an HTML comment enclosing an
   EOL marker: <!--EOL-->.
   
   @return The next line, or null if end of file.
  */
  public String readLine () {
    return reader.readLine();
  }
  
  public boolean close() {
    return reader.close();
  }
  
  public boolean isOK () {
    return (ok && reader.isOK());
  }
  
  public boolean isAtEnd() {
    return reader.isAtEnd();
  }
  

}
