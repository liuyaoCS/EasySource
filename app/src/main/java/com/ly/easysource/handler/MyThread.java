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
    public Handler getHandler(){
        return mHandler;
    }
}
