package com.ly.easysource.components;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import com.ly.easysource.components.receiver.binder.IIntentReceiver;
import com.ly.easysource.components.service.binder.IServiceConnection;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class MyLoadedApk {

    public class ServiceDispatcher {
        public final ServiceDispatcher.InnerConnection mIServiceConnection;
        private final ServiceConnection mConnection;
        IServiceConnection getIServiceConnection() {
            return mIServiceConnection;
        }
        private void connected(ComponentName name, IBinder service) {
            mConnection.onServiceConnected();
        }
        private static class InnerConnection extends IServiceConnection.Stub {
            final WeakReference<ServiceDispatcher> mDispatcher;

            InnerConnection(MyLoadedApk.ServiceDispatcher sd) {
                mDispatcher = new WeakReference<MyLoadedApk.ServiceDispatcher>(sd);
            }

            public void connected(ComponentName name, IBinder service) throws RemoteException {
                MyLoadedApk.ServiceDispatcher sd = mDispatcher.get();
                if (sd != null) {
                    sd.connected(name, service);
                }
            }
        }



    }
    public final IServiceConnection getServiceDispatcher(ServiceConnection c,
                                                         Context context, Handler handler, int flags) {
        MyLoadedApk.ServiceDispatcher sd = null;
        return sd.getIServiceConnection();
    }

    public class ReceiverDispatcher {
        final Handler mActivityThread;//ActivityThread里的H
        final IIntentReceiver.Stub mIIntentReceiver;
        final BroadcastReceiver mReceiver;
        public BroadcastReceiver getIntentReceiver() {
            return mReceiver;
        }
        public void performReceive(Intent intent, int resultCode, String data,
                                   Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
            MyArgs args = new MyArgs(intent, resultCode, data, extras, ordered,
                    sticky, sendingUser);
            mActivityThread.post(args);
        }

        final static class InnerReceiver extends IIntentReceiver.Stub {
            final WeakReference<MyLoadedApk.ReceiverDispatcher> mDispatcher;
            InnerReceiver(MyLoadedApk.ReceiverDispatcher rd, boolean strong) {
                mDispatcher = new WeakReference<MyLoadedApk.ReceiverDispatcher>(rd);
            }
            public void performReceive(Intent intent, int resultCode, String data,
                                       Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
                MyLoadedApk.ReceiverDispatcher rd = mDispatcher.get();
                rd.performReceive(intent, resultCode, data, extras,
                        ordered, sticky, sendingUser);
            }
        }
        public class MyArgs {
            public void run() {
                mReceiver.onReceive(mContext, intent);
            }
        }
    }
    public IIntentReceiver getReceiverDispatcher(BroadcastReceiver r,
                                                 Context context, Handler handler,
                                                 Instrumentation instrumentation, boolean registered) {
        MyLoadedApk.ReceiverDispatcher rd = null;
        return rd.getIntentReceiver();
    }

}
