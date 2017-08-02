package com.duolingo.wordsearch;

import android.content.SharedPreferences;

import com.duolingo.wordsearch.data.BoardCacheDataAccess;
import com.duolingo.wordsearch.data.BoardCloudDataAccess;
import com.duolingo.wordsearch.data.IBoardCacheDataAccess;
import com.duolingo.wordsearch.data.IBoardCloudDataAccess;
import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.network.NetworkUtils;
import com.duolingo.wordsearch.network.WorkerThreadCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

/**
 * Created by brad on 8/2/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class BoardCacheDataAccessTest {

    BoardCacheDataAccess mDataAccess;

    @Mock
    SharedPreferences mSharedPrefs;

    private String validBoardJson;
    private String invalidBoardJson;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mDataAccess = new BoardCacheDataAccess(mSharedPrefs);
        validBoardJson = "{\"source_language\": \"en\", \"word\": \"man\", \"character_grid\": [[\"i\", \"q\", \"\\u00ed\", \"l\", \"n\", \"n\", \"m\", \"\\u00f3\"], [\"f\", \"t\", \"v\", \"\\u00f1\", \"b\", \"m\", \"h\", \"a\"], [\"h\", \"j\", \"\\u00e9\", \"t\", \"e\", \"t\", \"o\", \"z\"], [\"x\", \"\\u00e1\", \"o\", \"i\", \"e\", \"\\u00f1\", \"m\", \"\\u00e9\"], [\"q\", \"\\u00e9\", \"i\", \"\\u00f3\", \"q\", \"s\", \"b\", \"s\"], [\"c\", \"u\", \"m\", \"y\", \"v\", \"l\", \"r\", \"x\"], [\"\\u00fc\", \"\\u00ed\", \"\\u00f3\", \"m\", \"o\", \"t\", \"e\", \"k\"], [\"a\", \"g\", \"r\", \"n\", \"n\", \"\\u00f3\", \"s\", \"m\"]], \"word_locations\": {\"6,1,6,2,6,3,6,4,6,5,6,6\": \"hombre\"}, \"target_language\": \"es\"}";
        invalidBoardJson = "nguage\": \"en\", \"word\": \"man\", \"character_grid\": [[\"i\", \"q\", \"\\u00ed\", \"l\", \"n\", \"n\", \"m\", \"\\u00f3\"], [\"f\", \"t\", \"v\", \"\\u00f1\", \"b\", \"m\", \"h\", \"a\"], [\"h\", \"j\", \"\\u00e9\", \"t\", \"e\", \"t\", \"o\", \"z\"], [\"x\", \"\\u00e1\", \"o\", \"i\", \"e\", \"\\u00f1\", \"m\", \"\\u00e9\"], [\"q\", \"\\u00e9\", \"i\", \"\\u00f3\", \"q\", \"s\", \"b\", \"s\"], [\"c\", \"u\", \"m\", \"y\", \"v\", \"l\", \"r\", \"x\"], [\"\\u00fc\", \"\\u00ed\", \"\\u00f3\", \"m\", \"o\", \"t\", \"e\", \"k\"], [\"a\", \"g\", \"r\", \"n\", \"n\", \"\\u00f3\", \"s\", \"m\"]], \"word_locations\": {\"6,1,6,2,6,3,6,4,6,5,6,6\": \"hombre\"}, \"target_language\": \"es\"}";
    }

    @Test
    public void testGetBoardsSuccess() throws Exception {

        doReturn(true).when(mSharedPrefs).contains(eq(mDataAccess.BOARDS_KEY));
        doReturn(validBoardJson).when(mSharedPrefs).getString(eq(mDataAccess.BOARDS_KEY), eq(""));

        mDataAccess.getBoards(new IBoardCacheDataAccess.BoardCacheDataAccessCallback() {
            @Override
            public void onGetBoardsSuccess(List<Board> boards) {
                assertTrue(boards != null && boards.size() == 1);
            }

            @Override
            public void onGetBoardsFailure(String message) {

            }
        });
    }

    @Test
    public void testGetBoardsInvalidJson() throws Exception {

        doReturn(true).when(mSharedPrefs).contains(eq(mDataAccess.BOARDS_KEY));
        doReturn(invalidBoardJson).when(mSharedPrefs).getString(eq(mDataAccess.BOARDS_KEY), eq(""));

        mDataAccess.getBoards(new IBoardCacheDataAccess.BoardCacheDataAccessCallback() {
            @Override
            public void onGetBoardsSuccess(List<Board> boards) {
                assertTrue(false);
            }

            @Override
            public void onGetBoardsFailure(String message) {
                assertTrue(true);
            }
        });
    }
}
