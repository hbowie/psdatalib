package com.powersurgepub.psdatalib.pslist;

  import java.util.*;

/**
  A pointer to a PSItem. 

  @author Herb Bowie
 */
public class PSItemProxy {
  
  private int itemIndex = -1;
  
  public PSItemProxy() {
    
  }
  
  public PSItemProxy(int itemIndex) {
    this.itemIndex = itemIndex;
  }
  
  public void setItemIndex(int itemIndex) {
    this.itemIndex = itemIndex;
  }
  
  public int getItemIndex() {
    return itemIndex;
  }
  
  public PSItem getItem(List<PSItem> list) {
    if (itemIndex < 0 || itemIndex >= list.size()) {
      return null;
    } else {
      return list.get(itemIndex);
    }
  }

}
