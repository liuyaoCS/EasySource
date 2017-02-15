package com.ly.easysource.core.client;

import android.os.Trace;
import android.view.View;

/**
 * Created by liuyao on 2017/2/15 0015.
 */

public class MyThreadedRenderer extends HardwareRenderer  {
    void draw(View view, AttachInfo attachInfo, HardwareDrawCallbacks callbacks) {
        updateRootDisplayList(view, callbacks);
    }
    private void updateRootDisplayList(View view, HardwareDrawCallbacks callbacks) {
        Trace.traceBegin(Trace.TRACE_TAG_VIEW, "Record View#draw()");
        updateViewTreeDisplayList(view);
        if (mRootNodeNeedsUpdate || !mRootNode.isValid()) {
            HardwareCanvas canvas = mRootNode.start(mSurfaceWidth, mSurfaceHeight);
            try {
                final int saveCount = canvas.save();
                canvas.translate(mInsetLeft, mInsetTop);
                callbacks.onHardwarePreDraw(canvas);

                canvas.insertReorderBarrier();
                canvas.drawRenderNode(view.getDisplayList());
                canvas.insertInorderBarrier();

                callbacks.onHardwarePostDraw(canvas);
                canvas.restoreToCount(saveCount);
                mRootNodeNeedsUpdate = false;
            } finally {
                mRootNode.end(canvas);
            }
        }
        Trace.traceEnd(Trace.TRACE_TAG_VIEW);
    }
    private void updateViewTreeDisplayList(View view) {
        view.mPrivateFlags |= View.PFLAG_DRAWN;
        view.mRecreateDisplayList = (view.mPrivateFlags & View.PFLAG_INVALIDATED)
                == View.PFLAG_INVALIDATED;
        view.mPrivateFlags &= ~View.PFLAG_INVALIDATED;
        //此处进入DisplayList分发：
        // getDisplayList->updateDisplayListIfDirty->dipatchGetDisplayList->getDisplayList
        //最终进入最底层view的getDisplayList，因为显示列表中记录了绘图的步骤，可以直接执行绘图指令，而无效在此调用绘图函数
        view.getDisplayList();
        view.mRecreateDisplayList = false;
    }
}
