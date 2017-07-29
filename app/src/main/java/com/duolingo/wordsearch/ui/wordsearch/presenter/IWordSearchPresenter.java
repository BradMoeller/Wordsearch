package com.duolingo.wordsearch.ui.wordsearch.presenter;

import com.duolingo.wordsearch.ui.wordsearch.view.IWordSearchView;

/**
 * Created by brad on 7/27/17.
 */

public interface IWordSearchPresenter extends IPresenter {
    void increaseCount(String currentCount);
    void fetchBoard();
    void setView(IWordSearchView view);
}
