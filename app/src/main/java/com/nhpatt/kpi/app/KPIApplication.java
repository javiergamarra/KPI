package com.nhpatt.kpi.app;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;
import com.nhpatt.kpi.jobs.FilmsJob;
import com.orm.SugarApp;
import com.squareup.leakcanary.LeakCanary;

/**
 * @author Javier Gamarra
 */
public class KPIApplication extends SugarApp {

    public static final String TAG = "KPIApp";

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);

        JobManager.create(this, new MyJobCreator());
    }

    private class MyJobCreator implements JobCreator {
        @Override
        public Job create(String tag) {
            switch (tag) {
                default:
                    return new FilmsJob();
            }
        }
    }
}
