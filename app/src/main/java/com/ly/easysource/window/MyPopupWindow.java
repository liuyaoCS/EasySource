package com.ly.easysource.window;

import android.content.Context;
import android.os.IBinder;
import android.view.View;

import com.ly.easysource.core.MyWindowManager;

/**
 * 虽然没有window，但是依然是类似的模式,唯一区别在于：
 * showXXX方法不能在onCreate或者onResume中调用，否则token会为空，报BadTokenException
 */
public class MyPopupWindow {
    private MyWindowManager myWindowManager;
    private View mContentView;

    public PopupWindow(View contentView, int width, int height, boolean focusable) {
        if (contentView != null) {
            mContext = contentView.getContext();
            myWindowManager = (MyWindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }

        setContentView(contentView);
    }
    public void setContentView(View contentView) {
        if (isShowing()) {
            return;
        }
        mContentView = contentView;
    }
    public void showAtLocation(IBinder token, int gravity, int x, int y) {
        if (isShowing() || mContentView == null) {
            return;
        }

        mIsShowing = true;
        mDecorView = createDecorView(mContentView);

        final MyWindowManager.LayoutParams p = createPopupLayoutParams(token);
        invokePopup(p);
    }
    private PopupDecorView createDecorView(View contentView) {
        final ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
        final int height;
        if (layoutParams != null && layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            height = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        final PopupDecorView decorView = new PopupDecorView(mContext);
        decorView.addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT, height);

        return decorView;
    }
    private void invokePopup(MyWindowManager.LayoutParams p) {
        final PopupDecorView decorView = mDecorView;
        mWindowManager.addView(decorView, p);
    }
}
