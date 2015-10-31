package com.nhpatt.kpi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhpatt.kpi.R;
import com.nhpatt.kpi.models.TitleAndDate;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Javier Gamarra
 */
public class TitleAndDateAdapter extends RecyclerView.Adapter<TitleAndDateAdapter.TitleAndDateViewHolder> {

    private final List<TitleAndDate> items;

    public TitleAndDateAdapter(List items) {
        this.items = items;
    }

    @Override
    public TitleAndDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new TitleAndDateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TitleAndDateViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class TitleAndDateViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView date;

        public TitleAndDateViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.item_name);
            date = (TextView) itemView.findViewById(R.id.item_date);
        }

        public void bind(TitleAndDate item) {
            name.setText(item.getTitle());
            if (item.getDate() != null) {
                date.setText(SimpleDateFormat.getDateInstance().format(item.getDate()));
            }
        }
    }
}
