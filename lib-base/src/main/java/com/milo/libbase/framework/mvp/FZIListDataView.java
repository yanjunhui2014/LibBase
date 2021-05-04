package com.milo.libbase.framework.mvp;

import android.content.Context;

/**
 * Created by zhouhl on 2016/2/26.
 * 数据列表视图接口，加载中，加载出错，没有更多，没有数据时的显示
 */
public interface FZIListDataView {

    void showList(boolean isHasMore);

    void showEmpty();

    void showError();

    void showLoading();

    Context getContext();

}
