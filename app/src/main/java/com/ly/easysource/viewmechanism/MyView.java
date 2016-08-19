package com.ly.easysource.viewmechanism;


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
}
