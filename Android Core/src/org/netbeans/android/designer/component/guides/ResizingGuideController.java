package org.netbeans.android.designer.component.guides;

import static com.android.SdkConstants.VALUE_MATCH_PARENT;
import static com.android.SdkConstants.VALUE_WRAP_CONTENT;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.AbstractSegment;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.HorizontalSegment;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.IResizingGuideController;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.ResizingGuideRenderer;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.SegmentIndex;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.guides.VerticalSegment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.netbeans.android.designer.component.draw.ComponentUtil;
import org.netbeans.fluidon.panel.content.util.CardinalPoint;

/**
 *
 */
public class ResizingGuideController implements IResizingGuideController {

    private final double MATCH_DISTANCE = 10.0;
    private final double CHROME_SIDE_LENGTH = 4.0;
    private final double DELTA = CHROME_SIDE_LENGTH + 2;
    public static final double WRAP_CONTENT_DISTANCE = 26.0;

    private final SegmentIndex widthIndex;
    private final SegmentIndex heightIndex;
    private final ResizingGuideRenderer renderer;
    private double suggestedWidth;
    private double suggestedHeight;

    public static final Color GUIDE_COLOR = new Color(0, 0.6078, 0.8666, 1);

    private final List<AbstractSegment> renderSegments = new ArrayList<>();
    private final Node node;
//    private final EditText editText;
    private boolean matchWidth, matchHeight;
    private CardinalPoint cardinalPoint;

    public ResizingGuideController(Node node, CardinalPoint cardinalPoint, Paint guideColor) {
        this.node = node;
        setCardinalPoint(cardinalPoint);

        this.renderer = new ResizingGuideRenderer(guideColor, CHROME_SIDE_LENGTH);
//        if (matchWidth) {
        widthIndex = new SegmentIndex();
//        } else {
//            widthIndex = null;
//        }
//        if (matchHeight) {
        heightIndex = new SegmentIndex();
//        } else {
//            heightIndex = null;
//        }
    }

    @Override
    public void addSampleBounds(Node parentNode) {
        assert parentNode != null;
        assert parentNode.getScene() != null;

        final Insets padding;

        Insets margin = ComponentUtil.getMargin(node);
        if (margin == null) {
            margin = new Insets(0, 0, 0, 0);
        }

        if (parentNode instanceof VBox) {
            padding = ((VBox) parentNode).getPadding();
        } else if (parentNode instanceof HBox) {
            padding = ((HBox) parentNode).getPadding();
        } else {
            throw new IllegalStateException("Unknown Layout found : " + parentNode);
        }

        final Bounds parentLayoutBounds = parentNode.getLayoutBounds();
        final Bounds parentBoundsInScene = parentNode.localToScene(parentLayoutBounds);
        final double parentMinX = parentBoundsInScene.getMinX();
        final double parentMinY = parentBoundsInScene.getMinY();
        final double parentMaxX = parentBoundsInScene.getMaxX();
        final double parentMaxY = parentBoundsInScene.getMaxY();

        final Bounds childLayoutBounds = node.getLayoutBounds();
        final Bounds childBoundsInScene = node.localToScene(childLayoutBounds);
        final double childMinX = childBoundsInScene.getMinX();
        final double childMinY = childBoundsInScene.getMinY();
//        final double childMaxX = childBoundsInScene.getMaxX();
//        final double childMaxY = childBoundsInScene.getMaxY();

        if ((widthIndex != null) && (parentMinX < parentMaxX)) {
            widthIndex.addSegment(new HorizontalSegment(VALUE_MATCH_PARENT, childMinX, parentMaxX - padding.getRight() - margin.getRight(), childMinY - DELTA, true));
            widthIndex.addSegment(new HorizontalSegment(VALUE_WRAP_CONTENT, childMinX, childMinX + WRAP_CONTENT_DISTANCE, childMinY - DELTA));
        }
        if ((heightIndex != null) && (parentMinY < parentMaxY)) {
            heightIndex.addSegment(new VerticalSegment(VALUE_MATCH_PARENT, childMinX - DELTA, childMinY, parentMaxY - padding.getBottom(), true));
            heightIndex.addSegment(new VerticalSegment(VALUE_WRAP_CONTENT, childMinX - DELTA, childMinY, childMinY + WRAP_CONTENT_DISTANCE));
        }
//         if ((widthIndex != null) && (parentMinX < parentMaxX)) {
//            widthIndex.addSegment(new HorizontalSegment(VALUE_MATCH_PARENT, parentMinX + padding.getLeft(), parentMaxX - padding.getRight(), childMinY - DELTA, true));
//            widthIndex.addSegment(new HorizontalSegment(VALUE_WRAP_CONTENT, parentMinX + padding.getLeft(), parentMinX + padding.getLeft() + WRAP_CONTENT_DISTANCE, childMinY - DELTA));
//        }
//        if ((heightIndex != null) && (parentMinY < parentMaxY)) {
//            heightIndex.addSegment(new VerticalSegment(VALUE_MATCH_PARENT, parentMinX - DELTA, parentMinY + padding.getTop(), parentMaxY - padding.getBottom(), true));
//            heightIndex.addSegment(new VerticalSegment(VALUE_WRAP_CONTENT, parentMinX - DELTA, parentMinY + padding.getTop(), parentMinY + padding.getTop() + WRAP_CONTENT_DISTANCE));
//        }

    }

