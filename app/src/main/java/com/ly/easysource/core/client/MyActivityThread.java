package com.ly.easysource.core.client;


import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.ArrayMap;

import com.ly.easysource.components.MyContextImpl;
import com.ly.easysource.components.MyInstrumentation;
import com.ly.easysource.components.service.MyService;
import com.ly.easysource.core.client.binder.IApplicationThread;
import com.ly.easysource.components.receiver.binder.IIntentReceiver;
import com.ly.easysource.window.MyActivity;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class MyActivityThread {
    final ArrayMap<IBinder, MyService> mServices = new ArrayMap<>();
    final ArrayMap<IBinder, MyActivityClientRecord> mActivities = new ArrayMap<>();

    final ApplicationThread mAppThread = new ApplicationThread();
    MyInstrumentation mInstrumentation;
    MyWindowManager wm;

    final H mH = new H();

    private class ApplicationThread extends Binder
            implements IApplicationThread {

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

    }
    private class H extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LAUNCH_ACTIVITY: {
                    handleLaunchActivity(r, null);
                } break;
                case RESUME_ACTIVITY:
                    handleResumeActivity((IBinder) msg.obj, true, msg.arg1 != 0, true);
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

                case NEW_INTENT:
                    handleNewIntent((NewIntentData)msg.obj);
                    break;
                case ACTIVITY_CONFIGURATION_CHANGED:
                    handleActivityConfigurationChanged((ActivityConfigChangeData)msg.obj);
                    break;
            }
        }
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


}
