package com.milo.libbase.widget.refreshview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.milo.libbase.R;

public class VerticalLoadMoreView implements ILoadMoreView {

    private ProgressBar mProgressBar;
    private TextView mTvNoMore;
    private View mRootView;
    private View.OnClickListener mRetryListener;

    private String mTextEmpty;
    private String mTextError;
    private boolean mIsError;

    public VerticalLoadMoreView(Context context) {
        init(context,
                context.getString(R.string.lib_ui_place_hold_empty),
                context.getString(R.string.lib_ui_place_hold_error));
    }

    public VerticalLoadMoreView(Context context, String textEmpty, String textError) {
        init(context, textEmpty, textError);
    }

    public void setRetryListener(View.OnClickListener retryListener) {
        mRetryListener = retryListener;
    }

    private void init(Context context, String textEmpty, String textError) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.lib_ui_view_more_vertical, null);
        mRootView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                context.getResources().getDimensionPixelSize(R.dimen.lib_ui_height_load_more)));
        mProgressBar = mRootView.findViewById(R.id.progress_bar);
        mTvNoMore = mRootView.findViewById(R.id.tv_no_more);
        mTextEmpty = textEmpty;
        mTextError = textError;

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsError && mRetryListener != null) {
                    mRetryListener.onClick(v);
                }
            }
        });
    }

    @NonNull
    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void showLoading() {
        mRootView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTvNoMore.setVisibility(View.GONE);
        mIsError = false;
    }

    @Override
    public void showNoMore() {
        mRootView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTvNoMore.setVisibility(View.VISIBLE);
        mTvNoMore.setText(mTextEmpty);
        mIsError = false;
    }

    @Override
    public void showError() {
        mRootView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTvNoMore.setVisibility(View.VISIBLE);
        mTvNoMore.setText(mTextError);
        mIsError = true;
    }

    @Override
    public void hide() {
        mRootView.setVisibility(View.GONE);
        mIsError = false;
    }
}
