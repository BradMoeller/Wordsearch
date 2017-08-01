package com.duolingo.wordsearch.data;

import com.duolingo.wordsearch.model.Board;

import java.util.List;

/**
 * Created by brad on 7/29/17.
 */

public interface IBoardCloudDataAccess {

    void getBoards(BoardDataAccessCallback callback);

    interface BoardDataAccessCallback {
        void onGetBoardsSuccess(List<Board> boards);
        void onGetBoardsFailure(String message);
    }
}
