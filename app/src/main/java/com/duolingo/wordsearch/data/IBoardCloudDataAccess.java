package com.duolingo.wordsearch.data;

import com.duolingo.wordsearch.model.Board;

import java.util.List;

/**
 * Created by brad on 7/29/17.
 */

public interface IBoardDataAccess {

    void getBoard(BoardDataAccessCallback callback);
    void saveBoard(List<Board> boards);

    interface BoardDataAccessCallback {
        void onGetBoardSuccess(List<Board> boards);
        void onGetBoardFailure(String message);
    }
}
