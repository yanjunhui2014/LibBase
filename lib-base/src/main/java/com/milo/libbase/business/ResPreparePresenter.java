package com.milo.libbase.business;

import android.text.TextUtils;

import androidx.annotation.Nullable;


import com.milo.libbase.data.impl.PrepareRes;
import com.milo.libbase.framework.mvp.MvpBasePresenter;
import com.milo.libbase.utils.LogUtils;
import com.milo.libbase.utils.Utils;
import com.milo.libbase.utils.download.DownloadConsumer;
import com.milo.libbase.utils.download.DownloadListener;
import com.milo.libbase.utils.download.FileDownloader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Title：需要准备资源的中介者
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/11/25
 */
public abstract class ResPreparePresenter extends MvpBasePresenter {

    protected final int ResPrepare_SUC  = 1;
    protected final int ResPrepare_FAIL = 2;

    private final int DOWNLOAD_Suc  = 1;
    private final int DOWNLOAD_Fail = 0;

    private List<DownloadConsumer> mDownloadList = new ArrayList<>();//下载任务列表;

    protected abstract List<PrepareRes> getPrepareResList();

    protected abstract void onItemResPrepared(PrepareRes prepareRes, String channel);

    protected abstract void onResPrepareError(@Nullable PrepareRes prepareRes, Throwable throwable);

    protected abstract void onResPreparedCompleted();

    @Override
    public void subscribe() {
        super.subscribe();
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        stopResPrepare();
    }

    protected void startResPrepare() {
        List<PrepareRes> list = getPrepareResList();
        if (Utils.isEmpty(list)) {
            onResPreparedCompleted();
            return;
        }

        List<Observable<Integer>> downloadObservableList = new ArrayList<>();
        for (final PrepareRes prepareRes : list) {
            if (TextUtils.isEmpty(prepareRes.getDownloadUrl())) {
                onResPrepareError(prepareRes, new Throwable("下载地址不存在"));
                return;
            } else if (TextUtils.isEmpty(prepareRes.getSavePath())) {
                onResPrepareError(prepareRes, new Throwable("本地保存地址不存在"));
                return;
            }
            downloadObservableList.add(Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(final ObservableEmitter<Integer> emitter) throws Exception {
                    if (prepareRes.overWrite()) {
                        Utils.delFileOrDir(prepareRes.getSavePath());
                    }

                    boolean isExists = Utils.isExist(prepareRes.getSavePath());
                    if (isExists) {
                        onItemResPrepared(prepareRes, "本地已存在");
                        emitter.onNext(DOWNLOAD_Suc);
                    } else if (!TextUtils.isEmpty(prepareRes.getDegradePath()) && Utils.isExist(prepareRes.getDegradePath())) {
                        Utils.copyFile(prepareRes.getDegradePath(), prepareRes.getSavePath());
                        onItemResPrepared(prepareRes, "存在降级文件");
                        emitter.onNext(DOWNLOAD_Suc);
                    } else {
                        DownloadConsumer consumer = FileDownloader.getInstance().getFileDownloadTaskBuilder()
                                .withDownloadUrl(prepareRes.getDownloadUrl())
                                .withSavePath(prepareRes.getSavePath())
                                .withRetryTimes(3)
                                .withRetryDelay(2000)
                                .withListener(new DownloadListener() {
                                    @Override
                                    public void onConnectIng() {

                                    }

                                    @Override
                                    public void onGetSize(long totalSize) {

                                    }

                                    @Override
                                    public void onDownloadIng(float progressF, int progress) {

                                    }

                                    @Override
                                    public void onRetry(int leftRetryTimes) {
                                        LogUtils.d(TAG, "触发重试，剩余重试次数:" + leftRetryTimes);
                                    }

                                    @Override
                                    public void onCancel() {
                                    }

                                    @Override
                                    public void onError(String error) {
                                        onResPrepareError(prepareRes, new Throwable("下载失败:" + error));
                                        emitter.onNext(DOWNLOAD_Fail);
                                        emitter.onComplete();
                                    }

                                    @Override
                                    public void onRepeatTask() {
                                        onItemResPrepared(prepareRes, "重复任务");
                                        emitter.onNext(DOWNLOAD_Suc);
                                        emitter.onComplete();
                                    }

                                    @Override
                                    public void onDone(String localPath) {
                                        onItemResPrepared(prepareRes, "下载成功");
                                        emitter.onNext(DOWNLOAD_Suc);
                                        emitter.onComplete();
                                    }
                                }).build().start();

                        if (consumer != null) {
                            mDownloadList.add(consumer);
                        }
                    }
                }
            }));
        }

        mCompositeDisposable.add(Observable.zip(downloadObservableList, new Function<Object[], Integer>() {
            @Override
            public Integer apply(Object[] objects) throws Exception {
                int result = 1;
                for (Object obj : objects) {
                    if (obj instanceof Integer) {
                        result &= (int) obj;
                    }
                }
                return result == 1 ? ResPrepare_SUC : ResPrepare_FAIL;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == ResPrepare_SUC) {
                            onResPreparedCompleted();
                        } else {
                            onResPrepareError(null, new Throwable("下载时发生错误"));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onResPrepareError(null, throwable);
                    }
                }));
    }

    protected void stopResPrepare() {
        for (DownloadConsumer task : mDownloadList) {
            task.cancel();
        }
    }

}
