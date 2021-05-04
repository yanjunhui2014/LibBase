package com.milo.libbase.widget.refreshview;

public interface IListView {

    void showList(boolean isHasMore);

    void showEmpty();

    void showError();

    void showLoading();
}
