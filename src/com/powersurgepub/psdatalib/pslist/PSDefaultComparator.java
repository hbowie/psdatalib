package com.powersurgepub.psdatalib.pslist;

  import java.util.*;

/**
 Everything is always equal. 

 @author Herb Bowie
 */
public class PSDefaultComparator     implements
      Comparator {
  
  public PSDefaultComparator() {
    
  }
  
  /**
   Compare the two events and return the result. This default comparator always
   determines any two objects to be equal. 
  
   @param obj1 The first item to be compared. 
   @param obj2 The second item to be compared. 
  
   @return -1 if the first item is lower than the second, 
           +1 of the first item is higher than the second, or
           zero if the two events have the same keys. 
  */
  public int compare (Object obj1, Object obj2) {
    
    int result = 0;
    return result;
  }
}
