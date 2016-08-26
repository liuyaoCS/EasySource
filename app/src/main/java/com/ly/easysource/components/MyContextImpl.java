package com.ly.easysource.components;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.UserHandle;
import android.view.Display;

import com.ly.easysource.components.provider.binder.IContentProvider;
import com.ly.easysource.components.receiver.binder.IIntentReceiver;
import com.ly.easysource.components.service.binder.IServiceConnection;
import com.ly.easysource.core.client.MyActivityManagerNative;
import com.ly.easysource.core.client.MyActivityThread;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class MyContextImpl {
    final MyLoadedApk mPackageInfo;
    private final ApplicationContentResolver mContentResolver;
    final MyActivityThread mMainThread;
    private ContextImpl(MyContextImpl container, MyActivityThread mainThread,
                        MyLoadedApk packageInfo, IBinder activityToken, UserHandle user, boolean restricted,
                        Display display, Configuration overrideConfiguration, int createDisplayWithId) {
        mMainThread = mainThread;
        mPackageInfo = packageInfo;
        mContentResolver = new ApplicationContentResolver(this, mainThread, user);
    }
    @Override
    public void startActivity() {
         MyActivityManagerNative.getDefault()
                .startActivities(whoThread, who.getBasePackageName(), intents, resolvedTypes,
                        token, options, userId);
    }
    @Override
    public ComponentName startService(Intent service) {
        ComponentName cn = MyActivityManagerNative.getDefault().startService(
                mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(
                        getContentResolver()), getOpPackageName(), user.getIdentifier());
    }
    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        IServiceConnection sd = mPackageInfo.getServiceDispatcher(conn, getOuterContext(),
                mMainThread.getHandler(), flags);

        int res = MyActivityManagerNative.getDefault().bindService(
                mMainThread.getApplicationThread(), getActivityToken(), service,
                service.resolveTypeIfNeeded(getContentResolver()),
                sd, flags, getOpPackageName(), user.getIdentifier());
        return  res!=0;
    }
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        IIntentReceiver rd = mPackageInfo.getReceiverDispatcher(
                receiver, context, scheduler,
                mMainThread.getInstrumentation(), true);
        return MyActivityManagerNative.getDefault().registerReceiver(
                mMainThread.getApplicationThread(), mBasePackageName,
                rd, filter, broadcastPermission, userId);
    }
    @Override
    public void sendBroadcast(Intent intent) {
        MyActivityManagerNative.getDefault().broadcastIntent(
                mMainThread.getApplicationThread(), intent, resolvedType, null,
                Activity.RESULT_OK, null, null, receiverPermissions, appOp, null, false, false,
                getUserId());
    }
    public ContentResolver getContentResolver() {
        return mContentResolver;
    }
    private static final class ApplicationContentResolver extends ContentResolver {
        private final MyActivityThread mMainThread;

        /**
         * ApplicationContentResolver的query最终会调用这个函数
         */
        protected IContentProvider acquireProvider(Context context, String auth) {
            return mMainThread.acquireProvider(context,
                    ContentProvider.getAuthorityWithoutUserId(auth),
                    resolveUserIdFromAuthority(auth), true);
        }
        @Override
        public final @Nullable Cursor query(final @NonNull Uri uri, @Nullable String[] projection,
                                            @Nullable String selection, @Nullable String[] selectionArgs,
                                            @Nullable String sortOrder, @Nullable CancellationSignal cancellationSignal) {

            return acquireProvider(uri);
        }
    }

}
