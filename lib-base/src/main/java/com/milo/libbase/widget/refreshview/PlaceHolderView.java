package com.milo.libbase.widget.refreshview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.milo.libbase.R;

public class PlaceHolderView implements IPlaceHolderView {

    protected Context        mContext;
    protected LayoutInflater mInflater;

    protected ImageView            mImg;
    protected TextView             mTvText;
    protected TextView             mBtnEmpty;
    protected ProgressBar          mPbLoading;
    protected View                 mRootView;
    protected View.OnClickListener mRetryListener;

    protected int     mImgErrorRes;
    protected int     mImgEmptyRes;
    protected String  mTextError;
    protected String  mTextEmpty;
    protected String  mTextLoading;
    protected boolean mIsError;


    public PlaceHolderView(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        init();
    }


    protected void init() {
        mRootView = mInflater.inflate(R.layout.libbase_placeholder_view, null, false);
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mImg = mRootView.findViewById(R.id.img);
        mTvText = mRootView.findViewById(R.id.tv_text);
        mBtnEmpty = mRootView.findViewById(R.id.mBtnEmpty);
        mPbLoading = mRootView.findViewById(R.id.pb_loading);
        mTextError = mContext.getString(R.string.libbase_place_hold_error);
        mTextEmpty = mContext.getString(R.string.libbase_place_hold_empty);
        mTextLoading = mContext.getString(R.string.libbase_place_hold_load);

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsError && mRetryListener != null) {
                    showLoading();
                    mRetryListener.onClick(v);
                }
            }
        });
    }

    public PlaceHolderView setRetryListener(View.OnClickListener retryListener) {
        mRetryListener = retryListener;
        return this;
    }

    public PlaceHolderView setImgErrorRes(@DrawableRes int errorRes) {
        mImgErrorRes = errorRes;
        return this;
    }

    public PlaceHolderView setImgEmptyRes(@DrawableRes int emptyRes) {
        mImgEmptyRes = emptyRes;
        return this;
    }

    public PlaceHolderView setTextError(String textError) {
        mTextError = textError;
        return this;
    }

    public PlaceHolderView setTextEmpty(String textEmpty) {
        mTextEmpty = textEmpty;
        return this;
    }

    public PlaceHolderView setTextLoading(String textLoading) {
        mTextLoading = textLoading;
        return this;
    }

    @Override
    public void showLoading() {
        mRootView.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mImg.setVisibility(View.GONE);
        mTvText.setText(mTextLoading);
        mIsError = false;
    }

    @Override
    public void showError() {
        mRootView.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);
        mImg.setVisibility(View.VISIBLE);
        if (mImgErrorRes == 0) {
            mImg.setVisibility(View.GONE);
        } else {
            mImg.setVisibility(View.VISIBLE);
            mImg.setImageResource(mImgErrorRes);
        }
        mTvText.setText(mTextError);
        mIsError = true;
    }

    @Override
    public void showEmpty() {
        mRootView.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);
        mImg.setVisibility(View.VISIBLE);
        if (mImgEmptyRes == 0) {
            mImg.setVisibility(View.GONE);
        } else {
            mImg.setVisibility(View.VISIBLE);
            mImg.setImageResource(mImgEmptyRes);
        }
        mTvText.setText(mTextEmpty);
        mIsError = false;
    }

    @Override
    public void showNothing() {
        mRootView.setVisibility(View.GONE);
        mIsError = false;
    }

    @Override
    public View getView() {
        return mRootView;
    }

    /**
     * 显示空视图按钮
     */
    public void showEmptyBtn(View.OnClickListener clickListener, String text) {
        mBtnEmpty.setText(text);
        mBtnEmpty.setOnClickListener(clickListener);
        mBtnEmpty.setVisibility(View.VISIBLE);
        showEmpty();
    }
}
