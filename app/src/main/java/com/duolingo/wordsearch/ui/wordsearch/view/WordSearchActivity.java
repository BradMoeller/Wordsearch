package com.duolingo.wordsearch.ui.wordsearch.view;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class WordSearchActivity extends AppCompatActivity implements IWordSearchView, View.OnTouchListener {

    private WordSearchActivityBinding mBinding;
    private Map<Point, WordSearchTextView> mTextViews = new HashMap<>();
    private int mBoardWidth;
    private int mBoardHeight;

    private Point mBeginHighlight;
    private Point mCurrentHighlight;

    @Inject
    IWordSearchPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout and toolbar
        mBinding = DataBindingUtil.setContentView(this, R.layout.word_search_activity);
        setSupportActionBar(mBinding.toolbar);

        // Inject the stack
        DaggerActivityComponent.builder().activityModule(new ActivityModule(this))
                .build().inject(this);
        mPresenter.setView(this);

        mBinding.boardContainer.setOnTouchListener(this);

        // Finally, fetch and show the board
        mPresenter.fetchBoard();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mTextViews == null || mTextViews.size() == 0) {
            return false;
        }

        float percentWidth = event.getX() / mBinding.boardContainer.getWidth();
        float percentHeight = event.getY() / mBinding.boardContainer.getHeight();

        int xIndex = (int)(percentWidth * mBoardWidth);
        int yIndex = (int)(percentHeight * mBoardHeight);

        if (xIndex < mBoardWidth && yIndex < mBoardHeight) {

            Point p = new Point(xIndex, yIndex);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startNewHighlight(p);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    continueHighlight(p);
                    break;
                case MotionEvent.ACTION_UP:
                    endHighlight(p);
                    break;
            }
        }

        return false;
    }

    private void startNewHighlight(Point touchIndex) {
        mBeginHighlight = touchIndex;
        WordSearchTextView tv = mTextViews.get(touchIndex);
        if (tv != null) {
            tv.setState(WordSearchTextView.State.HIGHLIGHTED);
        }
    }

    private void continueHighlight(Point touchIndex) {
        if (mCurrentHighlight == null || mCurrentHighlight.x != touchIndex.x  || mCurrentHighlight.y != touchIndex.y) {

            Point cardinalEnd = findCardinalEnd(mBeginHighlight, touchIndex);

            if (mCurrentHighlight != null) {
                List<WordSearchTextView> oldHighlighted = findTextViews(mBeginHighlight, mCurrentHighlight);
                for (WordSearchTextView tv : oldHighlighted) {
                    tv.setState(WordSearchTextView.State.NORMAL);
                }
            }

            List<WordSearchTextView> newHighlighted = findTextViews(mBeginHighlight, cardinalEnd);
            for (WordSearchTextView tv : newHighlighted) {
                tv.setState(WordSearchTextView.State.HIGHLIGHTED);
            }

            mCurrentHighlight = cardinalEnd;
        }
    }

    private void endHighlight(Point touchIndex) {
        mPresenter.verifyHighlight(mBeginHighlight.x, mBeginHighlight.y, mCurrentHighlight.x, mCurrentHighlight.y);
        if (mCurrentHighlight != null) {
            List<WordSearchTextView> oldHighlighted = findTextViews(mBeginHighlight, mCurrentHighlight);
            for (WordSearchTextView tv : oldHighlighted) {
                tv.setState(WordSearchTextView.State.NORMAL);
            }
        }
        mCurrentHighlight = null;
        mBeginHighlight = null;
        //mPresenter.verifyHighlight(mBeginHighlight.x, mBeginHighlight.y, mCurrentHighlight.x, mCurrentHighlight.y);
    }

    /**
     * Finds all textviews that are between the begin and end points, inclusively.
     * This method assumes the end point is in a cardinal or primary inter-cardinal direction
     * from the begin point.
     * @param begin the beginning point
     * @param end the end point
     * @return a list of text views that are between begin and end, inclusively
     */
    private List<WordSearchTextView> findTextViews(Point begin, Point end) {
        List<WordSearchTextView> tvs = new ArrayList<>();

        if (begin.y == end.y) {
            // Horizontal
            int startX = Math.min(begin.x, end.x);
            int endX = Math.max(begin.x, end.x);
            for (int i = startX; i <= endX; i++) {
                tvs.add(mTextViews.get(new Point(i, begin.y)));
            }
            return tvs;
        } else if (begin.x == end.x) {
            // Vertical
            int startY = Math.min(begin.y, end.y);
            int endY = Math.max(begin.y, end.y);
            for (int i = startY; i <= endY; i++) {
                tvs.add(mTextViews.get(new Point(begin.x, i)));
            }
            return tvs;
        }

        // Diagonal
        int diffX = end.x - begin.x;
        int diffY = end.y - begin.y;

        int xDirection = diffX > 0 ? 1 : -1;
        int yDirection = diffY > 0 ? 1 : -1;

        for (int i = 0; i <= Math.abs(diffX); i++) {
            tvs.add(mTextViews.get(new Point(begin.x + i * xDirection, begin.y + i * yDirection)));
        }

        return tvs;
    }


    /**
     * Transform the touchIndex into a new Point that is in a cardinal or primary inter-cardinal
     * direction from the begin point, while ensuring the transformed point is within valid board space
     * @param begin the beginning point
     * @param touchIndex the endpoint to transform
     * @return a new valid board point that is in a cardinal or primary inter-cardinal direction from begin
     */
    private Point findCardinalEnd(Point begin, Point touchIndex) {
        if (touchIndex.x == begin.x || touchIndex.y == begin.y) {
            // Horizontal or vertical
            return touchIndex;
        }

        // Find the correct diagonal point
        int diffX = touchIndex.x - begin.x;
        int diffY = touchIndex.y - begin.y;

        int diagonalLength = Math.max(Math.abs(diffX), Math.abs(diffY));
        int xDirection = diffX > 0 ? 1 : -1;
        int yDirection = diffY > 0 ? 1 : -1;

        // Keep the end within the bounds of the board
        if (xDirection > 0) {
            if (yDirection > 0) {
                int xAvailableSpace = mBoardWidth - 1 - begin.x;
                int yAvailableSpace = mBoardHeight - 1 - begin.y;
                diagonalLength = Math.min(diagonalLength, Math.min(xAvailableSpace, yAvailableSpace));
            } else {
                int xAvailableSpace = mBoardWidth - 1 - begin.x;
                int yAvailableSpace = begin.y;
                diagonalLength = Math.min(diagonalLength, Math.min(xAvailableSpace, yAvailableSpace));
            }
        } else {
            if (yDirection > 0) {
                int xAvailableSpace = begin.x;
                int yAvailableSpace = mBoardHeight - 1 - begin.y;
                diagonalLength = Math.min(diagonalLength, Math.min(xAvailableSpace, yAvailableSpace));
            } else {
                int xAvailableSpace = begin.x;
                int yAvailableSpace = begin.y;
                diagonalLength = Math.min(diagonalLength, Math.min(xAvailableSpace, yAvailableSpace));
            }
        }

        int newX = begin.x + (diagonalLength * xDirection);
        int newY = begin.y + (diagonalLength * yDirection);
        return new Point(newX, newY);
    }

    @Override
    public void displayNewBoard(Board board) {
        Toast.makeText(this, board.getWord(), Toast.LENGTH_LONG).show();
        mTextViews = new HashMap<>();

        int y = 0;
        int x = 0;
        for (List<String> row : board.getCharacter_grid()) {
            LinearLayout ll = makeRow();
            x = 0;
            for (String s : row) {
                WordSearchTextView tv = makeTextView(s);
                mTextViews.put(new Point(x, y), tv);
                ll.addView(tv);
                x++;
            }
            y++;
            mBinding.boardContainer.addView(ll);
        }

        if (mTextViews.size() > 0) {
            mBoardHeight = y;
            mBoardWidth = x;
        }
    }

    private LinearLayout makeRow() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        return ll;
    }

    private WordSearchTextView makeTextView (String text) {
        final WordSearchTextView tv = new WordSearchTextView(this);

        tv.setLayoutParams(new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT, 1));
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv.setText(text);
        tv.setState(WordSearchTextView.State.NORMAL);
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
