package com.milo.libbase.widget.refreshview.base;

import android.graphics.Rect;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class RecyclerViewCheckVisibleScrollListener extends RecyclerView.OnScrollListener {

    private static final int MSG_CHECK_VISIBLE = 1111;

    private final Handler              mHandler;
    private final OnItemExposeListener mOnItemExposeListener;
    private       RecyclerView         mRecyclerView;
    private       boolean              mIsFirst = true;

    public RecyclerViewCheckVisibleScrollListener(OnItemExposeListener onItemExposeListener) {
        mOnItemExposeListener = onItemExposeListener;
        mHandler = new Handler(msg -> {
            if (msg.what == MSG_CHECK_VISIBLE) {
                if (mRecyclerView != null) {
                    handleCurrentVisibleItems(mRecyclerView);
                }
            }
            return true;
        });
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mRecyclerView = recyclerView;
        if (mIsFirst) {
            mIsFirst = false;
            mHandler.sendEmptyMessageDelayed(MSG_CHECK_VISIBLE, 500);
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        mRecyclerView = recyclerView;
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            mHandler.sendEmptyMessageDelayed(MSG_CHECK_VISIBLE, 500);
        } else {
            mHandler.removeMessages(MSG_CHECK_VISIBLE);
        }
    }

    public void handleCurrentVisibleItems(RecyclerView recyclerView) {
        if (recyclerView.getVisibility() != View.VISIBLE ||
                !recyclerView.isShown() || !recyclerView.getGlobalVisibleRect(new Rect())) {
            return;
        }

        int[] range = new int[2];
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            range = findRangeLinear(linearLayoutManager);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) manager;
            range = findRangeStaggeredGrid(staggeredGridLayoutManager);
        }
        if (range.length < 2) {
            return;
        }
        for (int i = range[0]; i <= range[1]; i++) {
            View view = manager.findViewByPosition(i);
            setCallbackForLogicVisibleView(view, i);
        }
        for (int i = 0; i < range[0]; i++) {
            mOnItemExposeListener.onItemViewVisible(false, i);
        }
        if (range[1] < recyclerView.getAdapter().getItemCount() - 1) {
            for (int i = range[1] + 1; i < recyclerView.getAdapter().getItemCount(); i++) {
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

        boolean visibleHeightEnough = rect.height() > view.getMeasuredHeight() * 0.8f ;
        boolean goneHeightEnough =  rect.height() == 0;
        boolean visibleWidthEnough = rect.width() > view.getMeasuredWidth() * 0.8;
        boolean goneWidthEnough = rect.width() == 0;
        boolean isItemViewVisibleInLogic = visibleHeightEnough && visibleWidthEnough;
        boolean isItemViewGoneInLogic = goneHeightEnough && goneWidthEnough;

        if (cover && isItemViewVisibleInLogic) {
            mOnItemExposeListener.onItemViewVisible(true, position);
        } else {
            if (!cover || isItemViewGoneInLogic) {
                mOnItemExposeListener.onItemViewVisible(false, position);
            }
        }
    }


    private int[] findRangeLinear(LinearLayoutManager manager) {
        int[] range = new int[2];
        range[0] = manager.findFirstVisibleItemPosition();
        range[1] = manager.findLastVisibleItemPosition();
        return range;
    }

    private int[] findRangeStaggeredGrid(StaggeredGridLayoutManager manager) {
        int[] startPos = new int[manager.getSpanCount()];
        int[] endPos = new int[manager.getSpanCount()];
        manager.findFirstVisibleItemPositions(startPos);
        manager.findLastVisibleItemPositions(endPos);
        return findRange(startPos, endPos);
    }

    private int[] findRange(int[] startPos, int[] endPos) {
        int start = startPos[0];
        int end = endPos[0];
        for (int i = 1; i < startPos.length; i++) {
            if (start > startPos[i]) {
                start = startPos[i];
            }
        }
        for (int i = 1; i < endPos.length; i++) {
            if (end < endPos[i]) {
                end = endPos[i];
            }
        }
        return new int[]{start, end};
    }
}
