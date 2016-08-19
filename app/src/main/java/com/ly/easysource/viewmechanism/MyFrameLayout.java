package com.ly.easysource.viewmechanism;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class MyFrameLayout extends MyViewGroup{
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < count; i++) {
            final MyView child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            }
        }
    }
}

