package com.milo.libbase.utils.download;


/**
 * Title：通用下载回调
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/12/1
 */
public interface DownloadListener {

    void onConnectIng();

    void onGetSize(long totalSize);

    void onDownloadIng(float progressF, int progress);

    default void onRetry(int leftRetryTimes){}

    void onCancel();

    /**
     * 下载发生错误 #在主线程回调#
     *
     * @param error 错误原因
     */
    void onError(String error);

    void onRepeatTask();

    /**
     * 下载完成 #在主线程回调#
     *
     * @param localPath 本地保存地址
     */
    void onDone(String localPath);

}
