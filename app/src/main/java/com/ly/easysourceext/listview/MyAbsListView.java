package com.ly.easysourceext.listview;

import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListAdapter;

/**
 * Created by Administrator on 2016/9/8 0008.
 */
public class MyAbsListView extends AbsListView{
    ListAdapter mAdapter;
    AdapterDataSetObserver mDataSetObserver;

    @Override
    protected void onAttachedToWindow() {

        if (mAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
            // Data may have changed while we were detached. Refresh.
            mDataChanged = true;
            mOldItemCount = mItemCount;
            mItemCount = mAdapter.getCount();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        if (changed) {
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).forceLayout();
            }
            mRecycler.markChildrenDirty();
        }

        layoutChildren();
    }
    /**
     * Subclasses must override this method to layout their children.
     */
    protected void layoutChildren() {
    }
}
