package com.ly.easysource.viewmechanism;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.LayoutAnimationController;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public abstract  class MyViewGroup extends MyView  implements ViewParent {
    /**
     * 各个实现类只需要实现这个函数
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     *虽然ViewGroup中没有measure，但是它有measureChildXXX，其实就是measure的封装。
     *具体的ViewGroup实现只有通过onMeasure来操作这个函数。
     */
    protected void measureChildWithMargins(MyView child,
                                           int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
    /**
     *父容器约束+View自身的LayoutParams=MeasureSpec
     * 1 DecorView 父容器约束指的是窗口尺寸
     * 2 普通view  父容器约束指的是父容器的MeasureSpec
     * @param spec：父容器MeasureSpec padding:父容器已经占用的空间大小=margin+padding
     * @param childDimension：child自身的LayoutParams
     * @return 不考虑UNSPECIFIED，一般为系统行为
     *      1 当view采用固定高宽时，    EXACTLY+childSize
     *      2 当view采用match_parent时，parentSpecMode+parentSize
     *      3 当view采用wrap_content时，AT_MOST+parentSize
     *          直接继承View的自定义控件要重写onMeasure，并设置wrap_content时的自身大小，否则它的大小就如上parentSize
     */
    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        int size = Math.max(0, specSize - padding);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
            // Parent has imposed an exact size on us
            case MeasureSpec.EXACTLY:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size. So be it.
                    resultSize = size;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent has imposed a maximum size on us
            case MeasureSpec.AT_MOST:
                if (childDimension >= 0) {
                    // Child wants a specific size... so be it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size, but our size is not fixed.
                    // Constrain child to not be bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent asked to see how big we want to be
            case MeasureSpec.UNSPECIFIED:
                if (childDimension >= 0) {
                    // Child wants a specific size... let him have it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size... find out how big it should
                    // be
                    resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                    resultMode = MeasureSpec.UNSPECIFIED;
                } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size.... find out how
                    // big it should be
                    resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                    resultMode = MeasureSpec.UNSPECIFIED;
                }
                break;
        }
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }
    @Override
    public final void layout(int l, int t, int r, int b) {
        if (!mSuppressLayout && (mTransition == null || !mTransition.isChangingLayout())) {
            if (mTransition != null) {
                mTransition.layoutChange(this);
            }
            super.layout(l, t, r, b);
        } else {
            // record the fact that we noop'd it; request layout when transition finishes
            mLayoutCalledWhileSuppressed = true;
        }
    }
    @Override
    protected abstract void onLayout(boolean changed, int l, int t, int r, int b);
    public final void invalidateChild(View child, final Rect dirty) {
        ViewParent parent=this;
        do{
            //一直上溯，直到viewRootImpl
            parent = parent.invalidateChildInParent(location, dirty);
        }while(parent!=null);
    }
    public ViewParent invalidateChildInParent(final int[] location, final Rect dirty) {
        if ((mPrivateFlags & PFLAG_DRAWN) == PFLAG_DRAWN ||
                (mPrivateFlags & PFLAG_DRAWING_CACHE_VALID) == PFLAG_DRAWING_CACHE_VALID) {
            return mParent;
        }
        return null;
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        final int count = mChildrenCount;
        final View[] children = mChildren;

        for (int i = 0; i < count; i++) {
            final View child = children[i];
            if ((child.mViewFlags & VISIBILITY_MASK) == VISIBLE || child.getAnimation() != null) {
                drawChild(canvas, child, drawingTime);
            }
        }

    }
    protected boolean drawChild(Canvas canvas, MyView child, long drawingTime) {
        return child.draw(canvas, this, drawingTime);
    }
    @Override
    void dispatchAttachedToWindow(AttachInfo info, int visibility) {
        super.dispatchAttachedToWindow(info, visibility);
        final int count = mChildrenCount;
        final View[] children = mChildren;
        for (int i = 0; i < count; i++) {
            final MyView child = children[i];
            child.dispatchAttachedToWindow(info,
                    combineVisibility(visibility, child.getVisibility()));
        }
    }
    @Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        super.dispatchWindowFocusChanged(hasFocus);
        final int count = mChildrenCount;
        final MyView[] children = mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchWindowFocusChanged(hasFocus);
        }
    }
}
