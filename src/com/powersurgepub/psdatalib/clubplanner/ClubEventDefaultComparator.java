package com.powersurgepub.psdatalib.clubplanner;

  import java.util.*;

/**
  The default comparator for club events. 

  @author Herb Bowie.
 */
public class ClubEventDefaultComparator 
    implements
      Comparator {
  
  private final static String eventClassName
      = "com.powersurgepub.psdatalib.clubplanner.ClubEvent";
  
  public ClubEventDefaultComparator() {
    
  }
  
  /**
   Compare the two events and return the result. 
  
   @param event1 The first club event to be compared. 
   @param event2 The second club event to be compared. 
  
   @return -1 if the first event is lower than the second, 
           +1 of the first event is higher than the second, or
           zero if the two events have the same keys. 
  */
  public int compare (Object obj1, Object obj2) {
    
    int result = 0;
    // System.out.println ("ClubEventDefaultComparator compare");
    if (! obj1.getClass().getName().equals(eventClassName)) {
      result = 1;
      // System.out.println ("Object 1 not a club event: " + obj1.getClass().getName());
    } 
    else
    if (! obj2.getClass().getName().equals(eventClassName)) {
      result = -1;
      // System.out.println ("Object 2 not a club event: "+ obj2.getClass().getName());
    }
    
    if (result == 0) {
      ClubEvent event1 = (ClubEvent)obj1;
      ClubEvent event2 = (ClubEvent)obj2;
      result = event1.getSeqAsString().compareTo(event2.getSeqAsString());
        // System.out.println ("Comparing Seq fields " 
        //     + event1.getSeqAsString() + " to " + event2.getSeqAsString()
        //     + " result = " + String.valueOf(result));
      if (result == 0) {
        result = event1.getYmdAsString().compareTo(event2.getYmdAsString());
        // System.out.println ("Comparing YMD fields " 
        //     + event1.getYmdAsString() + " to " + event2.getYmdAsString()
        //     + " result = " + String.valueOf(result));
      }
      if (result == 0) {
        result = event1.getTypeAsString().compareTo(event2.getTypeAsString());
        // System.out.println ("Comparing types " 
        //     + event1.getTypeAsString() + " to " + event2.getTypeAsString()
        //     + " result = " + String.valueOf(result));
      }
      if (result == 0) {
        result = event1.getWhatAsString().compareTo(event2.getWhatAsString());
        // System.out.println ("Comparing what fields " 
        //     + event1.getWhatAsString() + " to " + event2.getWhatAsString()
        //     + " result = " + String.valueOf(result));
      }
    } 
    return result;
  }

}
