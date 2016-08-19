package com.ly.easysource.viewmechanism.inner;

import com.ly.easysource.viewmechanism.MyFrameLayout;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class MyPhoneWindow{
    private  DecorView mDecor;
    public DecorView getDecor(){
        return mDecor;
    }
    private final class DecorView extends MyFrameLayout {

    }
}
