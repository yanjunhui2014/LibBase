package com.milo.libbase.framework.mvp;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * Created by zhl on 16/2/21.
 * FZILoadMoreView
 */
public interface FZILoadMoreView {

    @NonNull
    View getView();

    void showLoading();

    void showNoMore();

    void showError();

    void setNoMoreText(String text);

    void hide();
}
