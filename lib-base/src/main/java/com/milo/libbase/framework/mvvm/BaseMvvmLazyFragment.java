package com.milo.libbase.framework.mvvm;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.milo.libbase.utils.LogUtils;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 8/19/21
 */
public abstract class BaseMvvmLazyFragment<VM extends BaseMvvmViewModel> extends BaseMvvmFragment<VM> {

    protected boolean mIsViewCreated;
    protected boolean mIsFetchData;

    public abstract void lazyLoad();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsViewCreated = true;
        lazyFetchDataIfPrepared();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsViewCreated = false;
        mIsFetchData = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyFetchDataIfPrepared();
        }
    }

    private void lazyFetchDataIfPrepared() {
        if (getUserVisibleHint() && !mIsFetchData && mIsViewCreated) {
            mIsFetchData = true;
            LogUtils.d(TAG, "lazyLoad");
            lazyLoad();
        }
    }

}
