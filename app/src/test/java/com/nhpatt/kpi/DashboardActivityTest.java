package com.nhpatt.kpi;

import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Javier Gamarra
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DashboardActivityTest {

    @Test
    public void testActivityWorksFine() {

        DashboardActivity activity = Robolectric.setupActivity(DashboardActivity.class);


        View viewById = activity.findViewById(R.id.view_pager);
        assertThat(viewById, is(not(nullValue())));
    }
}
