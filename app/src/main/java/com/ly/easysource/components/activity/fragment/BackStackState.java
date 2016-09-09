package com.ly.easysource.components.activity.fragment;

import android.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/9 0009.
 */
public class BackStackState {
    final class BackStackRecord extends FragmentTransaction implements
            MyFragmentManager.BackStackEntry, Runnable {
        final MyFragmentManager.FragmentManagerImpl mManager;
        Op mHead;
        Op mTail;
        public BackStackRecord(MyFragmentManager.FragmentManagerImpl manager) {
            mManager = manager;
        }
        static final class Op {
            Op next;
            Op prev;
            int cmd;
            MyFragment fragment;
            int enterAnim;
            int exitAnim;
            int popEnterAnim;
            int popExitAnim;
            ArrayList<MyFragment> removed;
        }

        /**
         *设置动画效果
         */
        public FragmentTransaction setCustomAnimations(int enter, int exit) {
            return setCustomAnimations(enter, exit, 0, 0);
        }
        public FragmentTransaction add(int containerViewId, MyFragment fragment) {
            //封装成Op
            doAddOp(containerViewId, fragment, null, OP_ADD);
            return this;
        }
        private void doAddOp(int containerViewId, MyFragment fragment, String tag, int opcmd) {
            Op op = new Op();
            op.cmd = opcmd;
            op.fragment = fragment;
            addOp(op);
        }
        void addOp(Op op) {
            if (mHead == null) {
                mHead = mTail = op;
            } else {
                op.prev = mTail;
                mTail.next = op;
                mTail = op;
            }
            op.enterAnim = mEnterAnim;
            op.exitAnim = mExitAnim;
            op.popEnterAnim = mPopEnterAnim;
            op.popExitAnim = mPopExitAnim;
            mNumOp++;
        }

        /**
         * 默认不允许状态丢失
         * 关于状态丢失：
         * 1 比如说如果在activity的onStop里调用commit()就会抛异常，显然一般我们不会这么做，所以一般情况可以直接用commit。
         * 2 但是有时有异步操作，尤其设计到handler异步消息处理的时候，可能会有问题，这是可以用commitAllowingStateLoss
         */
        public int commit() {
            return commitInternal(false);
        }
        /**
         * 允许状态丢失
         */
        public int commitAllowingStateLoss() {
            return commitInternal(true);
        }
        int commitInternal(boolean allowStateLoss) {

            if (mAddToBackStack) {
                mIndex = mManager.allocBackStackIndex(this);
            } else {
                mIndex = -1;
            }
            mManager.enqueueAction(this, allowStateLoss);
            return mIndex;
        }

            @Override
        public void run() {

            Op op = mHead;
            while (op != null) {
                switch (op.cmd) {
                    case OP_ADD: {
                        MyFragment f = op.fragment;
                        f.mNextAnim = op.enterAnim;
                        mManager.addFragment(f, false);
                    }
                    break;
                    case OP_REPLACE: {
                        MyFragment f = op.fragment;
                        int containerId = f.mContainerId;
                        if (mManager.mAdded != null) {
                            for (int i = 0; i < mManager.mAdded.size(); i++) {
                                Fragment old = mManager.mAdded.get(i);
                                if (FragmentManagerImpl.DEBUG) {
                                    Log.v(TAG,
                                            "OP_REPLACE: adding=" + f + " old=" + old);
                                }
                                if (old.mContainerId == containerId) {
                                    if (old == f) {
                                        op.fragment = f = null;
                                    } else {
                                        if (op.removed == null) {
                                            op.removed = new ArrayList<Fragment>();
                                        }
                                        op.removed.add(old);
                                        old.mNextAnim = op.exitAnim;
                                        if (mAddToBackStack) {
                                            old.mBackStackNesting += 1;
                                            if (FragmentManagerImpl.DEBUG) {
                                                Log.v(TAG, "Bump nesting of "
                                                        + old + " to " + old.mBackStackNesting);
                                            }
                                        }
                                        mManager.removeFragment(old, mTransition, mTransitionStyle);
                                    }
                                }
                            }
                        }
                        if (f != null) {
                            f.mNextAnim = op.enterAnim;
                            mManager.addFragment(f, false);
                        }
                    }
                    break;
                    case OP_REMOVE: {
                        Fragment f = op.fragment;
                        f.mNextAnim = op.exitAnim;
                        mManager.removeFragment(f, mTransition, mTransitionStyle);
                    }
                    break;
                    case OP_HIDE: {
                        Fragment f = op.fragment;
                        f.mNextAnim = op.exitAnim;
                        mManager.hideFragment(f, mTransition, mTransitionStyle);
                    }
                    break;
                    case OP_SHOW: {
                        Fragment f = op.fragment;
                        f.mNextAnim = op.enterAnim;
                        mManager.showFragment(f, mTransition, mTransitionStyle);
                    }
                    break;
                    case OP_DETACH: {
                        Fragment f = op.fragment;
                        f.mNextAnim = op.exitAnim;
                        mManager.detachFragment(f, mTransition, mTransitionStyle);
                    }
                    break;
                    case OP_ATTACH: {
                        Fragment f = op.fragment;
                        f.mNextAnim = op.enterAnim;
                        mManager.attachFragment(f, mTransition, mTransitionStyle);
                    }
                    break;
                    default: {
                        throw new IllegalArgumentException("Unknown cmd: " + op.cmd);
                    }
                }

                op = op.next;
            }

            mManager.moveToState(mManager.mCurState, mTransition,
                    mTransitionStyle, true);

            if (mAddToBackStack) {
                mManager.addBackStackState(this);
            }
        }
    }
}
