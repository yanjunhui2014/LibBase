package com.milo.libbase.framework.mvp;

import java.util.List;

public interface MvpListDataContract {

    interface View<P> extends MvpBaseView<P>, IListDataView {

    }

    interface Presenter<D> extends IBasePresenter {

        void refresh();

        void loadMore();

        List<D> getDataList();
    }

}
    