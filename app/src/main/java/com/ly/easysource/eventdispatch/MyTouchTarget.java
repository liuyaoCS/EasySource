package com.ly.easysource.eventdispatch;

import android.view.View;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class MyTouchTarget {
    // The touched child view.
    public View child;
    // The next target in the target list.一般情况下为空，我们的MyViewGroupSimple只考虑了为空的情况
    public MyTouchTarget next;

    public static MyTouchTarget obtain(View child) {
        return new MyTouchTarget();
    }

}
