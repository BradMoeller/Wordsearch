package com.duolingo.wordsearch.domain;

import android.os.Handler;
import android.os.Looper;

import com.duolingo.wordsearch.data.BoardCloudDataAccess;
import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.network.WorkerThreadCallback;
import com.duolingo.wordsearch.util.GsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.mockito.internal.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by brad on 7/29/17.
 */

public class BoardRepository implements IBoardRepository {

    @Override
    public void getBoard(final BoardCallback callback) {
        new BoardCloudDataAccess().getBoard(new WorkerThreadCallback() {
            @Override
            public void onSuccess(Response response, int statusCode) {

                try {
                    String stringResult = response.body().string();
                    List<Board> boards = parseResult(stringResult);

                    if (boards == null) {
                        onBoardError(callback, "No Boards were found.");
                    } else {
                        onBoardSuccess(callback, boards);
                    }
                } catch (IOException | JsonSyntaxException e) {
                    onBoardError(callback, e.getMessage());
                }
            }

            @Override
            public void onFailure(String response, int statusCode) {
                onBoardError(callback, response);
            }

            @Override
            public void onException(IOException exception) {
                onBoardError(callback, exception.getMessage());
            }
        });
    }

    public List<Board> parseResult(String jsonString) throws JsonSyntaxException {
        jsonString = StringEscapeUtils.unescapeJson(jsonString);

        if (jsonString != null) {
            String[] results = jsonString.split("\n");
            List<Board> boards = new ArrayList<Board>();
            for (String jsonBoard : results) {
                Board board = GsonUtils.getGson().fromJson(jsonBoard, Board.class);
                boards.add(board);
            }
            return boards;
        }
        return null;
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
