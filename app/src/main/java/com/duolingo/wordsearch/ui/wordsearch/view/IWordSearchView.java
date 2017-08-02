package com.duolingo.wordsearch.ui.wordsearch.view;

import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.model.Coord;

import java.util.List;

/**
 * Created by brad on 7/27/17.
 */

public interface IWordSearchView {
    void displayNewBoard(Board board);
    void displayError(String error);
    void displayFoundWord(List<Coord> wordCoords);
    void displayAllWordsFound();
    void showLoadingDialog();
    void hideLoadingDialog();
}
