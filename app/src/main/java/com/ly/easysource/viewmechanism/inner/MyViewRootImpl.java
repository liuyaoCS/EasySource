package com.ly.easysource.viewmechanism.inner;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ly.easysource.viewmechanism.MyView;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class MyViewRootImpl {
    MyView mView;
    private void performTraversals() {
        measureHierarchy(mView, lp, desiredWindowWidth, desiredWindowHeight);
        performLayout(lp, desiredWindowWidth, desiredWindowHeight);
        //下面两个监听函数在measure之后执行，可以得到view的测量参数
        attachInfo.mTreeObserver.dispatchOnGlobalLayout();
        attachInfo.mTreeObserver.dispatchOnPreDraw();
        performDraw();
    }
    private boolean measureHierarchy(final View host, final WindowManager.LayoutParams lp,
                                     final int desiredWindowWidth, final int desiredWindowHeight) {
        int childWidthMeasureSpec = getRootMeasureSpec(desiredWindowWidth, lp.width);
        int childHeightMeasureSpec = getRootMeasureSpec(desiredWindowHeight, lp.height);
        performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    public void setView(MyView view, WindowManager.LayoutParams attrs, View panelParentView) {
        mView=view;
    }

    /**
     *父容器约束+View自身的LayoutParams=MeasureSpec
     * 1 DecorView 父容器约束指的是窗口尺寸
     * 2 普通view  父容器约束指的是父容器的MeasureSpec
     * @param windowSize：窗口尺寸
     * @param rootDimension：DecorView自身的LayoutParams
     * @return
     *      1 当DecorView采用固定高宽时，    EXACTLY+childSize
     *      2 当DecorView采用match_parent时，EXACTLY+windowSize
     *      3 当DecorView采用wrap_content时，AT_MOST+windowSize
     */
    private static int getRootMeasureSpec(int windowSize, int rootDimension) {
        int measureSpec;
        switch (rootDimension) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                // Window can't resize. Force root view to be windowSize.
                measureSpec = MyView.MeasureSpec.makeMeasureSpec(windowSize, MyView.MeasureSpec.EXACTLY);
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                // Window can resize. Set max size for root view.
                measureSpec = MyView.MeasureSpec.makeMeasureSpec(windowSize, MyView.MeasureSpec.AT_MOST);
                break;
            default:
                // Window wants to be an exact size. Force root view to be that size.
                measureSpec = View.MeasureSpec.makeMeasureSpec(rootDimension, MyView.MeasureSpec.EXACTLY);
                break;
        }
        return measureSpec;
    }
    private void performMeasure(int childWidthMeasureSpec, int childHeightMeasureSpec) {
        //measure函数是final的，只有在View类里，这是因为流程都是一样的，里面会调用ViewGroup的onMeasure
        //因此mView,也就是DecorView这里面调用的是View的measure，里面执行了FrameLayout的onMeasure
        mView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
    private void performLayout(WindowManager.LayoutParams lp, int desiredWindowWidth,
                               int desiredWindowHeight){
        mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
    }
    private void performDraw() {
        mView.draw(canvas);
    }
}
