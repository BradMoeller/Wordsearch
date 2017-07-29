package com.duolingo.wordsearch.di.module;

import com.duolingo.wordsearch.domain.BoardRepository;
import com.duolingo.wordsearch.domain.IBoardRepository;
import com.duolingo.wordsearch.ui.wordsearch.presenter.IWordSearchPresenter;
import com.duolingo.wordsearch.ui.wordsearch.presenter.WordSearchPresenter;
import com.duolingo.wordsearch.util.CustomScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by brad on 7/29/17.
 */

@Module
public class ActivityModule {
    @Provides
    @CustomScope
    IWordSearchPresenter providePresenter(IBoardRepository repository) {
        return new WordSearchPresenter(repository);
    }

    @Provides
    @CustomScope
    IBoardRepository provideBoardRepository() {
        return new BoardRepository();
    }
}
