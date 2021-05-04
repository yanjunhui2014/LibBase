package com.milo.libbase.rxactivity;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * 标题：
 * 功能：
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/9/14 09:38
 */
public class ActivityOnResult {

    private static final String TAG = "ActivityOnResult";
    private RxPresenterFragment mRxPresenterFragment;

    public interface Callback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    public ActivityOnResult(AppCompatActivity activity) {
        mRxPresenterFragment = getPresenterFragment(activity);
    }

    public ActivityOnResult(Fragment fragment) {
        this((AppCompatActivity) fragment.getActivity());
    }

    private RxPresenterFragment getPresenterFragment(AppCompatActivity activity) {
        RxPresenterFragment fragment = findPresenterFragment(activity);
        if (fragment == null) {
            fragment = new RxPresenterFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    private RxPresenterFragment findPresenterFragment(AppCompatActivity activity) {
        return (RxPresenterFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }

    public void startForResult(Intent intent, int requestCode, Callback callback) {
        mRxPresenterFragment.startForResult(intent, requestCode, callback);
    }

    public void startForResult(Class<? extends Activity> clazz, int requestCode, Callback callback) {
        Intent intent = new Intent(mRxPresenterFragment.getActivity(), clazz);
        mRxPresenterFragment.startForResult(intent, requestCode, callback);
    }

}
