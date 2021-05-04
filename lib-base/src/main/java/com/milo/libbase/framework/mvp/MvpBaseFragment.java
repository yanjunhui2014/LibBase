package com.milo.libbase.framework.mvp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.milo.libbase.data.AppConstants;
import com.milo.libbase.receiver.LoginBroadcastReceiver;
import com.milo.libbase.utils.Utils;
import com.milo.libbase.widget.dialog.WaitDialog;

/**
 * 标题：fragment基类
 * 功能：
 * 备注：基于Created by zhouhl on 2016/2/17 优化，用来兼容少趣模块化，以更小的代价完成代码的迁移
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/11/9 11:12
 */
public class MvpBaseFragment<T extends IBasePresenter> extends BaseFragment implements MvpBaseView<T>, ITrackResource, LoginBroadcastReceiver.LoginListener {
    protected String TAG = this.getClass().getSimpleName();

    protected Activity mActivity;
    protected T          mPresenter;
    protected WaitDialog mWaitDialog;

    protected boolean mIsViewCreated;

    private LoginBroadcastReceiver mLoginBroadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Utils.writeCommonLog(getClass().getSimpleName(), "onCreate");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.writeCommonLog(getClass().getSimpleName(), "onViewCreated");
        mIsViewCreated = true;
        subscribe();
        try {
            mLoginBroadcastReceiver = new LoginBroadcastReceiver(this);
            IntentFilter filter = new IntentFilter();
            filter.addAction(AppConstants.ACTION_LOGIN_SUCCESS);
            filter.addAction(AppConstants.ACTION_LOGOUT_SUCCESS);

            mActivity.registerReceiver(mLoginBroadcastReceiver, filter);
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroyView() {
        Utils.writeCommonLog(getClass().getSimpleName(), "onDestroyView");
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
        try {
            if (mLoginBroadcastReceiver != null) {
                mActivity.unregisterReceiver(mLoginBroadcastReceiver);
            }
        } catch (Exception e) {

        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Utils.writeCommonLog(getClass().getSimpleName(), "onDestroy");
        super.onDestroy();
    }

    @Override
    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgress() {
        try {
            showWaitDialog("");
        } catch (Exception e) {

        }
    }

    @Override
    public void hideProgress() {
        try {
            dismissWaitDialog();
        } catch (Exception e) {

        }
    }

    @Override
    public void showToast(int resId) {
        showToast(mActivity.getResources().getString(resId));
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        mActivity.finish();
    }

    @Override
    public String getTrackName() {
        return getHoldingActivity().getTrackName();
    }

    @Override
    public String getJumpFrom() {
        return getHoldingActivity().getJumpFrom();
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLogOutSuccess() {

    }

    protected void subscribe() {
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    protected void showWaitDialog(String message) {
        if (mWaitDialog == null) {
            mWaitDialog = new WaitDialog(mActivity);
        }
        mWaitDialog.setMessage(message);
        mWaitDialog.show();
    }

    protected void showWaitDialog(String message, DialogInterface.OnCancelListener listener) {
        if (mWaitDialog == null) {
            mWaitDialog = new WaitDialog(mActivity);
            mWaitDialog.setMessage(message);
        }
        mWaitDialog.setMessage(message);
        if (listener != null) {
            mWaitDialog.setOnCancelListener(listener);
        }
        mWaitDialog.show();
    }

    protected void showWaitDialog(String message, boolean canceledOnTouchOutside, boolean cancelable) {
        if (mWaitDialog == null) {
            mWaitDialog = new WaitDialog(mActivity);
            mWaitDialog.setMessage(message);
        }
        mWaitDialog.setMessage(message);
        mWaitDialog.setCancelable(cancelable);
        mWaitDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        mWaitDialog.show();
    }

    protected void dismissWaitDialog() {
        if (mWaitDialog != null) {
            mWaitDialog.dismiss();
        }
    }

    /**
     * 获取持有fragment的activity
     *
     * @return
     */
    public MvpBaseActivity getHoldingActivity() {
        if (getActivity() != null && getActivity() instanceof MvpBaseActivity) {
            return ((MvpBaseActivity) mActivity);
        }
        return null;
    }
}
