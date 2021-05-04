package com.milo.libbase.demo.ui.contract;

import com.milo.libbase.framework.mvp.MvpBaseView;
import com.milo.libbase.framework.mvp.IBasePresenter;

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

    interface View extends MvpBaseView<Presenter> {
        void showDownloadOk(long timeMillis);

        void showCount(int cur, int total);
    }

    interface Presenter extends IBasePresenter {
        String getRootPath();

        void startDownload();
    }

}
