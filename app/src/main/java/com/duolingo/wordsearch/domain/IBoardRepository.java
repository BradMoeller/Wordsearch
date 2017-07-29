package com.duolingo.wordsearch.domain;

import com.duolingo.wordsearch.model.Board;

import java.util.List;

/**
 * Created by brad on 7/29/17.
 */

public interface IBoardRepository {
    interface BoardCallback {
        void onGetBoardSuccess(List<Board> boards);
        void onGetBoardFailure(String message);
    }

    void getBoard(BoardCallback callback);
}
