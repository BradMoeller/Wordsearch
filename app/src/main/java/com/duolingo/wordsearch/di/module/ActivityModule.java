package com.duolingo.wordsearch.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.duolingo.wordsearch.data.BoardCacheDataAccess;
import com.duolingo.wordsearch.data.BoardCloudDataAccess;
import com.duolingo.wordsearch.data.IBoardDataAccess;
import com.duolingo.wordsearch.domain.BoardRepository;
import com.duolingo.wordsearch.domain.IBoardRepository;
import com.duolingo.wordsearch.ui.wordsearch.presenter.IWordSearchPresenter;
import com.duolingo.wordsearch.ui.wordsearch.presenter.WordSearchPresenter;
import com.duolingo.wordsearch.util.CustomScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by brad on 7/29/17.
 */

@Module
public class ActivityModule {

    Context mContext;

    public ActivityModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @CustomScope
    Context provideContext() {
        return mContext;
    }

    @Provides
    @CustomScope
    IWordSearchPresenter providePresenter(IBoardRepository repository) {
        return new WordSearchPresenter(repository);
    }

    @Provides
    @CustomScope
    IBoardRepository provideBoardRepository(@Named("cloud") IBoardDataAccess dataAccess, @Named("cache") IBoardDataAccess cacheAccess) {
        return new BoardRepository(dataAccess, cacheAccess);
    }

    @Provides
    @CustomScope
    @Named("cloud")
    IBoardDataAccess provideBoardCloudDataAccess() {
        return new BoardCloudDataAccess();
    }

    @Provides
    @CustomScope
    @Named("cache")
    IBoardDataAccess provideBoardCacheDataAccess(SharedPreferences sp) {
        return new BoardCacheDataAccess(sp);
    }

    @Provides
    @CustomScope
    SharedPreferences provideSharedPreferences() {
        return provideContext().getSharedPreferences("com.duolingo.wordsearch.PREFS", Context.MODE_PRIVATE);
    }



}
