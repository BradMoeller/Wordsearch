package com.duolingo.wordsearch;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.duolingo.wordsearch.ui.wordsearch.view.WordSearchActivity;

import org.junit.Before;

import static org.mockito.Mockito.verify;

public class WordSearchActivityTest extends ActivityInstrumentationTestCase2<WordSearchActivity> {

    WordSearchActivity mActivity;
    Button mButton;


    public WordSearchActivityTest() {
        super(WordSearchActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        mActivity = getActivity();
        mButton = (Button) mActivity.findViewById(R.id.a_button);
    }

    public void testPreconditions() {
        assertNotNull("mActivity is null", mActivity);
        assertNotNull("mButton is null", mButton);
    }

    public void testUseAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.duolingo.wordsearch", appContext.getPackageName());
    }

    public void testButtonClick() throws Exception {
        Espresso.onView(ViewMatchers.withId(R.id.a_button))
                .perform(ViewActions.click());
        assertEquals("1", mButton.getText().toString());
    }
}
