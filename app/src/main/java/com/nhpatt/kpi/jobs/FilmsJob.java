package com.nhpatt.kpi.jobs;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.nhpatt.kpi.app.KPIApplication;
import com.nhpatt.kpi.models.Film;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Javier Gamarra
 */
public class FilmsJob extends Job {

    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://kat.cr/movies/?rss=1")
                    .build();
            com.squareup.okhttp.Response response = null;

            response = client.newCall(request).execute();

            String text = response.body().string();

            List<Film> films = parseFilms(text);

            //FIXME !

            return Result.SUCCESS;

        } catch (IOException | XmlPullParserException | ParseException e) {
            Log.e(KPIApplication.TAG, "Error parsing films", e);
            return Result.FAILURE;
        }
    }

    private List<Film> parseFilms(String text) throws
            XmlPullParserException, IOException, ParseException {
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
}