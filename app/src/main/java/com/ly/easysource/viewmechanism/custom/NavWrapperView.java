package com.ly.easysource.viewmechanism.custom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.chinaso.so.R;
import com.chinaso.so.common.entity.appInit.NewColumnItem;
import com.chinaso.so.net.request.UploadUserActionHelper;
import com.chinaso.so.ui.component.CommonSearchResultActivity;
import com.chinaso.so.ui.component.MainActivity;
import com.chinaso.so.ui.component.VerticalListWebViewActivity;
import com.chinaso.so.utility.DisplayUtil;
import com.chinaso.so.utility.ValidityCheckUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 注意onMeasure的实现
 */
public class NavWrapperView extends LinearLayout{
    private List<LinearLayout> mLayouts;
    private Context mContext;
    private int mItemHeight=55*3;
    private boolean isMoreItemsShow=false;


    public NavWrapperView(Context context) {
        super(context);
        mContext=context;
    }

    public NavWrapperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mHeight=(isMoreItemsShow?mLayouts.size():1)*mItemHeight;
        int mHeightMeasureSpec=MeasureSpec.getMode(heightMeasureSpec)+mHeight;
        super.onMeasure(widthMeasureSpec, mHeightMeasureSpec);
    }

}
