package com.nhpatt.kpi.async;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.nhpatt.kpi.DashboardActivity;
import com.nhpatt.kpi.fragments.ShowsFragment;
import com.nhpatt.kpi.models.XML;
import com.nhpatt.kpi.service.ShowsService;

import java.io.IOException;
import java.lang.ref.WeakReference;

import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

/**
 * @author Javier Gamarra
 */
public class ShowsAsyncTask extends AsyncTask<Void, Void, XML> {

    private final ShowsFragment showsFragment;

    public ShowsAsyncTask(ShowsFragment showsFragment) {
        this.showsFragment = showsFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected XML doInBackground(Void... params) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://showrss.info/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        ShowsService showsService = retrofit.create(ShowsService.class);
        try {
            return showsService.showToWatch().execute().body();
        } catch (IOException e) {
            Log.e("TAG", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(XML xml) {
        super.onPostExecute(xml);

        if (showsFragment != null && xml != null) {
            showsFragment.renderShows(xml);
        }
    }
}