    @Override
    public void clear() {
        renderer.setSegments(Collections.emptyList());
    }

    @Override
    public void match(Bounds targetBounds) {
        final List<AbstractSegment> matchingSegments = new ArrayList<>();
        List<AbstractSegment> matchingWidthSegments = Collections.EMPTY_LIST;
        List<AbstractSegment> matchingHeightSegments = Collections.EMPTY_LIST;

//        if (matchWidth) {
        final double targetWidth = targetBounds.getWidth();
        if (widthIndex == null) {
            suggestedWidth = targetWidth;
        } else {
            matchingWidthSegments = widthIndex.match(targetWidth, MATCH_DISTANCE);
            if (matchingWidthSegments.isEmpty()) {
                suggestedWidth = targetWidth;
            } else {
                if (isMatchWidth()) {
                    suggestedWidth = matchingWidthSegments.get(0).getLength();
                    matchingSegments.addAll(matchingWidthSegments);
                } else {
                    suggestedWidth = targetWidth;
                }
            }
        }
//        }

//        if (matchHeight) {
        final double targetHeight = targetBounds.getHeight();

        if (heightIndex == null) {
            suggestedHeight = targetHeight;
        } else {
            matchingHeightSegments = heightIndex.match(targetHeight, MATCH_DISTANCE);
            if (matchingHeightSegments.isEmpty()) {
                suggestedHeight = targetHeight;
            } else {
                if (isMatchHeight()) {
                    suggestedHeight = matchingHeightSegments.get(0).getLength();
                    matchingSegments.addAll(matchingHeightSegments);
                } else {
                    suggestedHeight = targetHeight;
                }
            }
        }
//        }
        renderer.setSegments(matchingSegments);

        getRenderSegments().clear();
        getRenderSegments().addAll(matchingWidthSegments);
        getRenderSegments().addAll(matchingHeightSegments);
//        System.out.println("RG matchingSegments " + matchingSegments);
    }

    @Override
    public double getSuggestedWidth() {
        return suggestedWidth;
    }

    @Override
    public double getSuggestedHeight() {
        return suggestedHeight;
    }

    @Override
    public Group getGuideGroup() {
        return renderer.getGuideGroup();
    }

    /**
     * @return the renderSegments
     */
    public List<AbstractSegment> getRenderSegments() {
        return renderSegments;
    }

    /**
     * @return the cardinalPoint
     */
    public CardinalPoint getCardinalPoint() {
        return cardinalPoint;
    }

    /**
     * @param cardinalPoint the cardinalPoint to set
     */
    public void setCardinalPoint(CardinalPoint cardinalPoint) {
        this.cardinalPoint = cardinalPoint;
        switch (cardinalPoint) {
            case N:
            case S:
                matchWidth = false;
                matchHeight = true;
                break;
            case E:
            case W:
                matchWidth = true;
                matchHeight = false;
                break;
            default:
            case SE:
            case SW:
            case NE:
            case NW:
                matchWidth = true;
                matchHeight = true;
                break;
        }
    }

    /**
     * @return the matchWidth
     */
    public boolean isMatchWidth() {
        return matchWidth;
    }

    /**
     * @return the matchHeight
     */
    public boolean isMatchHeight() {
        return matchHeight;
    }
}
