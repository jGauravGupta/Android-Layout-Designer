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
package org.netbeans.android.designer.component.draw;

import static com.android.SdkConstants.ATTR_HINT;
import static com.android.SdkConstants.ATTR_ID;
import static com.android.SdkConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_WIDTH;
import static com.android.SdkConstants.ATTR_TEXT;
import static com.android.SdkConstants.EDIT_TEXT;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.IResizer;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.IResizingHandler;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.fxom.glue.XMLBuffer;
import javafx.geometry.Insets;
import org.netbeans.android.designer.component.listener.ResizeListener;
import org.netbeans.jfluidon.annotation.Component;
import org.netbeans.jfluidon.annotation.Listener;
import org.netbeans.jfluidon.annotation.PostConstruct;
import org.netbeans.jfluidon.annotation.Resource;
import org.netbeans.jfluidon.annotation.Transient;
import org.netbeans.jfluidon.component.wrapper.IWrapperComponent;
import org.netbeans.jfluidon.source.serializer.SourceSerializer;

@Component(name = "Edit Text",
        icon = "org/netbeans/android/resource/library/icon/EditText.png",
        serialize = EDIT_TEXT,
        listener = @Listener(resize = ResizeListener.class))
public class EditText extends javafx.scene.control.TextField implements SourceSerializer<FXOMInstance, XMLBuffer>, IResizer, org.netbeans.jfluidon.component.Widget {

    @Transient
    private String resourceWidth = "match_parent";
    @Transient
    private String resourceHeight = "wrap_content";

    private String resourceId;
    private Insets resourcePadding;

    private static final Insets DEFAULT_PADDING = Insets.EMPTY;//new Insets(3, 9, 7, 9);

    @Resource
    private IWrapperComponent wrapperComponent;
    @Transient
    private ResizingHandler resizingHandler;

//    @Resource
//    private IFluidonFile fluidonFile;
    public EditText() {

        refreshPadding();
    }

    @Override
    public void serialize(FXOMInstance reader, XMLBuffer writer) {
        if (resourceId != null) {
            writer.addAttribute(ATTR_ID, ComponentUtil.getAndroidResourceId(resourceId));
        }
        writer.addAttribute(ATTR_LAYOUT_WIDTH, resourceWidth);
        writer.addAttribute(ATTR_LAYOUT_HEIGHT, resourceHeight);
        writer.addAttribute(ATTR_HINT, this.getPromptText());
        writer.addAttribute(ATTR_TEXT, this.getText());
        ComponentUtil.evalMargin(this, writer, wrapperComponent);
        ComponentUtil.evalPadding(this.getPadding(), writer, wrapperComponent);
    }

    @PostConstruct
    private void init() {
        if (resizingHandler == null) {
            resizingHandler = new ResizingHandler();
        }
        resizingHandler.setComponent(this);
        ComponentUtil.manageMatchParent(this, this);// resizingHandler.getWidth(), resizingHandler.getHeight());
//         VBox.setVgrow(this, Priority.ALWAYS); //not working so node.setPrefHeight(Double.MAX_VALUE); used //BUG
//                this.setMaxHeight(Double.MAX_VALUE);
//                this.setPrefHeight(Double.MAX_VALUE);
    }

    /**
     * @return the resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * @param resourceId the resourceId to set
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * @return the resourcePadding
     */
    public Insets getResourcePadding() {
        return resourcePadding;
    }

    /**
     * @param resourcePadding the resourcePadding to set
     */
    public void setResourcePadding(Insets resourcePadding) {
        this.resourcePadding = resourcePadding;
        refreshPadding();
    }

    private void refreshPadding() {
        if (resourcePadding == null) {
            resourcePadding = Insets.EMPTY;
        }
        Insets padding = new Insets(ComponentUtil.getPX(DEFAULT_PADDING.getTop() + resourcePadding.getTop()),
                ComponentUtil.getPX(DEFAULT_PADDING.getRight() + resourcePadding.getRight()),
                ComponentUtil.getPX(DEFAULT_PADDING.getBottom() + resourcePadding.getBottom()),
                ComponentUtil.getPX(DEFAULT_PADDING.getLeft() + resourcePadding.getLeft()));
        this.setPadding(padding);
    }

    @Override
    public IResizingHandler getResizingHandler() {
        if (resizingHandler == null) {
            resizingHandler = new ResizingHandler();
        }
        return resizingHandler;
    }

    /**
     * @return the resourceWidth
     */
    @Override
    public String getResourceWidth() {
        return resourceWidth;
    }

    /**
     * @param resourceWidth the resourceWidth to set
     */
    @Override
    public void setResourceWidth(String resourceWidth) {
        this.resourceWidth = resourceWidth;
    }

    /**
     * @return the resourceHeight
     */
    @Override
    public String getResourceHeight() {
        return resourceHeight;
    }

    /**
     * @param resourceHeight the resourceHeight to set
     */
    @Override
    public void setResourceHeight(String resourceHeight) {
        this.resourceHeight = resourceHeight;
    }

}
