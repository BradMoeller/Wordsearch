package com.duolingo.wordsearch.ui.wordsearch.presenter;

import com.duolingo.wordsearch.ui.wordsearch.view.IWordSearchView;

/**
 * Created by brad on 7/27/17.
 */

public interface IWordSearchPresenter extends IPresenter {
    void fetchBoard();
    void setView(IWordSearchView view);
    void verifyHighlight(int beginX, int beginY, int endX, int endY);
}
