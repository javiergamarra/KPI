package com.nhpatt.kpi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhpatt.kpi.HasFilms;
import com.nhpatt.kpi.R;
import com.nhpatt.kpi.adapters.TitleAndDateAdapter;
import com.nhpatt.kpi.models.Film;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javier Gamarra
 */
public class FilmsFragment extends Fragment implements Notifiable {

    private List films;
    private TitleAndDateAdapter filmsAdapter;

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

        films = new ArrayList();
        filmsAdapter = new TitleAndDateAdapter(films);

        RecyclerView filmsRecyclerView = (RecyclerView) view.findViewById(R.id.films);
        filmsRecyclerView.setAdapter(filmsAdapter);
        filmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        notifyEvent();
    }

    @Override
    public void notifyEvent() {
        List<Film> newFilms = ((HasFilms) getActivity()).getFilms();
        if (!newFilms.isEmpty()){
            films.clear();
            this.films.addAll(newFilms);
            filmsAdapter.notifyDataSetChanged();
        }
    }

}
