package com.milo.libbase.utils.filefactory;

import androidx.annotation.IntDef;

/**
 * Title：存储类型
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/10/27
 */
@IntDef({StorageType.External, StorageType.Storage})
public @interface StorageType {

    /**
     * android-19之后推荐的数据存储目录，数据会随着卸载app、清理app缓存等行为被删除 (私有存储)
     */
    int External = 1;
    /**
     * 外置存储目录，数据不会随着卸载app、清理app缓存等行为被删除，适用场景如：用户下载、uuid保存等 （公开存储）
     */
    int Storage = 2;

}
