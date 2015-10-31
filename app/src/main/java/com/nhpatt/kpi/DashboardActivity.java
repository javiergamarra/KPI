package com.nhpatt.kpi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.nhpatt.kpi.adapters.TitleAndDateAdapter;
import com.nhpatt.kpi.async.ShowsAsyncTask;
import com.nhpatt.kpi.graphs.BarChartRenderer;
import com.nhpatt.kpi.models.Channel;
import com.nhpatt.kpi.models.CommitActivity;
import com.nhpatt.kpi.models.Film;
import com.nhpatt.kpi.models.Show;
import com.nhpatt.kpi.models.XML;
import com.nhpatt.kpi.service.GitHubService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class DashboardActivity extends AppCompatActivity
        implements OnChartValueSelectedListener {

    public static final String TAG = "KPI";
    private List<Show> shows;
    private TitleAndDateAdapter showsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        requestCommits();
        requestShows();
        requestFilms();
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

        showsAdapter = new TitleAndDateAdapter(shows);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shows);
        recyclerView.setAdapter(showsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                TitleAndDateAdapter filmsAdapter = new TitleAndDateAdapter((List) msg.obj);

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.films);
                recyclerView.setAdapter(filmsAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("https://kat.cr/movies/?rss=1")
                            .build();
                    com.squareup.okhttp.Response response = client.newCall(request).execute();
                    String text = response.body().string();

                    List<Film> films = parseFilms(text);

                    Message msg = new Message();
                    msg.obj = films;
                    handler.sendMessage(msg);

                } catch (IOException | XmlPullParserException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<Film> parseFilms(String text) throws XmlPullParserException, IOException, ParseException {
        List<Film> films = new ArrayList<>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader(text));
        int eventType = xpp.getEventType();

        Film film = new Film();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if ("pubDate".equals(xpp.getName())) {
                    DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
                    film.setDate(formatter.parse(xpp.nextText()));
                } else if ("title".equals(xpp.getName())) {
                    film.setTitle(xpp.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG && "item".equals(xpp.getName())) {
                films.add(film);
                film = new Film();
            }
            eventType = xpp.next();
        }
        return films;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
    }

}