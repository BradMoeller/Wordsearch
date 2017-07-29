package com.duolingo.wordsearch.network;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public final class NetworkUtils {
    private static OkHttpClient mClientInstance;

    private static final String GET = "GET";

    private static OkHttpClient getClient() {
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
    public static void get(String url, final Callback callback) {
        final Request.Builder builder = new Request.Builder()
                .url(url)
                .method(GET, null);

        OkHttpClient client = getClient();
        client.newCall(builder.build())
                .enqueue(callback);
    }
}