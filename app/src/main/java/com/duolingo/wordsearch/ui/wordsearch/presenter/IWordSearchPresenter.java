package com.duolingo.wordsearch.ui.wordsearch.presenter;


import com.duolingo.wordsearch.model.Coord;
import com.duolingo.wordsearch.ui.wordsearch.view.IWordSearchView;

import java.util.List;


/**
 * Created by brad on 7/27/17.
 */

public interface IWordSearchPresenter extends IPresenter {
    void fetchBoard(boolean newBoard);
    void setView(IWordSearchView view);
    void verifyHighlight(List<Coord> point);
}
