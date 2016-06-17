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

import org.netbeans.jfluidon.component.Widget;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_RIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.SdkConstants.ATTR_PADDING_BOTTOM;
import static com.android.SdkConstants.ATTR_PADDING_LEFT;
import static com.android.SdkConstants.ATTR_PADDING_RIGHT;
import static com.android.SdkConstants.ATTR_PADDING_TOP;
import static com.android.SdkConstants.NEW_ID_PREFIX;
import static com.android.SdkConstants.UNIT_DP;
import static com.android.SdkConstants.VALUE_MATCH_PARENT;
import static com.android.SdkConstants.VALUE_WRAP_CONTENT;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.driver.relocater.IRelocater;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.IResizer;
import com.oracle.javafx.scenebuilder.kit.fxom.glue.XMLBuffer;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.netbeans.android.designer.component.driver.relocator.HorizontalLinearLayoutRelocater;
import org.netbeans.android.designer.component.driver.relocator.VerticalLinearLayoutRelocater;
import org.netbeans.android.designer.component.guides.ResizingGuideController;
import org.netbeans.fluidon.panel.content.util.CardinalPoint;
import org.netbeans.jfluidon.component.wrapper.IWrapperComponent;

/**
 *
 * @author Gaurav Gupta
 */
public class ComponentUtil {

    private static final double DP_RATIO = 0.73;//(73 / 100);

    public static void evalMargin(Region region, XMLBuffer writer, IWrapperComponent wrapperComponent) {
        final Insets margin = getMargin(region);
        if (margin != null) {
            writer.addAttribute(ATTR_LAYOUT_MARGIN_TOP, ComponentUtil.getDP(margin.getTop()));
            writer.addAttribute(ATTR_LAYOUT_MARGIN_LEFT, ComponentUtil.getDP(margin.getLeft()));
            writer.addAttribute(ATTR_LAYOUT_MARGIN_RIGHT, ComponentUtil.getDP(margin.getRight()));
            writer.addAttribute(ATTR_LAYOUT_MARGIN_BOTTOM, ComponentUtil.getDP(margin.getBottom()));
        }
    }

    public static void evalPadding(Insets padding, XMLBuffer writer, IWrapperComponent wrapperComponent) {
        writer.addAttribute(ATTR_PADDING_TOP, getDP(padding.getTop()));
        writer.addAttribute(ATTR_PADDING_LEFT, getDP(padding.getLeft()));
        writer.addAttribute(ATTR_PADDING_RIGHT, getDP(padding.getRight()));
        writer.addAttribute(ATTR_PADDING_BOTTOM, getDP(padding.getBottom()));
    }

    public static Insets getMargin(Node node) {
        if (node.getParent() instanceof VerticalLinearLayout) {
            return VerticalLinearLayout.getMargin(node);
        } else if (node.getParent() instanceof HorizontalLinearLayout) {
            return HorizontalLinearLayout.getMargin(node);
        } else {
            throw new IllegalStateException("Unknown Layout found : " + node.getParent());
        }
    }

    public static boolean isRoot(Region region) {
        return region.getParent().getParent() instanceof IWrapperComponent;
    }

    public static String getDP(double px , boolean defaultValue) {
        return getDP(px, 0 , defaultValue);
    }
      public static String getDP(double px) {
        return getDP(px, 0 , false);
    }

    public static String getDP(double px, double redundant_dp, boolean defaultValue) {
        if (Double.MAX_VALUE == px) {
            return VALUE_MATCH_PARENT;
        } else if (ResizingGuideController.WRAP_CONTENT_DISTANCE == px) {
            return VALUE_WRAP_CONTENT;
        }
        int dp = (int) ((int) (px / DP_RATIO) - redundant_dp);
        if (!defaultValue) {
            if (dp != 0) {
                return dp + UNIT_DP;
            } else {
                return null;
            }
        } else {
            return dp + UNIT_DP;
        }
    }

    public static double getPX(double dp) {
        return dp * DP_RATIO;
    }

    public static double getPX(String dp) {
        if (VALUE_MATCH_PARENT.equals(dp)) {
            return Double.MAX_VALUE;
        } else if (VALUE_WRAP_CONTENT.equals(dp)) {
            return ResizingGuideController.WRAP_CONTENT_DISTANCE;
        }
        Integer dpInt = Integer.valueOf(dp.substring(0, dp.length() - 2));
        return dpInt * DP_RATIO;
    }

