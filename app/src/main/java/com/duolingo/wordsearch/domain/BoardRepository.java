package com.duolingo.wordsearch.domain;

import android.os.Handler;
import android.os.Looper;

import com.duolingo.wordsearch.data.IBoardDataAccess;
import com.duolingo.wordsearch.model.Board;

import java.util.List;

/**
 * Created by brad on 7/29/17.
 */

public class BoardRepository implements IBoardRepository {

    private IBoardDataAccess mCloudDataAccess;
    private IBoardDataAccess mCacheDataAccess;

    public BoardRepository(IBoardDataAccess cloudAccess, IBoardDataAccess cacheAccess) {
        mCloudDataAccess = cloudAccess;
        mCacheDataAccess = cacheAccess;
    }

    @Override
    public void getBoard(final BoardCallback callback) {

        mCacheDataAccess.getBoard(new IBoardDataAccess.BoardDataAccessCallback() {
            @Override
            public void onGetBoardSuccess(List<Board> boards) {
                onBoardSuccess(callback, boards);
            }

            @Override
            public void onGetBoardFailure(String message) {
                mCloudDataAccess.getBoard(new IBoardDataAccess.BoardDataAccessCallback() {
                    @Override
                    public void onGetBoardSuccess(List<Board> boards) {
                        mCacheDataAccess.saveBoard(boards);
                        onBoardSuccess(callback, boards);
                    }

                    @Override
                    public void onGetBoardFailure(String message) {
                        onBoardError(callback, message);
                    }
                });
            }
        });
    }

    private void onBoardSuccess(final BoardCallback callback, final List<Board> boards) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onGetBoardSuccess(boards);
            }
        });
    }

    private void onBoardError(final BoardCallback callback, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onGetBoardFailure(message);
            }
        });
    }

    private void runOnUiThread(Runnable task) {
        new Handler(Looper.getMainLooper()).post(task);
    }
}
