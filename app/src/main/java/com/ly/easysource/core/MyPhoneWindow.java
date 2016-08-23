package com.ly.easysource.core;

import com.ly.easysource.viewmechanism.MyFrameLayout;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class MyPhoneWindow{
    private  DecorView mDecor;
    public DecorView getDecorView(){
        return mDecor;
    }

    public void setContentView(int layoutResID) {
    }

    public MyWindowManager getWindowManager() {
    }

    private final class DecorView extends MyFrameLayout {

    }
}
