package com.ly.easysource.eventdispatch.inner;

import android.view.Window;
import android.widget.FrameLayout;

import com.ly.easysource.eventdispatch.MyFrameLayout;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class MyPhoneWindow extends Window {
    private  DecorView mDecor;
    public DecorView getDecor(){
        return mDecor;
    }
    private final class DecorView extends MyFrameLayout {

    }
}
