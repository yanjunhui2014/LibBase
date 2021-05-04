package com.milo.libbase.widget.refreshview;


import com.milo.libbase.widget.refreshview.base.BaseRefreshView;

public class RefreshBindingAdapter {

//    @BindingAdapter(value = "loadingState")
    public static void showLoadingState(BaseRefreshView baseRefreshView, LoadingState loadingState) {
        if (loadingState == null) {
            baseRefreshView.showLoading();
            return;
        }
        switch (loadingState) {
            case LOADING:
                baseRefreshView.showLoading();
                break;
            case RELOADING:
                baseRefreshView.setRefreshing(true);
                break;
            case ERROR:
                baseRefreshView.showError();
                break;
            case NO_MORE:
                baseRefreshView.showList(false);
                break;
            case HAVE_MORE:
                baseRefreshView.showList(true);
                break;
            case EMPTY:
                baseRefreshView.showEmpty();
                break;
        }
    }

}
