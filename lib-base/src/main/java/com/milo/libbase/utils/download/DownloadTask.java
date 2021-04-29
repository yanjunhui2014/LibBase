package com.milo.libbase.utils.download;

import androidx.annotation.Nullable;

import java.util.Map;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/12/1
 */
public interface DownloadTask {

    String getDownloadUrl();

    String getSavePath();

    String getTaskId();

    int getRetryTimes();

    Map<String, String> getHeader();

    boolean forceReDownload();

    boolean getCompatUnStreaming();//兼容非流媒体文件

    String getTag();

    DownloadListener getListener();

    @Nullable
    DownloadConsumer start();

    interface LifeCiycle {
        void onCreate();

        void onBegin();

        void onRetry(int leftRetryTimes);

        void onIng();

        void onEnd();
    }

}
