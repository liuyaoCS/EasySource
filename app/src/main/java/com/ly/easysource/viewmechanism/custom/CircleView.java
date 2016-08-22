package com.ly.easysource.viewmechanism.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ly.easysource.R;

/**
 * 自定义view--直接继承View
 * 1 自己处理wrap_content情况
 * 2 自己处理padding情况
 * 3 如果有动画或者线程，需要及时停止，防止内存泄露
 *      1）onDetachedFromWindow 包含此view的activity退出，或者当前view被remove时调用
 *      2）view不可见时
 * 4 如果要自定义attribute
 *      1) 创建attrs.xml,定义declare-styleable attr
 *      2）xml中添加xmlns:app="http://schemas.android.com/apk/res-auto"，之后可以使用app:引用属性
 *      3）代码中context.obtainStyledAttributes，获取属性集
 */
public class CircleView extends View {

    private Paint mPaint;
    private int mColor= Color.BLUE;
    private int mWidth=600;
    private int mHeight=600;
    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mColor=ta.getColor(R.styleable.CircleView_color, Color.RED);
        ta.recycle();
        init();
    }

    private void init(){
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode= MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize= MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode= MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize= MeasureSpec.getSize(heightMeasureSpec);

        int destWidth=widthSpecMode== MeasureSpec.AT_MOST?mWidth:widthSpecSize;
        int destHeight=heightSpecMode== MeasureSpec.AT_MOST?mHeight:heightSpecSize;

        setMeasuredDimension(destWidth,destHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft=getPaddingLeft();
        int paddingRight=getPaddingRight();
        int paddingBottom=getPaddingBottom();
        int paddingTop=getPaddingTop();
        int height=getHeight()-paddingBottom-paddingTop;
        int width=getWidth()-paddingLeft-paddingRight;
        int radius= Math.min(height,width)/2;
        canvas.drawCircle(width/2+paddingLeft,height/2+paddingTop,radius,mPaint);
    }
}
