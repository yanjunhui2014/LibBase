package com.milo.libbase.framework.mvp;

import java.util.List;

public interface FZListDataContract {

    interface View<P> extends FZIBaseView<P>, FZIListDataView {

    }

    interface Presenter<D> extends IBasePresenter {

        void refresh();

        void loadMore();

        List<D> getDataList();
    }

}
    