package com.milo.libbase.framework.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.milo.libbase.R;
import com.milo.libbase.utils.RecyclerScrollUtil;
import com.milo.libbase.widget.refreshview.PlaceHolderView;
import com.milo.libbase.widget.refreshview.xSwipeRefreshLayout.SwipeRefreshRecyclerView;

public class MvpBaseRecyclerFragment<T extends IBasePresenter> extends MvpBaseFragment<T> implements IListDataView {

    protected PlaceHolderView          mPlaceHolderView;
    protected SwipeRefreshRecyclerView mRefreshRecyclerView;

    protected RecyclerScrollUtil mScrollUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = createRootView(inflater, container);

        initView((ViewGroup) rootView);

        return rootView;
    }

    protected View createRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.libbase_framework_mvp_fragment_base_list, container, false);
    }

    protected void initView(ViewGroup rootView) {
        mRefreshRecyclerView = new SwipeRefreshRecyclerView(getContext());
        mPlaceHolderView = new PlaceHolderView(mActivity);
        mRefreshRecyclerView.setPlaceHolderView(mPlaceHolderView);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        rootView.addView((View) mRefreshRecyclerView, params);

        mRefreshRecyclerView.getPlaceHolderView().getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetry();
            }
        });

        mScrollUtil = new RecyclerScrollUtil();

        mRefreshRecyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollUtil.onScrolled(recyclerView);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mScrollUtil.onScrollStateChanged(recyclerView, newState);
            }

        });
    }

    protected void onRetry() {
        showLoading();
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    protected void scrollToPosition(int position) {
        if (mScrollUtil != null && mRefreshRecyclerView.getRecyclerView() != null) {
            mScrollUtil.scrollToPosition(position, mRefreshRecyclerView.getRecyclerView());
        }
    }

    public PlaceHolderView getPlaceHolderView() {
        return mPlaceHolderView;
    }

    @Override
    public void showList(boolean isHasMore) {
        mRefreshRecyclerView.showList(isHasMore);
    }

    @Override
    public void showEmpty() {
        mRefreshRecyclerView.showEmpty();
    }

    @Override
    public void showError() {
        mRefreshRecyclerView.showError();
    }

    @Override
    public void showLoading() {
        mRefreshRecyclerView.showLoading();
    }
}
