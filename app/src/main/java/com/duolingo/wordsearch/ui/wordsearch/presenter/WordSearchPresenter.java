package com.duolingo.wordsearch.ui.wordsearch.presenter;

import com.duolingo.wordsearch.domain.IBoardRepository;
import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.model.Coord;
import com.duolingo.wordsearch.ui.wordsearch.view.IWordSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brad on 7/27/17.
 */

public class WordSearchPresenter implements IWordSearchPresenter {

    public IWordSearchView mWordSearchView;
    public IBoardRepository mBoardRepository;
    public Board mCurrentBoard;
    public Map<String, List<Coord>> mFoundWords = new HashMap<>();

    public WordSearchPresenter(IBoardRepository repository) {
        mBoardRepository = repository;
    }

    @Override
    public void setView(IWordSearchView view) {
        mWordSearchView = view;
    }

    @Override
    public void verifyHighlight(List<Coord> coords) {

        if (mCurrentBoard != null && mCurrentBoard.getWord_locations() != null) {

            for (Map.Entry<String, String> entry : mCurrentBoard.getWord_locations().entrySet()) {
                String[] wordArray = entry.getKey().split(",");

                if (wordArray.length > 0 && wordArray.length % 2 == 0) {
                    List<Coord> wordCoords = new ArrayList<>();
                    try {
                        for (int i = 0 ; i < wordArray.length; i+=2) {

                            int x = Integer.parseInt(wordArray[i]);
                            int y = Integer.parseInt(wordArray[i+1]);
                            Coord c = new Coord(x, y);
                            wordCoords.add(c);
                        }

                        if (doesExactlyMatch(coords, wordCoords) && !mFoundWords.containsKey(entry.getValue())) {
                            mWordSearchView.displayFoundWord(coords);
                            mFoundWords.put(entry.getValue(), coords);
                        }
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
            }

            if (mFoundWords.size() == mCurrentBoard.getWord_locations().size()) {
                mWordSearchView.displayAllWordsFound();
            }
        }
    }

    public boolean doesExactlyMatch (List<Coord> highlightedCoords, List<Coord> wordCoords) {
        if (highlightedCoords != null && wordCoords != null && highlightedCoords.size() > 0 && wordCoords.size() > 0) {

            int foundLetters = 0;

            for (Coord highlightedCoord : highlightedCoords) {
                if (wordCoords.contains(highlightedCoord)) {
                    foundLetters++;
                }
            }
            return foundLetters == highlightedCoords.size() && foundLetters == wordCoords.size();
        }
        return false;
    }

    @Override
    public void fetchBoard(final boolean newBoard) {
        if (mWordSearchView != null) {
            mWordSearchView.showLoadingDialog();
        }

        mBoardRepository.getBoard(new IBoardRepository.BoardCallback() {
            @Override
            public void onGetBoardSuccess(List<Board> boards) {
                if (mWordSearchView != null) {

                    int index;
                    if (newBoard) {
                        index = mBoardRepository.getNextBoardIndex() % boards.size();
                        mFoundWords = new HashMap<>();
                    } else {
                        index = mBoardRepository.getCurrentBoardIndex() % boards.size();
                    }

                    mCurrentBoard = boards.get(index);

                    mWordSearchView.displayNewBoard(mCurrentBoard);

                    if (mFoundWords != null && mFoundWords.size() > 0) {
                        for (Map.Entry<String, List<Coord>> entry : mFoundWords.entrySet()) {
                            mWordSearchView.displayFoundWord(entry.getValue());
                        }
                    }
                    mWordSearchView.hideLoadingDialog();
                }
            }

            @Override
            public void onGetBoardFailure(String message) {
                if (mWordSearchView != null) {
                    mWordSearchView.hideLoadingDialog();
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
        mWordSearchView = null;
    }
}
