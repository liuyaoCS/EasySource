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

                h.sendEmptyMessage(1);

                Message msg=new Message();
                msg.what=2;
                h.sendMessage(msg);

                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("ly","runnable msg");
                    }
                });

                Message msg2= Message.obtain(h, new Runnable() {
                    @Override
                    public void run() {
                        Log.i("ly","runnable msg2");
                    }
                });
                msg2.what=4;
                msg2.sendToTarget();

                h.sendEmptyMessage(5);
            }
        });


    }


}
