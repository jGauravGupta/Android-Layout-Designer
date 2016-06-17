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

import static com.android.SdkConstants.ANDROID_NS_NAME;
import static com.android.SdkConstants.ATTR_BACKGROUND;
import static com.android.SdkConstants.ATTR_ID;
import static com.android.SdkConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_WIDTH;
import static com.android.SdkConstants.ATTR_ORIENTATION;
import static com.android.SdkConstants.DRAWABLE_HDPI;
import static com.android.SdkConstants.DRAWABLE_LDPI;
import static com.android.SdkConstants.DRAWABLE_MDPI;
import static com.android.SdkConstants.DRAWABLE_PREFIX;
import static com.android.SdkConstants.DRAWABLE_XHDPI;
import static com.android.SdkConstants.LINEAR_LAYOUT;
import static com.android.SdkConstants.NS_RESOURCES;
import static com.android.SdkConstants.RES_FOLDER;
import static com.android.SdkConstants.VALUE_FILL_PARENT;
import static com.android.SdkConstants.VALUE_VERTICAL;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.IResizer;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.IResizingHandler;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.fxom.glue.XMLBuffer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;
import javax.xml.namespace.QName;
import org.netbeans.android.designer.component.listener.ResizeListener;
import org.netbeans.jfluidon.annotation.Component;
import org.netbeans.jfluidon.annotation.Listener;
import org.netbeans.jfluidon.annotation.PostConstruct;
import org.netbeans.jfluidon.annotation.Resource;
import org.netbeans.jfluidon.annotation.Transient;
import org.netbeans.jfluidon.component.wrapper.IWrapperComponent;
import org.netbeans.jfluidon.source.serializer.SourceSerializer;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author Gaurav Gupta
 */
@Component(name = "LinearLayout (Vertical)",
        icon = "org/netbeans/android/resource/library/icon/VerticalLinearLayout.png",
        fxml = "org/netbeans/android/resource/library/fxml/VerticalLinearLayout.fxml",
        serialize = LINEAR_LAYOUT,
        listener = @Listener(resize = ResizeListener.class))
public class VerticalLinearLayout extends VBox implements ILinearLayout, SourceSerializer<FXOMInstance, XMLBuffer>, IResizer, org.netbeans.jfluidon.component.Widget {

    @Transient
    private String resourceWidth = "match_parent";
    @Transient
    private String resourceHeight = "wrap_content";
    private String resourceId;
    private Insets resourcePadding;

    private static final Insets DEFAULT_PADDING = new Insets(0, 0, 0, 0);

    private Image backgroundImage;
    private String backgroundImagePath;

//    @Resource
//    private IFluidonFile file;
    @Resource
    private IWrapperComponent wrapperComponent;
    @Transient
    private ResizingHandler resizingHandler;

    @Override
    public void serialize(FXOMInstance reader, XMLBuffer writer) {
        if (ComponentUtil.isRoot(this)) {
            writer.setDefaultNameSpace(new QName(NS_RESOURCES, ANDROID_NS_NAME));
            writer.addAttribute(ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);
            writer.addAttribute(ATTR_LAYOUT_HEIGHT, VALUE_FILL_PARENT);
        } else {
            writer.addAttribute(ATTR_LAYOUT_WIDTH, resourceWidth);
            writer.addAttribute(ATTR_LAYOUT_HEIGHT, resourceHeight);
            ComponentUtil.evalMargin(this, writer, wrapperComponent);
        }
        if (resourceId != null) {
            writer.addAttribute(ATTR_ID, ComponentUtil.getAndroidResourceId(resourceId));
        }
        writer.addAttribute(ATTR_ORIENTATION, VALUE_VERTICAL);
        ComponentUtil.evalPadding(this.getPadding(), writer, wrapperComponent);

        //Background Image Start
        if (backgroundImage != null) {
            String path = backgroundImage.impl_getUrl();

//        path = "@drawable/asd";
            if (path.startsWith(DRAWABLE_PREFIX)) {

            } else {
                try {
                    int directoryIndex = path.lastIndexOf('\\');
                    if (directoryIndex == -1) {
                        directoryIndex = path.lastIndexOf('/');
                    }
                    String fileName = path.substring(directoryIndex + 1, path.lastIndexOf('.'));
                    String fileExt = path.substring(path.lastIndexOf('.') + 1);
                    BufferedImage bufferedBackgroundImage = SwingFXUtils.fromFXImage(backgroundImage, null);
                    FileObject resFileObject = reader.getFxomDocument().getFluidonFile().getFileObject().getParent().getParent();
                    if (RES_FOLDER.equals(resFileObject.getName())) {
                        for (FileObject fileObject : resFileObject.getChildren()) {
                            if (fileObject.getName() != null && (DRAWABLE_HDPI.equals(fileObject.getName())
                                    || DRAWABLE_LDPI.equals(fileObject.getName()) || DRAWABLE_MDPI.equals(fileObject.getName())
                                    || DRAWABLE_XHDPI.equals(fileObject.getName()) || "drawable-xxhdpi".equals(fileObject.getName()))) {
                                File file = new File(fileObject.getPath() + File.separatorChar + fileName + '.' + fileExt);
                                if (file.exists()) {
                                    file.createNewFile();
                                }
                                ImageIO.write(bufferedBackgroundImage, fileExt, file);
                            }
                        }
                    }

                    writer.addAttribute(ATTR_BACKGROUND, DRAWABLE_PREFIX + fileName);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        //Background Image End
    }

    /**
     * @return the backgroundImage
     */
    public Image getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * @param backgroundImage the backgroundImage to set
     */
    public void setBackgroundImage(Image backgroundImage) {
//        if (this.backgroundImage != backgroundImage) {
//            setBackgroundImage_Internal(backgroundImage);
//        }
        this.backgroundImage = backgroundImage;
//        this.backgroundImagePath =backgroundImage.impl_getUrl();

    }

    private void setBackgroundImage_Internal(Image backgroundImage) {
        this.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT
        )));
    }

    /**
     * @return the backgroundImagePath
     */
    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }

    /**
     * @param backgroundImagePath the backgroundImagePath to set
     */
    public void setBackgroundImagePath(String backgroundImagePath) {
        /**
         * Bug FXMLLoader.processValue()=>resolvePrefixedValue() remove
         * DRAWABLE_PREFIX because interpret it as RELATIVE_PATH_PREFIX so need
         * to unwrap backgroundImagePath
         *
         */
        if (backgroundImagePath.length() > DRAWABLE_PREFIX.length() + 3) {
            this.backgroundImagePath = backgroundImagePath.substring(1, backgroundImagePath.length() - 1);
        } else {
            this.backgroundImagePath = null;
        }
    }

    @PostConstruct
    private void init() {
        if (backgroundImage != null) {
            setBackgroundImage_Internal(backgroundImage);
        }
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
