package com.ly.easysource.components.activity;

import android.app.Activity;
import android.app.FragmentController;
import android.app.FragmentHostCallback;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;

import com.ly.easysource.components.activity.fragment.MyFragment;
import com.ly.easysource.components.activity.fragment.MyFragmentManager;


public class MyActivity extends Activity{
    private MyFragmentManager.FragmentManagerImpl mFragmentManager;
    //为了方便，其实为：private FragmentController mFragmentManager=FragmentController.createController(new HostCallbacks());
    //HostCallbacks继承自FragmentHostCallback（需要管理fragments的对象要继承这个抽象类）
    //    mHost    <- activity
    //    mContext <- activity
    


        /**
     * 只有activity对startActivity做了封装，其他组件启动都是contextImpl实现的
     *1 在activity里启动activity：
     *  startActivityForResult->mInstrumentation.execStartActivity->AMS
     *2 contextImpl里启动activity：
     *  startActivity->mInstrumentation.execStartActivity->AMS
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    //下面的函数是涉及到fragment
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.add(R.id.container,new MyFragment());
        ft.commit();
    }
    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBundle(WINDOW_HIERARCHY_TAG, mWindow.saveHierarchyState());
        //存储fragment，同时调用fragment的onSaveInstanceState
        //那如何恢复保存的fragment呢？可以在onCreate/onRestoreInstanceState里判断当savedInstanceState不为空时，用fragmentManager通过fragment的tag或者id恢复
        //由此说明，fragment的存储的系统默认做的，如果存储了，原来的fragment还在，这时候如果不判断outState，很有可能会重新创建fragment，造成重叠（旧fragment的show，hide已经失效）
        Parcelable p = mFragmentManager.saveAllState();
        if (p != null) {
            outState.putParcelable(FRAGMENTS_TAG, p);
        }
        getApplication().dispatchActivitySaveInstanceState(this, outState);
    }

}
