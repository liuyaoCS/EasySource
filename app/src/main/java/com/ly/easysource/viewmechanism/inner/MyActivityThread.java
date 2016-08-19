package com.ly.easysource.viewmechanism.inner;


/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class MyActivityThread {
    MyWindowManager wm;

    /**
     *
     *根据调用顺序 onResume在函数绘制之前调用，显然，onCreate/onResume都得不到view的测量参数
     */
    final void handleResumeActivity(){
        ActivityClientRecord r = performResumeActivity(token, clearHide);
        MyWindowManager.LayoutParams l = r.window.getAttributes();
        wm.addView(new MyPhoneWindow().getDecor(), l);
    }
}
