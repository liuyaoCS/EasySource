package com.ly.easysource.components.service;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class MyService extends Service{

    public final void attach(
            Context context,
            ActivityThread thread, String className, IBinder token,
            Application application, Object activityManager) {
        attachBaseContext(context);

    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    /**
     * 直接startService的方法调用onStartCommand，适合远程复杂计算
     *由activityThread里的回调可见，onStart已经废弃，调用的是这个onStartCommand
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     *bindService调用onBind，适合远程调用Service中的方法，当然这个方法也可以是复杂计算
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

}
