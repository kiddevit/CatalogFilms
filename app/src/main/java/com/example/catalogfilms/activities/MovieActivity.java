package com.example.catalogfilms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.repository.MovieRepository;
import com.example.catalogfilms.services.MovieSingletonService;
import com.example.catalogfilms.tasks.BackendAsyncTask;
import com.example.catalogfilms.R;
import com.example.catalogfilms.models.Movie;

import static com.example.catalogfilms.utils.RequestUtils.checkInternetConnection;
import static com.example.catalogfilms.utils.UrlUtils.getGenerateUrl;

public class MovieActivity extends AppCompatActivity {
    private long movieId;
    private RecyclerView recyclerView;
    private MovieSingletonService movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParameters();
        setContentView(R.layout.activity_movie);
        setTitle("About movie");

        initServices();
        initActionBar();
        initRecycleView();
        downloadAndShowMovieById(movieId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initServices() {
        movieService = MovieSingletonService.getInstance();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.favorite:
                onAddFavouriteMovie();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onAddFavouriteMovie() {
        MovieRepository movieRepository = new MovieRepository(this);
        movieRepository.open();

        Movie currentMovie = movieRepository.getMovieById(this.movieId);
        if (currentMovie != null) {
            Toast.makeText(this, "Already added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            if (movieRepository.add(movieService.getMovies().get(0)) != -1) {
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        }

        movieRepository.close();
    }

    private void initParameters() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            movieId = arguments.getLong("movieId");
        }
    }

    private void initRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.movies);
    }

    private void downloadAndShowMovieById(Long id) {
        if (!checkInternetConnection(this)) {
            return;
        }

        String jsonUrl = getGenerateUrl(Movie.class.getSimpleName(), id);
        BackendAsyncTask task = new BackendAsyncTask(this, this.recyclerView, Movie.class.getSimpleName());
        task.execute(jsonUrl);
    }
}
