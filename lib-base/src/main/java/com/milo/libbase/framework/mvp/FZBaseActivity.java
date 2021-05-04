package com.milo.libbase.framework.mvp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.milo.libbase.R;
import com.milo.libbase.data.AppConstants;
import com.milo.libbase.receiver.FZLoginBroadcastReceiver;
import com.milo.libbase.rxactivity.ActivityOnResult;
import com.milo.libbase.utils.FZSystemBarUtils;
import com.milo.libbase.utils.ToastUtils;
import com.milo.libbase.utils.Utils;
import com.milo.libbase.widget.dialog.FZWaitDialog;


/**
 * 标题：
 * 功能：
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2019/1/29 15:56
 */
public class FZBaseActivity<T extends IBasePresenter> extends BaseActivity implements ITrackResource, FZLoginBroadcastReceiver.LoginListener {

    public Activity mActivity;

    public TextView  mTvTitle;
    public TextView  mTvTitleLeft;
    public TextView  mTvTitleRight;
    public ImageView mImgTitleLeft;
    public ImageView mImgTitleLeftSecond;
    public ImageView mImgTitleRight;
    public ViewGroup mMailLayout;//消息UI
    public ImageView mImageMail;
    public TextView  mTvMailCount;
    public ImageView mImageMailPoint;
    public View      mToolbarDivider;

    public ViewGroup mToolbar;

    public FZWaitDialog mWaitDialog;

    public  RelativeLayout           mRootView;
    private FZLoginBroadcastReceiver mLoginBroadcastReceiver;

    protected T       mPresenter;
    protected boolean mIsBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        if (FZSystemBarUtils.isCanLightStatusBar() && tintStatusBar()) {
            setDarkMode(true);
            FZSystemBarUtils.tintStatusBar(this, ContextCompat.getColor(this, getStatusColor()), 0);
        }

