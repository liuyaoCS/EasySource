package com.ly.easysource.viewmechanism;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.ArrayList;

public class MyView {
    int mMeasuredHeight;
    int mMeasuredWidth;
    public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
        onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }
    protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        mMeasuredWidth = measuredWidth;
        mMeasuredHeight = measuredHeight;
    }
    public final int getMeasuredHeight() {
        return mMeasuredHeight;
    }
    public final int getMeasuredWidth() {
        return mMeasuredWidth;
    }

    /**
     *AT_MOST/EXACTLY 返回view测量后的大小
     *UNSPECIFIED，系统内部测量 ,一般返回为0
     *  1 没有设置背景 则返回0（一般mMinWidth/mMinHeight为0,除非layout的xml中设定）
     *  2 设置了背景
     *      1）图片背景 返回图片尺寸 比如BitmapDrawable
     *      2）无图背景 返回0       比如ShapeDrawable
     */
    public static int getDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    protected int getSuggestedMinimumWidth() {
        return (mBackground == null) ? mMinWidth : max(mMinWidth, mBackground.getMinimumWidth());
    }
    protected int getSuggestedMinimumHeight() {
        return (mBackground == null) ? mMinHeight : max(mMinHeight, mBackground.getMinimumHeight());

    }

    public void layout(int l, int t, int r, int b) {
        if ((mPrivateFlags3 & PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT) != 0) {
            onMeasure(mOldWidthMeasureSpec, mOldHeightMeasureSpec);
            mPrivateFlags3 &= ~PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT;
        }

        int oldL = mLeft;
        int oldT = mTop;
        int oldB = mBottom;
        int oldR = mRight;

        boolean changed = setFrame(l, t, r, b);

        if (changed ) {
            onLayout(changed, l, t, r, b);
            ListenerInfo li = mListenerInfo;
            if (li != null && li.mOnLayoutChangeListeners != null) {
                int numListeners = li.mOnLayoutChangeListeners.size();
                for (int i = 0; i < numListeners; ++i) {
                    li.mOnLayoutChangeListeners.get(i).onLayoutChange(this, l, t, r, b, oldL, oldT, oldR, oldB);
                }
            }
        }
    }
    protected boolean setFrame(int left, int top, int right, int bottom) {
        boolean changed = false;

        if (mLeft != left || mRight != right || mTop != top || mBottom != bottom) {
            changed = true;

            mLeft = left;
            mTop = top;
            mRight = right;
            mBottom = bottom;
        }
        return changed;
    }
    /**
     *ViewGroup的实现类需要实现这个方法，调用children的layout
     */
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    /**
     * Manually render this view (and all of its children) to the given Canvas.
     * The view must have already done a full layout before this function is
     * called.  When implementing a view, implement
     * {@link #onDraw(android.graphics.Canvas)} instead of overriding this method.
     * If you do need to override this method, call the superclass version.
     *
     * @param canvas The Canvas to which the View is rendered.
     */
    public void draw(Canvas canvas) {
        /*
         * Draw traversal performs several drawing steps which must be executed
         * in the appropriate order:
         *
         *      1. Draw the background
         *      2. Draw view's content
         *      3. Draw children
         *      4. Draw decorations (scrollbars for instance)
         */

        // Step 1, draw the background, if needed
        if (!dirtyOpaque) {
            background.draw(canvas);
        }

        // skip step 2 & 4 if possible (common case)
        if (!verticalEdges && !horizontalEdges) {
            // Step 2, draw the content
            if (!dirtyOpaque) onDraw(canvas);

            // Step 3, draw the children
            dispatchDraw(canvas);

            // Step 4, draw decorations (scrollbars)
            onDrawScrollBars(canvas);

            return;
        }
    }
    protected void onDraw(Canvas canvas) {
    }
    /**
     * Called by draw to draw the child views. This may be overridden
     * by derived classes to gain control just before its children are drawn
     * (but after its own view has been drawn).
     * @param canvas the canvas on which to draw the view
     */
    protected void dispatchDraw(Canvas canvas) {

    }

    public static class MeasureSpec {
        private static final int MODE_SHIFT = 30;
        private static final int MODE_MASK = 0x3 << MODE_SHIFT;

        /**
         * 父容器不对View有任何限制，一般用于系统内部，表示一种测量状态
         */
        public static final int UNSPECIFIED = 0 << MODE_SHIFT;

        /**
         * 父容器已经精确计算出View的大小，为SpecSize，它对应于LayoutParams中match_parent和具体数值
         */
        public static final int EXACTLY = 1 << MODE_SHIFT;

        /**
         * view的大小不能超过SpecSize，它对应LayoutParams中wrap_content
         */
        public static final int AT_MOST = 2 << MODE_SHIFT;
        /**
         * Creates a measure specification based on the supplied size and mode.
         *
         * The mode must always be one of the following:
         * <ul>
         *  <li>{@link android.view.View.MeasureSpec#UNSPECIFIED}</li>
         *  <li>{@link android.view.View.MeasureSpec#EXACTLY}</li>
         *  <li>{@link android.view.View.MeasureSpec#AT_MOST}</li>
         * </ul>
         * @param size the size of the measure specification
         * @param mode the mode of the measure specification
         * @return the measure specification based on size and mode
         */
        public static int makeMeasureSpec(int size, int mode) {
            return (size & ~MODE_MASK) | (mode & MODE_MASK);
        }
        /**
         * Extracts the mode from the supplied measure specification.
         *
         * @param measureSpec the measure specification to extract the mode from
         * @return {@link android.view.View.MeasureSpec#UNSPECIFIED},
         *         {@link android.view.View.MeasureSpec#AT_MOST} or
         *         {@link android.view.View.MeasureSpec#EXACTLY}
         */
        public static int getMode(int measureSpec) {
            return (measureSpec & MODE_MASK);
        }

        /**
         * Extracts the size from the supplied measure specification.
         *
         * @param measureSpec the measure specification to extract the size from
         * @return the size in pixels defined in the supplied measure specification
         */
        public static int getSize(int measureSpec) {
            return (measureSpec & ~MODE_MASK);
        }

    }

    public void dispatchWindowFocusChanged(boolean hasFocus) {
        onWindowFocusChanged(hasFocus);
    }
    public void onWindowFocusChanged(boolean hasWindowFocus) {

    }
}
