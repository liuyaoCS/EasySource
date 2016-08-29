package com.ly.easysource.thread;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.ly.easysource.R;
import com.ly.easysource.thread.asyncTask.MyAsyncTask;
import com.ly.easysource.thread.intentService.MyIntentService;
import com.ly.easysource.thread.thread.MyHandlerThread;
import com.ly.easysource.thread.thread.MyThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_handler_test);
        View send= findViewById(R.id.message);

        //一 自己创建thread和handler
        final MyThread t=new MyThread();
        t.start();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler h=t.getHandler();

                //1 空消息
                h.sendEmptyMessage(1);
                //2 普通消息
                Message msg=new Message();
                msg.what=2;
                h.sendMessage(msg);
                //3 runnable消息 msg.callback=runnable
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("ly","runnable msg");
                    }
                });
                //4 显示构造runnable消息
                Message msg2= Message.obtain(h, new Runnable() {
                    @Override
                    public void run() {
                        Log.i("ly","runnable msg2");
                    }
                });
                msg2.what=4;
                msg2.sendToTarget();
                //5 发送子线程退出信号，否则子线程一直处于loop状态
                h.sendEmptyMessage(5);
            }
        });

        //二 创建HandlerThread 在新线程中创建handler
        MyHandlerThread ht=new MyHandlerThread("test");
        //需要调用start，开启Looper机制
        ht.start();
        //新线程中创建handler
        Handler h=new Handler(ht.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.i("ly","receive msg->"+msg.what);
                return false;
            }
        });
        h.sendEmptyMessage(1);

        //三 一个AsyncTask只能执行一次execute
        AsyncTask<String,Integer,Long> task=new MyAsyncTask();
        MyAsyncTask task1=new MyAsyncTask();
        String[] params=new String[]{"im ly","hhh","what about u?"};
        //默认串行执行 task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,params);
        task.execute(params);
        //并行执行
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);

        //四 IntentService
        Intent service=new Intent(this, MyIntentService.class);
        startService(service);
        startService(service);

        //五 线程池
        Runnable commond=new Runnable() {
            @Override
            public void run() {

            }
        };
        //Executor->ExecutorService->ScheduledExecutorService
        //Executors: Executor的工具类
        ExecutorService fixedThreadPool=Executors.newFixedThreadPool(10);
        fixedThreadPool.execute(commond);

        ExecutorService  singleThreadExecutor=Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(commond);

        ExecutorService cachedThreadPool=Executors.newCachedThreadPool();
        cachedThreadPool.execute(commond);

        ScheduledExecutorService scheduledExecutorService=Executors.newScheduledThreadPool(5);
        scheduledExecutorService.execute(commond);
        scheduledExecutorService.schedule(commond,1, TimeUnit.SECONDS);

    }


}
