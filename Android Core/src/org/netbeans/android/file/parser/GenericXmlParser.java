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

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

public class GenericXmlParser {

    public XMLReader parseXML(KXmlParser parser,
            boolean ignoreWhitespaces) throws Exception {
        parser.next();

        return _parse(parser, ignoreWhitespaces);
    }

    private XMLReader _parse(KXmlParser parser, boolean ignoreWhitespaces) throws Exception {
        XMLReader node = new XMLReader(XMLReader.ELEMENT_NODE);

        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new Exception("Illegal XML state: "
                    + parser.getName() + ", " + parser.getEventType());
        } else {
            node.setName(parser.getName());

            for (int i = 0; i < parser.getAttributeCount(); i++) {
                node.setAttribute(parser.getAttributeName(i), parser.getAttributeValue(i));
            }

            parser.next();

            while (parser.getEventType() != XmlPullParser.END_TAG) {
                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    node.addChild(_parse(parser, ignoreWhitespaces));
                } else if (parser.getEventType() == XmlPullParser.TEXT) {
                    if (!ignoreWhitespaces || !parser.isWhitespace()) {
                        XMLReader child = new XMLReader(XMLReader.TEXT_NODE);

                        child.setValue(parser.getText());

                        node.addChild(child);
                    }
                }
                parser.next();
            }
        }
        return node;
    }
}
