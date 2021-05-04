package com.milo.libbase.framework.mvp;

/**
 * 标题：
 * 功能：
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2019/1/29 16:52
 */
public interface MvpBaseView<P> extends IBaseView<P> {

    void showProgress();

    void hideProgress();

    void showToast(int resId);

    void showToast(String msg);

    void finish();

}
