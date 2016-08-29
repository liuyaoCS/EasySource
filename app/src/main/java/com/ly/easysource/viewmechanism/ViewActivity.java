package com.ly.easysource.viewmechanism;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.ly.easysource.R;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mechanism);

        //一 view的层次结构
        //mDecorView（FrameLayout）
        //      ->LinearLayout
        //              ->title
        //              ->contentContainer
        //                       ->contentView
        ViewGroup contentContainer= (ViewGroup) findViewById(android.R.id.content);
        final View contentView=contentContainer.getChildAt(0);

        //二 如何获得view的测量参数
        //1 ViewTreeObserver 这个监听器在layout之后调用，因此可以直接得到view的高度宽度
        ViewTreeObserver observer=contentView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int height=contentView.getHeight();
                int width=contentView.getWidth();
            }
        });
        //2【适用view渲染简单】post 大多数情况下是可以的，一般创建activity，AMS在startActivity会连续促使activityThread
        // 回调消息1,2.虽然消息3是在消息1的流程发出的，但是往往晚于消息2进入messageQueue；
        // 但是有时view的渲染特别复杂，甚至需要多次渲染，多次发送消息2，这会使消息2晚于消息3
        //    消息1 scheduleLaunchActivity->...->onCreate()->消息3 handler.post
        //    消息2 scheduleResumeActivity->...->view.measure
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int height=contentView.getHeight();
                int width=contentView.getWidth();
            }
        });
        //3【推荐】 onWindowFocusChanged 这个函数被调用时，Activity已经加载完毕（完成渲染）
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
