package com.milo.libbase.demo.ui;

import android.content.Context;
import android.os.Bundle;

import com.milo.libbase.framework.OriginJump;
import com.milo.libbase.framework.mvp.FZBaseFragmentActivity;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/12/1
 */
public class DownloadTestActivity extends FZBaseFragmentActivity<DownloadTestFragment> {

    public static OriginJump createJump(Context context){
        return new OriginJump(context, DownloadTestActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new DownloadTestPresenter(mFragment);
    }

    @Override
    protected DownloadTestFragment createFragment() {
        return DownloadTestFragment.newInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
