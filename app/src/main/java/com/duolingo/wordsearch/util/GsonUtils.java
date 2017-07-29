package com.duolingo.wordsearch.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonUtils {

    private static Gson mGson;

    public static Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder().create();
        }
        return mGson;
    }
}
