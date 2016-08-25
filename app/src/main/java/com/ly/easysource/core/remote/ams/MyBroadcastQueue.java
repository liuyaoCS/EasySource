package com.ly.easysource.core.remote.ams;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

import com.ly.easysource.core.client.binder.IApplicationThread;
import com.ly.easysource.components.receiver.binder.IIntentReceiver;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class MyBroadcastQueue {
    final ArrayList<BroadcastRecord> mParallelBroadcasts = new ArrayList<>();
    final BroadcastHandler mHandler;
    IApplicationThread thread;
    public void enqueueParallelBroadcastLocked(BroadcastRecord r) {
        mParallelBroadcasts.add(r);
    }

    public void scheduleBroadcastsLocked() {
        mHandler.sendMessage(mHandler.obtainMessage(BROADCAST_INTENT_MSG, this));
    }
    private final class BroadcastHandler extends Handler {
        public BroadcastHandler(Looper looper) {
            super(looper, null, true);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BROADCAST_INTENT_MSG: {
                    processNextBroadcast(true);
                } break;
            }
        }
    }

    private void processNextBroadcast(boolean b) {
        // First, deliver any non-serialized broadcasts right away.
        while (mParallelBroadcasts.size() > 0) {
            r = mParallelBroadcasts.remove(0);
            final int N = r.receivers.size();
            for (int i=0; i<N; i++) {
                Object target = r.receivers.get(i);
                deliverToRegisteredReceiverLocked(r, (BroadcastFilter)target, false);
            }
        }
    }
    private void deliverToRegisteredReceiverLocked(BroadcastRecord r,
                                                   BroadcastFilter filter, boolean ordered) {
        performReceiveLocked(filter.receiverList.app, filter.receiverList.receiver,
                new Intent(r.intent), r.resultCode, r.resultData,
                r.resultExtras, r.ordered, r.initialSticky, r.userId);
    }
    private  void performReceiveLocked(ProcessRecord app, IIntentReceiver receiver,
                                             Intent intent, int resultCode, String data, Bundle extras,
                                             boolean ordered, boolean sticky, int sendingUser) throws RemoteException {
        // Send the intent to the receiver asynchronously using one-way binder calls.
        if (app != null) {
            if (thread != null) {
                // If we have an app thread, do the call through that so it is
                // correctly ordered with other one-way calls.
                thread.scheduleRegisteredReceiver(receiver, intent, resultCode,
                        data, extras, ordered, sticky, sendingUser, app.repProcState);
            } else {
                // Application has died. Receiver doesn't exist.
                throw new RemoteException("app.thread must not be null");
            }
        } else {
            receiver.performReceive(intent, resultCode, data, extras, ordered,
                    sticky, sendingUser);
        }
    }


}
