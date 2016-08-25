package com.ly.easysource.core.remote.ams;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.ly.easysource.components.MyLoadedApk;
import com.ly.easysource.core.remote.binder.IServiceConnection;
import com.ly.easysource.core.client.binder.IApplicationThread;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class MyActiveServices {
    IApplicationThread thread;
    public final void realStartServiceLocked(ServiceRecord r,
                                              ProcessRecord app, boolean execInFg) throws RemoteException {
        thread.scheduleCreateService(r, r.serviceInfo,
                mAm.compatibilityInfoForPackageLocked(r.serviceInfo.applicationInfo),
                app.repProcState);
        thread.scheduleServiceArgs(r, si.taskRemoved, si.id, flags, si.intent);
    }

    public int bindServiceLocked(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String callingPackage, int userId) {
        thread.scheduleBindService(r, i.intent.getIntent(), rebind,
                r.app.repProcState);
    }

    public void publishServiceLocked(IBinder token, Intent intent, IBinder service) {
        MyLoadedApk.ServiceDispatcher sd=null;
        sd.mIServiceConnection.connected(r.name, service);
    }
}
