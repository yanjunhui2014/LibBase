package com.milo.libbase.widget.refreshview;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.milo.libbase.R;

public class VerticalMoreViewHolder extends MoreViewHolder {

    private ProgressBar mPbLoading;
    private TextView mTvNoMore;
    private View.OnClickListener mRetryListener;

    private String mTextEmpty;
    private String mTextError;
    private boolean mIsError;


    public VerticalMoreViewHolder() {
        super();
    }

    public VerticalMoreViewHolder(String textEmpty, String textError) {
        super();
        mTextEmpty = textEmpty;
        mTextError = textError;
    }

    public void setRetryListener(View.OnClickListener retryListener) {
        mRetryListener = retryListener;
    }

    @Override
    public void findView(View view) {
        mPbLoading = view.findViewById(R.id.progress_bar);
        mTvNoMore = view.findViewById(R.id.tv_no_more);

        if (mTextEmpty == null) {
            mTextEmpty = mContext.getString(R.string.libbase_place_hold_empty);
        }

        if (mTextError == null) {
            mTextError = mContext.getString(R.string.libbase_place_hold_error);
        }

        if (view.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            layoutParams.setFullSpan(true);
        }

        mTvNoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsError && mRetryListener != null) {
                    mRetryListener.onClick(v);
                }
            }
        });
    }

    @Override
    public void updateView(LoadMore data, int position) {
        if (mItemView != null) {
            mItemView.setVisibility(View.VISIBLE);
            switch (mLoadMore.showWhat) {
                case LoadMore.SHOW_ERROR:
                    mPbLoading.setVisibility(View.GONE);
                    mTvNoMore.setVisibility(View.VISIBLE);
                    mTvNoMore.setText(mTextError);
                    mIsError = true;
                    break;
                case LoadMore.SHOW_LOADING:
                    mPbLoading.setVisibility(View.VISIBLE);
                    mTvNoMore.setVisibility(View.GONE);
                    mIsError = false;
                    break;
                case LoadMore.SHOW_NO_MORE:
                    mTvNoMore.setVisibility(View.VISIBLE);
                    mPbLoading.setVisibility(View.GONE);
                    mTvNoMore.setText(mTextEmpty);
                    mIsError = false;
                    break;
                case LoadMore.HIDE:
                    mItemView.setVisibility(View.GONE);
                    mIsError = false;
                    break;
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.libbase_framework_view_more_vertical;
    }
}
