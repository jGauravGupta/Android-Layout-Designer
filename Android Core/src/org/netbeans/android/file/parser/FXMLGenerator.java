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

import static com.android.SdkConstants.ATTR_BACKGROUND;
import static com.android.SdkConstants.ATTR_HINT;
import static com.android.SdkConstants.ATTR_ID;
import static com.android.SdkConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_RIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.SdkConstants.ATTR_LAYOUT_WIDTH;
import static com.android.SdkConstants.ATTR_ORIENTATION;
import static com.android.SdkConstants.ATTR_PADDING_BOTTOM;
import static com.android.SdkConstants.ATTR_PADDING_LEFT;
import static com.android.SdkConstants.ATTR_PADDING_RIGHT;
import static com.android.SdkConstants.ATTR_PADDING_TOP;
import static com.android.SdkConstants.ATTR_TEXT;
import static com.android.SdkConstants.BUTTON;
import static com.android.SdkConstants.DRAWABLE_HDPI;
import static com.android.SdkConstants.DRAWABLE_LDPI;
import static com.android.SdkConstants.DRAWABLE_MDPI;
import static com.android.SdkConstants.DRAWABLE_PREFIX;
import static com.android.SdkConstants.DRAWABLE_XHDPI;
import static com.android.SdkConstants.EDIT_TEXT;
import static com.android.SdkConstants.LINEAR_LAYOUT;
import static com.android.SdkConstants.RES_FOLDER;
import static com.android.SdkConstants.TEXT_VIEW;
import static com.android.SdkConstants.VALUE_HORIZONTAL;
import static com.android.SdkConstants.VALUE_VERTICAL;
import com.oracle.javafx.scenebuilder.kit.fxom.glue.GlueDocument;
import com.oracle.javafx.scenebuilder.kit.fxom.glue.GlueElement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import org.kxml2.io.KXmlParser;
import org.netbeans.android.designer.component.draw.Button;
import org.netbeans.android.designer.component.draw.ComponentUtil;
import org.netbeans.android.designer.component.draw.EditText;
import org.netbeans.android.designer.component.draw.HorizontalLinearLayout;
import org.netbeans.android.designer.component.draw.TextView;
import org.netbeans.android.designer.component.draw.VerticalLinearLayout;
import org.netbeans.jfluidon.file.IFluidonFile;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.xmlpull.v1.XmlPullParserException;

/**
 *
 * @author Gaurav Gupta
 */
public class FXMLGenerator {

