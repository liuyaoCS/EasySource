package com.ly.easysource.core.client;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ly.easysource.core.client.binder.IApplicationThread;
import com.ly.easysource.core.remote.binder.IActivityManager;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class MyActivityManagerNative extends Binder implements IActivityManager {
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
    @Override
    public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
        mRemote.transact(START_ACTIVITY_TRANSACTION, data, reply, 0);
        return result;
    }

    public static IActivityManager getDefault() {
        return gDefault.get();
    }
}
