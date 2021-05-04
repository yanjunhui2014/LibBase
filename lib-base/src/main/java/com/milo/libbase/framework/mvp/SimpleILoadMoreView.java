package com.milo.libbase.framework.mvp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.milo.libbase.R;

public class SimpleILoadMoreView implements ILoadMoreView {

    private Context mContext;

    ProgressBar mProgressBar;
    TextView mTvNoMore;

    private View mRootView;

    private View.OnClickListener mOnClickListener;

    public SimpleILoadMoreView(Context context){
        this.mContext = context;
        initView();
    }

    private void initView(){
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.libbase_framework_view_more_vertical, null);
        mProgressBar = mRootView.findViewById(R.id.progressBar);
        mTvNoMore = mRootView.findViewById(R.id.tv_no_more) ;
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
    }

    @Override
    public void showNoMore() {
        mRootView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTvNoMore.setVisibility(View.VISIBLE);
        mTvNoMore.setOnClickListener(null);
    }

    @Override
    public void showError() {
        mRootView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTvNoMore.setText("出错了，点击重试");
        mTvNoMore.setOnClickListener(mOnClickListener);
    }

    @Override
    public void setNoMoreText(String text) {
        mTvNoMore.setText(text);
    }

    @Override
    public void hide() {
        mRootView.setVisibility(View.INVISIBLE);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
