package com.duolingo.wordsearch.ui.wordsearch.presenter;

import android.content.SharedPreferences;

import com.duolingo.wordsearch.domain.IBoardRepository;
import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.ui.wordsearch.view.IWordSearchView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by brad on 7/27/17.
 */

public class WordSearchPresenter implements IWordSearchPresenter {

    private IWordSearchView mWordSearchView;
    private IBoardRepository mBoardRepository;
    private Board mCurrentBoard;

    public WordSearchPresenter(IBoardRepository repository) {
        mBoardRepository = repository;
    }

    @Override
    public void setView(IWordSearchView view) {
        mWordSearchView = view;
    }

    @Override
    public void verifyHighlight(int beginX, int beginY, int endX, int endY) {

    }

    @Override
    public void fetchBoard() {
        mBoardRepository.getBoard(new IBoardRepository.BoardCallback() {
            @Override
            public void onGetBoardSuccess(List<Board> boards) {
                if (mWordSearchView != null) {
                    int index = mBoardRepository.getCurrentBoardIndex() % boards.size();
                    mCurrentBoard = boards.get(index);

                    mWordSearchView.displayNewBoard(mCurrentBoard);
                }
            }

            @Override
            public void onGetBoardFailure(String message) {
                if (mWordSearchView != null) {
                    mWordSearchView.displayError(message);
                }
            }
        });
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
