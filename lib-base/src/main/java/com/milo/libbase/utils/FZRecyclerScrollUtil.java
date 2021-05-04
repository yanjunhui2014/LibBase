package com.milo.libbase.utils;

import android.view.animation.LinearInterpolator;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FZRecyclerScrollUtil {

    public boolean mScrollToMove;

    public int mScrollToPosition;

    public void onScrolled(RecyclerView recyclerView) {
        if (mScrollToMove) {
            mScrollToMove = false;
            scrollBy(recyclerView);
        }
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (mScrollToMove && newState == RecyclerView.SCROLL_STATE_IDLE) {
            mScrollToMove = false;
            scrollBy(recyclerView);
        }
    }

    /**
     * RecyclerView 移动到当前位置，
     */
    public void scrollToPosition(int position, RecyclerView recyclerView) {
        try {
            int firstItem = 0;
            int lastItem = 0;
            mScrollToPosition = position;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastItem = linearLayoutManager.findLastVisibleItemPosition();
            } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                firstItem = layoutManager.findFirstVisibleItemPosition();
                lastItem = layoutManager.findLastVisibleItemPosition();
            }
            LogUtils.e(getClass().getSimpleName(), "position: " + position + " firstItem: " + firstItem + " lastItem: " + lastItem);
            if (position <= firstItem) {
                recyclerView.scrollToPosition(position);
            } else if (position <= lastItem) {
                int top = recyclerView.getChildAt(position - firstItem).getTop();
                recyclerView.scrollBy(0, top);
            } else {
                mScrollToMove = true;
                recyclerView.scrollToPosition(position);
            }
        } catch (Exception e) {

        }
    }

    /**
     * RecyclerView 移动到指定位置
     *
     * @param position     - 指定位置
     * @param recyclerView
     * @param smooth       - 是否光滑移动
     */
    public void scrollToPosition(int position, RecyclerView recyclerView, boolean smooth) {
        try {
            int firstItem = 0;
            int lastItem = 0;
            mScrollToPosition = position;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastItem = linearLayoutManager.findLastVisibleItemPosition();
            } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                firstItem = layoutManager.findFirstVisibleItemPosition();
                lastItem = layoutManager.findLastVisibleItemPosition();
            }
            LogUtils.e(getClass().getSimpleName(), "position: " + position + " firstItem: " + firstItem + " lastItem: " + lastItem);
            if (position <= firstItem) {
                if (smooth) {
                    recyclerView.smoothScrollToPosition(position);
                } else {
                    recyclerView.scrollToPosition(position);
                }
            } else if (position <= lastItem) {
                int top = recyclerView.getChildAt(position - firstItem).getTop();
                if (smooth) {
                    recyclerView.smoothScrollBy(0, top, new LinearInterpolator(){
                        @Override
                        public float getInterpolation(float input) {
                            return super.getInterpolation(input);
                        }
                    });
                } else {
                    recyclerView.scrollBy(0, top);
                }
            } else {
                mScrollToMove = true;
                if (smooth) {
                    recyclerView.smoothScrollToPosition(position);
                } else {
                    recyclerView.scrollToPosition(position);
                }
            }
        } catch (Exception e) {

        }
    }

    void scrollBy(RecyclerView recyclerView) {
        try {
            int firstItem = 0;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstItem = mScrollToPosition - linearLayoutManager.findFirstVisibleItemPosition();
            } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                firstItem = mScrollToPosition - layoutManager.findFirstVisibleItemPosition();
            }

            if (0 <= firstItem && firstItem < recyclerView.getChildCount()) {
                //获取要置顶的项顶部离RecyclerView顶部的距离
                int top = recyclerView.getChildAt(firstItem).getTop();
                recyclerView.scrollBy(0, top);
            }
        } catch (Exception e) {

        }
    }
}
