package com.example.catalogfilms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.R;
import com.example.catalogfilms.models.Movie;
import com.example.catalogfilms.services.FavouriteMovieSingletonService;

import java.util.List;

import static com.example.catalogfilms.utils.ColorUtils.mapColorByRating;

public class FavouriteMovieAdapter extends RecyclerView.Adapter<FavouriteMovieAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Movie> movies;
    private FavouriteMovieSingletonService favouriteMovieService;

    public FavouriteMovieAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.inflater = LayoutInflater.from(context);
        this.favouriteMovieService = FavouriteMovieSingletonService.getInstance();
    }

    @Override
    public FavouriteMovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.favourite_movie_item, parent, false);
        return new FavouriteMovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteMovieAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.ratingView.setText(movie.getRating().toString());
        holder.ratingView.setTextColor(mapColorByRating(movie.getRating()));
        holder.titleView.setText(movie.getTitle());
        holder.descriptionView.setText(movie.getDescription());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView ratingView, titleView, descriptionView;

        ViewHolder(View view) {
            super(view);
            ratingView = (TextView) view.findViewById(R.id.favourite_rating);
            titleView = (TextView) view.findViewById(R.id.favourite_title);
            descriptionView = (TextView) view.findViewById(R.id.favourite_description);
            initClickItem(view, this);
        }
    }

    private void initClickItem(View view, final FavouriteMovieAdapter.ViewHolder viewHolder) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteMovieService.setFavouriteMovie(viewHolder);
            }
        });
    }
}
