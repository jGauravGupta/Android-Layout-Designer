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
package org.netbeans.android.designer.component.listener;

import javafx.scene.Node;
import javafx.scene.layout.Priority;
import org.netbeans.android.designer.component.draw.HorizontalLinearLayout;
import org.netbeans.android.designer.component.draw.VerticalLinearLayout;
import org.netbeans.fluidon.panel.content.util.CardinalPoint;
import org.netbeans.jfluidon.listener.IResizeListener;

/**
 *
 * @author Gaurav Gupta
 */
public class ResizeListener implements IResizeListener<Node> {

    @Override
    public void beforeResize(Node node, CardinalPoint tunable) {
        if (node.getParent() instanceof HorizontalLinearLayout) {
            if (HorizontalLinearLayout.getHgrow(node) == Priority.ALWAYS && (tunable != CardinalPoint.S && tunable != CardinalPoint.N)) {
                HorizontalLinearLayout.setHgrow(node, Priority.NEVER);
            }
        } else if (node.getParent() instanceof VerticalLinearLayout) {
            if (VerticalLinearLayout.getVgrow(node) == Priority.ALWAYS && (tunable != CardinalPoint.E && tunable != CardinalPoint.W)) {
                VerticalLinearLayout.setVgrow(node, Priority.NEVER);
            }
        }
    }

    @Override
    public void afterResize(Node node) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onResize(Node node) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
