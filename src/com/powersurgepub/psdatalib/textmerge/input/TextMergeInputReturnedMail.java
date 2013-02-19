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

  import com.powersurgepub.psdatalib.psdata.DataSource;
import com.powersurgepub.psdatalib.psdata.ReturnedMailReader;
  import java.io.*;

/**
 A PSTextMerge input module for reading Returned Mail files.

 @author Herb Bowie
 */
public class TextMergeInputReturnedMail 
    extends TextMergeInputModule {
  
  public TextMergeInputReturnedMail () {
    
    modifiers.add("");
    modifiers.add("returnedmail");
    
    labels.add("No Returned Mail");
    labels.add("Returned Mail");

  }
  
    /**
   Get the appropriate data source for this type of data. 
  
   @param chosenFile The file containing the data to be read.
  
   @return The desired data source.
  */
  public DataSource getDataSource(File chosenFile) {
    DataSource dataSource = new ReturnedMailReader (chosenFile);
    return dataSource;
  }

}
