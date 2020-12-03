package com.example.catalogfilms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.repository.MovieRepository;
import com.example.catalogfilms.services.MovieSingletonService;
import com.example.catalogfilms.tasks.BackendAsyncTask;
import com.example.catalogfilms.R;
import com.example.catalogfilms.models.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.catalogfilms.utils.RequestUtils.checkInternetConnection;
import static com.example.catalogfilms.utils.UrlUtils.getGenerateUrl;

public class MovieActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;

    private long movieId;
    private MovieSingletonService movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParameters();
        setContentView(R.layout.activity_movie);
        setTitle("About movie");

        initActionBar();
        initRecycleView();
        initBottomMenu();

        initServices();
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

    private void initBottomMenu() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.movie_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.catalogue:
                                break;
                            case R.id.favorite:
                                toFavoriteMovieActivity();
                                break;
                            case R.id.map:
                                toMapActivity();
                                break;
                        }
                        return false;
                    }
                });
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

        Movie currentMovie = movieService.getMovies().get(0);
        if (movieRepository.getMovieByTitle(currentMovie.getTitle()) != null) {
            Toast.makeText(this, "Already added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            if (movieRepository.add(currentMovie) != -1) {
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

    private void toFavoriteMovieActivity() {
        Intent intent = new Intent(this, FavouriteMovieActivity.class);
        startActivity(intent);
    }

    private void toMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
