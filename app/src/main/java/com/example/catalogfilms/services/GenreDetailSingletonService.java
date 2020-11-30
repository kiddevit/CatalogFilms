package com.example.catalogfilms.services;

import com.example.catalogfilms.adapters.GenreDetailAdapter;
import com.example.catalogfilms.models.DetailGenre;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

public final class GenreDetailSingletonService {
    private static volatile GenreDetailSingletonService instance;

    private PublishSubject<DetailGenre> selectDetailGenreStream;
    private List<DetailGenre> detailGenreList;

    private GenreDetailSingletonService() {
        this.selectDetailGenreStream = PublishSubject.create();
    }

    public static GenreDetailSingletonService getInstance() {
        GenreDetailSingletonService genreDetailSingletonService = instance;
        if (genreDetailSingletonService != null) {
            return genreDetailSingletonService;
        }

        synchronized (GenreDetailSingletonService.class) {
            if (instance == null) {
                instance = new GenreDetailSingletonService();
            }
            return instance;
        }
    }

    public List<DetailGenre> getDetailGenreList() {
        return detailGenreList;
    }

    public void setDetailGenreList(List<DetailGenre> detailGenreList) {
        this.detailGenreList = detailGenreList;
    }

    public PublishSubject<DetailGenre> getSelectDetailGenreStream() {
        return this.selectDetailGenreStream;
    }

    public void setSelectDetailGenre(GenreDetailAdapter.ViewHolder viewHolder) {
        int genreDetailAdapterPosition = viewHolder.getAdapterPosition();
        if (genreDetailAdapterPosition != -1) {
            DetailGenre detailGenre = detailGenreList.get(genreDetailAdapterPosition);
            if (detailGenre != null) {
                this.selectDetailGenreStream.onNext(detailGenre);
            }
        }
    }
}
