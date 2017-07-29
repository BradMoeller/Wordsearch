package com.duolingo.wordsearch.ui.wordsearch.presenter;

import com.duolingo.wordsearch.domain.IBoardRepository;
import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.ui.wordsearch.view.IWordSearchView;

import java.util.List;

/**
 * Created by brad on 7/27/17.
 */

public class WordSearchPresenter implements IWordSearchPresenter {

    private IWordSearchView mWordSearchView;
    private IBoardRepository mBoardRepository;

    public WordSearchPresenter(IBoardRepository repository) {
        mBoardRepository = repository;
    }

    @Override
    public void setView(IWordSearchView view) {
        mWordSearchView = view;
    }

    @Override
    public void fetchBoard() {
        mBoardRepository.getBoard(new IBoardRepository.BoardCallback() {
            @Override
            public void onGetBoardSuccess(List<Board> boards) {
                if (mWordSearchView != null) {
                    mWordSearchView.displayNewBoard(boards.get(0));
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
