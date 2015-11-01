package com.nhpatt.kpi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.nhpatt.kpi.HasCommits;
import com.nhpatt.kpi.R;
import com.nhpatt.kpi.graphs.BarChartRenderer;
import com.nhpatt.kpi.models.Commit;
import com.nhpatt.kpi.models.CommitPerYear;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javier Gamarra
 */
public class GithubFragment extends Fragment
        implements OnChartValueSelectedListener, Notifiable {

    public static GithubFragment newInstance() {
        Bundle args = new Bundle();
        GithubFragment fragment = new GithubFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.github, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        notifyEvent();
    }

    @Override
    public void notifyEvent() {

        CommitPerYear commitPerYear = ((HasCommits) getActivity()).getCommits();

        if (commitPerYear != null) {
            List<Commit> commits = commitPerYear.getCommits();

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
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


}
