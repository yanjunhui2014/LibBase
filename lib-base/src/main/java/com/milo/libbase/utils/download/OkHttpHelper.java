package com.milo.libbase.utils.download;

import okhttp3.OkHttpClient;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/12/1
 */
public class OkHttpHelper {

    public static OkHttpClient getClient() {
        return getClient(null);
    }

    private static OkHttpClient getClient(OkHttpClient.Builder builder) {
        OkHttpClient client = null;
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .build();
        }

        if (builder == null) {
            client.newBuilder().build();
        } else {
            client = builder.build();
        }
        client.dispatcher().setMaxRequests(FileDownloader.RUNNABLE_COUNT);

        return client;
    }

}
