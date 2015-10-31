package com.nhpatt.kpi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.nhpatt.kpi.R;
import com.nhpatt.kpi.app.KPIApplication;
import com.nhpatt.kpi.graphs.BarChartRenderer;
import com.nhpatt.kpi.models.CommitActivity;
import com.nhpatt.kpi.service.GitHubService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * @author Javier Gamarra
 */
public class GithubFragment extends Fragment implements OnChartValueSelectedListener {

    public static GithubFragment newInstance() {
        Bundle args = new Bundle();

        GithubFragment fragment = new GithubFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.github, container, false);

        requestCommits();

        return view;
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

        BarChart githubChart = (BarChart) getActivity().findViewById(R.id.github);
        githubChart.setOnChartValueSelectedListener(this);
        int color = getResources().getColor(R.color.accent);
        new BarChartRenderer().drawChart(githubChart, xValues, yValues, color);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
