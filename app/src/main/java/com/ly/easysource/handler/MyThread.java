package com.ly.easysource.handler;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class MyThread extends Thread {
    Handler mHandler;
    MyThread(){

    }
    @Override
    /**
     * run函数才是处于异步线程中，如果把mHandler的构造放到MyThread的构造函数中，则handler依然出于主线程中
     * 流程：
     * 1 Looper.prepare()创建Looper（Looper构造函数创建MessageQueue），存储在ThreadLocal中。
     * 2 子线程中创建Handler，注意，1,2步骤可以交换。
     * 3 Looper.loop();MessageQueue中不停的取消息，并分发消息msg.target.dispatchMessage(msg);
     *      1)如果消息有callback，也就是runnable消息，直接调用run方法
     *      2）否则如果用callback构造handler，发送消息到callback中
     *      3）如果没有callback构造或者callback处理返回false，则发送到handleMessage中
     * 4 handler发送消息：一般handler把消息发送到MessageQueue（handler通过Looper.myLooper()获取Looper，进而可以获得MessageQueue）
     *
     */
    public void run() {
       Looper.prepare();
        mHandler =new Handler(new Handler.Callback() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public boolean handleMessage(Message msg) {
                Log.i("ly","Handler.Callback,msg what->"+msg);
                if(msg.what==5){
                    Looper.myLooper().quitSafely();
                }
                return false;
            }
        }){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.i("ly","handleMessage,msg what->"+msg);
            }
        };
       Looper.loop();
       Log.i("ly","thread exit");
    }

    /**
     *主线程用于handler的引用，则可以在主线程中发消息到这个线程。
     */
    public Handler getHandler(){
        return mHandler;
    }
}
