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

package com.powersurgepub.psdatalib.pstextio;

/**
 The most basic interface for reading lines from somewhere.

  <p>The following classes implement this interface. </p>

 <ul>
   <li>FileLineReader - Reads lines from a file. </li>
 </ul>
 */
public interface TextLineReader {
  
  public boolean open ();
  
  public String readLine ();
  
  public boolean close();
  
  public boolean isOK ();
  
  public boolean isAtEnd();
  
  /**
   Return the file path or URL in the form of a string.
  
   @return The file path or URL. 
  */
  public String toString();

}
