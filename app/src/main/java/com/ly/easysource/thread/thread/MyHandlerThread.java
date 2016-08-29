package com.ly.easysource.thread.thread;

import android.os.HandlerThread;

/**
 * 在新线程中创建handler
 */
public class MyHandlerThread extends HandlerThread{
    public MyHandlerThread(String name) {
        super(name);
    }
}
