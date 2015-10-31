package com.nhpatt.kpi.jobs;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.nhpatt.kpi.app.KPIApplication;
import com.nhpatt.kpi.models.CommitActivity;
import com.nhpatt.kpi.service.GitHubService;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * @author Javier Gamarra
 */
public class GithubJob extends Job {
    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            GitHubService service = retrofit.create(GitHubService.class);

            Response<List<CommitActivity>> response = service.commitsPerWeek("nhpatt", "KPI").execute();
            EventBus.getDefault().post(response.body());
            return Result.SUCCESS;
        } catch (IOException e) {
            Log.e(KPIApplication.TAG, "Error retrieving commits", e);
        }
        return Result.FAILURE;
    }
}
