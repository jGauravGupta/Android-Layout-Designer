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
package org.netbeans.android.layout.serializer;

import com.oracle.javafx.scenebuilder.kit.fxom.FXOMDocument;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import com.oracle.javafx.scenebuilder.kit.fxom.glue.XMLBuffer;
import org.netbeans.jfluidon.annotation.Component;
import org.netbeans.jfluidon.source.serializer.SourceSerializer;

/**
 *
 * @author Gaurav Gupta
 */
public class AndroidLayoutSerializer implements SourceSerializer<FXOMDocument, XMLBuffer> {

    @Override
    public void serialize(FXOMDocument fxomDocument, XMLBuffer result) {
        result.addProlog();
        Object obj = fxomDocument.getFxomRoot().getSceneGraphObject();
        if (fxomDocument.getFxomRoot() instanceof FXOMInstance && obj instanceof SourceSerializer) {
            serialize((FXOMInstance) fxomDocument.getFxomRoot(), result);
        }
    }

    void serialize(FXOMInstance reader, XMLBuffer writer) {
        Object obj = reader.getSceneGraphObject();
        Component component = obj.getClass().getAnnotation(Component.class);
        writer.beginElement(component.serialize());
        ((SourceSerializer) obj).serialize(reader, writer);
        for (FXOMObject childInstance : reader.getChildObjects()) {
            if (childInstance instanceof FXOMInstance && childInstance.getSceneGraphObject() instanceof SourceSerializer) {
                serialize((FXOMInstance) childInstance, writer);
            }
        }
        writer.endElement();
    }

}
