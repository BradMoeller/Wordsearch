package com.duolingo.wordsearch;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.LinearLayout;

import com.duolingo.wordsearch.ui.wordsearch.view.WordSearchActivity;

import org.junit.Before;

import static org.mockito.Mockito.verify;

public class WordSearchActivityTest extends ActivityInstrumentationTestCase2<WordSearchActivity> {

    WordSearchActivity mActivity;
    LinearLayout mBoardContainer;


    public WordSearchActivityTest() {
        super(WordSearchActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        mActivity = getActivity();
        mBoardContainer = (LinearLayout) mActivity.findViewById(R.id.board_container);
    }

    public void testPreconditions() {
        assertNotNull("mActivity is null", mActivity);
        assertNotNull("mBoardContainer is null", mBoardContainer);
    }

    public void testUseAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.duolingo.wordsearch", appContext.getPackageName());
    }

    public void testButtonClick() throws Exception {
        Espresso.onView(ViewMatchers.withId(R.id.board_container))
                .perform(ViewActions.click());
    }
}
