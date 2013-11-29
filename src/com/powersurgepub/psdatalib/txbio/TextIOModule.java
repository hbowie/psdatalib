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

package com.powersurgepub.psdatalib.txbio;

  import com.powersurgepub.psdatalib.txbmodel.*;
  import com.powersurgepub.pstextio.TextWriter;
  import java.net.*;
  import java.util.*;
  import org.xml.sax.*;
  import org.xml.sax.helpers.*;

/**
 This abstract class defines the basics for any module that will perform
 input and/or output functions for TextBlocs. 
 */
public abstract class TextIOModule
    extends DefaultHandler {
  
  protected     TextTree              tree;
  protected     TextNode              currentNode;
  
  public abstract void registerTypes (List types);
  
  public abstract boolean load  (TextTree tree, URL url, TextIOType type, String parm);
  
  public abstract boolean store 
      (TextTree tree, TextWriter writer, TextIOType type, 
       boolean epub, String epubSite);

}
