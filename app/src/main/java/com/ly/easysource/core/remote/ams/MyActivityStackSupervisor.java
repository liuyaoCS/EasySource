package com.ly.easysource.core.remote.ams;

import android.os.RemoteException;

import com.ly.easysource.core.client.binder.IApplicationThread;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class MyActivityStackSupervisor {
    IApplicationThread thread;
    final boolean realStartActivityLocked(ActivityRecord r,
                                          ProcessRecord app, boolean andResume, boolean checkConfig)
            throws RemoteException {
        thread.scheduleLaunchActivity();
    }
    boolean resumeTopActivitiesLocked() {
        thread.scheduleResumeActivity();
    }

}
