package com.duolingo.wordsearch.data;

import android.content.SharedPreferences;

import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.util.GsonUtils;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by brad on 7/29/17.
 */

public class BoardCacheDataAccess implements IBoardCacheDataAccess {

    private SharedPreferences mPrefs;
    private final String BOARDS_KEY = "com.duolingo.wordsearch.BOARDS_KEY";
    private final String CURRENT_BOARD_KEY = "com.duolingo.wordsearch.CURRENT_BOARD_KEY";

    public BoardCacheDataAccess(SharedPreferences prefs) {
        mPrefs = prefs;
    }

    @Override
    public void getBoards(BoardCacheDataAccessCallback callback) {
        try {
            if (mPrefs.contains(BOARDS_KEY)) {
                String boardsString = mPrefs.getString(BOARDS_KEY, "");
                Type listType = new TypeToken<List<Board>>()
                {
                }.getType();

                List<Board> boards = GsonUtils.getGson().fromJson(boardsString, listType);

                if (boards != null && boards.size() > 0) {
                    callback.onGetBoardsSuccess(boards);
                    return;
                }
            }
        } catch (JsonSyntaxException jse) {
            callback.onGetBoardsFailure(jse.toString());
            return;
        }

        callback.onGetBoardsFailure("No Boards Found");
    }

    @Override
    public void saveBoards(List<Board> boards) {
        mPrefs.edit().putString(BOARDS_KEY, GsonUtils.getGson().toJson(boards)).apply();
    }

    @Override
    public int getCurrentBoardIndex() {
        return mPrefs.getInt(CURRENT_BOARD_KEY, 0);
    }

    @Override
    public void setCurrentBoardIndex(int index) {
        mPrefs.edit().putInt(CURRENT_BOARD_KEY, index).apply();
    }
}
