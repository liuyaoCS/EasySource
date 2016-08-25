package com.ly.easysource.components.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 一般来讲，无需通过指定android.process,这是因为如果跨进程，那么他和主进程无法通信
 * 1 虽然进程不同，但是他们的线程都在各自进程的main线程中
 * 2 主进程改变变量，receiver进程访问不到，实际上他只能得到变量的初始值。
 */
public class MyReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
    }
}
