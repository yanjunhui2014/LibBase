package com.milo.libbase.framework.mvp;

public interface IListDataView {

    void showList(boolean isHasMore);

    void showEmpty();

    void showError();

    void showLoading();

}
