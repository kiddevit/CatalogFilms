package com.example.catalogfilms.services;

import com.example.catalogfilms.adapters.MovieAdapter;
import com.example.catalogfilms.models.Genre;
import com.example.catalogfilms.models.Movie;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

public final class MovieSingletonService {
    private static volatile MovieSingletonService instance;

    private PublishSubject<Movie> selectMovieStream;
    private List<Movie> movies;

    private MovieSingletonService() {
        this.selectMovieStream = PublishSubject.create();
    }

    public static MovieSingletonService getInstance() {
        MovieSingletonService movieSingletonService = instance;
        if (movieSingletonService != null) {
            return movieSingletonService;
        }

        synchronized (MovieSingletonService.class) {
            if (instance == null) {
                instance = new MovieSingletonService();
            }
            return instance;
        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public PublishSubject<Movie> getSelectMovieStream() {
        return this.selectMovieStream;
    }

    public void setSelectMovie(MovieAdapter.ViewHolder viewHolder) {
        int movieAdapterPosition = viewHolder.getAdapterPosition();
        if (movieAdapterPosition != -1) {
            Movie movie = movies.get(movieAdapterPosition);
            if (movie != null) {
                this.selectMovieStream.onNext(movie);
            }
        }
    }
}
