package com.ly.easysource.core.remote.ams;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;

import com.ly.easysource.components.receiver.binder.IIntentReceiver;
import com.ly.easysource.components.service.binder.IServiceConnection;
import com.ly.easysource.core.client.MyActivityManagerNative;
import com.ly.easysource.core.client.binder.IApplicationThread;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class MyActivityManagerService extends MyActivityManagerNative {
    private MyActivityStackSupervisor mStackSupervisor;
    final MyActiveServices mServices;
    final HashMap<IBinder, ReceiverList> mRegisteredReceivers = new HashMap<>();
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
    public Intent registerReceiver(IApplicationThread caller, String callerPackage,
                                   IIntentReceiver receiver, IntentFilter filter, String permission, int userId) {
        mRegisteredReceivers.put(receiver.asBinder(), rl);
        BroadcastFilter bf = new BroadcastFilter(filter, rl, callerPackage,
                permission, callingUid, userId);
        rl.add(bf);
        mReceiverResolver.addFilter(bf);
    }
    public final int broadcastIntent(IApplicationThread caller,
                                     Intent intent, String resolvedType, IIntentReceiver resultTo,
                                     int resultCode, String resultData, Bundle resultExtras,
                                     String[] requiredPermissions, int appOp, Bundle options,
                                     boolean serialized, boolean sticky, int userId) {
        // By default broadcasts do not go to stopped apps.
        intent.addFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);

        final MyBroadcastQueue queue = broadcastQueueForIntent(intent);
        BroadcastRecord r = new BroadcastRecord(queue, intent, callerApp,
                callerPackage, callingPid, callingUid, resolvedType, requiredPermissions,
                appOp, brOptions, registeredReceivers, resultTo, resultCode, resultData,
                resultExtras, ordered, sticky, false, userId);
        queue.enqueueParallelBroadcastLocked(r);
        queue.scheduleBroadcastsLocked();
    }
}
