package com.milo.libbase.framework.mvp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.milo.libbase.R;
import com.milo.libbase.utils.LogUtils;

public abstract class MvpBaseFragmentActivity<T extends Fragment> extends MvpBaseActivity {

    protected T mFragment;

    protected abstract T createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libbase_framework_mvp_activity_base_fragment);
        initData();
        addFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragment = null;
    }

    protected void initData() {

    }

    protected void addFragment() {
        mFragment = (T) getSupportFragmentManager().findFragmentById(R.id.layout_content);

        if (mFragment != null) {
            LogUtils.e(getClass().getSimpleName(), "mFragment is: " + mFragment.getClass().getSimpleName());
        }
        if (mFragment == null) {
            mFragment = createFragment();
            addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.layout_content);
        }
    }

    public void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                      @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (needSaveInstanceState()) {
            super.onSaveInstanceState(outState);
        }
    }

    protected boolean needSaveInstanceState() {
        return false;
    }

    protected boolean isFinish() {
        return false;
    }
}
