package com.ly.easysource.window;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.view.MotionEvent;
import android.view.Window;

import com.ly.easysource.core.MyPhoneWindow;
import com.ly.easysource.core.MyWindowManager;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
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
     *
     * 注意：具体调用是在W类里调的，和binder有关，学完handler进一步分析？？
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }

    @Override
    public void onAttachedToWindow() {

    }

    @Override
    public void onDetachedFromWindow() {

    }
}
