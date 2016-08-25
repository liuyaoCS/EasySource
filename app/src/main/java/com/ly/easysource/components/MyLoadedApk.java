package com.ly.easysource.components;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import com.ly.easysource.core.remote.binder.IServiceConnection;

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
}
