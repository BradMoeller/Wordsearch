package com.duolingo.wordsearch.data;

import com.duolingo.wordsearch.network.NetworkUtils;

import okhttp3.Callback;

/**
 * Created by brad on 7/29/17.
 */

public class BoardCloudDataAccess {

    public void getBoard(Callback callback) {
        NetworkUtils.get("https://s3.amazonaws.com/duolingo-data/s3/js2/find_challenges.txt", callback);
    }
}
