package com.milo.libbase.framework.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.milo.libbase.utils.LogUtils;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 5/4/21
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(getClass().getSimpleName(), "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(getClass().getSimpleName(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i(getClass().getSimpleName(), "onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(getClass().getSimpleName(), "onDestroy");
    }

}
