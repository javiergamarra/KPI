package com.nhpatt.kpi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, OnChartValueSelectedListener {

    public static final String TAG = "KPI";
    public BarChart githubChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        findViewById(R.id.daily_objective).setMinimumHeight(200);
        findViewById(R.id.star).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.nhpatt.com"));
                startActivity(intent);
            }
        });

        drawChart();
    }

    private void drawChart() {
        githubChart = (BarChart) findViewById(R.id.github);
        githubChart.setOnChartValueSelectedListener(this);

        XAxis xAxis = githubChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = githubChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);

        YAxis rightAxis = githubChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend legend = githubChart.getLegend();
        legend.setEnabled(false);

        githubChart.setDescription("");

        String[] xValues = {"0", "1", "2", "3"};
        Float[] yValues = {3.4F, 4.1F, 4.7F, 3.9F};
        setData(xValues, yValues);
    }

    private void setData(String[] xValues, Float[] yValues) {

        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < yValues.length; i++) {
            barEntries.add(new BarEntry(yValues[i], i));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setBarSpacePercent(35f);
        barDataSet.setColor(getResources().getColor(R.color.accent));

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);

        BarData data = new BarData(xValues, dataSets);
        data.setValueTextSize(10f);

        githubChart.setData(data);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("example", true);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
    }

}
