package com.powersurgepub.psdatalib.txbio;

  import com.powersurgepub.psdatalib.pstextio.TextLineWriter;
  import java.io.*;

/**
  An interface for writing nodes to an output data store. 

  @author hbowie
 */
public interface NodeWriter {
  
  /**
  Set the file to be used for output.
  
  @param file The file to be written to. 
  */
  public void setFile(File file);
  
  public String getFileExt();
  
  public TextLineWriter getTextLineWriter();
  
  public boolean openForOutput();
  
  public boolean writeComment(String comment);
  
  public boolean writeNode(String name, String data);
  
  public boolean writeAttribute(String name, String data);
  
  public boolean startNodeOut(String name);
  
  public boolean startNodeOut(String name, boolean isAttribute);
  
  public boolean writeData(String name);
  
  public boolean writeData(String data, boolean isAttribute);
  
  public boolean endNodeOut(String name);
  
  public boolean endNodeOut(String name, boolean isAttribute);
  
  public boolean close();

  
}
