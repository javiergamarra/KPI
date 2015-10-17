package com.nhpatt.kpi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.nhpatt.kpi.service.CommitActivity;
import com.nhpatt.kpi.service.GitHubService;
import com.nhpatt.kpi.service.ShowsService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, OnChartValueSelectedListener, TextWatcher {

    public static final String TAG = "KPI";
    public BarChart githubChart;

    public String objective;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load("https://pbs.twimg.com/profile_images/1210256780/avatar.jpg").into(imageView);

        TextView dailyObjective = (TextView) findViewById(R.id.daily_objective);
        if (dailyObjective != null) {
            dailyObjective.setMinimumHeight(200);
            dailyObjective.addTextChangedListener(this);
            dailyObjective.setText(getSharedPreferences().getString("objective", ""));
        }

        View star = findViewById(R.id.star);
        if (star != null) {
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(intent, 1);
                }
            });
        }

        drawChart();

        Show tbbt = new Show("The Big Bang Theory");
        Show homeland = new Show("Homeland");
        List<Show> shows = new ArrayList<>(Arrays.asList(tbbt, homeland));

        ShowsAdapter adapter = new ShowsAdapter(shows);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shows);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                View content = findViewById(android.R.id.content);

                String message = "Last commits: " + response.body().get(response.body().size() - 1).getTotal();

                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void requestShows() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://showrss.info/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        ShowsService showsService = retrofit.create(ShowsService.class);
        showsService.showToWatch().enqueue(new Callback<XML>() {
            @Override
            public void onResponse(Response<XML> response, Retrofit retrofit) {
                Log.e(TAG, response.toString());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(imageBitmap);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    private SharedPreferences getSharedPreferences() {
        return getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        objective = s.toString();

        SharedPreferences preferences = getSharedPreferences();
        preferences.edit().putString("objective", objective).commit();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
