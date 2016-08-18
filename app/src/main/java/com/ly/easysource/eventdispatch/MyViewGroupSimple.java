package com.ly.easysource.eventdispatch;

import android.view.MotionEvent;
import android.view.View;

/**
 * 模型 activity->mDecorView->RealativeLayout->LinearLayout->Button
 * 1 down事件，一路传递，直到传到Button,以后事件直接找到Button，不会在循环遍历LinearLayout的其他child。
 *   viewgroup和activity都不会回调onTouchEvent处理。
 * 2 button 在处理了一系列move后，突然不处理move了，返回false，上层viewGroup依然还是把move传给这个Button，
 *   自己的onTouchEvent不处理，一路上溯返回false，由最终的activity在本身onTouchEvent处理
 * 3 一开始down事件，中间ViewGoup在onInterceptTouchEvent拦截，child就不会再收到消息了
 * 4 如果ViewGroup不是拦截down事件，而是在move事件做出拦截，则之前的down事件找到的target会被置空，且child会收到cancel事件
 *   但是第一个move事件会丢失，从第二个move事件开始，交由这个ViewGroup的onTouchEvent处理（只要是clickable的就返回true）；
 *   同理，如果是拦截的up事件，则之前的down事件找到的target会被置空，且child会收到cancel事件，这个up事件会消失。
 */
public  class MyViewGroupSimple extends MyView {

    private MyTouchTarget mTarget;
    protected static final int FLAG_DISALLOW_INTERCEPT = 0x80000;
    /**
     *这个函数表示事件的分发，每一层都会调用（前题是事件可以穿到这一层），控制着onInterceptTouchEvent和onTouchEvent的调用。
     * 流程：先看看是否需要拦截，再根据是否有target做后续处理
     * 1 如果不拦截，则在child中找target；之后根据是否找到target做处理
     *  1）没找到target，自己处理
     *  2）找到target，后续交给target处理
     * 2 如果拦截，则根据target是否存在做处理
     *  1）target不存在，自己处理
     *  2）target存在，给target发送cancel信号，置空target，从下个事件（第一个move事件丢失问题）开始转到1）自己处理
     * @return true 表示分发成功，自己或者child可以处理事件 ； false 表示分发失败，自己或者child不可以处理事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = false;
        final int actionMasked = ev.getAction() & MotionEvent.ACTION_MASK;

        final boolean intercepted;
        if (actionMasked == MotionEvent.ACTION_DOWN || mTarget != null) {
            final boolean disallowIntercept = FLAG_DISALLOW_INTERCEPT != 0;
            if (!disallowIntercept) {
                intercepted = onInterceptTouchEvent(ev);
            } else {
                intercepted = false;
            }
        } else {
            // There are no touch targets and this action is not an initial down
            // so this view group continues to intercept touches.
            intercepted = true;
        }

        final boolean canceled = actionMasked == MotionEvent.ACTION_CANCEL;

        boolean alreadyDispatchedToNewTouchTarget = false;

        if (!canceled && !intercepted) {
            if (actionMasked == MotionEvent.ACTION_DOWN){
                for (MyView child:childs) {
                    if(dispatchTransformedTouchEvent(ev,child,false)){
                        //有child可以处理事件，dispatchTransformedTouchEvent返回true，并置位alreadyDispatchedToNewTouchTarget
                        alreadyDispatchedToNewTouchTarget = true;
                        //找到target
                        mTarget =  MyTouchTarget.obtain(child);
                        break;
                    }

                }

            }

        }

        if (mTarget == null) { // 没有找到target或者在上一步拦截了事件清空了target，自己处理
            handled=dispatchTransformedTouchEvent(ev,null,canceled);
        }else{                 //找到target，分派给target处理
            if (alreadyDispatchedToNewTouchTarget) {//1 down事件，且成功在childs中找到target并已分派，直接返回true了
                handled = true;
            } else {                               // 2 move up 事件，直接分派给target
                final boolean cancelChild = intercepted;
                if(dispatchTransformedTouchEvent(ev, mTarget.child, intercepted)){
                       handled =true;
                }
                if (cancelChild) {                 //如果此viewgroup拦截，则置空target
                    mTarget = null;
                }
            }

        }

        return handled;
    }

    /**
     *
     * @param ev
     * @return 默认不拦截,比如XXLayout，特定ViewGroup的实现类可能会依据业务逻辑拦截，比如ScrollView
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private boolean dispatchTransformedTouchEvent(MotionEvent event, View child, boolean canceled) {
        final boolean handled;
        final int oldAction = event.getAction();
        if (canceled || oldAction == MotionEvent.ACTION_CANCEL) {
            event.setAction(MotionEvent.ACTION_CANCEL);
            if (child == null) {
                handled = super.dispatchTouchEvent(event);
            } else {
                handled = child.dispatchTouchEvent(event);
            }
            event.setAction(oldAction);
            return handled;
        }

        if (child == null) {
            handled= super.dispatchTouchEvent(event);
        }else{
            handled=child.dispatchTouchEvent(event);
        }
        return handled;
    }

}
