package com.ly.easysource.components;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.UserHandle;

import com.ly.easysource.core.remote.binder.IServiceConnection;
import com.ly.easysource.core.client.MyActivityManagerNative;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class MyContextImpl {
    final MyLoadedApk mPackageInfo;
    @Override
    public ComponentName startService(Intent service) {
        return startServiceCommon(service, mUser);
    }
    private ComponentName startServiceCommon(Intent service, UserHandle user) {
        ComponentName cn = MyActivityManagerNative.getDefault().startService(
                mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(
                        getContentResolver()), getOpPackageName(), user.getIdentifier());
    }

    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return bindServiceCommon(service, conn, flags, Process.myUserHandle());
    }
    private boolean bindServiceCommon(Intent service, ServiceConnection conn, int flags,
                                      UserHandle user) {

        IServiceConnection sd = mPackageInfo.getServiceDispatcher(conn, getOuterContext(),
                mMainThread.getHandler(), flags);

        int res = MyActivityManagerNative.getDefault().bindService(
                mMainThread.getApplicationThread(), getActivityToken(), service,
                service.resolveTypeIfNeeded(getContentResolver()),
                sd, flags, getOpPackageName(), user.getIdentifier());
        return  res!=0;
    }
}
