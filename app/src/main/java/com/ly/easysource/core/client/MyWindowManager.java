package com.ly.easysource.core.client;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;

import com.ly.easysource.viewmechanism.MyView;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class MyWindowManager implements ViewManager{

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        MyViewRootImpl root = new MyViewRootImpl(view.getContext(), display);
        root.setView(view, wparams, panelParentView);
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {

    }

    @Override
    public void removeView(View view) {

    }

    public static class LayoutParams {
    }
}
