package com.duolingo.wordsearch;

import com.duolingo.wordsearch.domain.IBoardRepository;
import com.duolingo.wordsearch.model.Coord;
import com.duolingo.wordsearch.ui.wordsearch.presenter.WordSearchPresenter;
import com.duolingo.wordsearch.ui.wordsearch.view.IWordSearchView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WordSearchPresenterTest {

    @Mock
    IWordSearchView view;

    @Mock
    IBoardRepository repository;


    @Inject
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

}