package com.powersurgepub.psdatalib.txbmodel;

import com.powersurgepub.psutils.*;

/**
 An entry in a generated table of contents. 

 @author Herb Bowie
 */
public class TocEntry {
  
  private int           level = 0;
  private StringBuilder heading = new StringBuilder();
  private String        id = "";
  
  public TocEntry () {
    
  }
  
  public TocEntry (int level, String heading, String id) {
    this.level = level;
    this.heading.append (heading);
    this.id = id;
  }
  
  public void setLevel(int level) {
    this.level = level;
  }
  
  public int getLevel () {
    return level;
  }
  
  public void setHeading (String heading) {
    this.heading = new StringBuilder(heading);
  }
  
  public void append (String str) {
    heading.append(str);
  }
  
  public String getHeading () {
    return heading.toString();
  }
  
  public void setID (String id) {
    this.id = id;
  }
  
  public boolean hasID() {
    return (id.length() > 0);
  }
  
  public boolean lacksID() {
    return (id.length() == 0);
  }
  
  public void deriveID() {
    id = StringUtils.makeFileName(heading.toString(), false);
  }
  
  public String getID () {
    return id;
  }
  
  public String getLink() {
    return ("#" + id);
  }

}
