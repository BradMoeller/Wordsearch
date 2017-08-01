package com.duolingo.wordsearch.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.duolingo.wordsearch.R;
import com.duolingo.wordsearch.data.BoardCacheDataAccess;
import com.duolingo.wordsearch.data.BoardCloudDataAccess;
import com.duolingo.wordsearch.data.IBoardCacheDataAccess;
import com.duolingo.wordsearch.data.IBoardCloudDataAccess;
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
    IBoardRepository provideBoardRepository(IBoardCloudDataAccess dataAccess, IBoardCacheDataAccess cacheAccess) {
        return new BoardRepository(dataAccess, cacheAccess);
    }

    @Provides
    @CustomScope
    IBoardCloudDataAccess provideBoardCloudDataAccess() {
        return new BoardCloudDataAccess();
    }

    @Provides
    @CustomScope
    IBoardCacheDataAccess provideBoardCacheDataAccess(SharedPreferences sp) {
        return new BoardCacheDataAccess(sp);
    }

    @Provides
    @CustomScope
    SharedPreferences provideSharedPreferences() {
        return provideContext().getSharedPreferences(
                provideContext().getResources().getString(R.string.shared_preference_name), Context.MODE_PRIVATE);
    }



}
