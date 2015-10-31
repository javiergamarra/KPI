package com.nhpatt.kpi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.evernote.android.job.JobRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.nhpatt.kpi.adapters.TitleAndDateAdapter;
import com.nhpatt.kpi.app.KPIApplication;
import com.nhpatt.kpi.async.ShowsAsyncTask;
import com.nhpatt.kpi.fragments.FilmsFragment;
import com.nhpatt.kpi.graphs.BarChartRenderer;
import com.nhpatt.kpi.models.Channel;
import com.nhpatt.kpi.models.CommitActivity;
import com.nhpatt.kpi.models.Film;
import com.nhpatt.kpi.models.Show;
import com.nhpatt.kpi.models.XML;
import com.nhpatt.kpi.service.GitHubService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class DashboardActivity extends AppCompatActivity
        implements OnChartValueSelectedListener {

    @Bind(R.id.shows)
    public RecyclerView showsRecyclerView;
    private List<Show> shows;
    private TitleAndDateAdapter showsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new FilmsFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);

        requestCommits();
        requestShows();
        requestFilms();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);

        super.onPause();
    }

    public void onEventMainThread(List<Film> films) {
        TitleAndDateAdapter filmsAdapter = new TitleAndDateAdapter(films);

        RecyclerView filmsRecyclerView = (RecyclerView) findViewById(R.id.films);
        filmsRecyclerView.setAdapter(filmsAdapter);
        filmsRecyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
    }


    private void requestCommits() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);

        service.commitsPerWeek("nhpatt", "KPI").enqueue(new Callback<List<CommitActivity>>() {
            @Override
            public void onResponse(Response<List<CommitActivity>> response, Retrofit retrofit) {
                renderCommits(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(KPIApplication.TAG, t.getMessage());
            }
        });
    }

    private void renderCommits(List<CommitActivity> commits) {

        List<String> xValues = new ArrayList<>();
        List<Float> yValues = new ArrayList<>();
        int lastWeek = 0;

        while (lastWeek < 7 && commits.size() > lastWeek) {
            xValues.add(String.valueOf(lastWeek));
            int total = commits.get(commits.size() - 1 - lastWeek).getTotal();
            yValues.add(Float.valueOf(total));
            lastWeek++;
        }

        BarChart githubChart = (BarChart) findViewById(R.id.github);
        githubChart.setOnChartValueSelectedListener(DashboardActivity.this);
        int color = getResources().getColor(R.color.accent);
        new BarChartRenderer().drawChart(githubChart, xValues, yValues, color);
    }

    private void requestShows() {
        shows = new ArrayList<>();

        showsAdapter = new TitleAndDateAdapter(shows);

        showsRecyclerView.setAdapter(showsAdapter);
        showsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ShowsAsyncTask showsAsyncTask = new ShowsAsyncTask(new WeakReference<>(this));
        showsAsyncTask.execute();
    }

    public void renderShows(XML xml) {
        Channel channel = xml.getChannel();

        for (Show show : channel.getShows()) {
            shows.add(show);
        }
        showsAdapter.notifyDataSetChanged();
    }

    private void requestFilms() {
        if (!isNetworkAvailable()) {
            List<Film> films = Film.listAll(Film.class);
            EventBus.getDefault().post(films);
        } else {
            new JobRequest.Builder("films")
                    .setExact(1L)
                    .build()
                    .schedule();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
    }

}