package com.ly.easysource.components.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.ly.easysource.components.MyContextImpl;
import com.ly.easysource.components.MyInstrumentation;
import com.ly.easysource.core.client.MyActivityThread;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class MyActivity extends Activity{
    private MyInstrumentation mInstrumentation;
    MyActivityThread mMainThread;
    MyContextImpl mBase;

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

    @Override
    public ComponentName startService(Intent service) {
        return mBase.startService(service);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return mBase.bindService(service, conn, flags);
    }
}
