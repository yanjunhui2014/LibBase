package com.milo.libbase.widget.refreshview.base;

import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;

public class ListViewCheckVisibleScrollListener implements AbsListView.OnScrollListener {

    private static final int MSG_CHECK_VISIBLE = 1111;

    private final OnItemExposeListener mOnItemExposeListener;
    private final Handler mHandler;
    private AbsListView mAbsListView;
    private int mTotalItemCount;
    private boolean mIsFirst = true;

    public ListViewCheckVisibleScrollListener(OnItemExposeListener onItemExposeListener) {
        mOnItemExposeListener = onItemExposeListener;
        mHandler = new Handler(msg -> {
            if (msg.what == MSG_CHECK_VISIBLE) {
                if (mAbsListView != null) {
                    handleCurrentVisibleItems(mAbsListView, mAbsListView.getFirstVisiblePosition(),
                            mAbsListView.getLastVisiblePosition() - mAbsListView.getFirstVisiblePosition() + 1,
                            mTotalItemCount);
                }
            }
            return true;
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mAbsListView = view;
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mHandler.sendEmptyMessageDelayed(MSG_CHECK_VISIBLE, 500);
        } else {
            mHandler.removeMessages(MSG_CHECK_VISIBLE);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mAbsListView = view;
        mTotalItemCount = totalItemCount;
        if (mIsFirst) {
            mIsFirst = false;
            mHandler.sendEmptyMessageDelayed(MSG_CHECK_VISIBLE, 500);
        }
    }

    private void handleCurrentVisibleItems(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        for (int i = 0; i < visibleItemCount ; i++) {
            View view = listView.getChildAt(i);
            setCallbackForLogicVisibleView(view, firstVisibleItem + i);
        }
        for (int i = 0; i < firstVisibleItem; i++) {
            mOnItemExposeListener.onItemViewVisible(false, i);
        }
        if (firstVisibleItem + visibleItemCount - 1 < totalItemCount) {
            for (int i = firstVisibleItem + visibleItemCount; i < totalItemCount; i++) {
                mOnItemExposeListener.onItemViewVisible(false, i);
            }
        }
    }

    private void setCallbackForLogicVisibleView(View view, int position) {
        if (view == null || view.getVisibility() != View.VISIBLE ||
                !view.isShown() || !view.getGlobalVisibleRect(new Rect())) {
            return;
        }

        Rect rect = new Rect();

        boolean cover = view.getGlobalVisibleRect(rect);
        boolean visibleHeightEnough = rect.height() > view.getMeasuredHeight() * 0.8 ;
        boolean goneHeightEnough = rect.height() == 0;

        if (cover && visibleHeightEnough) {
            mOnItemExposeListener.onItemViewVisible(true, position);
        }else {
            if (!cover || goneHeightEnough) {
                mOnItemExposeListener.onItemViewVisible(false, position);
            }
        }
    }
}
