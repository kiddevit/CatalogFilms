package com.example.catalogfilms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.catalogfilms.models.Genre;

import static com.example.catalogfilms.utils.RequestUtils.checkInternetConnection;
import static com.example.catalogfilms.utils.UrlUtils.getGenerateUrl;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.genreList);
        downloadAndShowGenreList(null);

        /*Intent intent = new Intent(this, GenreDetailActivity.class);
        intent.putExtra("genreId", (long) 8);
        startActivity(intent);*/
    }

    private void downloadAndShowGenreList(Long id) {
        if (!checkInternetConnection(this)) {
            return;
        }

        String jsonUrl = getGenerateUrl(Genre.class.getSimpleName(), id);
        DownloadJsonTask task = new DownloadJsonTask(this, this.recyclerView, Genre.class.getSimpleName());
        task.execute(jsonUrl);
    }
}