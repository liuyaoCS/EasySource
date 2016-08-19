package com.ly.easysource.viewmechanism;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.ly.easysource.R;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mechanism);
        //mDecorView（FrameLayout）
        //      ->LinearLayout
        //              ->title
        //              ->contentContainer
        //                       ->contentView
        ViewGroup contentContainer= (ViewGroup) findViewById(android.R.id.content);
        View contentView=contentContainer.getChildAt(0);
    }
}
