package com.duolingo.wordsearch.data;

import com.duolingo.wordsearch.model.Board;

import java.util.List;

/**
 * Created by brad on 8/1/17.
 */

public interface IBoardCacheDataAccess {

    void getBoards(BoardCacheDataAccessCallback callback);
    void saveBoards(List<Board> boards);
    int getCurrentBoardIndex();
    void setCurrentBoardIndex(int index);

    interface BoardCacheDataAccessCallback {
        void onGetBoardsSuccess(List<Board> boards);
        void onGetBoardsFailure(String message);
    }
}
