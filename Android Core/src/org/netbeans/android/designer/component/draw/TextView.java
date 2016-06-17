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

import static com.android.SdkConstants.ATTR_ID;
import static com.android.SdkConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_WIDTH;
import static com.android.SdkConstants.ATTR_TEXT;
import static com.android.SdkConstants.TEXT_VIEW;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.IResizer;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.IResizingHandler;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.fxom.glue.XMLBuffer;
import javafx.geometry.Insets;
import org.netbeans.jfluidon.annotation.Component;
import org.netbeans.jfluidon.annotation.PostConstruct;
import org.netbeans.jfluidon.annotation.Resource;
import org.netbeans.jfluidon.annotation.Transient;
import org.netbeans.jfluidon.component.wrapper.IWrapperComponent;
import org.netbeans.jfluidon.source.serializer.SourceSerializer;

@Component(name = "Text View",
        icon = "org/netbeans/android/resource/library/icon/TextView.png",
        fxml = "org/netbeans/android/resource/library/fxml/TextView.fxml",
        serialize = TEXT_VIEW)
public class TextView extends javafx.scene.control.Label implements SourceSerializer<FXOMInstance, XMLBuffer>, IResizer, org.netbeans.jfluidon.component.Widget {

    @Transient
    private String resourceWidth = "match_parent";
    @Transient
    private String resourceHeight = "wrap_content";
    private String resourceId;
    private Insets resourcePadding;

    private static final Insets DEFAULT_PADDING = new Insets(0, 0, 0, 0);
    @Resource
    private IWrapperComponent wrapperComponent;
    @Transient
    private ResizingHandler resizingHandler;

    @Override
    public void serialize(FXOMInstance reader, XMLBuffer writer) {
        if (resourceId != null) {
            writer.addAttribute(ATTR_ID, ComponentUtil.getAndroidResourceId(resourceId));
        }
        writer.addAttribute(ATTR_LAYOUT_WIDTH, resourceWidth);
        writer.addAttribute(ATTR_LAYOUT_HEIGHT, resourceHeight);
        writer.addAttribute(ATTR_TEXT, this.getText());
        ComponentUtil.evalMargin(this, writer, wrapperComponent);
        ComponentUtil.evalPadding(this.getPadding(), writer, wrapperComponent);
    }

    @PostConstruct
    private void init() {
        if (resizingHandler == null) {
            resizingHandler = new ResizingHandler();
        }
        ComponentUtil.manageMatchParent(this, this);
        resizingHandler.setComponent(this);
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
        Insets padding = new Insets(DEFAULT_PADDING.getTop() + ComponentUtil.getPX(resourcePadding.getTop()),
                DEFAULT_PADDING.getRight() + ComponentUtil.getPX(resourcePadding.getRight()),
                DEFAULT_PADDING.getBottom() + ComponentUtil.getPX(resourcePadding.getBottom()),
                DEFAULT_PADDING.getLeft() + ComponentUtil.getPX(resourcePadding.getLeft()));
        this.setPadding(padding);
    }

    @Override
    public IResizingHandler getResizingHandler() {
        if (resizingHandler == null) {
            resizingHandler = new ResizingHandler();
        }
        return resizingHandler;
    }

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
