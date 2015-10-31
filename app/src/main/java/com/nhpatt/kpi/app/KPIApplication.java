package com.nhpatt.kpi.app;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;
import com.nhpatt.kpi.jobs.FilmsJob;
import com.nhpatt.kpi.jobs.GithubJob;
import com.nhpatt.kpi.jobs.ShowsJob;
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
                case "github":
                    return new GithubJob();
                case "shows":
                    return new ShowsJob();
                default:
                    return new FilmsJob();
            }
        }
    }
}
