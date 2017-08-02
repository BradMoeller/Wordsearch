package com.duolingo.wordsearch.ui.wordsearch.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.duolingo.wordsearch.R;

/**
 * Created by brad on 8/1/17.
 */

public class WordSearchTextView extends android.support.v7.widget.AppCompatTextView {

    public enum State {
        NORMAL,
        HIGHLIGHTED
    }

    private boolean mIsVerified;
    private State mState;

    public WordSearchTextView(Context context) {
        super(context);
    }

    public WordSearchTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WordSearchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
        updateBackgroundColor();
    }

    public boolean isVerified() {
        return mIsVerified;
    }

    public void setIsVerified(boolean isVerified) {
        mIsVerified = isVerified;
        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        if (mState == State.HIGHLIGHTED) {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text_highlighted_background));
        } else if (mIsVerified) {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text_verified_background));
        } else {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text_normal_background));
        }
    }
}
