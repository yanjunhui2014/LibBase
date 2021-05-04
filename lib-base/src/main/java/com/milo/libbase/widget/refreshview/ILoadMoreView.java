package com.milo.libbase.widget.refreshview;

import android.view.View;

import androidx.annotation.NonNull;

public interface ILoadMoreView {

    @NonNull
    View getView();

    void showLoading();

    void showNoMore();

    void showError();

    void hide();
}
