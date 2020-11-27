package com.example.catalogfilms;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.models.DetailGenre;

import static com.example.catalogfilms.utils.RequestUtils.checkInternetConnection;
import static com.example.catalogfilms.utils.UrlUtils.getGenerateUrl;

public class GenreDetailActivity extends AppCompatActivity {
    private long genreId;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParameters();
        setContentView(R.layout.activity_genre_detail);

        recyclerView = (RecyclerView) findViewById(R.id.genreDetailList);
        downloadAndShowGenreDetailList(genreId);
    }

    private void initParameters() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            genreId = arguments.getLong("genreId");
        }
    }

    private void downloadAndShowGenreDetailList(Long id) {
        if (!checkInternetConnection(this)) {
            return;
        }

        String jsonUrl = getGenerateUrl(DetailGenre.class.getSimpleName(), id);
        DownloadJsonTask task = new DownloadJsonTask(this, this.recyclerView, DetailGenre.class.getSimpleName());
        task.execute(jsonUrl);
    }
}
