package com.ly.easysource.core.client;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import com.ly.easysource.viewmechanism.MyFrameLayout;
import com.ly.easysourceext.layoutinflater.MyLayoutInflater;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class MyPhoneWindow{
    private  DecorView mDecor;
    private MyLayoutInflater mLayoutInflater;
    private ViewGroup mContentParent;

    public DecorView getDecorView() {
        return null;
    }

    public void setContentView(int layoutResID) {
        if (mContentParent == null) {
            installDecor();
        } else if (!hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
            mContentParent.removeAllViews();
        }
        mLayoutInflater.inflate(layoutResID, mContentParent);
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
