package com.milo.libbase.framework.mvp;


import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FZBasePresenter implements IBasePresenter {
    protected String TAG = getClass().getSimpleName();

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
        mCompositeDisposable = new CompositeDisposable();
    }
}
