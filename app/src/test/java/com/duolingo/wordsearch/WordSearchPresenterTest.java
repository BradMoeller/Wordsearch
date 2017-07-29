package com.duolingo.wordsearch;

import com.duolingo.wordsearch.ui.wordsearch.view.IWordSearchView;
import com.duolingo.wordsearch.ui.wordsearch.presenter.WordSearchPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Inject;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WordSearchPresenterTest {

    @Mock
    IWordSearchView view;

    @Inject
    private WordSearchPresenter presenter;

    @Before
    public void setUp() throws Exception {
        //presenter = new WordSearchPresenter(view);
    }

    @Test
    public void increaseCountAdds1() throws Exception {
        String original = "1";
        presenter.increaseCount(original);
        verify(view).setButtonText("2");
    }
}