package com.ly.sourcetest;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ly.sourcetest.fragment.MyFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;


public class MainActivity extends Activity {
    TextView tv,tv1,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.tv);
        tv1= (TextView) findViewById(R.id.tv1);
        tv2= (TextView) findViewById(R.id.tv2);

//        testIO1();
//        testIO2();
//        testIO3();
//        testIO4();

        Log.i("ly","main activity onCreate");
        testFragment();
    }

    private void testFragment() {
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.add(R.id.container,new MyFragment());
        ft.commit();
    }

    private void testIO4() {
        Resources rs=getResources();
        InputStream is=rs.openRawResource(R.raw.test);

        Scanner in=new Scanner(is);
        StringBuilder out=new StringBuilder();

        while(in.hasNextLine()){
            out.append(in.nextLine()+"\n");//涉及到每行实际内容，并不输出换行符，需要自己添加
        }
        in.close();

        tv1.setText(out.toString());
    }

    /**
     * 字符串流读写
     */
    private void testIO3() {
        AssetManager am=getAssets();
        try {
            InputStream is=am.open("test");

            Scanner in=new Scanner(is);
            StringBuilder out=new StringBuilder();

            while(in.hasNextLine()){
                out.append(in.nextLine()+"\n");//涉及到每行实际内容，并不输出换行符，需要自己添加
            }
            in.close();

            tv2.setText(out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符流读写
     */
    private void testIO2() {
        AssetManager am=getAssets();
        try {
            InputStream is=am.open("test");
            ByteArrayOutputStream os=new ByteArrayOutputStream();

            InputStreamReader in=new InputStreamReader(is);
            OutputStreamWriter out=new OutputStreamWriter(os);

            int length=0;
            char[] buffer=new char[1024];
            while((length=in.read(buffer))!=-1){
                out.write(buffer,0,length);
            }
            in.close();
            out.close();

            tv1.setText(out.toString());//没有重写toString，得不到里面的内容
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字节流读写
     */
    private void testIO1() {
        AssetManager am=getAssets();
        try {
            InputStream in=am.open("test");
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            int length=0;
            byte[] buffer=new byte[1024];
            while((length=in.read(buffer))!=-1){
                out.write(buffer,0,length);
            }
            in.close();
            out.close();

            tv.setText(out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("ly","activity onAcitivity result ->"+requestCode);
    }
}
