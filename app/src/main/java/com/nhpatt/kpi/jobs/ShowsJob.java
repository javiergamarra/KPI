package com.nhpatt.kpi.jobs;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.nhpatt.kpi.service.ShowsService;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

/**
 * @author Javier Gamarra
 */
public class ShowsJob extends Job {
    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://showrss.info/")
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();

            ShowsService showsService = retrofit.create(ShowsService.class);

            EventBus.getDefault().post(showsService.showToWatch().execute().body());
            return Result.SUCCESS;
        } catch (IOException e) {
            Log.e("TAG", "Error retrieving shows", e);
        }
        return Result.FAILURE;
    }
}
