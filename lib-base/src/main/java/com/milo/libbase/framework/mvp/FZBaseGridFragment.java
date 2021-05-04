package com.milo.libbase.framework.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.milo.libbase.R;
import com.milo.libbase.widget.refreshview.IPlaceHolderView;
import com.milo.libbase.widget.refreshview.PlaceHolderView;
import com.milo.libbase.widget.refreshview.xSwipeRefreshLayout.SwipeRefreshGridView;

public class FZBaseGridFragment<T extends IBasePresenter> extends FZBaseFragment<T> implements FZIListDataView {

    protected SwipeRefreshGridView mRefreshGridView;
    protected IPlaceHolderView     mPlaceHolderView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.libbase_framework_mvp_fragment_base_list, container, false);

        initView((ViewGroup) rootView);

        return rootView;
    }

    protected void initView(ViewGroup rootView) {
        mRefreshGridView = new SwipeRefreshGridView(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        rootView.addView((View) mRefreshGridView, params);
        mPlaceHolderView = getPlaceHolderView();
        mRefreshGridView.setPlaceHolderView(getPlaceHolderView());
        mRefreshGridView.getPlaceHolderView().getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public void showList(boolean isHasMore) {
        mRefreshGridView.showList(isHasMore);
    }

    @Override
    public void showEmpty() {
        mRefreshGridView.showEmpty();
    }

    @Override
    public void showError() {
        mRefreshGridView.showError();
    }

    @Override
    public void showLoading() {
        mRefreshGridView.showLoading();
    }

    public IPlaceHolderView getPlaceHolderView() {
        return new PlaceHolderView(mActivity);
    }

    protected void onRetry() {
        showLoading();
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }
}
