package com.ly.easysource.viewmechanism;

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
        //2 post ??
    }
}
