package com.duolingo.wordsearch;

import com.duolingo.wordsearch.domain.IBoardRepository;
import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.model.Coord;
import com.duolingo.wordsearch.ui.wordsearch.presenter.WordSearchPresenter;
import com.duolingo.wordsearch.ui.wordsearch.view.IWordSearchView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WordSearchPresenterTest {

    @Mock
    IWordSearchView view;

    @Mock
    IBoardRepository repository;

    private WordSearchPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new WordSearchPresenter(repository);
    }

    @Test
    public void verifyDoesExactlyMatch() throws Exception {
        List<Coord> highlighted = new ArrayList<>();
        List<Coord> word = new ArrayList<>();

        highlighted.add(new Coord(1, 1));
        highlighted.add(new Coord(2, 1));
        highlighted.add(new Coord(3, 1));

        word.add(new Coord(1, 1));
        word.add(new Coord(3, 1));
        word.add(new Coord(2, 1));
        boolean result = presenter.doesExactlyMatch(highlighted, word);
        assertTrue (result);
    }

    @Test
    public void verifyHighlightSuccess() throws Exception {
        List<Coord> highlighted = new ArrayList<>();
        highlighted.add(new Coord(1, 1));
        highlighted.add(new Coord(2, 1));
        highlighted.add(new Coord(3, 1));

        List<Coord> word = new ArrayList<>();
        word.add(new Coord(1, 1));
        word.add(new Coord(3, 1));
        word.add(new Coord(2, 1));
        Board b = new Board();
        Map<String, String> wordLocations = new HashMap<>();
        wordLocations.put("1,1,3,1,2,1", "soy");
        b.setWord_locations(wordLocations);

        doNothing().when(view).displayAllWordsFound();

        presenter.mCurrentBoard = b;
        presenter.setView(view);
        presenter.verifyHighlight(highlighted);
        verify(view).displayFoundWord(ArgumentMatchers.<Coord>anyList());
    }

}