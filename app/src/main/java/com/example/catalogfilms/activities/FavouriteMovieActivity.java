package com.example.catalogfilms.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.R;
import com.example.catalogfilms.adapters.FavouriteMovieAdapter;
import com.example.catalogfilms.models.Movie;
import com.example.catalogfilms.repository.MovieRepository;
import com.example.catalogfilms.services.FavouriteMovieSingletonService;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class FavouriteMovieActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavouriteMovieSingletonService favouriteMovieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movie);
        setTitle("Favourite movies");

        initServices();
        initRecycleView();
        loadDataFromDB();
        recycleViewSelectItemSubscriber();
    }


    private void initServices() {
        favouriteMovieService = FavouriteMovieSingletonService.getInstance();
    }

    private void initRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.favouriteMovies);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void loadDataFromDB() {
        MovieRepository movieRepository = new MovieRepository(this);
        movieRepository.open();

        List<Movie> favouriteMovies = movieRepository.getMovies();
        favouriteMovieService.setFavouriteMovies(favouriteMovies);
        FavouriteMovieAdapter adapter = new FavouriteMovieAdapter(this, favouriteMovies);
        this.recyclerView.setAdapter(adapter);

        movieRepository.close();
    }

    private void recycleViewSelectItemSubscriber() {
        Disposable disposable = favouriteMovieService.getSelectFavouriteMovieStream().subscribeWith(new DisposableObserver<Movie>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onNext(Movie movie) {
                toMovieActivity(movie.getId());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void toMovieActivity(long movieId) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }
}
