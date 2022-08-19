package com.milo.libbase.widget.refreshview;

import android.view.View;

import androidx.annotation.NonNull;

import com.milo.libbase.commonadapter.BaseViewHolder;

public abstract class MoreViewHolder extends BaseViewHolder<MoreViewHolder.LoadMore> implements ILoadMoreView {

    protected LoadMore mLoadMore;

    public MoreViewHolder() {
        mLoadMore = new LoadMore();
    }

    public void showLoading() {
        mLoadMore.showWhat = LoadMore.SHOW_LOADING;
        updateView(mLoadMore, 0);
    }

    public void showNoMore() {
        mLoadMore.showWhat = LoadMore.SHOW_NO_MORE;
        updateView(mLoadMore, 0);
    }

    public void showError() {
        mLoadMore.showWhat = LoadMore.SHOW_ERROR;
        updateView(mLoadMore, 0);
    }

    public void hide() {
        mLoadMore.showWhat = LoadMore.HIDE;
        updateView(mLoadMore, 0);
    }

    @NonNull
    @Override
    public View getView() {
        return getItemView();
    }

    public static class LoadMore {

        public static final int SHOW_LOADING = 1;
        public static final int SHOW_NO_MORE = 2;
        public static final int SHOW_ERROR   = 3;
        public static final int HIDE         = 4;

        public int showWhat;

        public String noMoreText;
    }
}
