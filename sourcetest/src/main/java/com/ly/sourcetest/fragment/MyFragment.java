package com.ly.sourcetest.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ly.sourcetest.MainActivity;
import com.ly.sourcetest.R;
import com.ly.sourcetest.TestActivity;

/**
 * Created by Administrator on 2016/9/9 0009.
 */
public class MyFragment extends Fragment {
    private Context mContext;
    @Override
    public void onAttach(Context context) {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onAttach(context);
        mContext=context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        //一定要把attachToRoot置为false，因为这里返回的view会被自动添加到container；
        //如果不设置false，可以返回null(super.onCreateView就是返回的null),但是这样写不规范。
        View view=LayoutInflater.from(mContext).inflate(R.layout.fragment_layout,container,false);
        Button btn= (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getActivity(), TestActivity.class);
               startActivityForResult(it,100);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("ly","fragment onActivityResult resultCode->"+requestCode);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onStart() {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i("ly",Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onDetach();
    }
}
