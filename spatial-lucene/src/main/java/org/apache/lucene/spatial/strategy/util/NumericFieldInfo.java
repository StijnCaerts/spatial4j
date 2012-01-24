/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.spatial.strategy.util;

import org.apache.lucene.analysis.NumericTokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexableField;

/**
 * Hold some of the parameters used by solr...
 */
public class NumericFieldInfo {
  public int precisionStep = 8; // same as solr default
  public boolean store = true;
  public boolean index = true;
  public boolean omitNorms = true;
  public boolean omitTF = true;

  public void setPrecisionStep( int p ) {
    precisionStep = p;
    if (precisionStep<=0 || precisionStep>=64)
      precisionStep=Integer.MAX_VALUE;
  }

  public IndexableField createDouble( String name, double v ) {
    FieldType fieldType = new FieldType();
    fieldType.setStored(store);
    fieldType.setOmitNorms(omitNorms);
    fieldType.setIndexed(index);
    fieldType.setTokenized(index);

    if(store) {
      Field f = new Field(name, v, fieldType);
      if(index) {
        NumericTokenStream ts = new NumericTokenStream(precisionStep);
        ts.setDoubleValue(v);
        f.setTokenStream(ts);
      }
      
      return f;
    }
    
    if(index) {
      NumericTokenStream ts = new NumericTokenStream(precisionStep);
      ts.setDoubleValue(v);
      return new Field(name,ts,fieldType);
    }

    throw new IllegalArgumentException("field must be indexed or stored");
  }
}