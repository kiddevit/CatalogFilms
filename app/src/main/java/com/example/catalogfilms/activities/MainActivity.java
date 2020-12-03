package com.example.catalogfilms.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.catalogfilms.services.GenreSingletonService;
import com.example.catalogfilms.tasks.BackendAsyncTask;
import com.example.catalogfilms.R;
import com.example.catalogfilms.models.Genre;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.example.catalogfilms.utils.RequestUtils.checkInternetConnection;
import static com.example.catalogfilms.utils.UrlUtils.getGenerateUrl;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private GenreSingletonService genreService;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Genres");

        initRecycleView();
        initBottomMenu();

        initServices();
        downloadAndShowGenreList();
        recycleViewSelectItemSubscriber();
    }

    private void initServices() {
        genreService = GenreSingletonService.getInstance();
    }

    private void initBottomMenu() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.genre_bottom_navigation);
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
                toGenreDetailActivity(genre.getId());
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

    private void toGenreDetailActivity(long genreId) {
        Intent intent = new Intent(this, GenreDetailActivity.class);
        intent.putExtra("genreId", genreId);
        startActivity(intent);
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