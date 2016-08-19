package com.ly.easysource.viewmechanism.inner;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.ly.easysource.viewmechanism.MyView;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class MyWindowManager {
    @Override
    public void addView(@NonNull MyView view, @NonNull ViewGroup.LayoutParams params) {
        MyViewRootImpl root = new MyViewRootImpl(view.getContext(), display);
        root.setView(view, wparams, panelParentView);
    }

    public class LayoutParams {
    }
}
