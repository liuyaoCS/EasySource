package com.ly.easysource.viewmechanism;

import android.view.View;
import android.widget.RelativeLayout;

import static android.view.View.GONE;

/**
 * Created by liuyao on 2017/2/15 0015.
 */

public class MyRelativeLayout {
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //  The layout has actually already been performed and the positions
        //  cached.  Apply the cached values to the children.
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                RelativeLayout.LayoutParams st =
                        (RelativeLayout.LayoutParams) child.getLayoutParams();
                child.layout(st.mLeft, st.mTop, st.mRight, st.mBottom);
            }
        }
    }
}
