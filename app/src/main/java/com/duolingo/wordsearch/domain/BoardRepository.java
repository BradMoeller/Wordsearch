package com.duolingo.wordsearch.domain;

import android.os.Handler;
import android.os.Looper;

import com.duolingo.wordsearch.data.IBoardCacheDataAccess;
import com.duolingo.wordsearch.data.IBoardCloudDataAccess;
import com.duolingo.wordsearch.model.Board;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by brad on 7/29/17.
 */

public class BoardRepository implements IBoardRepository {

    private IBoardCloudDataAccess mCloudDataAccess;
    private IBoardCacheDataAccess mCacheDataAccess;

    @Inject
    public BoardRepository(IBoardCloudDataAccess cloudAccess, IBoardCacheDataAccess cacheAccess) {
        mCloudDataAccess = cloudAccess;
        mCacheDataAccess = cacheAccess;
    }

    @Override
    public void getBoard(final BoardCallback callback) {

        mCacheDataAccess.getBoards(new IBoardCacheDataAccess.BoardCacheDataAccessCallback() {
            @Override
            public void onGetBoardsSuccess(List<Board> boards) {
                onBoardSuccess(callback, boards);
            }

            @Override
            public void onGetBoardsFailure(String message) {
                mCloudDataAccess.getBoards(new IBoardCloudDataAccess.BoardDataAccessCallback() {
                    @Override
                    public void onGetBoardsSuccess(List<Board> boards) {
                        mCacheDataAccess.saveBoards(boards);
                        onBoardSuccess(callback, boards);
                    }

                    @Override
                    public void onGetBoardsFailure(String message) {
                        onBoardError(callback, message);
                    }
                });
            }
        });
    }

    @Override
    public int getCurrentBoardIndex() {
        return mCacheDataAccess.getCurrentBoardIndex();
    }

    @Override
    public int getNextBoardIndex() {
        int index = getCurrentBoardIndex();
        mCacheDataAccess.setCurrentBoardIndex(++index);
        return index;
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
