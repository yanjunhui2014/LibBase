package com.milo.libbase.framework.mvvm;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.milo.libbase.data.AppConstants;
import com.milo.libbase.framework.mvp.BaseActivity;
import com.milo.libbase.receiver.LoginBroadcastReceiver;
import com.milo.libbase.utils.LogUtils;
import com.milo.libbase.widget.refreshview.LoadingState;
import com.milo.libbase.widget.refreshview.PlaceHolderView;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 8/19/21
 */
public abstract class BaseMvvmActivity<VM extends BaseMvvmViewModel> extends BaseActivity implements LoginBroadcastReceiver.LoginListener {
    protected String TAG = "BaseMvvmFragment";

    protected VM viewModel;

    private LoginBroadcastReceiver loginBroadcastReceiver;

    protected PlaceHolderView placeHolderView;

    protected View rootView;

    public abstract void initViewModel();

    public abstract void initObserve();

    public abstract void initView();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.TAG = this.getClass().getSimpleName();

        loginBroadcastReceiver = new LoginBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.ACTION_LOGIN_SUCCESS);
        intentFilter.addAction(AppConstants.ACTION_LOGOUT_SUCCESS);
        registerReceiver(loginBroadcastReceiver, intentFilter);

        initViewModel();
        if (viewModel == null) {
            throw new NullPointerException("view is not allow empty");
        }
        initView();
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(loginBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        this.rootView = view;
        initView();
        initPlaceHolderViewAndObserve();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        initPlaceHolderViewAndObserve();
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLogOutSuccess() {

    }

    public void initPlaceHolderViewAndObserve() {
        getPlaceHolderView();
        viewModel.loadingStateLiveData.observe(this, new Observer<LoadingState>() {
            @Override
            public void onChanged(LoadingState loadingState) {
                if (placeHolderView == null) {
                    return;
                }
                if (placeHolderView.getView().getParent() == null) {
                    attachPlaceHolderViewToRootView(placeHolderView);
                }
                switch (loadingState) {
                    case LOADING:
                        placeHolderView.showLoading();
                        break;
                    case ERROR:
                        if (!TextUtils.isEmpty(loadingState.getMsg())) {
                            placeHolderView.setTextError(loadingState.getMsg());
                        }
                        placeHolderView.showError();
                        break;
                    case EMPTY:
                        if (!TextUtils.isEmpty(loadingState.getMsg())) {
                            placeHolderView.setTextEmpty(loadingState.getMsg());
                        }
                        placeHolderView.showEmpty();
                    case SHOW_CONTENT:
                        placeHolderView.showNothing();
                        break;
                    default:
                        placeHolderView.showNothing();
                }
            }
        });
        initObserve();
    }

    public PlaceHolderView getPlaceHolderView() {
        placeHolderView = new PlaceHolderView(this);
        placeHolderView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetryClick(v);
            }
        });
        return placeHolderView;
    }

    public void onRetryClick(View v) {
        LogUtils.d(LogUtils.getLoganTag(TAG), "onRetryClick");
    }

    protected void attachPlaceHolderViewToRootView(PlaceHolderView placeHolderView) {
        if (rootView instanceof ConstraintLayout) {
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT);
            ((ConstraintLayout) rootView).addView(placeHolderView.getView(), layoutParams);
        } else if (rootView instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT);
            ((ConstraintLayout) rootView).addView(placeHolderView.getView(), layoutParams);
        } else if (rootView instanceof LinearLayout) {
            throw new IllegalArgumentException("attachPlaceHolderViewToRootView fail, LinearLayout rootView is not support");
        }
    }

}
