package com.duolingo.wordsearch.ui.wordsearch.view;

import com.duolingo.wordsearch.model.Board;

/**
 * Created by brad on 7/27/17.
 */

public interface IWordSearchView {
    void setButtonText(String text);
    void displayNewBoard(Board board);
    void displayError(String error);
    void showLoadingDialog();
    void hideLoadingDialog();
}
