package com.powersurgepub.psdatalib.script;

  import com.powersurgepub.psutils.*;
  import java.io.*;
  import javax.swing.*;

/**
  Module to record script actions. 

  @author Herb Bowie
 */
public class ScriptRecorder {
  
	private     boolean             scriptRecording = false;
  
	private     ScriptFile          outScript;
  private     File                outScriptFile;
  
	private     ScriptAction        outAction;
  
  private     JTextArea           scriptText;
  
  public ScriptRecorder() {
    
  }
  
	public void recordScriptAction (String module, String action, String modifier, 
	    String object, String value) {
	  if (scriptRecording) {
  	  outAction = new ScriptAction (module, action, modifier, object, value);
  	  outScript.nextRecordOut (outAction);
  	  scriptText.append (outAction.toString() + GlobalConstants.LINE_FEED_STRING);
	  }
	} // end recordScriptAction method

}
