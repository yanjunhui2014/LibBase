package com.milo.libbase.framework.mvvm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.milo.libbase.widget.refreshview.LoadingState;

import io.reactivex.rxjava3.disposables.CompositeDisposable;


/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 8/19/21
 */
public class BaseMvvmViewModel extends ViewModel {
    protected String TAG = "BaseMvvmViewModel";

    public MutableLiveData<LoadingState> loadingStateLiveData = new MutableLiveData<>();

    public final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public BaseMvvmViewModel() {
        TAG = getClass().getSimpleName();
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.clear();
        super.onCleared();
    }

}
