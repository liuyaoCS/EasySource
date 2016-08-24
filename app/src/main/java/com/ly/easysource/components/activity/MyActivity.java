package com.ly.easysource.components.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;

import com.ly.easysource.components.MyInstrumentation;
import com.ly.easysource.core.client.MyActivityThread;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class MyActivity extends Activity{
    private MyInstrumentation mInstrumentation;
    MyActivityThread mMainThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        MyInstrumentation.ActivityResult ar =
                mInstrumentation.execStartActivity(
                        this, mMainThread.getApplicationThread(), mToken, this,
                        intent, requestCode, options);
    }
}
