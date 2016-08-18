package com.ly.easysource.eventdispatch;

import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 通过代码发现，view中事件的响应顺序为，activity为1->3，而对于2，一般1，3不响应时，才会调用2
 * 1 onTouch
 * 2 onTouchEvent
 * 3 onClickListener
 *
 */
public class MyView{
    /**
     * Pass the touch screen motion event down to the target view, or this
     * view if it is the target.
     *
     * @param event The motion event to be dispatched.
     * @return True if the event was handled by the view, false otherwise.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        ListenerInfo li = mListenerInfo;
        if (li != null && li.mOnTouchListener != null && (mViewFlags & ENABLED_MASK) == ENABLED
                && li.mOnTouchListener.onTouch(this, event)) {
            return true;
        }

        if (onTouchEvent(event)) {
            return true;
        }

        return false;
    }

    /**
     *
     * @param event
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int viewFlags = mViewFlags;

        if ((viewFlags & ENABLED_MASK) == DISABLED) {
            // A disabled view that is clickable still consumes the touch
            // events, it just doesn't respond to them.
            return (((viewFlags & CLICKABLE) == CLICKABLE || (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE));
        }

        if (((viewFlags & CLICKABLE) == CLICKABLE || (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if (li != null && li.mOnClickListener != null) {
                        li.mOnClickListener.onClick(this);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return true;
        }

        //使能状态，但是不可点击，返回false
        return false;
    }
}
