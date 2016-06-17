/**
 * Copyright [2015] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.android.file.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XMLReader {

    public static final int TEXT_NODE = 0;
    public static final int ELEMENT_NODE = 1;

    private int nodeType = 0;
    private String name = null;
    private String value = null;
    private List<XMLReader> children = null;
    public Map<String, String> attributes = null;

    public XMLReader(int nodeType) {
        this.nodeType = nodeType;
        this.children = new ArrayList<>();
        this.attributes = new HashMap<>();
    }

    public String[] getAttributeNames() {
        String[] names = new String[attributes.size()];

        Set<String> e = attributes.keySet();

        int i = 0;
        Iterator<String> itr = e.iterator();
        while (itr.hasNext()) {
            names[i] = itr.next();

            i++;
        }
        return names;
    }

    public void setAttribute(String key, String value) {
        if (key.lastIndexOf(":") != -1) {
            key = key.substring(key.lastIndexOf(":") + 1);
        }
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public void addChild(XMLReader child) {
        this.getChildren().add(child);
    }

    /**
     * @return the children
     */
    public List<XMLReader> getChildren() {
        return children;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
