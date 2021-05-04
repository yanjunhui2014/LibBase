package com.milo.libbase.data.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.rxjava3.core.Observable;


/**
 * Title：需要进行准备的资源
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/11/25
 */
public interface PrepareRes<T> {

    @NonNull
    String getDownloadUrl();

    @NonNull
    String getSavePath();

    /**
     * 降级地址，会先从本地判断降级文件是否存在，若存在，copy至 {@link #getSavePath()}
     *
     * @return
     */
    @Nullable
    String getDegradePath();

    /**
     * 是否覆盖资源
     *
     * @return
     */
    boolean overWrite();

    @Nullable
    Observable<T> getHandlerObservable();

}
