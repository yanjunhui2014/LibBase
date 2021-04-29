package com.milo.libbase.utils.download;

import android.text.TextUtils;

import com.milo.libbase.AppConfig;
import com.milo.libbase.utils.LogUtils;
import com.milo.libbase.utils.ToastUtils;
import com.milo.libbase.utils.Utils;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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
public class FileDownloadTask implements DownloadTask, DownloadTask.LifeCiycle {
    private static final String TAG = "FileDownloadTask";

    private String              downloadUrl;
    private String              savePath;
    private int                 retryTimes;
    private int                 retryDelays;
    private boolean             isCompatUnStreaming;
    private boolean             forceReDownload;
    private DownloadListener    listener;
    private Map<String, String> headerMap;
    private String              tag;

    @Override
    public String getDownloadUrl() {
        return downloadUrl;
    }

    @Override
    public String getSavePath() {
        return savePath;
    }

    @Override
    public String getTaskId() {
        try {
            return Utils.getMD5(downloadUrl + savePath);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return String.valueOf(System.currentTimeMillis());
    }

    @Override
    public int getRetryTimes() {
        return retryTimes;
    }

    @Override
    public Map<String, String> getHeader() {
        return headerMap;
    }

    @Override
    public boolean forceReDownload() {
        return forceReDownload;
    }

    @Override
    public boolean getCompatUnStreaming() {
        return isCompatUnStreaming;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public DownloadListener getListener() {
        return listener;
    }

    @Override
    public DownloadConsumer start() {
        return FileDownloader.getInstance().addToQueue(this);
    }

    @Override
    public void onCreate() {
        //        LogUtils.d(TAG, getTaskId() + "::onCreate");
    }

    @Override
    public void onBegin() {
        //        LogUtils.d(TAG, getTaskId() + "::onBegin");
    }

    @Override
    public void onRetry(int leftRetryTimes) {
        LogUtils.d(TAG, getTaskId() + "::onRetry::剩余重试次数 = " + leftRetryTimes);
    }

    @Override
    public void onIng() {
        //        LogUtils.d(TAG, getTaskId() + "::onIng");
    }

    @Override
    public void onEnd() {
        //        LogUtils.d(TAG, getTaskId() + "::onEnd");
    }

    protected void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    protected int getRetryDealy() {
        return retryDelays;
    }

    public static final class Builder {
        private String              downloadUrl;
        private String              savePath;
        private int                 retryTimes          = 0;
        private int                 retryDelay          = 1000;
        private boolean             forceReDownload     = false;
        private boolean             isCompatUnStreaming = true;
        private String              tag                 = null;
        private DownloadListener    listener;
        private Map<String, String> headerMap;

        public Builder() {
        }

        public Builder withDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public Builder withSavePath(String savePath) {
            this.savePath = savePath;
            return this;
        }

        public Builder withRetryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
            return this;
        }

        public Builder withRetryDelay(int retryDelay) {
            this.retryDelay = retryDelay;
            return this;
        }

        public Builder withForceReDownload(boolean forceReDownload) {
            this.forceReDownload = forceReDownload;
            return this;
        }

        public Builder withIsCompatUnStreaming(boolean isCompatUnStreaming) {
            this.isCompatUnStreaming = isCompatUnStreaming;
            return this;
        }

        public Builder withTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder withListener(DownloadListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder withHeader(String name, String value) {
            if (this.headerMap == null) {
                headerMap = new HashMap<>();
            }
            headerMap.put(name, value);
            return this;
        }

        public FileDownloadTask build() {
            if (this.retryDelay < 0) {
                this.retryDelay = 1000;
            }

            FileDownloadTask fileDownloadTask = new FileDownloadTask();
            fileDownloadTask.forceReDownload = this.forceReDownload;
            fileDownloadTask.isCompatUnStreaming = this.isCompatUnStreaming;
            fileDownloadTask.downloadUrl = this.downloadUrl;
            fileDownloadTask.headerMap = this.headerMap;
            fileDownloadTask.retryTimes = this.retryTimes;
            fileDownloadTask.retryDelays = this.retryDelay;
            fileDownloadTask.listener = this.listener;
            fileDownloadTask.savePath = this.savePath;
            fileDownloadTask.tag = this.tag;

            if (!AppConfig.isRelease) {
                if (TextUtils.isEmpty(downloadUrl)) {
                    throw new NullPointerException("下载地址为空");
                } else if (TextUtils.isEmpty(savePath)) {
                    throw new NullPointerException("保存地址为空");
                }
            } else {
                if (TextUtils.isEmpty(downloadUrl)) {
                    ToastUtils.show(AppConfig.app, "警告:下载地址为空");
                } else if (TextUtils.isEmpty(savePath)) {
                    ToastUtils.show(AppConfig.app, "警告:保存地址为空");
                }
            }

            fileDownloadTask.onCreate();

            return fileDownloadTask;
        }
    }

}
