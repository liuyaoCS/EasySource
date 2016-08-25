package com.ly.easysource.core.remote.ams;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;

import com.ly.easysource.core.remote.binder.IServiceConnection;
import com.ly.easysource.core.client.MyActivityManagerNative;
import com.ly.easysource.core.client.binder.IApplicationThread;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class MyActivityManagerService extends MyActivityManagerNative {
    private MyActivityStackSupervisor mStackSupervisor;
    final MyActiveServices mServices;

    @Override
    public final int startActivity(IApplicationThread caller, String callingPackage,
                                   Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode,
                                   int startFlags, ProfilerInfo profilerInfo, Bundle options) {
        return mStackSupervisor.realStartActivityLocked(ProcessRecord app);
    }
    public ComponentName startService(IApplicationThread caller, Intent service,
                                      String resolvedType, String callingPackage, int userId)
            throws TransactionTooLargeException {
       return mServices.realStartServiceLocked();
    }

    @Override
    public int bindService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String callingPackage, int userId) throws RemoteException {
        return mServices.bindServiceLocked(caller, token, service,
                resolvedType, connection, flags, callingPackage, userId);
    }
    public void publishService(IBinder token, Intent intent, IBinder service) {

        mServices.publishServiceLocked(token, intent, service);
    }
}
