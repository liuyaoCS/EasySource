package com.ly.easysource.core.remote;

import android.graphics.Rect;
import android.os.Message;

import com.ly.easysource.core.client.MyWindowManager;
import com.ly.easysource.core.remote.binder.IWindowManager;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class MyWindowManagerService extends IWindowManager.Stub{
    MyWindowState mCurrentFocus = null;
    public int addWindow(MySession mySession, IWindow window, int seq, MyWindowManager.LayoutParams attrs,
                         int viewVisibility, int displayId, Rect outContentInsets, Rect outStableInsets,
                         Rect outOutsets, InputChannel outInputChannel) {
        WindowToken token = mTokenMap.get(attrs.token);
        //检查token
        if(token==null){
            return WindowManagerGlobal.ADD_BAD_APP_TOKEN;
        }

        MyWindowState win = new MyWindowState(this, session, client, token,
                attachedWindow, appOp[0], seq, attrs, viewVisibility, displayContent);
        mCurrentFocus=win;
        boolean focusChanged = false;
        if (win.canReceiveKeys()) {
            focusChanged = updateFocusedWindowLocked(UPDATE_FOCUS_WILL_ASSIGN_LAYERS,
                    false /*updateInputWindows*/);
        }
    }
    private boolean updateFocusedWindowLocked(int mode, boolean updateInputWindows) {
        mH.sendEmptyMessage(H.REPORT_FOCUS_CHANGE);
    }
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case REPORT_FOCUS_CHANGE:
                mCurrentFocus.reportFocusChangedSerialized(true, mInTouchMode);
                break;
        }
    }
}
