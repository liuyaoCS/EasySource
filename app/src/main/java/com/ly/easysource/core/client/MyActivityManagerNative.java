package com.ly.easysource.core.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ly.easysource.components.receiver.binder.IIntentReceiver;
import com.ly.easysource.components.service.binder.IServiceConnection;
import com.ly.easysource.core.client.binder.IApplicationThread;
import com.ly.easysource.core.remote.binder.IActivityManager;

import android.app.core.ServiceManager;
import android.app.core.Singleton;


public class MyActivityManagerNative extends Binder implements IActivityManager {
    private IBinder mRemote;//ActivityManagerProxy

    private static final Singleton<IActivityManager> gDefault = new Singleton<IActivityManager>() {
        protected IActivityManager create() {
            IBinder b = ServiceManager.getService("activity");
            if (false) {
                Log.v("ActivityManager", "default service binder = " + b);
            }
            IActivityManager am = asInterface(b);
            if (false) {
                Log.v("ActivityManager", "default service = " + am);
            }
            return am;
        }
    };

    public static IActivityManager getDefault() {
        return gDefault.get();
    }

    public void attachApplication(IApplicationThread app) throws RemoteException {
        mRemote.transact(ATTACH_APPLICATION_TRANSACTION, data, reply, 0);
    }

    @Override
    public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
        mRemote.transact(START_ACTIVITY_TRANSACTION, data, reply, 0);
    }

    public ComponentName startService(IApplicationThread caller, Intent service,
                                      String resolvedType, String callingPackage, int userId) throws RemoteException {
        mRemote.transact(START_SERVICE_TRANSACTION, data, reply, 0);
    }

    public int bindService(IApplicationThread caller, IBinder token,
                           Intent service, String resolvedType, IServiceConnection connection,
                           int flags, String callingPackage, int userId) throws RemoteException {
        mRemote.transact(BIND_SERVICE_TRANSACTION, data, reply, 0);
    }

    public Intent registerReceiver(IApplicationThread caller, String packageName,
                                   IIntentReceiver receiver,
                                   IntentFilter filter, String perm, int userId) throws RemoteException {
        mRemote.transact(REGISTER_RECEIVER_TRANSACTION, data, reply, 0);
    }

    public int broadcastIntent(IApplicationThread caller,
                               Intent intent, String resolvedType, IIntentReceiver resultTo,
                               int resultCode, String resultData, Bundle map,
                               String[] requiredPermissions, int appOp, Bundle options, boolean serialized,
                               boolean sticky, int userId) throws RemoteException {
        mRemote.transact(BROADCAST_INTENT_TRANSACTION, data, reply, 0);
    }
}
