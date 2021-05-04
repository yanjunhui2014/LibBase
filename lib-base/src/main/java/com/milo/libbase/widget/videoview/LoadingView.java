package com.milo.libbase.widget.videoview;

import android.view.View;
import android.view.ViewGroup;

/**
 * Title：加载中视图
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/4/15
 */
public interface LoadingView {

    View getLoadingView();

    void showLoadingView(ViewGroup parent);

    void hideLoadingView(ViewGroup parent);

}
