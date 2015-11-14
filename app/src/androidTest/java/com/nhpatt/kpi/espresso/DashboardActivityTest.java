package com.nhpatt.kpi.espresso;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.nhpatt.kpi.DashboardActivity;
import com.nhpatt.kpi.R;
import com.nhpatt.kpi.jobs.GithubJob;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DashboardActivityTest {

    @Rule
    public ActivityTestRule<DashboardActivity> mActivityRule = new ActivityTestRule(DashboardActivity.class);

    @Before
    public void setUp() {
        //
    }

    @Test
    public void viewPagerIsDisplayed() {

        GithubJob githubJob = new GithubJob();

        githubJob.registerIdleTransitionCallback(new IdlingResource.ResourceCallback() {
            @Override
            public void onTransitionToIdle() {
                onView(withId(R.id.view_pager)).check(matches(isDisplayed()));
            }
        });

        Espresso.registerIdlingResources(githubJob);

        githubJob.start();
    }
}