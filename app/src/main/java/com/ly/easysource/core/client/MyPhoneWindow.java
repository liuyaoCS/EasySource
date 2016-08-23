package com.ly.easysource.core.client;

import android.view.Window;

import com.ly.easysource.viewmechanism.MyFrameLayout;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class MyPhoneWindow{
    private  DecorView mDecor;

    public DecorView getDecorView() {
        return null;
    }

    public void setContentView(int layoutResID) {
    }

    public MyWindowManager getWindowManager() {
    }

    private final class DecorView extends MyFrameLayout {
        @Override
        public void onWindowFocusChanged(boolean hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);

            final Window.Callback cb = getCallback();
            if (cb != null && !isDestroyed() && mFeatureId < 0) {
                //回调activity
                cb.onWindowFocusChanged(hasWindowFocus);
            }

        }

    }
}
