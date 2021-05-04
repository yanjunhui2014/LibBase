package com.milo.libbase.framework.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.milo.libbase.R;
import com.milo.libbase.widget.refreshview.base.RefreshListener;
import com.zhl.commonadapter.BaseViewHolder;
import com.zhl.commonadapter.CommonRecyclerAdapter;

public abstract class FZListDataFragment<P extends FZListDataContract.Presenter<D>, D> extends FZBaseRecyclerFragment<P>
        implements FZListDataContract.View<P> {

    protected CommonRecyclerAdapter<D> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        init();

        return rootView;
    }

    protected abstract void onItemClick(View view, int position);

    protected CommonRecyclerAdapter<D> createAdapter() {
        return new CommonRecyclerAdapter<D>(mPresenter.getDataList()) {

            @Override
            public BaseViewHolder<D> createViewHolder(int type) {
                return FZListDataFragment.this.createViewHolder();
            }
        };
    }

    protected abstract BaseViewHolder<D> createViewHolder();

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    protected void init() {
        mAdapter = createAdapter();

        mRefreshRecyclerView.getXSwipeRefreshLayout().setColorSchemeResources(R.color.libbase_c1);
        mRefreshRecyclerView.setLayoutManager(getLayoutManager());
        mRefreshRecyclerView.setAdapter(mAdapter);

        mRefreshRecyclerView.setRefreshListener(new RefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh();
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });

        mAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FZListDataFragment.this.onItemClick(view, position);
            }
        });
    }

}
