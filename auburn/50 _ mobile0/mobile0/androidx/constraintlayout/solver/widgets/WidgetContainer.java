// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget
{
    protected ArrayList<ConstraintWidget> mChildren;
    
    public WidgetContainer() {
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public WidgetContainer(final int n, final int n2) {
        super(n, n2);
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public WidgetContainer(final int n, final int n2, final int n3, final int n4) {
        super(n, n2, n3, n4);
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public static Rectangle getBounds(final ArrayList<ConstraintWidget> list) {
        final Rectangle rectangle = new Rectangle();
        if (list.size() == 0) {
            return rectangle;
        }
        int n = Integer.MAX_VALUE;
        int n2 = 0;
        int n3 = Integer.MAX_VALUE;
        int n4 = 0;
        int x;
        int y;
        int right;
        int bottom;
        for (int i = 0; i < list.size(); ++i, n = x, n2 = right, n3 = y, n4 = bottom) {
            final ConstraintWidget constraintWidget = list.get(i);
            if (constraintWidget.getX() < (x = n)) {
                x = constraintWidget.getX();
            }
            if (constraintWidget.getY() < (y = n3)) {
                y = constraintWidget.getY();
            }
            if (constraintWidget.getRight() > (right = n2)) {
                right = constraintWidget.getRight();
            }
            if (constraintWidget.getBottom() > (bottom = n4)) {
                bottom = constraintWidget.getBottom();
            }
        }
        rectangle.setBounds(n, n3, n2 - n, n4 - n3);
        return rectangle;
    }
    
    public void add(final ConstraintWidget e) {
        this.mChildren.add(e);
        if (e.getParent() != null) {
            ((WidgetContainer)e.getParent()).remove(e);
        }
        e.setParent(this);
    }
    
    public void add(final ConstraintWidget... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.add(array[i]);
        }
    }
    
    public ConstraintWidget findWidget(final float n, final float n2) {
        final ConstraintWidget constraintWidget = null;
        final int drawX = this.getDrawX();
        final int drawY = this.getDrawY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        ConstraintWidget constraintWidget2 = constraintWidget;
        if (n >= drawX) {
            constraintWidget2 = constraintWidget;
            if (n <= width + drawX) {
                constraintWidget2 = constraintWidget;
                if (n2 >= drawY) {
                    constraintWidget2 = constraintWidget;
                    if (n2 <= height + drawY) {
                        constraintWidget2 = this;
                    }
                }
            }
        }
        for (int i = 0; i < this.mChildren.size(); ++i) {
            final ConstraintWidget constraintWidget3 = this.mChildren.get(i);
            if (constraintWidget3 instanceof WidgetContainer) {
                final ConstraintWidget widget = ((WidgetContainer)constraintWidget3).findWidget(n, n2);
                if (widget != null) {
                    constraintWidget2 = widget;
                }
            }
            else {
                final int drawX2 = constraintWidget3.getDrawX();
                final int drawY2 = constraintWidget3.getDrawY();
                final int width2 = constraintWidget3.getWidth();
                final int height2 = constraintWidget3.getHeight();
                if (n >= drawX2 && n <= width2 + drawX2 && n2 >= drawY2 && n2 <= height2 + drawY2) {
                    constraintWidget2 = constraintWidget3;
                }
            }
        }
        return constraintWidget2;
    }
    
    public ArrayList<ConstraintWidget> findWidgets(int i, int size, final int n, final int n2) {
        final ArrayList<ConstraintWidget> list = new ArrayList<ConstraintWidget>();
        final Rectangle rectangle = new Rectangle();
        rectangle.setBounds(i, size, n, n2);
        ConstraintWidget e;
        Rectangle rectangle2;
        for (i = 0, size = this.mChildren.size(); i < size; ++i) {
            e = this.mChildren.get(i);
            rectangle2 = new Rectangle();
            rectangle2.setBounds(e.getDrawX(), e.getDrawY(), e.getWidth(), e.getHeight());
            if (rectangle.intersects(rectangle2)) {
                list.add(e);
            }
        }
        return list;
    }
    
    public ArrayList<ConstraintWidget> getChildren() {
        return this.mChildren;
    }
    
    public ConstraintWidgetContainer getRootConstraintContainer() {
        final ConstraintWidget parent = this.getParent();
        ConstraintWidgetContainer constraintWidgetContainer = null;
        ConstraintWidget parent2 = parent;
        if (this instanceof ConstraintWidgetContainer) {
            constraintWidgetContainer = (ConstraintWidgetContainer)this;
            parent2 = parent;
        }
        while (true) {
            final ConstraintWidgetContainer constraintWidgetContainer2 = (ConstraintWidgetContainer)parent2;
            if (constraintWidgetContainer2 == null) {
                break;
            }
            parent2 = constraintWidgetContainer2.getParent();
            if (!(constraintWidgetContainer2 instanceof ConstraintWidgetContainer)) {
                continue;
            }
            constraintWidgetContainer = constraintWidgetContainer2;
            parent2 = parent2;
        }
        return constraintWidgetContainer;
    }
    
    public void layout() {
        this.updateDrawPosition();
        final ArrayList<ConstraintWidget> mChildren = this.mChildren;
        if (mChildren == null) {
            return;
        }
        for (int size = mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer)constraintWidget).layout();
            }
        }
    }
    
    public void remove(final ConstraintWidget o) {
        this.mChildren.remove(o);
        o.setParent(null);
    }
    
    public void removeAllChildren() {
        this.mChildren.clear();
    }
    
    @Override
    public void reset() {
        this.mChildren.clear();
        super.reset();
    }
    
    @Override
    public void resetSolverVariables(final Cache cache) {
        super.resetSolverVariables(cache);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).resetSolverVariables(cache);
        }
    }
    
    @Override
    public void setOffset(int i, int size) {
        super.setOffset(i, size);
        for (size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).setOffset(this.getRootX(), this.getRootY());
        }
    }
    
    @Override
    public void updateDrawPosition() {
        super.updateDrawPosition();
        final ArrayList<ConstraintWidget> mChildren = this.mChildren;
        if (mChildren == null) {
            return;
        }
        for (int size = mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.setOffset(this.getDrawX(), this.getDrawY());
            if (!(constraintWidget instanceof ConstraintWidgetContainer)) {
                constraintWidget.updateDrawPosition();
            }
        }
    }
}
