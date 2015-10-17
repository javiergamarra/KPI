package com.nhpatt.kpi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nhpatt.kpi.R;
import com.nhpatt.kpi.models.Show;

import java.text.SimpleDateFormat;
import java.util.List;

public class ShowListAdapter extends ArrayAdapter<Show> {

    public ShowListAdapter(Context context, List<Show> shows) {
        super(context, R.layout.show_layout, shows);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Show show = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.show_layout, parent, false);
        }

        TextView showDate = (TextView) convertView.findViewById(R.id.show_date);
        TextView showName = (TextView) convertView.findViewById(R.id.show_name);

        showDate.setText(new SimpleDateFormat("dd/MM").format(show.getDate()));
        showName.setText(show.getTitle());

        return convertView;
    }
}