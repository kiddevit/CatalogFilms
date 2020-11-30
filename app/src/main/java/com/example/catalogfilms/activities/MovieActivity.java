package com.example.catalogfilms.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.tasks.BackendAsyncTask;
import com.example.catalogfilms.R;
import com.example.catalogfilms.models.Movie;

import static com.example.catalogfilms.utils.RequestUtils.checkInternetConnection;
import static com.example.catalogfilms.utils.UrlUtils.getGenerateUrl;

public class MovieActivity extends AppCompatActivity {
    private long movieId;
    private RecyclerView recyclerView;
    private String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParameters();
        setContentView(R.layout.activity_movie);
        setTitle(activityTitle);

        initRecycleView();
        downloadAndShowMovieById(movieId);
    }

    private void initParameters() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            movieId = arguments.getLong("movieId");
            activityTitle = arguments.getString("movieName");
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
