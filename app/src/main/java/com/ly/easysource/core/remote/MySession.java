package com.ly.easysource.core.remote;

import android.graphics.Rect;
import android.view.WindowManager;

import com.ly.easysource.core.remote.MyWindowManagerService;
import com.ly.easysource.core.remote.binder.IWindowSession;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class MySession extends IWindowSession.Stub {
    final MyWindowManagerService mService;
    @Override
    public int addToDisplay(IWindow window, int seq, WindowManager.LayoutParams attrs,
                            int viewVisibility, int displayId, Rect outContentInsets, Rect outStableInsets,
                            Rect outOutsets, InputChannel outInputChannel) {
        return mService.addWindow(this, window, seq, attrs, viewVisibility, displayId,
                outContentInsets, outStableInsets, outOutsets, outInputChannel);
    }

}
