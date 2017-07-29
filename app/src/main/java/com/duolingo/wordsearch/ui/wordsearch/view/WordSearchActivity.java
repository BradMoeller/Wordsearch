package com.duolingo.wordsearch.ui.wordsearch.view;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duolingo.wordsearch.R;
import com.duolingo.wordsearch.databinding.WordSearchActivityBinding;
import com.duolingo.wordsearch.di.component.DaggerActivityComponent;
import com.duolingo.wordsearch.di.module.ActivityModule;
import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.ui.wordsearch.presenter.IWordSearchPresenter;

import java.util.List;

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

        mPresenter.fetchBoard();
    }

    @Override
    public void onClick(View v) {
        mPresenter.fetchBoard();
    }

    @Override
    public void displayNewBoard(Board board) {
        Toast.makeText(this, board.getWord(), Toast.LENGTH_LONG).show();

        for (List<String> row : board.getCharacter_grid()) {
            LinearLayout ll = makeRow();
            for (String s : row) {
                ll.addView(makeTextView(s));
            }
            mBinding.boardContainer.addView(ll);
        }
    }

    private LinearLayout makeRow() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        return ll;
    }

    private TextView makeTextView (String text) {
        TextView tv = new TextView(this);

        tv.setLayoutParams(new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT, 1));
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv.setText(text);

        return tv;
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
