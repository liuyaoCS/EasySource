package com.ly.easysource.viewmechanism.inner;


/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class MyActivityThread {
    MyWindowManager wm;
    final void handleResumeActivity(){
        MyWindowManager.LayoutParams l = r.window.getAttributes();
        wm.addView(new MyPhoneWindow().getDecor(), l);
    }
}
