package com.ly.easysource.thread.intentService;

import android.app.IntentService;
import android.content.Intent;

/**
 * 用于执行后台耗时任务，执行完毕自动停止
 */
public class MyIntentService extends IntentService{
    /**
     * 需要构造无惨构造函数,否则Intent service=new Intent(this, MyIntentService.class);无法实例化
     */
    public MyIntentService() {
        this("test");
    }
    public MyIntentService(String name) {
        super(name);
    }

    /**
     * 封装了HandlerThread和Handler
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     *如果是耗时操作，可以开辟新的线程处理，这样消息不会积压
     * 如果不开辟新的线程，
     *  消息添加实际上是在主线程
     *  消息执行是handlerThread，顺序执行。
     */
    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
