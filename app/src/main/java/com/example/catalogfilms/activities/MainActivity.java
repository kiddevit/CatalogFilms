package com.example.catalogfilms.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.catalogfilms.services.GenreSingletonService;
import com.example.catalogfilms.tasks.BackendAsyncTask;
import com.example.catalogfilms.R;
import com.example.catalogfilms.models.Genre;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.example.catalogfilms.utils.RequestUtils.checkInternetConnection;
import static com.example.catalogfilms.utils.UrlUtils.getGenerateUrl;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GenreSingletonService genreService;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Genres");

        initServices();
        initRecycleView();
        downloadAndShowGenreList();
        recycleViewSelectItemSubscriber();
    }

    private void initServices() {
        genreService = GenreSingletonService.getInstance();
    }

    private void initRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.genreList);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void downloadAndShowGenreList() {
        if (!checkInternetConnection(this)) {
            return;
        }

        String jsonUrl = getGenerateUrl(Genre.class.getSimpleName(), null);
        BackendAsyncTask task = new BackendAsyncTask(this, this.recyclerView, Genre.class.getSimpleName());
        task.execute(jsonUrl);
    }

    private void recycleViewSelectItemSubscriber() {
        Disposable disposable = genreService.getSelectGenreStream().subscribeWith(new DisposableObserver<Genre>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onNext(Genre genre) {
                if (genre.getAmount() < 1) {
                    Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                toGenreDetailActivity(genre.getId(), genre.getName());
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

    private void toGenreDetailActivity(long genreId, String genreName) {
        Intent intent = new Intent(this, GenreDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("genreId", genreId);
        intent.putExtra("genreName", genreName);
        startActivity(intent);
    }
}