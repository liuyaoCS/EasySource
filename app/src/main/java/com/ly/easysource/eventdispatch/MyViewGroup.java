package com.ly.easysource.eventdispatch;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 建议参看MyViewGroupSimple版本
 */
@Deprecated
public  class MyViewGroup  extends MyView {

    private MyTouchTarget mFirstTouchTarget;
    protected static final int FLAG_DISALLOW_INTERCEPT = 0x80000;
    /**
     *
     * @param ev
     * @return true 表示分发成功，自己或者child可以处理事件 ； false 表示分发失败，自己或者child不可以处理事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = false;
        final int actionMasked = ev.getAction() & MotionEvent.ACTION_MASK;

        final boolean intercepted;
        if (actionMasked == MotionEvent.ACTION_DOWN || mFirstTouchTarget != null) {
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
        MyTouchTarget newTouchTarget = null;
        if (!canceled && !intercepted) {
            if (actionMasked == MotionEvent.ACTION_DOWN){
                for (MyView child:childs) {
                    //Gets the touch target for specified child view.
                    newTouchTarget = getTouchTarget(child);
                    if(dispatchTransformedTouchEvent(ev,child,false)){
                        //有child可以处理事件，dispatchTransformedTouchEvent返回true，并置位alreadyDispatchedToNewTouchTarget
                        alreadyDispatchedToNewTouchTarget = true;
                        //Adds a touch target for specified child to the beginning of the list.
                        //暂时人为只有在这里对mFirstTouchTarget赋值，为newTouchTarget
                        newTouchTarget = addTouchTarget(child);
                        break;
                    }

                }
                if (newTouchTarget == null && mFirstTouchTarget != null) {
                    //以前的事件找到过target，但是此次事件没有找到
                    // Did not find a child to receive the event.
                    // Assign the pointer to the least recently added target.
                    newTouchTarget = mFirstTouchTarget;
                    while (newTouchTarget.next != null) {
                        newTouchTarget = newTouchTarget.next;
                    }
                }
            }

        }

        if (mFirstTouchTarget == null) {//没有child可以处理，本viewgroup处理
            // No touch targets so treat this as an ordinary view.
            handled=dispatchTransformedTouchEvent(ev,null,canceled);
        }else{//

            MyTouchTarget target = mFirstTouchTarget;
            MyTouchTarget predecessor = null;
            while (target != null) {
                final MyTouchTarget next = target.next;
                if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
                    //大多数情况是这样的，child可以处理事件，直接返回true了
                    handled = true;
                } else {//非down事件直接dispatch到target
                    final boolean cancelChild = intercepted;
                    if(dispatchTransformedTouchEvent(ev, target.child, intercepted)){
                           handled =true;
                    }
                    if (cancelChild) {
                        if (predecessor == null) {
                            mFirstTouchTarget = next;
                        } else {
                            predecessor.next = next;
                        }
                        target = next;
                        continue;
                    }
                }
                target = next;
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
    private MyTouchTarget getTouchTarget(View child) {
        for (MyTouchTarget target = mFirstTouchTarget; target != null; target = target.next) {
            if (target.child == child) {
                return target;
            }
        }
        return null;
    }
    private MyTouchTarget addTouchTarget(View child, int pointerIdBits) {
        MyTouchTarget target = MyTouchTarget.obtain(child, pointerIdBits);
        target.next = mFirstTouchTarget;
        mFirstTouchTarget = target;
        return target;
    }
}