        mLoginBroadcastReceiver = new FZLoginBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.ACTION_LOGIN_SUCCESS);
        filter.addAction(AppConstants.ACTION_LOGOUT_SUCCESS);

        mActivity.registerReceiver(mLoginBroadcastReceiver, filter);
        Utils.writeCommonLog(getClass().getSimpleName(), "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImgTitleLeft.setContentDescription(mActivity.getString(R.string.libbase_back_content, mTvTitle.getText()));
        Utils.writeCommonLog(getClass().getSimpleName(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.writeCommonLog(getClass().getSimpleName(), "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        Utils.writeCommonLog(getClass().getSimpleName(), "onDestroy");
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
        try {
            dismissWaitDialog();
            mWaitDialog = null;
        } catch (Exception e) {

        }

        if (mLoginBroadcastReceiver != null) {
            unregisterReceiver(mLoginBroadcastReceiver);
        }
        super.onDestroy();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mRootView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.libbase_framework_mvp_activity_base, null);//

        mToolbar = (ViewGroup) mRootView.findViewById(R.id.toolbar);
        mImgTitleLeft = (ImageView) mRootView.findViewById(R.id.img_title_left);
        mImgTitleRight = (ImageView) mRootView.findViewById(R.id.img_title_right);
        mTvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        mTvTitleLeft = (TextView) mRootView.findViewById(R.id.tv_title_left);
        mTvTitleRight = (TextView) mRootView.findViewById(R.id.tv_title_right);
        mImgTitleLeftSecond = mRootView.findViewById(R.id.img_title_left_second);
        mMailLayout = mRootView.findViewById(R.id.mMailLayout);
        mImageMail = mRootView.findViewById(R.id.mImageMail);
        mTvMailCount = mRootView.findViewById(R.id.mTvMailCount);
        mImageMailPoint = mRootView.findViewById(R.id.mImageMailPoint);
        mToolbarDivider = mRootView.findViewById(R.id.divider_toolbar);
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (!isRelativeFragment()) {
            layoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar);
        }
        view.setLayoutParams(layoutParams);
        mRootView.addView(view);
        super.setContentView(mRootView);

        rippleClickDelay(mImgTitleLeft, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mIsBackPressed) {
            mIsBackPressed = true;
            super.onBackPressed();
        }
    }

    @Override
    public String getTrackName() {
        //默认classSimpleName，需要重写此方法
        return this.getClass().getSimpleName();
    }

    @Override
    public String getJumpFrom() {
        //这么写是为了不依赖注入框架，加入"未知"是为了更好的排查遗漏的来源统计
        String jumpFrom = getIntent().getStringExtra(KEY_JUMP_FROM);
        return jumpFrom == null ? UNKNOW_JUMP_FROM : jumpFrom;
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLogOutSuccess() {

    }

    /**
     * 显示右侧邮件（消息）
     *
     * @param hasUnRead 是否有未读消息
     */
    public void showMailLayout(boolean hasUnRead) {
        mMailLayout.setVisibility(View.VISIBLE);
        mTvMailCount.setVisibility(View.GONE);
        if (hasUnRead) {
            mImageMailPoint.setVisibility(View.VISIBLE);
        } else {
            mImageMailPoint.setVisibility(View.GONE);
        }
    }

    /**
     * 显示右侧邮件（消息）
     *
     * @param unReadCount 未读消息数
     */
    public void showMailLayout(int unReadCount) {
        mMailLayout.setVisibility(View.VISIBLE);
        mImageMailPoint.setVisibility(View.GONE);

        if (unReadCount <= 0) {
            mTvMailCount.setVisibility(View.GONE);
        } else {
            mTvMailCount.setVisibility(View.VISIBLE);
            if (unReadCount > 99) {
                mTvMailCount.setText("99+");
            } else {
                mTvMailCount.setText(String.valueOf(unReadCount));
            }
        }
    }

    /**
     * 隐藏右侧邮件（消息）
     */
    public void hideMailLayout() {
        mMailLayout.setVisibility(View.GONE);
    }

    /**
     * 是否是相对的fragment
     *
     * @return
     */
    public boolean isRelativeFragment() {
        return false;
    }

    protected void hideTitleShadow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(0);
        }
    }

    protected void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
    }

    /**
     * 是否设置状态栏颜色
     */
    protected boolean tintStatusBar() {
        return true;
    }

    public void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    /**
     * 设置状态栏字体颜色
     *
     * @param isDarkMode true 深色，false 浅色
     */
    public void setDarkMode(boolean isDarkMode) {
        if (isDarkMode) {
            FZSystemBarUtils.setStatusBarDarkMode(this);
        } else {
            FZSystemBarUtils.setStatusBarLightMode(this);
        }
    }

    public void setTvTile(String title) {
        mTvTitle.setText(title);
    }

    protected int getStatusColor() {
        return R.color.libbase_white;
    }

    /**
     * 自动填充状态栏
     */
    public void autoFillStatusBar(int id) {
        autoFillStatusBar(findViewById(id));
    }

    /**
     * 自动填充状态栏
     *
     * @param statusBar
     */
    public void autoFillStatusBar(@NonNull View statusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.MarginLayoutParams params = null;

            if (statusBar.getParent() instanceof RelativeLayout) {
                params = (RelativeLayout.LayoutParams) statusBar.getLayoutParams();
            } else if (statusBar.getParent() instanceof LinearLayout) {
                params = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            }

            params.height = FZSystemBarUtils.getStatusBarHeight(mActivity);
            statusBar.setLayoutParams(params);
            statusBar.setVisibility(View.VISIBLE);
        }
    }

    protected void showWaitDialog(String message) {
        if (mWaitDialog == null) {
            mWaitDialog = new FZWaitDialog(this);
            mWaitDialog.setMessage(message);
        }
        mWaitDialog.setMessage(message);
        mWaitDialog.show();
    }

    protected void showWaitDialog(String message, DialogInterface.OnCancelListener listener) {
        if (mWaitDialog == null) {
            mWaitDialog = new FZWaitDialog(this);
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
            mWaitDialog = new FZWaitDialog(this);
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

    public void rippleClickDelay(final View view, @NonNull final View.OnClickListener onClickListener) {
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onClickListener.onClick(view);
                        }
                    }, 300);
                }
            });
        }
    }

    /**
     * 执行带回调的跳转
     * 该方法中的回调脱离了 activity.onActivityResult 可单独使用，这样提高了代码的可读性和优雅度
     *
     * @param intnet
     * @param requestCode
     * @param callback
     */
    public void startActivityForResult(Intent intnet, int requestCode, ActivityOnResult.Callback callback) {
        if (intnet == null) {
            ToastUtils.show(this, "There is no intent matched");
        } else if (!Utils.isFastDoubleClick()) {
            ActivityOnResult activityOnResult = new ActivityOnResult(this);
            activityOnResult.startForResult(intnet, requestCode, callback);
        }
    }

}
