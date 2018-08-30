package ir.waspar.resturanttestapp;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Dimension;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class LayoutManager extends LinearLayoutManager {

    private static final int MIN_RADIUS = 0;
    private static final int MIN_PEEK = 0;

    @IntDef(value = {
            Gravity.START,
            Gravity.END
    })
    public @interface Gravity {
        int START = android.view.Gravity.START;
        int END = android.view.Gravity.END;
    }

    @IntDef(value = {
            Orientation.VERTICAL,
            Orientation.HORIZONTAL
    })
    public @interface Orientation {
        int VERTICAL = LinearLayoutManager.VERTICAL;
        int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    }

    @Gravity
    private int gravity;
    @Dimension
    private int radius;
    @Dimension
    private int peekDistance;
    private boolean rotate;
    private Point center;

    public LayoutManager(Context context, @Gravity int gravity, @Orientation int orientation, @Dimension int radius, @Dimension int peekDistance, boolean rotate) {
        super(context, orientation, false);
        this.gravity = gravity;
        this.radius = Math.max(radius, MIN_RADIUS);
        this.peekDistance = Math.min(Math.max(peekDistance, MIN_PEEK), radius);
        this.rotate = rotate;
        this.center = new Point();
    }

    public void setGravity(@Gravity int gravity) {
        this.gravity = gravity;
        requestLayout();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int by = super.scrollVerticallyBy(dy, recycler, state);
        setChildOffsetsVertical(gravity, radius, center, peekDistance);
        return by;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int by = super.scrollHorizontallyBy(dx, recycler, state);
        setChildOffsetsHorizontal(gravity, radius, center, peekDistance);
        return by;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        this.center = deriveCenter(gravity, getOrientation(), radius, peekDistance, center);
        setChildOffsets(gravity, getOrientation(), radius, center, peekDistance);
    }

    private Point deriveCenter(@Gravity int gravity, int orientation, @Dimension int radius, @Dimension int peekDistance, Point out) {
        final int gravitySign = gravity == Gravity.START ? -1 : 1;
        final int distanceMultiplier = gravity == Gravity.START ? 0 : 1;
        int x, y;
        switch (orientation) {
            case Orientation.HORIZONTAL:
                y = (distanceMultiplier * getHeight()) + gravitySign * (Math.abs(radius - peekDistance));
                x = getWidth() / 2;
                break;
            case Orientation.VERTICAL:
            default:
                y = getHeight() / 2;
                x = (distanceMultiplier * getWidth()) + gravitySign * (Math.abs(radius - peekDistance));
                break;
        }
        out.set(x, y);
        return out;
    }

    private double resolveOffsetX(double radius, double viewY, Point center, int peekDistance) {
        final double opposite = Math.abs(center.y - viewY);
        final double radiusSquared = radius * radius;
        final double oppositeSquared = opposite * opposite;
        final double adjacentSideLength = Math.sqrt(radiusSquared - oppositeSquared);
        return adjacentSideLength - radius + peekDistance;
    }

    private double resolveOffsetY(double radius, double viewX, Point center, int peekDistance) {
        final double adjacent = Math.abs(center.x - viewX);
        final double radiusSquared = radius * radius;
        final double adjacentSquared = adjacent * adjacent;
        final double oppositeSideLength = Math.sqrt(radiusSquared - adjacentSquared);
        return oppositeSideLength - radius + peekDistance;
    }

    private void setChildOffsets(@Gravity int gravity, int orientation, @Dimension int radius, Point center, int peekDistance) {
        if (orientation == VERTICAL) {
            setChildOffsetsVertical(gravity, radius, center, peekDistance);
        } else if (orientation == HORIZONTAL) {
            setChildOffsetsHorizontal(gravity, radius, center, peekDistance);
        }
    }

    private void setChildOffsetsVertical(@Gravity int gravity, @Dimension int radius, Point center, int peekDistance) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int xOffset = (int) resolveOffsetX(radius, child.getY() + child.getHeight() / 2, center, peekDistance);
            final int x = gravity == Gravity.START ? xOffset + layoutParams.getMarginStart()
                    : getWidth() - xOffset - child.getWidth() - layoutParams.getMarginStart();
            child.layout(x, child.getTop(), child.getWidth() + x, child.getBottom());
            setChildRotationVertical(gravity, child, radius, center);
        }
    }

    private void setChildRotationVertical(@Gravity int gravity, View child, int radius, Point center) {
        if (!rotate) {
            child.setRotation(0);
            return;
        }
        boolean childPastCenter = (child.getY() + child.getHeight() / 2) > center.y;
        float directionMult;
        if (gravity == Gravity.END) {
            directionMult = childPastCenter ? -1 : 1;
        } else {
            directionMult = childPastCenter ? 1 : -1;
        }
        final float opposite = Math.abs(child.getY() + child.getHeight() / 2 - center.y);
        child.setRotation((float) (directionMult * Math.toDegrees(Math.asin(opposite / radius))));
    }

    private void setChildOffsetsHorizontal(@Gravity int gravity, @Dimension int radius, Point center, int peekDistance) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int yOffset = (int) resolveOffsetY(radius, child.getX() + child.getWidth() / 2, center, peekDistance);
            final int y = gravity == Gravity.START ? yOffset + layoutParams.getMarginStart()
                    : getHeight() - yOffset - child.getHeight() - layoutParams.getMarginStart();

            child.layout(child.getLeft(), y, child.getRight(), child.getHeight() + y);
            setChildRotationHorizontal(gravity, child, radius, center);
        }
    }

    private void setChildRotationHorizontal(@Gravity int gravity, View child, int radius, Point center) {
        if (!rotate) {
            child.setRotation(0);
            return;
        }
        boolean childPastCenter = (child.getX() + child.getWidth() / 2) > center.x;
        float directionMult;
        if (gravity == Gravity.END) {
            directionMult = childPastCenter ? 1 : -1;
        } else {
            directionMult = childPastCenter ? -1 : 1;
        }
        final float opposite = Math.abs(child.getX() + child.getWidth() / 2 - center.x);
        child.setRotation((float) (directionMult * Math.toDegrees(Math.asin(opposite / radius))));
    }
}