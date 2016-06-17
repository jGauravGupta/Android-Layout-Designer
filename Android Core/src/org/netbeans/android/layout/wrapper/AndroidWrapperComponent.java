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
package org.netbeans.android.layout.wrapper;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.netbeans.jfluidon.component.wrapper.IWrapperComponent;

/**
 *
 * @author Gaurav Gupta
 */
public class AndroidWrapperComponent extends VBox implements IWrapperComponent {

    @FXML
    private VBox container;

    public AndroidWrapperComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("resources/WrapperComponent.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        DropShadow dropShadow = new DropShadow(20, Color.GREY);
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        this.setEffect(dropShadow);

    }

    @Override
    public double getWrapperWidth() {
        return this.getPrefWidth();
    }

    @Override
    public double getWrapperHeight() {
        return this.getPrefHeight() - 20;
    }

    /**
     * @return the container
     */
    public VBox getContainer() {
        return container;
    }
}
