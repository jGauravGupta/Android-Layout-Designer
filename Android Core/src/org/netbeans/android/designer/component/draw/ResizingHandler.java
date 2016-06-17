/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.android.designer.component.draw;

import org.netbeans.jfluidon.component.Widget;
import static com.android.SdkConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_WIDTH;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.driver.relocater.IRelocater;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.AbstractSegment;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.HorizontalSegment;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.IResizingGuideController;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.IResizingHandler;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.VerticalSegment;
import com.oracle.javafx.scenebuilder.kit.metadata.util.Property;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Region;
import org.netbeans.android.designer.component.guides.ResizingGuideController;
import org.netbeans.fluidon.panel.content.util.CardinalPoint;
import org.netbeans.jfluidon.specification.metadata.entity.IProperty;

/**
 *
 * @author gaurav
 */
public class ResizingHandler implements IResizingHandler {

    private ResizingGuideController resizingGuideController;
    public static final IProperty LAYOUT_WIDTH = new Property(ATTR_LAYOUT_WIDTH);
    public static final IProperty LAYOUT_HEIGHT = new Property(ATTR_LAYOUT_HEIGHT);
    private final List<IProperty> properties = new ArrayList<>();

    private Region region;
    private Widget component;

    public ResizingHandler() {
        properties.add(LAYOUT_WIDTH);
        properties.add(LAYOUT_HEIGHT);
    }

    public void setComponent(Region region) {
        this.region = region;
        this.component = (Widget) region;

    }

    @Override
    public IRelocater getRelocater() {
        return ComponentUtil.getRelocater(region);
    }

    @Override
    public IResizingGuideController getController(CardinalPoint cardinalPoint) {
        if (resizingGuideController == null) {
            resizingGuideController = ComponentUtil.getController(region, cardinalPoint);
        }
        resizingGuideController.setCardinalPoint(cardinalPoint);
        return resizingGuideController;
    }

    @Override
    public List<IProperty> getResizingProperties() {
        return properties;
    }

    @Override
    public Object getValue(IProperty propertyName) {
        assert propertyName != null;
        assert properties.contains(propertyName);

        final Object result;
        if (propertyName.equals(LAYOUT_WIDTH)) {
            if (resizingGuideController != null && resizingGuideController.isMatchWidth()) {
                List<AbstractSegment> renderSegments = resizingGuideController.getRenderSegments();
                for (AbstractSegment renderSegment : renderSegments) {
                    if (renderSegment instanceof HorizontalSegment && null != renderSegment.getId()) {
                        component.setResourceWidth(renderSegment.getId());
                        return component.getResourceWidth();
                    }
                }
                component.setResourceWidth(ComponentUtil.getDP(region.getWidth() , true));
                result = component.getResourceWidth();
            } else {
                result = component.getResourceWidth();
            }
        } else if (propertyName.equals(LAYOUT_HEIGHT)) {
            if (resizingGuideController != null && resizingGuideController.isMatchHeight()) {
                List<AbstractSegment> renderSegments = resizingGuideController.getRenderSegments();
                for (AbstractSegment renderSegment : renderSegments) {
                    if (renderSegment instanceof VerticalSegment && null != renderSegment.getId()) {
                        component.setResourceHeight(renderSegment.getId());
                        return renderSegment.getId();
                    }
                }
                component.setResourceHeight(ComponentUtil.getDP(region.getHeight(), true));
                result = component.getResourceHeight();
            } else {
                result = component.getResourceHeight();
            }
        } else {
            result = null;
        }

        return result;
    }

}
