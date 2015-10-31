package com.nhpatt.kpi.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evernote.android.job.JobRequest;
import com.nhpatt.kpi.R;
import com.nhpatt.kpi.adapters.TitleAndDateAdapter;
import com.nhpatt.kpi.models.Film;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author Javier Gamarra
 */
public class FilmsFragment extends Fragment {

    public static FilmsFragment newInstance() {

        Bundle args = new Bundle();

        FilmsFragment fragment = new FilmsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.films, container, false);

        requestFilms();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);

        super.onPause();
    }

    public void onEventMainThread(List<Film> films) {
        TitleAndDateAdapter filmsAdapter = new TitleAndDateAdapter(films);

        RecyclerView filmsRecyclerView = (RecyclerView) getActivity().findViewById(R.id.films);
        filmsRecyclerView.setAdapter(filmsAdapter);
        filmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
