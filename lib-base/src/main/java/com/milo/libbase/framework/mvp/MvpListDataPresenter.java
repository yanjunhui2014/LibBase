package com.milo.libbase.framework.mvp;

import com.milo.libbase.data.impl.Response;

import java.util.ArrayList;
import java.util.List;

public abstract class MvpListDataPresenter<V extends MvpListDataContract.View, M, D> extends MvpBasePresenter
        implements MvpListDataContract.Presenter<D> {

    protected V mView;
    protected M mModel;

    protected List<D> mDataList = new ArrayList<>();

    protected int mStart;
    protected int mRows = 10;

    /**
     * 是否首次加载
     */
    protected boolean mIsFirstLoad = true;
    /**
     * 是否刷新
     */
    protected boolean mIsRefresh = false;
    /**
     * 是否包含更多
     */
    protected boolean mHasMore = true;

    public MvpListDataPresenter(V view, M model) {
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        super.subscribe();
        mView.showLoading();
        refresh();
    }

    @Override
    public void refresh() {
        mStart = 0;
        mHasMore = true;
        mIsRefresh = true;
        loadData();
    }

    @Override
    public void loadMore() {
        mStart += mRows;
        loadData();
    }

    @Override
    public List<D> getDataList() {
        return mDataList;
    }

    protected abstract void loadData();

    protected void success(Response<List<D>> response) {
        mIsFirstLoad = false;
        mIsRefresh = false;
        List<D> dataList = response.getData();
        success(dataList);
    }

    protected void success(List<D> dataList) {
        if (mView == null) {
            return;
        }
        mIsFirstLoad = false;
        mIsRefresh = false;
        if (isRefresh()) {
            mDataList.clear();
        }
        if (dataList != null && !dataList.isEmpty()) {
            mDataList.addAll(dataList);
            mView.showList(true);
        } else if (mDataList.isEmpty()) {
            mView.showEmpty();
        } else {
            mHasMore = false;
            mView.showList(false);
        }
    }

    protected void fail(String message) {
        if (mView == null) {
            return;
        }
        mStart -= mRows;
        mStart = Math.max(0, mStart);

        if (mIsFirstLoad) {
            mView.showError();
        } else {
            mView.showList(true);
        }
    }

    public boolean isRefresh() {
        return mStart == 0;
    }

//    private class FZResponse<T>  {
//
//        public static final int STATUS_SUCCESS  = 1;
//        public static final int STATUS_FAIL     = 0;
//        public static final int STATUS_403      = 403;
//        public static final int STATUS_NO_CLASS = 2; //作业列表没有班级。 这个设计不好
//
//        public int status;
//
//        public String msg;
//
//        public T data;
//    }

}
