package com.ly.easysource.components.activity.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Parcelable;
import android.view.LayoutInflater;

import java.util.ArrayList;

/**
 * FragmentManager维护FragmentTransaction（BackStackRecord实现）来封装fragment op
 * 调用commit后运行op的run，真正执行相应的操作。
 */
public class MyFragmentManager {
    public class FragmentManagerImpl extends FragmentManager implements LayoutInflater.Factory2 {
        ArrayList<MyFragment> mAdded;
        ArrayList<Runnable> mPendingActions;
        Runnable mExecCommit = new Runnable() {
            @Override
            public void run() {
                execPendingActions();
            }
        };
        private boolean mStateSaved;

        @Override
        public FragmentTransaction beginTransaction() {
            return new BackStackState.BackStackRecord(this);
        }


        public void enqueueAction(Runnable action, boolean allowStateLoss) {
            if (!allowStateLoss) {
                checkStateLoss();
            }
            synchronized (this) {

                mPendingActions.add(action);
                if (mPendingActions.size() == 1) {
                    mHost.getHandler().removeCallbacks(mExecCommit);
                    mHost.getHandler().post(mExecCommit);
                }
            }
        }
        private void checkStateLoss() {
            if (mStateSaved) {
                throw new IllegalStateException(
                        "Can not perform this action after onSaveInstanceState");
            }
        }
        public Parcelable saveAllState() {
            // Make sure all pending operations have now been executed to get
            // our state update-to-date.
            execPendingActions();
            mStateSaved = true;
            //save state ....
        }

        public boolean execPendingActions() {
            startPendingDeferredFragments();
        }
        void startPendingDeferredFragments() {
            if (mActive == null) return;

            for (int i=0; i<mActive.size(); i++) {
                Fragment f = mActive.get(i);
                if (f != null) {
                    performPendingDeferredStart(f);
                }
            }
        }
        public void performPendingDeferredStart(Fragment f) {
            if (f.mDeferStart) {
                moveToState(f, mCurState, 0, 0, false);
            }
        }
        void moveToState(MyFragment f, int newState, int transit, int transitionStyle,
                         boolean keepActive) {
            switch (f.mState) {
                case MyFragment.INITIALIZING:
                    f.onAttach(mHost.getContext());
                    f.performCreate(f.mSavedFragmentState);
                    f.mView = f.performCreateView(f.getLayoutInflater(
                            f.mSavedFragmentState), null, f.mSavedFragmentState);
                    if (f.mView != null) {
                        f.onViewCreated(f.mView, f.mSavedFragmentState);
                    }
                case MyFragment.CREATED:
                    f.performActivityCreated(f.mSavedFragmentState);
                case MyFragment.ACTIVITY_CREATED:
                case MyFragment.STOPPED:
                    if (newState > Fragment.STOPPED) {
                        f.performStart();
                    }
                case MyFragment.STARTED:
                    if (newState > Fragment.STARTED) {
                        f.mResumed = true;
                        f.performResume();
                    }
            }
        }
        public void addFragment(MyFragment fragment, boolean moveToStateNow) {

            if (!fragment.mDetached) {
                mAdded.add(fragment);
                fragment.mAdded = true;
                if (moveToStateNow) {
                    moveToState(fragment);
                }
            }
        }

    }
}
