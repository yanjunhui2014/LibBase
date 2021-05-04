package com.milo.libbase.widget.refreshview.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.milo.libbase.widget.refreshview.MoreViewHolder;
import com.zhl.commonadapter.CommonRecyclerAdapter;

public abstract class BaseRefreshRecyclerView extends BaseRefreshView {

    private RecyclerView          mRecyclerView;
    private MoreViewHolder        mMoreViewHolder;
    private CommonRecyclerAdapter mAdapter;
    private RecyclerViewCheckVisibleScrollListener mRecyclerViewCheckVisibleScrollListener;
    private float mDownY;
    private int mLastCount;

    private boolean mIsUpPull;

    private boolean mIsSetMoreViewHolder;

    public BaseRefreshRecyclerView(Context context) {
        super(context);
    }

    public BaseRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void init(){
        super.init();
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (mDownY == 0) {
                            mDownY = motionEvent.getY();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mIsUpPull = (mDownY - motionEvent.getY()) > 0;
                        mDownY = 0;
                        break;
                }
                return false;
            }
        });
        mRefreshLayout.getSelf().addView(mRecyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    protected AbsListView addAbsListView() {
        return null;
    }

    @Override
    protected int getFirstVisiblePosition() {
        return getFirstPosition();
    }

    @Override
    public void showList(boolean isHasMore) {
        super.showList(isHasMore);
        if (mAdapter != null) {
            if (mLastCount >= mAdapter.getItemCount() || mLastCount == 0) {
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.notifyItemInserted(Math.max(mLastCount, 0));
            }
            mLastCount = mAdapter.getItemCount();
        }
    }

    @Override
    public void setItemExposeListener(OnItemExposeListener onItemExposeListener) {
        if (mRecyclerViewCheckVisibleScrollListener != null) {
            getRecyclerView().removeOnScrollListener(mRecyclerViewCheckVisibleScrollListener);
        }
        mRecyclerViewCheckVisibleScrollListener = new RecyclerViewCheckVisibleScrollListener(onItemExposeListener);
        getRecyclerView().addOnScrollListener(mRecyclerViewCheckVisibleScrollListener);
    }

    public RecyclerViewCheckVisibleScrollListener getRecyclerViewCheckVisibleScrollListener() {
        return mRecyclerViewCheckVisibleScrollListener;
    }

    public void setAdapter(@NonNull CommonRecyclerAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
        if (!mIsSetMoreViewHolder && mMoreViewHolder != null) {
            mAdapter.addFooter(mMoreViewHolder);
            mIsSetMoreViewHolder = true;
        }
    }

    public void setMoreViewHolder(@NonNull MoreViewHolder moreViewHolder) {
        mMoreViewHolder = moreViewHolder;
        mLoadMoreView = moreViewHolder;
        if (!mIsSetMoreViewHolder && mAdapter != null) {
            mAdapter.addFooter(mMoreViewHolder);
            mIsSetMoreViewHolder = true;
        }
    }

    public void setLayoutManager(@NonNull final RecyclerView.LayoutManager layoutManager,
                                 GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        mRecyclerView.setLayoutManager(layoutManager);
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            if (spanSizeLookup != null) {
                gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
            }
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = getLastPosition();

                if (!mIsLoading
                        && mIsUpPull
                        && mIsHasMore
                        && mAdapter.getItemCount() == (lastPosition + 1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mIsLoading = true;
                    mIsRefresh = false;
                    if (mRefreshListener != null) {
                        if (mMoreViewHolder != null) {
                            mMoreViewHolder.showLoading();
                        }
                        mRefreshListener.onLoadMore();
                    }
                } else if (!mIsHasMore) {
                    showNoMore();
                }
            }
        });
    }

    public void setLayoutManager(@NonNull final RecyclerView.LayoutManager layoutManager) {
        setLayoutManager(layoutManager, new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    if (mAdapter != null && mAdapter.getFooterSize() != 0) {
                        if (position == mAdapter.getItemCount() - 1) {
                            return gridLayoutManager.getSpanCount();
                        } else {
                            return 1;
                        }
                    }
                }
                return 1;
            }
        });
    }

    private int getFirstPosition() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        int firstPosition = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            firstPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findFirstVisibleItemPositions(firstPositions);
            firstPosition = firstPositions[0];
            for (int firstPositionTmp : firstPositions) {
                if (firstPosition > firstPositionTmp) {
                    firstPosition = firstPositionTmp;
                }
            }
        }

        return firstPosition;
    }

    private int getLastPosition() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        int lastPosition = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            lastPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
            lastPosition = lastPositions[0];
            for (int lastPositionTmp : lastPositions) {
                if (lastPosition < lastPositionTmp) {
                    lastPosition = lastPositionTmp;
                }
            }
        }

        return lastPosition;
    }
}
