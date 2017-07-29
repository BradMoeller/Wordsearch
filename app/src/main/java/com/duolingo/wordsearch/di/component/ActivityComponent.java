package com.duolingo.wordsearch.di.component;

import com.duolingo.wordsearch.di.module.ActivityModule;
import com.duolingo.wordsearch.ui.wordsearch.view.WordSearchActivity;
import com.duolingo.wordsearch.util.CustomScope;

import dagger.Component;

/**
 * Created by brad on 7/29/17.
 */

@CustomScope
@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(WordSearchActivity target);
}
