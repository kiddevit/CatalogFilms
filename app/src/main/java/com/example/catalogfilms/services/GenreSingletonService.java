package com.example.catalogfilms.services;

import com.example.catalogfilms.adapters.GenreAdapter;
import com.example.catalogfilms.models.Genre;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

public final class GenreSingletonService {
    private static volatile GenreSingletonService instance;

    private PublishSubject<Genre> selectGenreStream;
    private List<Genre> genres;

    private GenreSingletonService() {
        this.selectGenreStream = PublishSubject.create();
    }

    public static GenreSingletonService getInstance() {
        GenreSingletonService genreSingletonService = instance;
        if (genreSingletonService != null) {
            return genreSingletonService;
        }

        synchronized (GenreSingletonService.class) {
            if (instance == null) {
                instance = new GenreSingletonService();
            }
            return instance;
        }
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public PublishSubject<Genre> getSelectGenreStream() {
        return this.selectGenreStream;
    }

    public void setSelectGenre(GenreAdapter.ViewHolder viewHolder) {
        int genreAdapterPosition = viewHolder.getAdapterPosition();
        if (genreAdapterPosition != -1) {
            Genre genre = genres.get(genreAdapterPosition);
            if (genre != null) {
                this.selectGenreStream.onNext(genre);
            }
        }
    }
}
