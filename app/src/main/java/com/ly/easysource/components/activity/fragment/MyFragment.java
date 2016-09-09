package com.ly.easysource.components.activity.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *FragmentActivity是support.v4包里的，对应的Fragment也要是v4包里的
 * 现在我们的应用都在3.0以上了，建议直接使用android.app里面的Activity和Fragment
 */
public class MyFragment extends Fragment{
    static final int INITIALIZING = 0;     // Not yet created.
    static final int CREATED = 1;          // Created.
    static final int ACTIVITY_CREATED = 2; // The activity has finished its creation.
    static final int STOPPED = 3;          // Fully created, not started.
    static final int STARTED = 4;          // Created and started, not resumed.
    static final int RESUMED = 5;          // Created started and resumed.

    void performCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            Parcelable p = savedInstanceState.getParcelable(Activity.FRAGMENTS_TAG);
            if (p != null) {
                mChildFragmentManager.dispatchCreate();
            }
        }
    }

    View performCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * activity里存储和恢复fragment本身，这里存储和恢复开发者需要存取的其他状态信息
     *注意Fragment没有onRestoreInstanceState这个函数，如何恢复状态？官网如下：
     * You can retain the state of a fragment using a Bundle, in case the activity's process is killed and you need to restore the fragment state when the activity is recreated.
     * You can save the state during the fragment's onSaveInstanceState() callback
     * and restore it during either onCreate(), onCreateView(), or onActivityCreated()
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Fragment里面直接调用startActivityForResult，最后会回调到这onActivityResult
     * 如果要用Activity的调起，应该是getActivity().startActivityForResult,最后会回调activity的onActivityResult
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //需要LayoutInflater加载view，注意
        // 一定要把attachToRoot置为false，因为这里返回的view会被自动添加到container；
        // 如果不设置false，可以返回null(super.onCreateView就是返回的null),但是这样写不规范。
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     *onCreateView调用完后立即调用,之后
     *  1 如果activity创建完了就调用onActivityCreated（onClick触发创建Fragment）
     *  2 否则等到activity创建完成调用onActivityCreated(onCreate触发创建Fragment)
     *  实际上，activity->onCreate之后立即调用mFragments->dispatchOnCreate,所以这个时候其实系统对activity的创建已经完成，用户onCreate的内容还没有完成，
     *  等onCreate调用完毕之后，立即调用mFragments->dispatchOnCreate，从而激发onActivityCreated，然后->onStart->onResume
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     *如果需要系统级别的activity信息，则不一定要等到这个函数，之前的任何一个函数都可以；
     * 如果需要用户自定义在activity->onCreate中的信息，则一定要要等到这个函数
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
