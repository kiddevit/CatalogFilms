package com.example.catalogfilms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.R;
import com.example.catalogfilms.models.Genre;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Genre> genreList;

    public GenreAdapter(Context context, List<Genre> genreList) {
        this.genreList = genreList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.genre_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreAdapter.ViewHolder holder, int position) {
        Genre genre = genreList.get(position);
        holder.nameView.setText(genre.getName());
        holder.amountView.setText(genre.getAmount().toString().concat("  ›"));
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, amountView;

        ViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            amountView = (TextView) view.findViewById(R.id.amount);
        }
    }
}
