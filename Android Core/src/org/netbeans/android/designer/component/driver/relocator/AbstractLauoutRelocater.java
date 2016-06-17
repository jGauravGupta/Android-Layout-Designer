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
package org.netbeans.android.designer.component.driver.relocator;

import com.oracle.javafx.scenebuilder.kit.editor.panel.content.driver.relocater.AbstractRelocater;
import com.oracle.javafx.scenebuilder.kit.metadata.util.PropertyName;
import com.oracle.javafx.scenebuilder.kit.util.MathUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.netbeans.jfluidon.specification.metadata.entity.IPropertyName;

public class AbstractLauoutRelocater<T extends Parent> extends AbstractRelocater {

    private final double originalLayoutX;
    private final double originalLayoutY;
    private final IPropertyName layoutXName = new PropertyName("layoutX"); //NOI18N
    private final IPropertyName layoutYName = new PropertyName("layoutY"); //NOI18N
    private final List<IPropertyName> propertyNames = new ArrayList<>();

    public AbstractLauoutRelocater(Node sceneGraphObject, Class<T> parentClass) {
        super(sceneGraphObject, parentClass);
        this.originalLayoutX = sceneGraphObject.getLayoutX();
        this.originalLayoutY = sceneGraphObject.getLayoutY();
        propertyNames.add(layoutXName);
        propertyNames.add(layoutYName);
    }

    /*
     * AbstractRelocater
     */
    @Override
    public void moveToLayoutX(double newLayoutX, Bounds newLayoutBounds) {
        sceneGraphObject.setLayoutX(Math.round(newLayoutX));
        // newLayoutBounds is no use for this subclass
    }

    @Override
    public void moveToLayoutY(double newLayoutY, Bounds newLayoutBounds) {
        sceneGraphObject.setLayoutY(Math.round(newLayoutY));
        // newLayoutBounds is no use for this subclass
    }

    @Override
    public void revertToOriginalLocation() {
        sceneGraphObject.setLayoutX(originalLayoutX);
        sceneGraphObject.setLayoutY(originalLayoutY);
    }

    @Override
    public List<IPropertyName> getPropertyNames() {
        return propertyNames;
    }

    @Override
    public Object getValue(IPropertyName propertyName) {
        assert propertyName != null;
        assert propertyNames.contains(propertyName);

        final Object result;
        if (propertyName.equals(layoutXName)) {
            result = sceneGraphObject.getLayoutX();
        } else if (propertyName.equals(layoutYName)) {
            result = sceneGraphObject.getLayoutY();
        } else {
            // Emergency code
            result = null;
        }

        return result;
    }

    @Override
    public Map<IPropertyName, Object> getChangeMap() {
        final Map<IPropertyName, Object> result = new HashMap<>();
        if (MathUtils.equals(sceneGraphObject.getLayoutX(), originalLayoutX) == false) {
            result.put(layoutXName, sceneGraphObject.getLayoutX());
        }
        if (MathUtils.equals(sceneGraphObject.getLayoutY(), originalLayoutY) == false) {
            result.put(layoutYName, sceneGraphObject.getLayoutY());
        }
        return result;
    }
}
