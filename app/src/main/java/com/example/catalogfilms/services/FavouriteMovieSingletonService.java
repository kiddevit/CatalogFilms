package com.example.catalogfilms.services;

import com.example.catalogfilms.adapters.FavouriteMovieAdapter;
import com.example.catalogfilms.models.Movie;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class FavouriteMovieSingletonService {
    private static volatile FavouriteMovieSingletonService instance;

    private PublishSubject<Movie> selectFavouriteMovieStream;
    private List<Movie> favouriteMovies;

    private FavouriteMovieSingletonService() {
        this.selectFavouriteMovieStream = PublishSubject.create();
    }

    public static FavouriteMovieSingletonService getInstance() {
        FavouriteMovieSingletonService favouriteMovieSingletonService = instance;
        if (favouriteMovieSingletonService != null) {
            return favouriteMovieSingletonService;
        }

        synchronized (FavouriteMovieSingletonService.class) {
            if (instance == null) {
                instance = new FavouriteMovieSingletonService();
            }
            return instance;
        }
    }

    public List<Movie> getFavouriteMovies() {
        return favouriteMovies;
    }

    public void setFavouriteMovies(List<Movie> favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }

    public PublishSubject<Movie> getSelectFavouriteMovieStream() {
        return this.selectFavouriteMovieStream;
    }

    public void setFavouriteMovie(FavouriteMovieAdapter.ViewHolder viewHolder) {
        int favouriteMovieAdapterPosition = viewHolder.getAdapterPosition();
        if (favouriteMovieAdapterPosition != -1) {
            Movie favouriteMovie = favouriteMovies.get(favouriteMovieAdapterPosition);
            if (favouriteMovie != null) {
                this.selectFavouriteMovieStream.onNext(favouriteMovie);
            }
        }
    }
}
