package com.nhpatt.kpi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhpatt.kpi.R;
import com.nhpatt.kpi.models.Show;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Javier Gamarra
 */
public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder> {
    private final List<Show> shows;

    public ShowsAdapter(List<Show> shows) {
        this.shows = shows;
    }

    @Override
    public ShowsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_layout, parent, false);
        return new ShowsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShowsViewHolder holder, int position) {
        holder.bind(shows.get(position));
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    public class ShowsViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView date;

        public ShowsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.show_name);
            date = (TextView) itemView.findViewById(R.id.show_date);
        }

        public void bind(Show show) {
            name.setText(show.getTitle());
            date.setText(new SimpleDateFormat("dd/MM").format(show.getDate()));
        }
    }
}
