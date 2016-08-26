package com.ly.easysource.handler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ly.easysource.R;

public class HandlerTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_handler_test);
        View send= findViewById(R.id.message);

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


    }


}
