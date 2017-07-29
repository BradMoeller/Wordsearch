package com.duolingo.wordsearch.ui.wordsearch.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.duolingo.wordsearch.R;
import com.duolingo.wordsearch.databinding.WordSearchActivityBinding;
import com.duolingo.wordsearch.di.component.DaggerActivityComponent;
import com.duolingo.wordsearch.di.module.ActivityModule;
import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.ui.wordsearch.presenter.IWordSearchPresenter;

import javax.inject.Inject;

public class WordSearchActivity extends AppCompatActivity implements IWordSearchView, View.OnClickListener {

    private WordSearchActivityBinding mBinding;

    @Inject
    IWordSearchPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout and toolbar
        mBinding = DataBindingUtil.setContentView(this, R.layout.word_search_activity);
        setSupportActionBar(mBinding.toolbar);

        //Inject the stack
        DaggerActivityComponent.builder().activityModule(new ActivityModule(this))
                .build().inject(this);
        mPresenter.setView(this);

        // Click listeners
        mBinding.aButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mPresenter.fetchBoard();
    }

    @Override
    public void setButtonText(String text) {
        mBinding.aButton.setText(text);
    }

    @Override
    public void displayNewBoard(Board board) {
        Toast.makeText(this, board.getWord(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void hideLoadingDialog() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
