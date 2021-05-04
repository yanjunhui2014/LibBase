package com.milo.libbase.demo.ui.contract;

import com.milo.libbase.framework.mvp.FZIBasePresenter;
import com.milo.libbase.framework.mvp.FZIBaseView;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/12/1
 */
public interface DownloadTestContract {

    interface View extends FZIBaseView<Presenter> {
        void showDownloadOk(long timeMillis);

        void showCount(int cur, int total);
    }

    interface Presenter extends FZIBasePresenter {
        String getRootPath();

        void startDownload();
    }

}