    public static File generate(IFluidonFile fluidonFile) {
        File fxmlFile = null;
        try {
            fxmlFile = File.createTempFile("AND_", "fxml");
            fxmlFile.deleteOnExit();

            KXmlParser parser = new KXmlParser();
            parser.setInput(new FileReader(fluidonFile.getFile()));
            GenericXmlParser gParser = new GenericXmlParser();
            XMLReader reader = gParser.parseXML(parser, true);

//            File subDirectory = new File(fxmlFile.getParent() + File.separator + fluidonFile.getId());
//            subDirectory.createNewFile();
            GlueDocument document = new GlueDocument();
            parseLayoutXml(reader, null, null, document, fluidonFile);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fxmlFile))) {
                writer.write(document.toString());
            }

        } catch (FileNotFoundException | XmlPullParserException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return fxmlFile;
    }

    private static void parseLayoutXml(XMLReader reader, GlueElement parentElement, GlueElement containerElement, GlueDocument document, IFluidonFile fluidonFile) {
        GlueElement element = null;
        if (reader.getName().equals(LINEAR_LAYOUT)) {
            switch (reader.getAttribute(ATTR_ORIENTATION)) {
                case VALUE_VERTICAL:
                    element = new GlueElement(document, VerticalLinearLayout.class.getCanonicalName());
                    element.getAttributes().put("fillWidth", "false");
                    parseLinearLayout(reader, element, parentElement, containerElement, document, fluidonFile);
                    break;
                case VALUE_HORIZONTAL:
                    element = new GlueElement(document, HorizontalLinearLayout.class.getCanonicalName());
                    element.getAttributes().put("fillHeight", "false");
                    parseLinearLayout(reader, element, parentElement, containerElement, document, fluidonFile);
                    break;
            }
        } else if (reader.getName().equals(EDIT_TEXT)) {
            element = new GlueElement(document, EditText.class.getCanonicalName());
            parseResourceId(reader, element);
            parsePromptText(reader, element);
            parseText(reader, element);
            parseDimension(reader, element);
            parsePadding(reader, element, document);
            parseMargin(reader, element, parentElement, document);
        } else if (reader.getName().equals(BUTTON)) {
            element = new GlueElement(document, Button.class.getCanonicalName());
            parseResourceId(reader, element);
            parseText(reader, element);
            parseDimension(reader, element);
            parsePadding(reader, element, "resourcePadding", document);
            parseMargin(reader, element, parentElement, document);
        } else if (reader.getName().equals(TEXT_VIEW)) {
            element = new GlueElement(document, TextView.class.getCanonicalName());
            element.getAttributes().put("wrapText", "true");
            parseResourceId(reader, element);
            parseText(reader, element);
            parseDimension(reader, element);
            parsePadding(reader, element, document);
            parseMargin(reader, element, parentElement, document);
        }

        if (containerElement != null) {
            containerElement.getChildren().add(element);
        } else {
            document.setRootElement(element);
        }

    }

    private static void parseDimension(XMLReader reader, GlueElement element) {
        element.getAttributes().put("resourceWidth", reader.getAttribute(ATTR_LAYOUT_WIDTH));
        element.getAttributes().put("resourceHeight", reader.getAttribute(ATTR_LAYOUT_HEIGHT));
        //element.getAttributes().put("prefWidth", Double.toString(ComponentUtil.getPX(reader.getAttribute(ATTR_LAYOUT_WIDTH))));
        //element.getAttributes().put("prefHeight", Double.toString(ComponentUtil.getPX(reader.getAttribute(ATTR_LAYOUT_HEIGHT))));
    }

    private static void parseResourceId(XMLReader reader, GlueElement element) {
        if (reader.getAttribute(ATTR_ID) != null) {
            element.getAttributes().put("resourceId", ComponentUtil.getFXResourceId(reader.getAttribute(ATTR_ID)));
        }
    }

    private static void parseText(XMLReader reader, GlueElement element) {
        if (reader.getAttribute(ATTR_TEXT) != null) {
            element.getAttributes().put("text", reader.getAttribute(ATTR_TEXT));
        }
    }

    private static void parsePromptText(XMLReader reader, GlueElement element) {
        if (reader.getAttribute(ATTR_HINT) != null) {
            element.getAttributes().put("promptText", reader.getAttribute(ATTR_HINT));
        }
    }

    private static void parsePadding(XMLReader reader, GlueElement element, GlueDocument document) {
        parsePadding(reader, element, null, document);
    }

    private static void parsePadding(XMLReader reader, GlueElement element, String wrapper, GlueDocument document) {

        if (reader.getAttribute(ATTR_PADDING_TOP) != null || reader.getAttribute(ATTR_PADDING_LEFT) != null || reader.getAttribute(ATTR_PADDING_RIGHT) != null || reader.getAttribute(ATTR_PADDING_BOTTOM) != null) {
            GlueElement element_padding;
            if (wrapper == null) {
                element_padding = new GlueElement(document, "padding");
            } else {
                element_padding = new GlueElement(document, wrapper);
            }
            element.getChildren().add(element_padding);
            GlueElement element_Insets = new GlueElement(document, Insets.class.getCanonicalName());
            element_padding.getChildren().add(element_Insets);

            if (reader.getAttribute(ATTR_PADDING_TOP) != null) {
                element_Insets.getAttributes().put("top", Double.toString(ComponentUtil.getPX(reader.getAttribute(ATTR_PADDING_TOP))));
            }
            if (reader.getAttribute(ATTR_PADDING_LEFT) != null) {
                element_Insets.getAttributes().put("left", Double.toString(ComponentUtil.getPX(reader.getAttribute(ATTR_PADDING_LEFT))));
            }
            if (reader.getAttribute(ATTR_PADDING_RIGHT) != null) {
                element_Insets.getAttributes().put("right", Double.toString(ComponentUtil.getPX(reader.getAttribute(ATTR_PADDING_RIGHT))));
            }
            if (reader.getAttribute(ATTR_PADDING_BOTTOM) != null) {
                element_Insets.getAttributes().put("bottom", Double.toString(ComponentUtil.getPX(reader.getAttribute(ATTR_PADDING_BOTTOM))));
            }

        }
    }

    private static void parseMargin(XMLReader reader, GlueElement element, GlueElement parentElement, GlueDocument document) {
        //margin not required in top element
        if (reader.getAttribute(ATTR_LAYOUT_MARGIN_TOP) != null || reader.getAttribute(ATTR_LAYOUT_MARGIN_LEFT) != null || reader.getAttribute(ATTR_LAYOUT_MARGIN_RIGHT) != null || reader.getAttribute(ATTR_LAYOUT_MARGIN_BOTTOM) != null) {
            String marginAttrClass;
            if (parentElement.getTagName().equals(VerticalLinearLayout.class.getCanonicalName())) {
                marginAttrClass = VerticalLinearLayout.class.getSuperclass().getCanonicalName();
            } else if (parentElement.getTagName().equals(HorizontalLinearLayout.class.getCanonicalName())) {
                marginAttrClass = HorizontalLinearLayout.class.getSuperclass().getCanonicalName();
            } else {
                throw new IllegalStateException("Unknown Layout found");
            }
            GlueElement element_margin = new GlueElement(document, marginAttrClass + ".margin");
            element.getChildren().add(element_margin);
            GlueElement element_Insets = new GlueElement(document, Insets.class.getCanonicalName());
            element_margin.getChildren().add(element_Insets);
            if (reader.getAttribute(ATTR_LAYOUT_MARGIN_TOP) != null) {
                element_Insets.getAttributes().put("top", Double.toString(ComponentUtil.getPX(reader.getAttribute(ATTR_LAYOUT_MARGIN_TOP))));
            }
            if (reader.getAttribute(ATTR_LAYOUT_MARGIN_LEFT) != null) {
                element_Insets.getAttributes().put("left", Double.toString(ComponentUtil.getPX(reader.getAttribute(ATTR_LAYOUT_MARGIN_LEFT))));
            }
            if (reader.getAttribute(ATTR_LAYOUT_MARGIN_RIGHT) != null) {
                element_Insets.getAttributes().put("right", Double.toString(ComponentUtil.getPX(reader.getAttribute(ATTR_LAYOUT_MARGIN_RIGHT))));
            }
            if (reader.getAttribute(ATTR_LAYOUT_MARGIN_BOTTOM) != null) {
                element_Insets.getAttributes().put("bottom", Double.toString(ComponentUtil.getPX(reader.getAttribute(ATTR_LAYOUT_MARGIN_BOTTOM))));
            }
        }
    }

    private static void parseLinearLayout(XMLReader reader, GlueElement element, GlueElement parentElement, GlueElement containerElement, GlueDocument document, IFluidonFile fluidonFile) {
        element.getAttributes().put("spacing", "1");

        parseResourceId(reader, element);

        if (!reader.getChildren().isEmpty()) {
            GlueElement element_children = new GlueElement(document, "children");
            element.getChildren().add(element_children);
            for (XMLReader childReader : reader.getChildren()) {
                parseLayoutXml(childReader, element, element_children, document, fluidonFile);
            }
        }

        parsePadding(reader, element, document);
        if (containerElement != null) {
            parseMargin(reader, element, parentElement, document);
            parseDimension(reader, element);
        }

        /**
         * Bug FXMLLoader.processValue()=>resolvePrefixedValue() remove
         * DRAWABLE_PREFIX because interpret it as RELATIVE_PATH_PREFIX so need
         * to wrap backgroundImagePath
         *
         */
//                if (reader.getAttribute(ATTR_BACKGROUND) != null) {
//                    element.getAttributes().put("backgroundImagePath", '[' + reader.getAttribute(ATTR_BACKGROUND) + ']');
//                }
        if (reader.getAttribute(ATTR_BACKGROUND) != null) {
            String background = reader.getAttribute(ATTR_BACKGROUND);
            String fileName = background.substring(background.indexOf(DRAWABLE_PREFIX) + 1);
            GlueElement element_backgroundImage = new GlueElement(document, "backgroundImage");
            element.getChildren().add(element_backgroundImage);
            GlueElement element_Image = new GlueElement(document, Image.class.getCanonicalName());
            element_backgroundImage.getChildren().add(element_Image);

            FileObject resFileObject = fluidonFile.getFileObject().getParent().getParent();
            if (RES_FOLDER.equals(resFileObject.getName())) {
                for (FileObject fileObject : resFileObject.getChildren()) {
                    if (fileObject.getName() != null && (DRAWABLE_HDPI.equals(fileObject.getName())
                            || DRAWABLE_LDPI.equals(fileObject.getName()) || DRAWABLE_MDPI.equals(fileObject.getName())
                            || DRAWABLE_XHDPI.equals(fileObject.getName()) || "drawable-xxhdpi".equals(fileObject.getName()))) {
                        for (FileObject imageFileObject : fileObject.getChildren()) {
                            if ((DRAWABLE_PREFIX + imageFileObject.getName()).equals(background)) {

                                element_Image.getAttributes().put("url", FileUtil.toFile(imageFileObject).toURI().toString());

                                break;
                            }
                        }
                    }
                }
            }

//      <Image url="@../../../Documents/NetBeansProjects/JavaApplication1/src/fff/res/drawable-hdpi/IMG_1621.JPG" />
        }
    }

}
