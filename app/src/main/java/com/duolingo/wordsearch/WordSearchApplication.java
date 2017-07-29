package com.duolingo.wordsearch;

import android.app.Application;
import android.content.Context;

import com.duolingo.wordsearch.di.component.AppComponent;
import com.duolingo.wordsearch.di.component.DaggerAppComponent;
import com.duolingo.wordsearch.di.module.AppModule;

/**
 * Created by brad on 7/27/17.
 */

public class WordSearchApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static WordSearchApplication get(Context context) {
        return (WordSearchApplication) context.getApplicationContext();
    }
}
