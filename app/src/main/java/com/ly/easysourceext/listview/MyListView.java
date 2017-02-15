package com.ly.easysourceext.listview;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * ListView内部维护了
 *  mAdapter
 *  mDataSetObserver--订阅者（AbsListView 中）
 *
 * Adapter采用适配器模式：将ListView需要的关于item View接口抽象到Adapter对象，Adapter统一将Item View输出到view供ListView布局调用
 * Adapter本身作为内部也维护了
 *  mDataSetObservable--发布者（BaseAdapter中），所以notifyDataSetChanged也由有发布者调用。
 */
public class MyListView extends MyAbsListView {
    public void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;
        if(mAdapter!=null){
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
        requestLayout();
    }
    @Override
    protected void layoutChildren() {
        switch (mLayoutMode) {
            case LAYOUT_FORCE_BOTTOM:
                sel = fillUp(mItemCount - 1, childrenBottom);
                break;
            case LAYOUT_FORCE_TOP:
                sel = fillDown(mFirstPosition,childrenTop);
                break;
        }
    }
    //从上到下布局view
    private View fillDown(int pos, int nextTop) {
        View selectedView = null;
        while (nextTop < end && pos < mItemCount) {

            View child = makeAndAddView(pos, nextTop, true, mListPadding.left, selected);
            pos++;
        }
        return selectedView;
    }
    private View makeAndAddView(int position, int y, boolean flow, int childrenLeft,
                                boolean selected) {
        View child;
        // Make a new view for this position, or convert an unused view if possible
        child = obtainView(position, mIsScrap);
        // This needs to be positioned and measured
        setupChild(child, position, y, flow, childrenLeft, selected, mIsScrap[0]);

        return child;
    }
    View obtainView(int position, boolean[] isScrap) {

        final View scrapView = mRecycler.getScrapView(position);
        final View child ;
        if (scrapView != null) {
            child=mAdapter.getView(position,scrapView,this);
        }else{
            child=mAdapter.getView(position,null,this);
        }

        return child;
    }

}
