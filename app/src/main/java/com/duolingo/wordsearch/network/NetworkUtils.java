package com.duolingo.wordsearch.network;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NetworkUtils {
    private OkHttpClient mClientInstance;

    private final String GET = "GET";

    private OkHttpClient getClient() {
        if (mClientInstance == null) {
            mClientInstance = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        }

        return mClientInstance;
    }

    /**
     * Performs a GET network request to the route provided.
     *
     * @param url route to hit
     * @param callback callback to be invoked
     */
    public void get(String url, final Callback callback) {
        final Request.Builder builder = new Request.Builder()
                .url(url)
                .method(GET, null);

        OkHttpClient client = getClient();
        client.newCall(builder.build())
                .enqueue(callback);
    }
}