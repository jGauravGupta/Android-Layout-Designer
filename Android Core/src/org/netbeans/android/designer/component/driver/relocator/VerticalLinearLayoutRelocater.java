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

import javafx.scene.Node;
import org.netbeans.android.designer.component.draw.VerticalLinearLayout;

public class VerticalLinearLayoutRelocater extends AbstractLauoutRelocater<VerticalLinearLayout> {

    public VerticalLinearLayoutRelocater(Node sceneGraphObject) {
        super(sceneGraphObject, VerticalLinearLayout.class);
    }

}
