package com.ly.easysource.thread.asyncTask;

import android.os.AsyncTask;
import android.os.Message;
import android.support.annotation.MainThread;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static android.os.AsyncTask.Status.PENDING;

/**
 * 原理：封装了thread(执行后台任务，如果任务比较耗时，建议使用线程池)和handler(主线程更新ui)
 *      android1.6之前，串行执行任务
 *      android1.6之后，线程池并行执行任务
 *      android3.0之后, 为避免并发错误，又改为串行线程池执行，但是可以用executeOnExecutor并行执行。
 */
public class MyAsyncTask extends AsyncTask<String,Integer,Long> {
    private final FutureTask<Result> mFuture;
    private final WorkerRunnable<Params, Result> mWorker;
    private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR;
    //SERIAL_EXECUTOR 用于任务的排队
    public static final Executor SERIAL_EXECUTOR = new SerialExecutor();
    private static class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
        Runnable mActive;

        public synchronized void execute(final Runnable r) {
            mTasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }
    //真正的执行任务线程池
    public static final Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(
            CORE_POOL_SIZE, // 核心线程数量=cpu数+1
            MAXIMUM_POOL_SIZE,//最大线程数量=2*cpu数+1
            KEEP_ALIVE, TimeUnit.SECONDS,//非核心线程超时时间=1s
            sPoolWorkQueue,  //任务队列 容量128 一般要大于MAXIMUM_POOL_SIZE
            sThreadFactory); //创建线程的工厂

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        Log.i("ly","onPreExecute");
    }

    /**
     * 需要 1 发布进度 2 处理cancel
     */
    @Override
    protected Long doInBackground(String... params) {
       long count=0;
       for(int i=0;i<params.length;i++){
           if(isCancelled()){
               break;
           }else{
               count+=params[i].length();
               float per=i/(float)params.length;
               publishProgress((int)(per*100));
           }

       }
        return count;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.i("ly","onProgressUpdate="+values[0]);

    }
    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
        Log.i("ly","onPostExecute ret="+aLong);
    }
    public AsyncTask() {
        mWorker = new WorkerRunnable<Params, Result>() {
            public Result call() throws Exception {

                Result result = doInBackground(mParams);
                return postResult(result);
            }
        };

        mFuture = new FutureTask<Result>(mWorker) {
            @Override
            protected void done() {
                postResult(result);
            }
        };
    }
    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;
    }
    private Result postResult(Result result) {
        @SuppressWarnings("unchecked")
        Message message = getHandler().obtainMessage(MESSAGE_POST_RESULT,
                new AsyncTaskResult<Result>(this, result));
        message.sendToTarget();
        return result;
    }
    @WorkerThread
    protected final void publishProgress(Progress... values) {
        if (!isCancelled()) {
            getHandler().obtainMessage(MESSAGE_POST_PROGRESS,
                    new AsyncTaskResult<Progress>(this, values)).sendToTarget();
        }
    }
    private static class InternalHandler extends Handler {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
        @Override
        public void handleMessage(Message msg) {
            AsyncTaskResult<?> result = (AsyncTaskResult<?>) msg.obj;
            switch (msg.what) {
                case MESSAGE_POST_RESULT:
                    // There is only one result
                    if (isCancelled()) {
                        onCancelled(result);
                    } else {
                        onPostExecute(result);
                    }
                    mStatus = Status.FINISHED;
                    break;
                case MESSAGE_POST_PROGRESS:
                    result.mTask.onProgressUpdate(result.mData);
                    break;
            }
        }
    }
    @MainThread
    public final AsyncTask<Params, Progress, Result> execute(Params... params) {
        return executeOnExecutor(sDefaultExecutor, params);
    }
    @MainThread
    public final AsyncTask<Params, Progress, Result> executeOnExecutor(Executor exec,
                                                                       Params... params) {
        if (mStatus != PENDING) {
            switch (mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task has already been executed "
                            + "(a task can be executed only once)");
            }
        }

        mStatus = Status.RUNNING;

        onPreExecute();

        mWorker.mParams = params;
        exec.execute(mFuture);

        return this;
    }
}
