package com.example.baking;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityListItemClickTest {

    private IdlingResource mIdlingResource;
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule=new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource(){
        mIdlingResource=mainActivityActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void onMainActivityListItemClicked_DetailActivityStart(){
        Espresso.onView(ViewMatchers.withId(R.id.rv_cakeList)).perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        Espresso.onView(ViewMatchers.withId(R.id.lbl_ingredients)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
