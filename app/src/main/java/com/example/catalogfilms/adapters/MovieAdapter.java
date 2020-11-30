package com.example.catalogfilms.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.R;
import com.example.catalogfilms.models.Movie;
import com.example.catalogfilms.services.MovieSingletonService;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Movie> movies;
    private MovieSingletonService movieService;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.inflater = LayoutInflater.from(context);
        this.movieService = MovieSingletonService.getInstance();
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        byte[] decodedString = Base64.decode(movie.getPoster(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.movieImage.setImageBitmap(decodedByte);

        holder.titleView.setText(movie.getTitle());
        holder.descriptionView.setText("Description:\n".concat(movie.getDescription()));
        holder.yearView.setText("Year: ".concat(movie.getYear().toString()));
        holder.genreView.setText("Genre: ".concat(movie.getGenre()));
        holder.directorView.setText("Director: ".concat(movie.getDirector()));
        holder.actorsView.setText("Actors: ".concat(movie.getActors()));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView movieImage;
        final TextView titleView, descriptionView, yearView, genreView, directorView, actorsView;

        ViewHolder(View view) {
            super(view);
            movieImage = (ImageView) view.findViewById(R.id.movie_image);
            titleView = (TextView) view.findViewById(R.id.movie_title);
            descriptionView = (TextView) view.findViewById(R.id.movie_description);
            yearView = (TextView) view.findViewById(R.id.movie_year);
            genreView = (TextView) view.findViewById(R.id.movie_genre);
            directorView = (TextView) view.findViewById(R.id.movie_director);
            actorsView = (TextView) view.findViewById(R.id.movie_actors);
            initClickItem(view, this);
        }
    }

    private void initClickItem(View view, final MovieAdapter.ViewHolder viewHolder) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieService.setSelectMovie(viewHolder);
            }
        });
    }
}
