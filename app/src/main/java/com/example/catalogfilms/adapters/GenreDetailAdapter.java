package com.example.catalogfilms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.R;
import com.example.catalogfilms.models.DetailGenre;

import java.util.List;

public class GenreDetailAdapter extends RecyclerView.Adapter<GenreDetailAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<DetailGenre> detailGenreList;

    public GenreDetailAdapter(Context context, List<DetailGenre> detailGenreList) {
        this.detailGenreList = detailGenreList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public GenreDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.genre_detail_item, parent, false);
        return new GenreDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreDetailAdapter.ViewHolder holder, int position) {
        DetailGenre detailGenre = detailGenreList.get(position);
        holder.ratingView.setText(detailGenre.getRating().toString());
        holder.titleView.setText(detailGenre.getTitle());
        holder.descriptionView.setText(detailGenre.getDescription());
    }

    @Override
    public int getItemCount() {
        return detailGenreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView ratingView, titleView, descriptionView;

        ViewHolder(View view) {
            super(view);
            ratingView = (TextView) view.findViewById(R.id.rating);
            titleView = (TextView) view.findViewById(R.id.title);
            descriptionView = (TextView) view.findViewById(R.id.description);
        }
    }
}
