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
 *   下一次事件则会由自己处理了（第一个拦截的move事件会丢失，如果是拦截的up事件，那么这个事件丢失）
 *
 * 滑动冲突解决
 * 1 外部拦截法 符合事件分发机制，只需要修改父容器的onInterceptTouchEvent即可。
 *  1）down事件目标child处理
 *  2）move事件根据需要父容器通过onInterceptTouchEvent拦截
 *      如果父容器拦截 则之后所有的move/up事件由父容器处理
 *      如果父容器不拦截 所有的move/up由目标child处理
 *  3）up事件父容器不拦截
 *      假如父容器拦截up，那么目标child收不到up，无法处理onclick事件
 * 2 内部拦截法 父容器拦截除了down之外的所有事件，目标child根据需求通过requestDisallowInterceptTouchEvent来决定
 *   是否消费事件，如果不消费，则返回给父容器处理。
 *   1）down事件由目标child处理
 *      因为目标child在dispatchTouchEvent里才能调用requestDisallowInterceptTouchEvent，所以第一个down事件还来不及
 *      改变父容器的拦截策略，所以down事件不能让父容器拦截。
 *   2）move/up事件父容器通过onInterceptTouchEvent拦截
 *      如果目标child需要消费这个事件，通过requestDisallowInterceptTouchEvent禁止父容器的拦截，处理move/up事件
 *      如果目标child不需要消费这个事件，不作处理
 *
 */
public  class MyViewGroupSimple extends MyView {

    private MyTouchTarget mTarget;
    protected static final int FLAG_DISALLOW_INTERCEPT = 0x80000;
    /**
     * 流程：这个函数就是用来分发事件，首先会考虑是否进行拦截，然后根据target情况做进一步处理。
     * 1 如果不拦截
     *  1）target为空 则自己处理
     *  2）target不为空 转交给target处理
     * 2 如果拦截
     *  1）target为空 则自己处理
     *  2）target不为空 给target传递cancel事件，清空mTarget，
     *    下一次事件可以自己处理了（如果拦截的是move，第一个move事件丢失；如果拦截up，则up事件丢失）
     *
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

        if (mTarget == null) { // 没有找到target，自己处理
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

    /**
     *
     * @param event
     * @return 默认处理事件，返回true（前提是view可以点击）
     */
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
