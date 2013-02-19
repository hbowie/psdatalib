/*
 * MarkupTagWriter.java
 *
 * Created on March 2, 2007, 5:59 PM
 *
 * An interface for writing markup. 
 */

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

package com.powersurgepub.psdatalib.markup;

  import org.xml.sax.*;
  import org.xml.sax.helpers.*;

/**
 *
 * @author hbowie
 */
public interface MarkupTagWriter {
  
  public void writeStartTag (
      String namespaceURI,
      String localName,
      String qualifiedName,
      Attributes attributes,
      boolean emptyTag);
  
  public void writeContent (
      String s);
  
  public void writeEndTag (
      String namespaceURI,
      String localName,
      String qualifiedName);
  
}

