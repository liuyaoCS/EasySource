package com.ly.easysource;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.ly.easysource.R;
import com.ly.easysource.eventdispatch.MyPhoneWindow;

/**
 * 1 activity的onTouchEvent默认返回false
 */
public class MainActivity extends AppCompatActivity implements View.OnTouchListener,View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if (MyPhoneWindow.mDecor.dispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onClick(View view) {

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
