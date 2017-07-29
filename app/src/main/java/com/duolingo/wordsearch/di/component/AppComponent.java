package com.duolingo.wordsearch.di.component;

import android.app.Application;

import com.duolingo.wordsearch.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by brad on 7/29/17.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Application application();
}
