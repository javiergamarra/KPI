package com.nhpatt.kpi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhpatt.kpi.HasXML;
import com.nhpatt.kpi.R;
import com.nhpatt.kpi.adapters.TitleAndDateAdapter;
import com.nhpatt.kpi.models.Channel;
import com.nhpatt.kpi.models.Show;
import com.nhpatt.kpi.models.XML;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javier Gamarra
 */
public class ShowsFragment extends Fragment implements Notifiable {

    private List<Show> shows;
    private TitleAndDateAdapter showsAdapter;

    public static ShowsFragment newInstance() {
        Bundle args = new Bundle();
        ShowsFragment fragment = new ShowsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shows, container, false);

        shows = new ArrayList<>();

        showsAdapter = new TitleAndDateAdapter(shows);

        RecyclerView showsRecyclerView = (RecyclerView) view.findViewById(R.id.shows);
        showsRecyclerView.setAdapter(showsAdapter);
        showsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        notifyEvent();
    }

    @Override
    public void notifyEvent() {

        XML xml = ((HasXML) getActivity()).getXML();

        if (xml != null) {

            Channel channel = xml.getChannel();

            for (Show show : channel.getShows()) {
                shows.add(show);
            }
            showsAdapter.notifyDataSetChanged();
        }
    }
}
