package com.ly.easysource.window;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;

import com.ly.easysource.core.MyPhoneWindow;
import com.ly.easysource.core.MyWindowManager;
import com.ly.easysource.viewmechanism.MyView;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class MyDialog implements Window.Callback{
    private final MyWindowManager mWindowManager;
    private final MyPhoneWindow mWindow;
    MyView mDecor;
    /**
     * 1 创建window
     */
    Dialog(@NonNull Context context, @StyleRes int themeResId, boolean createContextThemeWrapper) {

        mWindowManager = (MyWindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final MyPhoneWindow w = new MyPhoneWindow(mContext);
        mWindow = w;
        w.setCallback(this);
        w.setOnWindowDismissedCallback(this);
        w.setWindowManager(mWindowManager, null, null);
        w.setGravity(Gravity.CENTER);

    }

    /**
     * 2 将dialog视图添加到DecorView中
     */
    public void setContentView(@LayoutRes int layoutResID) {
        mWindow.setContentView(layoutResID);
    }

    /**
     * 将DecorView添加到window并显示
     */
    public void show() {
        mDecor = mWindow.getDecorView();
        mWindowManager.addView(mDecor, l);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

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
