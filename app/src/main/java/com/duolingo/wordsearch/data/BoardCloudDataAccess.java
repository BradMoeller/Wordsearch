package com.duolingo.wordsearch.data;

import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.network.NetworkUtils;
import com.duolingo.wordsearch.network.WorkerThreadCallback;
import com.duolingo.wordsearch.util.GsonUtils;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by brad on 7/29/17.
 */

public class BoardCloudDataAccess implements IBoardCloudDataAccess {

    @Override
    public void getBoards(final BoardDataAccessCallback callback) {
        NetworkUtils.get("https://s3.amazonaws.com/duolingo-data/s3/js2/find_challenges.txt",
            new WorkerThreadCallback() {
            @Override
            public void onSuccess(Response response, int statusCode) {

                try {
                    String stringResult = response.body().string();
                    List<Board> boards = parseResult(stringResult);

                    if (boards == null) {
                        callback.onGetBoardsFailure("No Boards were found.");
                    } else {
                        callback.onGetBoardsSuccess(boards);
                    }
                } catch (IOException | JsonSyntaxException e) {
                    callback.onGetBoardsFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(String response, int statusCode) {
                callback.onGetBoardsFailure(response);
            }

            @Override
            public void onException(IOException exception) {
                callback.onGetBoardsFailure(exception.getMessage());
            }
        });
    }

    public List<Board> parseResult(String jsonString) throws JsonSyntaxException {
        jsonString = StringEscapeUtils.unescapeJson(jsonString);

        if (jsonString != null) {
            String[] results = jsonString.split("\n");
            List<Board> boards = new ArrayList<>();
            for (String jsonBoard : results) {
                Board board = GsonUtils.getGson().fromJson(jsonBoard, Board.class);
                boards.add(board);
            }
            return boards;
        }
        return null;
    }
}
