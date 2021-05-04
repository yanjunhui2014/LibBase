package com.milo.libbase.business;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.milo.libbase.AppConfig;
import com.milo.libbase.data.impl.PrepareRes;
import com.milo.libbase.utils.Utils;

import io.reactivex.rxjava3.core.Observable;

/**
 * Title：视频工厂
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/11/25
 */
public class VideoResPrepareFactory extends ResPrepareFactory {

    @Override
    public PrepareRes createRes(final String downloadUrl, final String savePath) {
        if ((TextUtils.isEmpty(downloadUrl) || TextUtils.isEmpty(savePath)) && AppConfig.isDebug) {
            throw new NullPointerException("downloadUrl 和 savePath 不可以为空");
        }
        return new PrepareRes() {
            @NonNull
            @Override
            public String getDownloadUrl() {
                return downloadUrl;
            }

            @NonNull
            @Override
            public String getSavePath() {
                return savePath;
            }

            @Nullable
            @Override
            public String getDegradePath() {
                return Utils.getProxyUrl(AppConfig.app, downloadUrl);
            }

            @Override
            public boolean overWrite() {
                return false;
            }

            @Nullable
            @Override
            public Observable getHandlerObservable() {
                return null;
            }
        };
    }

}
