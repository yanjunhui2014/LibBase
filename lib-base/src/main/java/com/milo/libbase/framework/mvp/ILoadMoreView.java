package com.milo.libbase.framework.mvp;

import android.view.View;

import androidx.annotation.NonNull;

public interface ILoadMoreView {

    @NonNull
    View getView();

    void showLoading();

    void showNoMore();

    void showError();

    void setNoMoreText(String text);

    void hide();
}
