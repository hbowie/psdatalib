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

package com.powersurgepub.psdatalib.textmerge;

/**
 The interface for an application calling the text merge modules. 

 @author Herb Bowie
 */
public interface TextMergeController {
  
  /**
   Indicate whether or not a list has been loaded. 
  
   @param listAvailable True if a list has been loaded, false if the list
                        is not available. 
  */
  public void setListAvailable (boolean listAvailable);
  
  /**
   Indicate whether or not a list has been loaded. 
  
   @return True if a list has been loaded, false if the list is not 
           available. 
  */
  public boolean isListAvailable();
  
}
