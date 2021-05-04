package com.milo.libbase.widget.refreshview.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.milo.libbase.widget.refreshview.IListView;
import com.milo.libbase.widget.refreshview.ILoadMoreView;
import com.milo.libbase.widget.refreshview.IPlaceHolderView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRefreshView extends RelativeLayout implements IListView {

    protected RefreshListener  mRefreshListener;
    protected IPlaceHolderView                   mPlaceHolderView;
    protected ILoadMoreView                      mLoadMoreView;
    protected List<AbsListView.OnScrollListener> mOnScrollListeners = new ArrayList<>();
    protected BaseAdapter mBaseAdapter;
    protected RefreshLayoutInterface mRefreshLayout;

    private AbsListView mAbsListView;

    protected boolean mIsLoading;
    protected boolean mIsHasMore;
    protected boolean mIsRefresh = true;
    protected boolean mIsManualRefresh;

    public BaseRefreshView(Context context) {
        this(context, null, 0);
    }

    public BaseRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        this.isInEditMode();
        mRefreshLayout = createRefreshLayout();
        addRefreshLayout(mRefreshLayout);
        mAbsListView = addAbsListView();
        if (mAbsListView != null) {
            mAbsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (!mIsLoading
                            && mIsHasMore
                            && view.getCount() == (view.getLastVisiblePosition() + 1)
                            && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        mIsLoading = true;
                        mIsRefresh = false;
                        if (mRefreshListener != null) {
                            if (mLoadMoreView != null) {
                                mLoadMoreView.showLoading();
                            }
                            mRefreshListener.onLoadMore();
                        }
                    } else if (!mIsHasMore) {
                        showNoMore();
                    }

                    for (AbsListView.OnScrollListener onScrollListener : mOnScrollListeners) {
                        onScrollListener.onScrollStateChanged(view, scrollState);
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    for (AbsListView.OnScrollListener onScrollListener : mOnScrollListeners) {
                        onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                    }
                }
            });
        }
    }

    protected abstract AbsListView addAbsListView();

    protected abstract RefreshLayoutInterface createRefreshLayout();

    protected void addRefreshLayout(RefreshLayoutInterface refreshLayout) {
        this.addView(refreshLayout.getSelf(), LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setAdapter(@NonNull BaseAdapter adapter) {
        mBaseAdapter = adapter;
        mAbsListView.setAdapter(adapter);
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        mRefreshListener = refreshListener;
        mRefreshLayout.setOnRefreshListener(new RefreshLayoutInterface.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsLoading = true;
                mIsRefresh = true;
                mIsManualRefresh = true;
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }
        });
    }

    public void setRefreshEnable(boolean refreshEnable) {
        mRefreshLayout.setRefreshEnable(refreshEnable);
    }

    public void setRefreshing(boolean isRefreshing) {
        mRefreshLayout.setRefreshing(isRefreshing);
        mIsLoading = true;
        mIsRefresh = true;
    }

    public void setPlaceHolderView(@NonNull IPlaceHolderView placeHolderView) {
        if (mPlaceHolderView != null) {
            this.removeView(mPlaceHolderView.getView());
        }
        mPlaceHolderView = placeHolderView;
        this.addView(mPlaceHolderView.getView(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Deprecated
    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        addOnScrollListener(onScrollListener);
    }

    public void addOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        mOnScrollListeners.add(onScrollListener);
    }

    public RefreshLayoutInterface getRefreshLayout() {
        return mRefreshLayout;
    }

    public void showList(boolean isHasMore) {
        mRefreshLayout.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
        mIsLoading = false;
        mIsHasMore = isHasMore;

        if (mPlaceHolderView != null) {
            mPlaceHolderView.showNothing();
        }

        if (mLoadMoreView != null) {
            mLoadMoreView.hide();
            if (!isHasMore) {
                showNoMore();
            }
        }

        if (mBaseAdapter != null) {
            mBaseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showEmpty() {
        mIsLoading = false;
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setVisibility(View.GONE);
        if (mPlaceHolderView != null) {
            mPlaceHolderView.showEmpty();
        }
    }

    @Override
    public void showError() {
        if (mIsRefresh) {
            mIsLoading = false;
            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.setVisibility(View.GONE);
            if (mPlaceHolderView != null) {
                mPlaceHolderView.showError();
            }
        } else {
            if (mLoadMoreView != null) {
                mLoadMoreView.showError();
            }
        }
    }

    @Override
    public void showLoading() {
        mIsLoading = true;
        if (mIsRefresh) {
            if (!mIsManualRefresh) {
                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setVisibility(View.GONE);
                if (mPlaceHolderView != null) {
                    mPlaceHolderView.showLoading();
                }
            } else {
                mIsManualRefresh = false;
            }
        } else {
            if (mLoadMoreView != null) {
                mLoadMoreView.showLoading();
            }
        }
    }

    protected int getFirstVisiblePosition() {
        return mAbsListView.getFirstVisiblePosition();
    }

    protected void showNoMore() {
        if (mLoadMoreView != null) {
            if (getFirstVisiblePosition() <= 0) {
                mLoadMoreView.hide();
            } else {
                mLoadMoreView.showNoMore();
            }
        }
    }

    public IPlaceHolderView getPlaceHolderView() {
        return mPlaceHolderView;
    }

    public void setItemExposeListener(OnItemExposeListener onItemExposeListener) {
        mOnScrollListeners.add(new ListViewCheckVisibleScrollListener(onItemExposeListener));
    }

}
