package com.powersurgepub.psdatalib.pstextio;

  import com.powersurgepub.psutils.*;

/**
 Implements the TextLineReader interface with an input String.

 @author Herb Bowie
 */
public class StringLineReader 
    implements TextLineReader {
  
  public static final String HTML_EOL = "<!--EOL-->";
  
  private             String s;
  private             int i           = 0;
  
  /**
   Constructor.
  
   @param s The string to be used as input. 
  */
  public StringLineReader(String s) {
    this.s = s;
  }
 
  /**
   Ready the input source to be read. 
   
   @return 
  */
  public boolean open () {
    i = 0;
    return true;
  }
  
  /**
   Read the next line from the input String. End of line characters or Strings
   are not returned. Line endings are denoted by a Line Feed or Carriage Return
   (each optionally followed by the other), or an HTML comment enclosing an
   EOL marker: <!--EOL-->.
   
   @return The next line, or null if end of file.
  */
  public String readLine () {
    int start = i;
    while (i < s.length()
        && s.charAt(i) != GlobalConstants.CARRIAGE_RETURN
        && s.charAt(i) != GlobalConstants.LINE_FEED
        && (! match(s, i, HTML_EOL))) {
      i++;
    }
    int end = i;
    
    if (i >= s.length()) {
      // No need to further adjust index
    }
    else
    if (s.charAt(i) == GlobalConstants.CARRIAGE_RETURN) {
      i++;
      if (match(s, i, GlobalConstants.LINE_FEED_STRING)) {
        i++;
      }
    }
    else
    if (s.charAt(i) == GlobalConstants.LINE_FEED) {
      i++;
      if (match(s, i, GlobalConstants.CARRIAGE_RETURN_STRING)) {
        i++;
      }
    } else {
      i = i + HTML_EOL.length();
    }
    if (start >= s.length()) {
      return null;
    } else {
      return s.substring(start, end);
    }
  }
  
  private boolean match (String s, int i, String sMatch) {
    int j = i + sMatch.length();
    if (i >= s.length() || j > s.length()) {
      return false;
    } else {
      return (s.substring(i, j).equalsIgnoreCase(sMatch));
    }
  }
  
  public boolean close() {
    return true;
  }
  
  public boolean isOK () {
    return true;
  }
  
  public boolean isAtEnd() {
    return (i >= s.length());
  }

}
