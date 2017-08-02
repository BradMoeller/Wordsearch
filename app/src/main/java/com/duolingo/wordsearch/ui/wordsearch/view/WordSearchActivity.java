package com.duolingo.wordsearch.ui.wordsearch.view;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.duolingo.wordsearch.R;
import com.duolingo.wordsearch.databinding.WordSearchActivityBinding;
import com.duolingo.wordsearch.di.component.DaggerActivityComponent;
import com.duolingo.wordsearch.di.module.ActivityModule;
import com.duolingo.wordsearch.model.Board;
import com.duolingo.wordsearch.model.Coord;
import com.duolingo.wordsearch.ui.wordsearch.presenter.IWordSearchPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class WordSearchActivity extends AppCompatActivity implements IWordSearchView, View.OnTouchListener {

    private WordSearchActivityBinding mBinding;
    private Map<Coord, WordSearchTextView> mTextViews = new HashMap<>();
    public int mBoardWidth;
    public int mBoardHeight;

    public Coord mBeginHighlight;
    public Coord mCurrentHighlight;

    private MaterialDialog mLoadingDialog;
    private boolean mIsSuccessDisplayed;

    @Inject
    IWordSearchPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout and toolbar
        mBinding = DataBindingUtil.setContentView(this, R.layout.word_search_activity);
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setTitle("Wordsearch");

        // Inject the presenter or recover it after a configuration change
        attachPresenter();

        mBinding.boardContainer.setOnTouchListener(this);


        // Finally, fetch and show the board
        mPresenter.fetchBoard(false);
        showLoadingDialog();
    }

    private void attachPresenter() {
        mPresenter = (IWordSearchPresenter) getLastCustomNonConfigurationInstance();
        if (mPresenter == null) {
            // Inject the presenter
            DaggerActivityComponent.builder().activityModule(new ActivityModule(this))
                    .build().inject(this);

        }
        mPresenter.setView(this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mPresenter;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mTextViews == null || mTextViews.size() == 0) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startNewHighlight(getBoardIndex(event.getX(), event.getY()));
                return true;
            case MotionEvent.ACTION_MOVE:
                continueHighlight(getBoardIndex(event.getX(), event.getY()));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                endHighlight();
                break;
        }

        return false;
    }

    /**
     * Find the X, Y index on the board for the ontouch location
     * @param touchX the x coordinate of the touch
     * @param touchY the y coordinate of the touch
     * @return the X,Y board coordinate on which the touch took place
     */
    private Coord getBoardIndex(float touchX, float touchY) {
        float percentWidth = touchX / mBinding.boardContainer.getWidth();
        float percentHeight = touchY / mBinding.boardContainer.getHeight();

        int xIndex = (int)(percentWidth * mBoardWidth);
        int yIndex = (int)(percentHeight * mBoardHeight);

        // Make sure the x,y index is within range of the board
        xIndex = Math.max(0, Math.min(xIndex, mBoardWidth-1));
        yIndex = Math.max(0, Math.min(yIndex, mBoardHeight-1));

        return new Coord(xIndex, yIndex);
    }

    /**
     * Begins a highlight
     * @param touchIndex The x,y board coordinate of the last touch
     */
    private void startNewHighlight(Coord touchIndex) {
        mBeginHighlight = touchIndex;
        WordSearchTextView tv = mTextViews.get(touchIndex);
        if (tv != null) {
            tv.setState(WordSearchTextView.State.HIGHLIGHTED);
        }
    }

    /**
     * Updates the highlighted cells on the board.
     * 1) Converts the touch coordinate to a cardinal direction from the beginning touch
     * 2) Un-highlights the previous selection
     * 3) Highlights the current selection
     * @param touchIndex The x,y board coordinate of the last touch
     */
    private void continueHighlight(Coord touchIndex) {
        if (mCurrentHighlight == null || mCurrentHighlight.x != touchIndex.x  || mCurrentHighlight.y != touchIndex.y) {

            Coord cardinalEnd = findCardinalEnd(mBeginHighlight, touchIndex);

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

    /**
     * Occurs when the user has finished their selection
     * 1) Clear the board of highlights
     * 2) Check to see if they have highlighted a valid word
     */
    private void endHighlight() {
        if (mCurrentHighlight != null) {
            List<WordSearchTextView> oldHighlighted = findTextViews(mBeginHighlight, mCurrentHighlight);
            for (WordSearchTextView tv : oldHighlighted) {
                tv.setState(WordSearchTextView.State.NORMAL);
            }
            List<Coord> coords = findCoords(mBeginHighlight, mCurrentHighlight);
            mPresenter.verifyHighlight(coords);
        }
        mCurrentHighlight = null;
        mBeginHighlight = null;
    }

    /**
     * Finds all Textviews that are between the begin and end coords, inclusively.
     * @param begin The beginning coord
     * @param end The end coord
     * @return a list of textviews that are between the begin and end, inclusively
     */
    private List<WordSearchTextView> findTextViews(Coord begin, Coord end) {
        List<WordSearchTextView> tvs = new ArrayList<>();
        List<Coord> coords = findCoords(begin, end);
        for (Coord p : coords) {
            tvs.add(mTextViews.get(p));
        }
        return tvs;
    }

    /**
     * Finds all Coords that are between the begin and end coords, inclusively.
     * This method assumes the end coord is in a cardinal or primary inter-cardinal direction
     * from the begin coord.
     * @param begin the beginning coord
     * @param end the end coord
     * @return a list of coords that are between begin and end, inclusively
     */
    private List<Coord> findCoords(Coord begin, Coord end) {
        List<Coord> coords = new ArrayList<>();

        if (begin.y == end.y) {
            // Horizontal
            int startX = Math.min(begin.x, end.x);
            int endX = Math.max(begin.x, end.x);
            for (int i = startX; i <= endX; i++) {
                coords.add(new Coord(i, begin.y));
            }
            return coords;

        } else if (begin.x == end.x) {
            // Vertical
            int startY = Math.min(begin.y, end.y);
            int endY = Math.max(begin.y, end.y);
            for (int i = startY; i <= endY; i++) {
                coords.add(new Coord(begin.x, i));
            }
            return coords;
        }

        // Diagonal
        int diffX = end.x - begin.x;
        int diffY = end.y - begin.y;

        int xDirection = diffX > 0 ? 1 : -1;
        int yDirection = diffY > 0 ? 1 : -1;

        for (int i = 0; i <= Math.abs(diffX); i++) {
            Coord p = new Coord(begin.x + i * xDirection, begin.y + i * yDirection);
            coords.add(p);
        }

        return coords;
    }


    /**
     * Transform the touchIndex into a new Coord that is in a cardinal or primary inter-cardinal
     * direction from the begin coord, while ensuring the transformed coord is within valid board space
     * @param begin the beginning coord
     * @param touchIndex the endcoord to transform
     * @return a new valid board coord that is in a cardinal or primary inter-cardinal direction from begin
     */
    private Coord findCardinalEnd(Coord begin, Coord touchIndex) {
        if (touchIndex.x == begin.x || touchIndex.y == begin.y) {
            // Horizontal or vertical
            return touchIndex;
        }

        // Find the correct diagonal coord
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
        return new Coord(newX, newY);
    }

    /**
     * Creates and displays a new board
     * @param board the board model
     */
    @Override
    public void displayNewBoard(Board board) {
        mBinding.boardContainer.removeAllViews();
        mBinding.sourceWordText.setText(board.getWord());
        mTextViews = new HashMap<>();

        int y = 0;
        int x = 0;
        for (List<String> row : board.getCharacter_grid()) {
            LinearLayout ll = makeRow();
            x = 0;
            for (String s : row) {
                WordSearchTextView tv = makeTextView(s);
                mTextViews.put(new Coord(x, y), tv);
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
    public void displayFoundWord(List<Coord> wordCoords) {
        for (Coord coord : wordCoords) {
            mTextViews.get(coord).setIsVerified(true);
        }
    }

    @Override
    public void displayAllWordsFound() {
        new MaterialDialog.Builder(this)
            .title(R.string.all_words_found_title)
            .content(R.string.all_words_found_content)
            .positiveText(R.string.ok)
            .negativeText(R.string.dismiss)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    mPresenter.fetchBoard(true);
                    mIsSuccessDisplayed = false;
                }
            })
            .dismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mIsSuccessDisplayed = false;
                }
            })
            .show();
        mIsSuccessDisplayed = true;
    }

    @Override
    public void displayError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new MaterialDialog.Builder(this).progress(true, 100).build();
        }
        mLoadingDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.hide();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey("isSuccessShown")) {
            displayAllWordsFound();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mIsSuccessDisplayed) {
            outState.putBoolean("isSuccessShown", true);
        }
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
