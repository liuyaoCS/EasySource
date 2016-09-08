package com.ly.sourcetest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {
    View btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getAssets().open("");
        getResources().getDimension(0);
        getResources().getDisplayMetrics();
        getResources().openRawResource(0);
    }
}
