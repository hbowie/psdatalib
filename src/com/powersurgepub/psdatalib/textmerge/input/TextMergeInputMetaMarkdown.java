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

package com.powersurgepub.psdatalib.textmerge.input;

  import com.powersurgepub.psdatalib.txbio.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.txbio.*;
  import java.io.*;

/**
 A PSTextMerge input module for reading Markdown files with embedded metadata.

 @author Herb Bowie
 */
public class TextMergeInputMetaMarkdown 
    extends TextMergeInputModule {
  
  public TextMergeInputMetaMarkdown () {
    
    modifiers.add("");
    modifiers.add("mmdown");
    modifiers.add("mmdowntags");
    
    labels.add("No Markdown");
    labels.add("Markdown Metadata");
    labels.add("Markdown Metadata Tags");
  }
  
  /**
   Is this input module interested in processing the specified file?
  
   @param candidate The file being considered. 
  
   @return True if the input module thinks this file is worth processing,
           otherwise false. 
  */
  public boolean isInterestedIn(File candidate) {
    return MetaMarkdownReader.isInterestedIn(candidate);
  }
  
  /**
   Get the appropriate data source for this type of data. 
  
   @param chosenFile The file containing the data to be read.
  
   @return The desired data source.
  */
  public DataSource getDataSource(File chosenFile) {
    DataSource dataSource;
    dataSource = new MetaMarkdownReader(chosenFile, inputType);
    return dataSource;
  }

}
