/*
 * Copyright 2016 - 2016 Herb Bowie
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

package com.powersurgepub.psdatalib.markup;

  import org.pegdown.*;

/**
 Converts markdown text to HTML. 

 @author Herb Bowie
 */
public class MdToHTML {
  
  private static MdToHTML mdToHTML = null;
  
  private    PegDownProcessor   pegDown;
  
  public static MdToHTML getShared() {
    if (mdToHTML == null) {
      mdToHTML = new MdToHTML();
    }
    return mdToHTML;
  }
  
  public MdToHTML() {
    int pegDownOptions = 0;
    pegDownOptions = pegDownOptions + Extensions.SMARTYPANTS;
    pegDownOptions = pegDownOptions + Extensions.DEFINITIONS;
    pegDown = new PegDownProcessor(pegDownOptions);
  }
  
  public String markdownToHtml(String md) {
    return pegDown.markdownToHtml(md);
  }

}
