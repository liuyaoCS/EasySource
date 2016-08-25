package com.ly.easysource.components.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;


public class MyActivity extends Activity{
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

}