    public static void manageMatchParent(Region node, Widget comp) {
        
        if (comp.getParent() instanceof HorizontalLinearLayout) {
            HorizontalLinearLayout.setHgrow(node, Priority.NEVER);
        } else if (comp.getParent() instanceof VerticalLinearLayout) {
            VerticalLinearLayout.setVgrow(node, Priority.NEVER);
        }
        
        if (VALUE_MATCH_PARENT.equals(comp.getResourceWidth())) {
            if (comp.getParent() instanceof HorizontalLinearLayout) {
                HorizontalLinearLayout.setHgrow(node, Priority.ALWAYS);
                node.setPrefWidth(0);
            } else if (node.getParent() instanceof VerticalLinearLayout) {
                node.setPrefWidth(Double.MAX_VALUE);
            }
        } else if (VALUE_WRAP_CONTENT.equals(comp.getResourceWidth())) {
            node.setPrefWidth(ResizingGuideController.WRAP_CONTENT_DISTANCE);
        } else {
            node.setPrefWidth(ComponentUtil.getPX(comp.getResourceWidth()));
        }

        if (VALUE_MATCH_PARENT.equals(comp.getResourceHeight())) {
            if (node.getParent() instanceof VerticalLinearLayout) {
                VerticalLinearLayout.setVgrow(node, Priority.ALWAYS); 
                node.setMaxHeight(Double.MAX_VALUE);
                node.setPrefHeight(10);
            } else if (node.getParent() instanceof HorizontalLinearLayout) {
                node.setPrefHeight(Double.MAX_VALUE);
            }
        } else if (VALUE_WRAP_CONTENT.equals(comp.getResourceHeight())) {
            node.setPrefHeight(ResizingGuideController.WRAP_CONTENT_DISTANCE);
        } else {
            node.setPrefHeight(ComponentUtil.getPX(comp.getResourceHeight()));
        }
    }

//    public static void manageMatchParent(Region node, IProperty LAYOUT_WIDTH, IProperty LAYOUT_HEIGHT) {
//        if (Double.MAX_VALUE == node.getPrefWidth()) {
//            LAYOUT_WIDTH.setValue(VALUE_MATCH_PARENT);
//        } else if (ResizingGuideController.WRAP_CONTENT_DISTANCE == node.getPrefWidth()) {
//            LAYOUT_WIDTH.setValue(VALUE_WRAP_CONTENT);
//        } else {
//            LAYOUT_WIDTH.setValue(node.getWidth());
//        }
//        if (Double.MAX_VALUE == node.getPrefHeight()) {
//            LAYOUT_HEIGHT.setValue(VALUE_MATCH_PARENT);
//        } else if (ResizingGuideController.WRAP_CONTENT_DISTANCE == node.getPrefHeight()) {
//            LAYOUT_HEIGHT.setValue(VALUE_WRAP_CONTENT);
//        } else {
//            LAYOUT_HEIGHT.setValue(node.getHeight());
//        }
//        if (VALUE_MATCH_PARENT.equals(LAYOUT_WIDTH.getValue())) {
//            if (node.getParent() instanceof HorizontalLinearLayout) {
//                HorizontalLinearLayout.setHgrow(node, Priority.ALWAYS);
//                node.setMaxWidth(Double.MAX_VALUE);
//                node.setPrefWidth(Double.MAX_VALUE);
//            } else if (node.getParent() instanceof VerticalLinearLayout) {
//                node.setPrefWidth(Double.MAX_VALUE);
//            }
//        } else if (VALUE_WRAP_CONTENT.equals(LAYOUT_WIDTH.getValue())) {
//            node.setPrefWidth(ResizingGuideController.WRAP_CONTENT_DISTANCE);
//        }
//        if (VALUE_MATCH_PARENT.equals(LAYOUT_HEIGHT.getValue())) {
//            if (node.getParent() instanceof VerticalLinearLayout) {
////                VerticalLinearLayout.setVgrow(node, Priority.ALWAYS); //not working so node.setPrefHeight(Double.MAX_VALUE); used //BUG
////                node.setMaxHeight(Double.MAX_VALUE);
////                node.setPrefHeight(Double.MAX_VALUE);
//            } else if (node.getParent() instanceof HorizontalLinearLayout) {
//                node.setPrefHeight(Double.MAX_VALUE);
//            }
//        } else if (VALUE_WRAP_CONTENT.equals(LAYOUT_HEIGHT.getValue())) {
//            node.setPrefHeight(ResizingGuideController.WRAP_CONTENT_DISTANCE);
//        }
//    }
    public static IRelocater getRelocater(Node node) {
        if (node.getParent().getClass() == VerticalLinearLayout.class) {
            return new VerticalLinearLayoutRelocater(node);
        } else if (node.getParent().getClass() == HorizontalLinearLayout.class) {
            return new HorizontalLinearLayoutRelocater(node);
        }
        return null;
    }

    public static ResizingGuideController getController(Node node, CardinalPoint cardinalPoint) {
        ResizingGuideController resizingGuideController = new ResizingGuideController(node, cardinalPoint, ResizingGuideController.GUIDE_COLOR);
        resizingGuideController.addSampleBounds(node.getParent());
        return resizingGuideController;
    }

    public static String getAndroidResourceId(String resourceId_In) {
        if (resourceId_In != null) {
            return NEW_ID_PREFIX + resourceId_In;
        }
        return null;
    }

    public static String getFXResourceId(String resourceId_In) {
        if (resourceId_In != null && resourceId_In.length() > NEW_ID_PREFIX.length()) {
            return resourceId_In.substring(NEW_ID_PREFIX.length());
        }
        return null;
    }

}
