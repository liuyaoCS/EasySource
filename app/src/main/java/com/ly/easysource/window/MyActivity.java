package com.ly.easysource.window;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.view.MotionEvent;
import android.view.Window;
import com.ly.easysource.core.client.MyPhoneWindow;
import com.ly.easysource.core.client.MyWindowManager;

public class MyActivity implements Window.Callback{
    private MyPhoneWindow mWindow;
    private MyWindowManager mWindowManager;

    final void attach(Context context, ActivityThread aThread,
                      Instrumentation instr, IBinder token, int ident,
                      Application application, Intent intent, ActivityInfo info
                      ) {
        attachBaseContext(context);

        mWindow = new MyPhoneWindow(this);
        mWindow.setCallback(this);
        mWindow.setOnWindowDismissedCallback(this);
        mWindow.setWindowManager(
                (MyWindowManager)context.getSystemService(Context.WINDOW_SERVICE),
                mToken, mComponent.flattenToString(),
                (info.flags & ActivityInfo.FLAG_HARDWARE_ACCELERATED) != 0);

        mWindowManager = mWindow.getWindowManager();

    }
    public void setContentView(@LayoutRes int layoutResID) {
        getWindow().setContentView(layoutResID);
    }
    public MyPhoneWindow getWindow() {
        return mWindow;
    }

    public MyWindowManager getWindowManager() {
        return mWindowManager;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    /**
     * 监听一个Activity加载完毕（完成渲染），此时可以
     * 1 获取view的尺寸参数
     * 2 显示PopUpWindow
     *      因为PopUpWindow需要一个anchor，而这个anchor肯定是activity里的某个view。
     *      虽然activity的token在回调onCreate的时候已经创建了，但是anchorView需要onAttachedToWindow后才有token
     *      而调用顺序一般为：onCreate->onResume->activity.onAttachedToWindow->anchorView.onAttachedToWindow
     *
     * 原理： 在handleResumeActivity里通过IWindowSession类型的mWindowSession远程调用Session，继而调用WMS添加window，
     *      之后updateFocusedWindowLocked通过消息机制调用IWindow mClient的windowFocusChanged，传回客户端
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }

    /**
     * 这个函数调用在onWindowFocusChanged之前,一般不建议使用！！
     * 但是activity的onAttachedToWindow会早于其中的view的onAttachedToWindow，所以这里面不能显示PopUpView
     */
    @Override
    public void onAttachedToWindow() {

    }

    @Override
    public void onDetachedFromWindow() {

    }
}
