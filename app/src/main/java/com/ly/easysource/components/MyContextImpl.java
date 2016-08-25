package com.ly.easysource.components;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

import com.ly.easysource.components.receiver.binder.IIntentReceiver;
import com.ly.easysource.components.service.binder.IServiceConnection;
import com.ly.easysource.core.client.MyActivityManagerNative;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class MyContextImpl {
    final MyLoadedApk mPackageInfo;
    @Override
    public void startActivity() {
         MyActivityManagerNative.getDefault()
                .startActivities(whoThread, who.getBasePackageName(), intents, resolvedTypes,
                        token, options, userId);
    }
    @Override
    public ComponentName startService(Intent service) {
        ComponentName cn = MyActivityManagerNative.getDefault().startService(
                mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(
                        getContentResolver()), getOpPackageName(), user.getIdentifier());
    }
    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        IServiceConnection sd = mPackageInfo.getServiceDispatcher(conn, getOuterContext(),
                mMainThread.getHandler(), flags);

        int res = MyActivityManagerNative.getDefault().bindService(
                mMainThread.getApplicationThread(), getActivityToken(), service,
                service.resolveTypeIfNeeded(getContentResolver()),
                sd, flags, getOpPackageName(), user.getIdentifier());
        return  res!=0;
    }
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        IIntentReceiver rd = mPackageInfo.getReceiverDispatcher(
                receiver, context, scheduler,
                mMainThread.getInstrumentation(), true);
        return MyActivityManagerNative.getDefault().registerReceiver(
                mMainThread.getApplicationThread(), mBasePackageName,
                rd, filter, broadcastPermission, userId);
    }
    @Override
    public void sendBroadcast(Intent intent) {
        MyActivityManagerNative.getDefault().broadcastIntent(
                mMainThread.getApplicationThread(), intent, resolvedType, null,
                Activity.RESULT_OK, null, null, receiverPermissions, appOp, null, false, false,
                getUserId());
    }


}
