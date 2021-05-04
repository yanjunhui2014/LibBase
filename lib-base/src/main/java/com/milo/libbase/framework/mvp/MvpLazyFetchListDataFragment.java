package com.milo.libbase.framework.mvp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public abstract class MvpLazyFetchListDataFragment<P extends MvpListDataContract.Presenter<D>, D>
        extends MvpListDataFragment<P, D> {

    protected boolean mIsViewCreated;
    protected boolean mIsFetchData;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsViewCreated = true;
        lazyFetchDataIfPrepared();
    }

    @Override
    protected void subscribe() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsViewCreated = false;
        mIsFetchData = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyFetchDataIfPrepared();
        }
    }

    @Override
    protected void onRetry() {
        lazyFetchData();
    }

    private void lazyFetchData() {
        mIsFetchData = true;
        mPresenter.subscribe();
    }

    private void lazyFetchDataIfPrepared() {
        if (getUserVisibleHint() && !mIsFetchData && mIsViewCreated) {
            lazyFetchData();
        }
    }
}
