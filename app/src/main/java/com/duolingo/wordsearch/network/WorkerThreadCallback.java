package com.duolingo.wordsearch.network;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class WorkerThreadCallback implements Callback {

    public abstract void onSuccess(Response response, int statusCode);

    public abstract void onFailure(String response, int statusCode);

    public abstract void onException(IOException exception);

    @Override
    public final void onFailure(Call call, IOException exception) {
        onException(exception);
    }

    @Override
    public final void onResponse(Call call, Response response) {
        try {
            if (response.isSuccessful()) {
                onSuccess(response, response.code());
            } else {
                onFailure(response.body().string(), response.code());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            onException(exception);
        }
    }
}