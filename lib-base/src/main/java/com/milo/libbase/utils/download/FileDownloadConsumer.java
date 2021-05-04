package com.milo.libbase.utils.download;

import android.os.Handler;
import android.os.Looper;

import com.milo.libbase.utils.LogUtils;
import com.milo.libbase.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Title：下载任务消费者
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/12/1
 */
public class FileDownloadConsumer implements Runnable, DownloadConsumer {
    private static final String TAG = "DownloadTaskConsumer";

    protected FileDownloadTask task;
    protected int              STATUS;

    private File      tempFile;
    private Timer     timer;
    private TimerTask timerTask;

    private long  totalSize;//文件大小
    private long  downloadSize;//已下载文件大小
    private int   progress;//进度int精度
    private float progressF;//进度float精度

    private boolean isCancel;

    public FileDownloadConsumer(final FileDownloadTask task) {
        this.task = task;
        this.tempFile = new File(task.getSavePath() + "_temp");
        this.timer = new Timer();
        this.timerTask = new TimerTask() {
            @Override
            public void run() {
                updateProgress();
            }
        };
    }

    @Override
    public void run() {
        if (Utils.isExist(task.getSavePath()) && !task.forceReDownload()) {
            onDone();
            return;
        }
        onConnectIng();

        Request.Builder builder = null;
        try{
            builder = new Request.Builder().url(task.getDownloadUrl());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            onError(e.getMessage());
            return;
        }

        if (tempFile.exists()) {
            builder.header("RANGE", "bytes=" + tempFile.length() + "-");
            LogUtils.d(TAG, "任务:" + task.getTaskId() + " 开启断点续传，开始位置 == " + tempFile.length());
        }

        OkHttpHelper.getClient().newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!onRetry()) {
                    onError("request请求错误");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                RandomAccessFile accessFile = null;
                try {
                    if (response.body() == null || response.body().byteStream() == null || (!task.getCompatUnStreaming() && response.body().contentLength() == -1)) {
                        if (!onRetry()) {
                            onError("request获取文件大小时发生错误");
                        }
                    } else {
                        is = response.body().byteStream();
                        File parentFile = tempFile.getParentFile();
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                        if (!tempFile.exists()) {
                            tempFile.createNewFile();
                        }

                        accessFile = new RandomAccessFile(tempFile, "rw");
                        accessFile.seek(tempFile.length());
                        onDownloadIng();

                        downloadSize = 0;
                        if (tempFile.length() != 0) {
                            totalSize = response.body().contentLength() + tempFile.length();
                            addDownloadSize(tempFile.length());
                            updateProgress();
                        } else {
                            totalSize = response.body().contentLength();
                        }
                        if (task.getListener() != null) {
                            task.getListener().onGetSize(totalSize);
                        }

//                        LogUtils.d(TAG, "任务:" + task.getTaskId() + "网络请求完毕，开启下载...");
                        //定时检查下载进度
                        if (timer != null) {
                            timer.schedule(timerTask, 100, 500);
                        }

                        byte[] buf = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buf)) != -1 && !isCancel) {
                            accessFile.write(buf, 0, len);
                            addDownloadSize(len);
                        }

                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }

                        if (isCancel) {
                            //下载暂停
                            onCancel(true);
                        } else {
                            //下载结束
                            renameWhenDownloadOk();
                        }
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, e.toString());
                    if (!onRetry()) {
                        onError("下载时发生异常:原地址" + task.getDownloadUrl() + "，本地地址:"+ task.getSavePath() + ", 异常:" + e.getMessage());
                    }
                }

                try {
                    Utils.close(is);
                    Utils.close(accessFile);
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void cancel() {
        this.isCancel = true;
        if (STATUS != STATUS_DOWN_ING) {
            onCancel(false);
        }
    }

    @Override
    public int getStatus() {
        return STATUS;
    }

    @Override
    public String getTaskId() {
        return task.getTaskId();
    }

    /**
     * 下载完成后重命名
     */
    private void renameWhenDownloadOk() {
        if (Utils.rename(tempFile.toString(), task.getSavePath())) {
            onDone();
        } else {
            onError("文件重命名时发生错误:" + task.getSavePath());
        }
    }

    private void onConnectIng() {
        STATUS = STATUS_CONNECT;
        task.onBegin();

        if (task.getListener() != null) {
            task.getListener().onConnectIng();
        }
    }

    private void onDownloadIng() {
        STATUS = STATUS_DOWN_ING;
        task.onIng();
    }

    private boolean onRetry() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if(isCancel){
            return false;
        }

        if (task.getRetryTimes() > 0) {
            task.onRetry(task.getRetryTimes() - 1);
            task.setRetryTimes(task.getRetryTimes() - 1);

            Disposable disposable = Observable.timer(task.getRetryDealy(), TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            if(!isCancel){
                                FileDownloadConsumer.this.run();
                            } else {
                                LogUtils.d(TAG, "retry任务已取消");
                            }
                        }
                    });
            return true;
        }
        return false;
    }

    private void onError(String error) {
        STATUS = STATUS_DOWN_ERROR;
        task.onEnd();

        FileDownloader.getInstance().removeFromQueue(this);
        if (task.getListener() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    task.getListener().onError(error);
                }
            });
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void onCancel(boolean updateProgress) {
        STATUS = STATUS_DOWN_CANCEL;
        task.onEnd();

        if (updateProgress) {
            updateProgress();
        }

        FileDownloader.getInstance().removeFromQueue(this);
        if (task.getListener() != null && STATUS != STATUS_DOWN_DONE && STATUS != STATUS_DOWN_ERROR) {
            task.getListener().onCancel();
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void onDone() {
        STATUS = STATUS_DOWN_DONE;
        task.onEnd();

        updateProgress();
        FileDownloader.getInstance().removeFromQueue(this);
        if (task.getListener() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    task.getListener().onDone(task.getSavePath());
                }
            });
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void addDownloadSize(long size) {
        downloadSize += size;
    }

    private void updateProgress() {
        if (totalSize == 0) {
            progressF = 100;
        } else {
            progressF = Math.min(downloadSize * 1.0f / totalSize * 1.0f, 100.0f) * 100.0f;
            progress = (int) progressF;
        }

        if (task.getListener() != null) {
            task.getListener().onDownloadIng(progressF, progress);
        }
    }

}
