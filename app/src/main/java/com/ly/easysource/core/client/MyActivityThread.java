package com.ly.easysource.core.client;


import android.annotation.TargetApi;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.ArrayMap;

import com.ly.easysource.components.MyContextImpl;
import com.ly.easysource.components.MyInstrumentation;
import com.ly.easysource.components.provider.binder.IContentProvider;
import com.ly.easysource.components.service.MyService;
import com.ly.easysource.core.client.binder.IApplicationThread;
import com.ly.easysource.components.receiver.binder.IIntentReceiver;
import com.ly.easysource.core.remote.binder.IActivityManager;
import com.ly.easysource.window.MyActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class MyActivityThread {
    final ArrayMap<IBinder, MyService> mServices = new ArrayMap<>();
    final ArrayMap<IBinder, MyActivityClientRecord> mActivities = new ArrayMap<>();

    final ApplicationThread mAppThread = new ApplicationThread();
    Instrumentation mInstrumentation;
    MyWindowManager wm;

    final H mH = new H();

    public static void main(String[] args) {
        Looper.prepareMainLooper();

        MyActivityThread thread = new MyActivityThread();
        // ams.attachApplication-> activityThread.bindApplication
        // 1) 完成ContextImpl环境创建
        //        后期activity的attach就可以拿到这个环境的引用，赋给自己的mBase
        // 2）完成Instrumentation工具创建
        //        后期mInstrumentation可以辅助activity application的创建
        // 3）创建Application
        // 4) 创建Provider
        // 5）Application->oncreate
        thread.attach(false);

        Looper.loop();
    }

    private void attach(boolean b) {
        final IActivityManager mgr = MyActivityManagerNative.getDefault();
        try {
            mgr.attachApplication(mAppThread);
        } catch (RemoteException ex) {
            // Ignore
        }
    }

    private class ApplicationThread extends Binder
            implements IApplicationThread {

        public final void bindApplication(String processName, ApplicationInfo appInfo,
                                          List<ProviderInfo> providers, ComponentName instrumentationName,
                                          ProfilerInfo profilerInfo, Bundle instrumentationArgs,
                                          IInstrumentationWatcher instrumentationWatcher,
                                          IUiAutomationConnection instrumentationUiConnection, int debugMode,
                                          boolean enableOpenGlTrace, boolean trackAllocation, boolean isRestrictedBackupMode,
                                          boolean persistent, Configuration config, CompatibilityInfo compatInfo,
                                          Map<String, IBinder> services, Bundle coreSettings) {
            mH.sendMessage(H.BIND_APPLICATION, data);
        }
        @Override
        public final void scheduleLaunchActivity() {
            mH.sendMessage(H.LAUNCH_ACTIVITY, r);
        }
        public final void scheduleResumeActivity(IBinder token, int processState,
                                                 boolean isForward, Bundle resumeArgs) {
            mH.sendMessage(H.RESUME_ACTIVITY, token, isForward ? 1 : 0);
        }
        public final void schedulePauseActivity(IBinder token, boolean finished,
                                                boolean userLeaving, int configChanges, boolean dontReport) {
            mH.sendMessage();
        }

        public final void scheduleStopActivity(IBinder token, boolean showWindow,
                                               int configChanges) {
            mH.sendMessage();
        }
        public final void scheduleDestroyActivity(IBinder token, boolean finishing,
                                                  int configChanges) {
            mH.sendMessage(H.DESTROY_ACTIVITY, token, finishing ? 1 : 0, configChanges);

        }
        public final void scheduleCreateService(IBinder token,
                                                ServiceInfo info, CompatibilityInfo compatInfo, int processState) {

            mH.sendMessage(H.CREATE_SERVICE, s);
        }
        public final void scheduleServiceArgs(IBinder token, boolean taskRemoved, int startId,
                                              int flags ,Intent args) {

            mH.sendMessage(H.SERVICE_ARGS, s);
        }
        public final void scheduleBindService(IBinder token, Intent intent,
                                              boolean rebind, int processState) {
            mH.sendMessage(H.BIND_SERVICE, s);
        }

        public final void scheduleUnbindService(IBinder token, Intent intent) {
            mH.sendMessage(H.UNBIND_SERVICE, s);
        }
        public void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent,
                                               int resultCode, String dataStr, Bundle extras, boolean ordered,
                                               boolean sticky, int sendingUser, int processState) throws RemoteException {
            receiver.performReceive(intent, resultCode, dataStr, extras, ordered,
                    sticky, sendingUser);
        }
        @Override
        public void scheduleInstallProvider(ProviderInfo provider) {
            mH.sendMessage(H.INSTALL_PROVIDER, provider);
        }

    }
    private class H extends Handler {
        private static final int BIND_APPLICATION = 0;
        private static final int LAUNCH_ACTIVITY = 1;
        private static final int RESUME_ACTIVITY = 2;
        public static final int STOP_ACTIVITY_HIDE  = 104;
        private static final int CREATE_SERVICE = 3;
        private static final int SERVICE_ARGS = 4;
        private static final int BIND_SERVICE = 5;
        private static final int UNBIND_SERVICE = 6;
        private static final int INSTALL_PROVIDER = 7;
        private static final int NEW_INTENT = 8;
        private static final int ACTIVITY_CONFIGURATION_CHANGED = 9;
        private static final int DESTROY_ACTIVITY = 10;


        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BIND_APPLICATION:
                    AppBindData data = (AppBindData)msg.obj;
                    handleBindApplication(data);
                    break;
                case LAUNCH_ACTIVITY: {
                    handleLaunchActivity(r, null);
                } break;
                case RESUME_ACTIVITY:
                    handleResumeActivity((IBinder) msg.obj, true, msg.arg1 != 0, true);
                    break;
                case STOP_ACTIVITY_HIDE:
                    handleStopActivity((IBinder)msg.obj, false, msg.arg2);
                    break;

                case CREATE_SERVICE:
                    handleCreateService((CreateServiceData)msg.obj);
                    break;
                case SERVICE_ARGS:
                    handleServiceArgs((ServiceArgsData)msg.obj);
                    break;
                case BIND_SERVICE:
                    handleBindService((BindServiceData)msg.obj);
                    break;
                case UNBIND_SERVICE:
                    handleUnbindService((BindServiceData)msg.obj);
                    break;
                case STOP_SERVICE:
                    handleStopService((IBinder)msg.obj);
                    break;

                case RECEIVER:
                    handleReceiver((ReceiverData)msg.obj);
                    break;
                case DISPATCH_PACKAGE_BROADCAST:
                    handleDispatchPackageBroadcast(msg.arg1, (String[])msg.obj);
                    break;

                case INSTALL_PROVIDER:
                    handleInstallProvider((ProviderInfo) msg.obj);
                    break;

                case NEW_INTENT:
                    handleNewIntent((NewIntentData)msg.obj);
                    break;
                case ACTIVITY_CONFIGURATION_CHANGED:
                    handleActivityConfigurationChanged((ActivityConfigChangeData)msg.obj);
                    break;
            }
        }
    }
    private void handleBindApplication(AppBindData data) {
        final MyContextImpl appContext = MyContextImpl.createAppContext(this, data.info);

        java.lang.ClassLoader cl = instrContext.getClassLoader();
        mInstrumentation = (Instrumentation)
                cl.loadClass(data.instrumentationName.getClassName()).newInstance();
        mInstrumentation.init(this, instrContext, appContext,
                new ComponentName(ii.packageName, ii.name), data.instrumentationWatcher,
                data.instrumentationUiAutomationConnection);


        Application app = data.info.makeApplication(data.restrictedBackupMode, null);
        List<ProviderInfo> providers = data.providers;
        if (providers != null) {
            installContentProviders(app, providers);
        }
        mInstrumentation.callApplicationOnCreate(app);
    }
    private void installContentProviders(
            Context context, List<ProviderInfo> providers) {

        final ArrayList<IActivityManager.ContentProviderHolder> results =
                new ArrayList<IActivityManager.ContentProviderHolder>();

        for (ProviderInfo cpi : providers) {
            IActivityManager.ContentProviderHolder cph = installProvider(context, null, cpi,
                    false /*noisy*/, true /*noReleaseNeeded*/, true /*stable*/);
            results.add(cph);
        }

        try {
            MyActivityManagerNative.getDefault().publishContentProviders(
                    getApplicationThread(), results);
        } catch (RemoteException ex) {
        }
    }
    private IActivityManager.ContentProviderHolder installProvider(Context context,
                                                                   IActivityManager.ContentProviderHolder holder, ProviderInfo info,
                                                                   boolean noisy, boolean noReleaseNeeded, boolean stable) {
        ContentProvider localProvider = null;
        IContentProvider provider;
        final java.lang.ClassLoader cl = c.getClassLoader();
        localProvider = (ContentProvider)cl.loadClass(info.name).newInstance();
        provider = localProvider.getIContentProvider();

        localProvider.onCreate();
    }
    public final IContentProvider acquireProvider(
            Context c, String auth, int userId, boolean stable) {
        final IContentProvider provider = acquireExistingProvider(c, auth, userId, stable);
        if (provider != null) {
            return provider;
        }

        IActivityManager.ContentProviderHolder holder = null;
        try {
            holder = MyActivityManagerNative.getDefault().getContentProvider(
                    getApplicationThread(), auth, userId, stable);
        } catch (RemoteException ex) {
        }

        holder = installProvider(c, holder, holder.info,
                true /*noisy*/, holder.noReleaseNeeded, stable);
        return holder.provider;
    }
    public final IContentProvider acquireExistingProvider(
            Context c, String auth, int userId, boolean stable) {
        final ProviderClientRecord pr = mProviderMap.get(key);
        if (pr == null) {
            return null;
        }
        IContentProvider provider = pr.mProvider;
        return provider;
    }

    private void handleLaunchActivity(MyActivityClientRecord r, Intent customIntent) {
        performLaunchActivity(r, customIntent);
    }
    private MyActivity performLaunchActivity(MyActivityClientRecord r, Intent customIntent) {
        ComponentName component = r.intent.getComponent();
        java.lang.ClassLoader cl = r.packageInfo.getClassLoader();
        MyActivity activity = mInstrumentation.newActivity(cl, component.getClassName(), r.intent);

        Application app = r.packageInfo.makeApplication(false, mInstrumentation);
        activity.attach(appContext, this, getInstrumentation(), r.token,
                r.ident, app, r.intent, r.activityInfo);

        mInstrumentation.callActivityOnCreate(activity, r.state);
        if (r.state != null) {
            mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state);
        }

        return r.activity;
    }

    /**
     * 1 activityThread将handleLaunchActivity中创建的PhoneWindow交给WindowManager管理，
     * WindowManager创建一个ViewRootImpl，管理window的添加，删除，更新，下面以添加为例：
     * 1）通过requestLayout渲染view
     * 2）随后通过binder将PhoneWindow添加到WindowManagerService上
     * 2 根据调用顺序 onResume在函数绘制之前调用，显然，onCreate/onResume都得不到view的测量参数
     */
    final void handleResumeActivity() {
        MyActivityClientRecord r = performResumeActivity(token, clearHide);

        wm = r.activity.getWindowManager();
        wm.addView(r.activity.getWindow().getDecorView(), r.window.getAttributes());
    }
    public final MyActivityClientRecord performResumeActivity(IBinder token, boolean clearHide) {
        MyActivityClientRecord r = mActivities.get(token);
        r.activity.performResume();
        return r;
    }
    private void handleStopActivity(IBinder token, boolean show, int configChanges) {
        performStopActivityInner(r, info, show, true);
    }
    private void performStopActivityInner(ActivityClientRecord r,
                                          StopInfo info, boolean keepShown, boolean saveState) {
        // Next have the activity save its current state and managed dialogs...
        if (!r.activity.mFinished && saveState) {
            if (r.state == null) {
                callCallActivityOnSaveInstanceState(r);
            }
        }

        if (!keepShown) {
            try {
                // Now we are idle.
                r.activity.performStop();
            } catch (Exception e) {
                if (!mInstrumentation.onException(r.activity, e)) {
                    throw new RuntimeException(
                            "Unable to stop activity "
                                    + r.intent.getComponent().toShortString()
                                    + ": " + e.toString(), e);
                }
            }
            r.stopped = true;
        }
    }
    private void handleCreateService(CreateServiceData data) {

        LoadedApk packageInfo = getPackageInfoNoCheck(
                data.info.applicationInfo, data.compatInfo);
        java.lang.ClassLoader cl = packageInfo.getClassLoader();
        MyService service = (MyService) cl.loadClass(data.info.name).newInstance();


        MyContextImpl context = MyContextImpl.createAppContext(this, packageInfo);
        context.setOuterContext(service);

        Application app = packageInfo.makeApplication(false, mInstrumentation);
        service.attach(context, this, MyActivityManagerNative.getDefault());
        service.onCreate();

        mServices.put(data.token, service);
    }
    private void handleServiceArgs(ServiceArgsData data) {
        s.onStartCommand(data.args, data.flags, data.startId);
    }
    private void handleBindService(BindServiceData data) {
        MyService s = mServices.get(data.token);
        //多次绑定同一service，onBind只会执行一次，其余执行onRebind
        if (!data.rebind) {
            IBinder binder = s.onBind(data.intent);
            MyActivityManagerNative.getDefault().publishService(
                    data.token, data.intent, binder);
        } else {
            s.onRebind(data.intent);
            MyActivityManagerNative.getDefault().serviceDoneExecuting(
                    data.token, SERVICE_DONE_EXECUTING_ANON, 0, 0);
        }
    }
    public void handleInstallProvider(ProviderInfo info) {
        installContentProviders(mInitialApplication, Lists.newArrayList(info));
    }

}
