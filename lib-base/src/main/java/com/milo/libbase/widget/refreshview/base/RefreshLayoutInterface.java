package com.milo.libbase.widget.refreshview.base;

import android.view.ViewGroup;

public interface RefreshLayoutInterface {

    void setOnRefreshListener(OnRefreshListener onRefreshListener);

    void setRefreshEnable(boolean isEnable);

    void setRefreshing(boolean isRefreshing);

    void setVisibility(int setVisibility);

    ViewGroup getSelf();

    interface OnRefreshListener {

        void onRefresh();
    }
}
