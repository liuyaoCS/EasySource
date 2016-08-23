package com.ly.easysource.core.remote;

import android.view.View;

import com.ly.easysource.core.client.MyWindowManager;
import com.ly.easysource.core.client.binder.IWindow;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class MyWindowState {
    final IWindow mClient;
    public void reportFocusChangedSerialized(boolean focused, boolean inTouchMode) {
        mClient.windowFocusChanged(focused, inTouchMode);
    }
    /**
     * @return true if this window desires key events.
     */
    public final boolean canReceiveKeys() {
        return isVisibleOrAdding()
                && (mViewVisibility == View.VISIBLE)
                && ((mAttrs.flags & MyWindowManager.LayoutParams.FLAG_NOT_FOCUSABLE) == 0);
    }

}
