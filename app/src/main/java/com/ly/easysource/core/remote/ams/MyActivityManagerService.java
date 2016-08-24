package com.ly.easysource.core.remote.ams;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.ly.easysource.core.client.MyActivityManagerNative;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class MyActivityManagerService extends MyActivityManagerNative {
    private MyActivityStackSupervisor mStackSupervisor;

    @Override
    public final int startActivity(IApplicationThread caller, String callingPackage,
                                   Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode,
                                   int startFlags, ProfilerInfo profilerInfo, Bundle options) {
        return mStackSupervisor.realStartActivityLocked(ProcessRecord app);
    }

}
