package com.nhpatt.kpi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.nhpatt.kpi.adapters.ShowsAdapter;
import com.nhpatt.kpi.async.ShowsAsyncTask;
import com.nhpatt.kpi.graphs.BarChartRenderer;
import com.nhpatt.kpi.models.Channel;
import com.nhpatt.kpi.models.CommitActivity;
import com.nhpatt.kpi.models.Show;
import com.nhpatt.kpi.models.XML;
import com.nhpatt.kpi.service.GitHubService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class DashboardActivity extends AppCompatActivity
        implements OnChartValueSelectedListener {

    public static final String TAG = "KPI";
    private List<Show> shows;
    private ShowsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        requestCommits();
        requestShows();
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
                Log.e(TAG, t.getMessage());
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

        adapter = new ShowsAdapter(shows);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shows);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ShowsAsyncTask showsAsyncTask = new ShowsAsyncTask(new WeakReference<>(this));
        showsAsyncTask.execute();
    }

    public void renderShows(XML xml) {
        Channel channel = xml.getChannel();

        for (Show show : channel.getShows()) {
            shows.add(show);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
    }

}