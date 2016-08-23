package com.ly.easysource.core.client;


import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

import com.ly.easysource.window.MyActivity;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class MyActivityThread {
    MyWindowManager wm;

    private void handleLaunchActivity(MyActivityClientRecord r, Intent customIntent) {
        performLaunchActivity(r, customIntent);
    }

    /**
     * 1 activityThread将handleLaunchActivity中创建的PhoneWindow交给WindowManager管理，
     * WindowManager创建一个ViewRootImpl，管理window的添加，删除，更新，下面一添加为例：
     * 1）通过requestLayout渲染view
     * 2）随后通过binder将PhoneWindow添加到WindowManagerService上
     * 2 根据调用顺序 onResume在函数绘制之前调用，显然，onCreate/onResume都得不到view的测量参数
     */
    final void handleResumeActivity() {
        MyActivityClientRecord r = performResumeActivity(token, clearHide);

        wm = r.activity.getWindowManager();
        wm.addView(r.activity.getWindow().getDecorView(), r.window.getAttributes());
    }

    private MyActivity performLaunchActivity(MyActivityClientRecord r, Intent customIntent) {
        ComponentName component = r.intent.getComponent();
        java.lang.ClassLoader cl = r.packageInfo.getClassLoader();
        MyActivity activity = mInstrumentation.newActivity(cl, component.getClassName(), r.intent);

        Application app = r.packageInfo.makeApplication(false, mInstrumentation);
        activity.attach(appContext, this, getInstrumentation(), r.token,
                r.ident, app, r.intent, r.activityInfo);

        mInstrumentation.callActivityOnCreate(activity, r.state);
        if (r.state != null) {
            mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state);
        }
        return r.activity;
    }
    public final MyActivityClientRecord performResumeActivity(IBinder token, boolean clearHide) {
        MyActivityClientRecord r = mActivities.get(token);
        r.activity.performResume();
        return r;
    }
}
