package com.milo.libbase.utils.download;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Title：菲助文件下载器
 * Describe：
 * Remark：支持多线程并行下载
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/12/1
 */
public class FileDownloader {
    private static final String TAG            = FileDownloader.class.getSimpleName();
    public static        int    RUNNABLE_COUNT = 10;//最大同时下载任务数

    private static FileDownloader mInstance;
    private static AtomicBoolean  mIsRunning = new AtomicBoolean(true);

    private ExecutorService                                                                   mExecotor         = Executors.newFixedThreadPool(RUNNABLE_COUNT + 1);//线程池
    private LinkedBlockingQueue<FileDownloadConsumer> mDownloadQueue    = new LinkedBlockingQueue<>(1024);//下载任务队列
    private CopyOnWriteArraySet<FileDownloadConsumer> mDownloadIngQueue = new CopyOnWriteArraySet<>();//下载中队列

    private Map<String, DownloadConsumer> mConsumerMap = new HashMap<>();

    private FileDownloader() {
        init();
    }

    public static FileDownloader getInstance() {
        if (mInstance == null) {
            synchronized (FileDownloader.class) {
                mInstance = new FileDownloader();
            }
        }
        return mInstance;
    }

    public FileDownloadTask.Builder getFileDownloadTaskBuilder() {
        return new FileDownloadTask.Builder();
    }

    public void cancelAllTask() {
        mIsRunning.set(false);

        Object[] consumers = mDownloadQueue.toArray();
        if (consumers != null) {
            for (Object itemObj : consumers) {
                if (itemObj != null && itemObj instanceof FileDownloadConsumer) {
                    ((FileDownloadConsumer) itemObj).cancel();
                }
            }
        }
        mDownloadQueue.clear();

        Object[] ingConsumers = mDownloadIngQueue.toArray();
        if (ingConsumers != null) {
            for (Object itemIngObj : ingConsumers) {
                if (itemIngObj != null && itemIngObj instanceof FileDownloadConsumer) {
                    ((FileDownloadConsumer) itemIngObj).cancel();
                }
            }
        }
        mDownloadIngQueue.clear();

        mIsRunning.set(true);
    }

    public void canceTaskById(String taskId) {
        DownloadConsumer consumer = mConsumerMap.get(taskId);
        if (consumer != null) {
            consumer.cancel();
        }
    }

    public void canceTaskByTag(@NonNull String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        mIsRunning.set(false);

        Object[] consumers = mDownloadQueue.toArray();
        if (consumers != null) {
            for (Object itemObj : consumers) {
                FileDownloadConsumer consumer = ((FileDownloadConsumer) itemObj);
                if (tag.equals(consumer.task.getTag())) {
                    consumer.cancel();
                }
            }
        }
        mDownloadQueue.clear();

        Object[] ingConsumers = mDownloadIngQueue.toArray();
        if (ingConsumers != null) {
            for (Object itemIngObj : ingConsumers) {
                FileDownloadConsumer consumer = ((FileDownloadConsumer) itemIngObj);
                if (tag.equals(consumer.task.getTag())) {
                    consumer.cancel();
                }
            }
        }
        mDownloadIngQueue.clear();

        mIsRunning.set(true);
    }

    protected FileDownloadConsumer addToQueue(final FileDownloadTask task) {
        Object[] consumers = mDownloadQueue.toArray();
        if (consumers != null) {
            for (Object itemObj : consumers) {
                FileDownloadConsumer itemConsumer = ((FileDownloadConsumer) itemObj);
                if (itemConsumer.task.getTaskId().equals(task.getTaskId()) || task.getSavePath().equals(itemConsumer.task.getSavePath())) {
                    if (task.getListener() != null) {
                        task.getListener().onRepeatTask();
                        return null;
                    }
                }
            }
        }

        Object[] ingConsumers = mDownloadIngQueue.toArray();
        if (ingConsumers != null) {
            for (Object itemIngObj : ingConsumers) {
                FileDownloadConsumer itemConsumer = ((FileDownloadConsumer) itemIngObj);
                if (itemConsumer.task.getTaskId().equals(task.getTaskId()) || task.getSavePath().equals(itemConsumer.task.getSavePath())) {
                    if (task.getListener() != null) {
                        task.getListener().onRepeatTask();
                        return null;
                    }
                }
            }
        }

        FileDownloadConsumer consumer = new FileDownloadConsumer(task);
        mConsumerMap.put(task.getTaskId(), consumer);
        mDownloadQueue.add(consumer);
        return consumer;
    }

    protected void removeFromQueue(FileDownloadConsumer consumer) {
        if (consumer != null) {
            removeDownloadIngTask(consumer);
            mDownloadQueue.remove(consumer);
            mConsumerMap.remove(consumer.task.getTaskId());
        }
    }

    private void addDownloadIngTask(FileDownloadConsumer taskConsumer) {
        if (taskConsumer != null) {
            mDownloadIngQueue.add(taskConsumer);
        }
    }

    private void removeDownloadIngTask(FileDownloadConsumer taskConsumer) {
        if (taskConsumer != null) {
            mDownloadIngQueue.remove(taskConsumer);
        }
    }

    /**
     * 开启核心线程
     */
    private void init() {
        Runnable core = new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        if (mDownloadIngQueue.size() < RUNNABLE_COUNT && mIsRunning.get()) {
                            FileDownloadConsumer taskConsumer = mDownloadQueue.take();
                            if (taskConsumer != null) {
                                mExecotor.execute(taskConsumer);
                                addDownloadIngTask(taskConsumer);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mExecotor.execute(core);
    }


}
