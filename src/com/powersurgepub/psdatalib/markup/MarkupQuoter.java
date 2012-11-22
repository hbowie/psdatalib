/*
 * MarkupQuoter.java
 *
 * Created on March 15, 2007, 6:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.powersurgepub.psdatalib.markup;

/**
 *
 * @author hbowie
 */
public class MarkupQuoter {
  
  private   boolean                 firstText = false;
  private   MarkupElement           lastText = null;
  private   boolean                 lastParagraph = false;
  
  /** Creates a new instance of MarkupQuoter */
  public MarkupQuoter() {
  }
  
  public void setFirstText (boolean firstText) {
    this.firstText = firstText;
  }
  
  public boolean isFirstText () {
    return firstText;
  }
  
  public void setLastText (MarkupElement lastText) {
    this.lastText = lastText;
  }
  
  public MarkupElement getLastText () {
    return lastText;
  }
  
  public void setLastParagraph (boolean lastParagraph) {
    this.lastParagraph = lastParagraph;
  }
  
  public boolean isLastParagraph () {
    return lastParagraph;
  }
  
}
