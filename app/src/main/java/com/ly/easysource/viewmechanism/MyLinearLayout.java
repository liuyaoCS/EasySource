package com.ly.easysource.viewmechanism;

import android.graphics.Canvas;

import com.ly.easysource.eventdispatch.*;

/**
 * Created by Administrator on 2016/8/22 0022.
 */
public class MyLinearLayout extends MyViewGroup{
    @Override
    /**
     * ViewGroup实现类里的onDraw就是绘制自己
     * 这点和onMeasure，onLayout不同，他们是调用child
     * ViewGroup实现类的调用child是dispatchDraw来实现的
     */
    protected void onDraw(Canvas canvas) {
        if (mDivider == null) {
            return;
        }

        if (mOrientation == VERTICAL) {
            drawDividersVertical(canvas);
        } else {
            drawDividersHorizontal(canvas);
        }
    }
}
