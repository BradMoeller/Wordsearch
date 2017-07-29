package com.duolingo.wordsearch.network;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class MainThreadCallback implements Callback {

    public abstract void onSuccess(String response, int statusCode);

    public abstract void onFailure(String response, int statusCode);

    public abstract void onException(IOException exception);

    @Override
    public final void onFailure(Call call, final IOException exception) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onException(exception);
            }
        });
    }

    @Override
    public final void onResponse(final Call call, final Response response) {
        try {
            final String responseString = response.body().string();
            final int statusCode = response.code();
            final boolean isSuccessful = response.isSuccessful();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isSuccessful) {
                        onSuccess(responseString, statusCode);
                    } else {
                        onFailure(responseString, statusCode);
                    }
                }
            });
        } catch (final IOException exception) {
            exception.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onException(exception);
                }
            });
        }
    }

    private void runOnUiThread(Runnable task) {
        new Handler(Looper.getMainLooper()).post(task);
    }
}