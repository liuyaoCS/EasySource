package com.ly.easysource.eventdispatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.ly.easysource.R;
import com.ly.easysource.core.MyPhoneWindow;

public class EventDispatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dispatch);
    }
    /**
     *
     * @param ev The touch screen event.
     *
     * @return boolean Return true if this event was consumed.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        //mDecor extends FrameLayout ,是setContentView里面的view的父容器
        if (new MyPhoneWindow().getDecorView().dispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
