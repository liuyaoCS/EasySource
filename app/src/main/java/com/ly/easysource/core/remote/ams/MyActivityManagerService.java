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
import java.util.List;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class MyActivityManagerService extends MyActivityManagerNative {
    private MyActivityStackSupervisor mStackSupervisor;
    final MyActiveServices mServices;
    final HashMap<IBinder, ReceiverList> mRegisteredReceivers = new HashMap<>();
    @Override
    public final void attachApplication(IApplicationThread thread) {
        thread.bindApplication(processName, appInfo, providers, app.instrumentationClass,
                profilerInfo, app.instrumentationArguments, app.instrumentationWatcher,
                app.instrumentationUiAutomationConnection, testMode, enableOpenGlTrace,
                enableTrackAllocation, isRestrictedBackupMode || !normalMode, app.persistent,
                new Configuration(mConfiguration), app.compat,
                getCommonServicesLocked(app.isolated),
                mCoreSettingsObserver.getCoreSettingsLocked());
    }
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
        //如果需要让未启动的应用接受消息，Intent需要添加Intent.FLAG_INCLUDE_STOPPED_PACKAGES
        //遗憾的是6.0以后不适用
        intent.addFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);

        final MyBroadcastQueue queue = broadcastQueueForIntent(intent);
        BroadcastRecord r = new BroadcastRecord(queue, intent, callerApp,
                callerPackage, callingPid, callingUid, resolvedType, requiredPermissions,
                appOp, brOptions, registeredReceivers, resultTo, resultCode, resultData,
                resultExtras, ordered, sticky, false, userId);
        queue.enqueueParallelBroadcastLocked(r);
        queue.scheduleBroadcastsLocked();
    }
    public final void publishContentProviders(IApplicationThread caller,
                                              List<ContentProviderHolder> providers) {

        final int N = providers.size();
        for (int i = 0; i < N; i++) {
            ContentProviderHolder src = providers.get(i);
            if (src == null || src.info == null || src.provider == null) {
                continue;
            }
            ContentProviderRecord dst = r.pubProviders.get(src.info.name);
            if (dst != null) {
                ComponentName comp = new ComponentName(dst.info.packageName, dst.info.name);
                mProviderMap.putProviderByClass(comp, dst);
                String names[] = dst.info.authority.split(";");
                for (int j = 0; j < names.length; j++) {
                    mProviderMap.putProviderByName(names[j], dst);
                }
            }
        }
    }
    public final ContentProviderHolder getContentProvider(
            IApplicationThread caller, String name, int userId, boolean stable) {
        //接下来，如果provider存在，直接返回
        ContentProviderRecord cpr = mProviderMap.getProviderByName(name, userId);
        //不存在，如果进程启动，调度安装provider；否则创建进程
            //等待进程安装完provider发布到此，清除mLaunchingProviders里相应的provider
        if (proc != null && proc.thread != null) {
            if (DEBUG_PROVIDER) Slog.d(TAG_PROVIDER,
                    "Installing in existing process " + proc);
            if (!proc.pubProviders.containsKey(cpi.name)) {
                checkTime(startTime, "getContentProviderImpl: scheduling install");
                proc.pubProviders.put(cpi.name, cpr);
                try {
                    proc.thread.scheduleInstallProvider(cpi);
                } catch (RemoteException e) {
                }
            }
        } else {
            proc = startProcessLocked(cpi.processName,
                    cpr.appInfo, false, 0, "content provider",
                    new ComponentName(cpi.applicationInfo.packageName,
                            cpi.name), false, false, false);

        }
        cpr.launchingApp = proc;
        mLaunchingProviders.add(cpr);
    }
}
