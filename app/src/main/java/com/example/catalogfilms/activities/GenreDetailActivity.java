package com.example.catalogfilms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.services.GenreDetailSingletonService;
import com.example.catalogfilms.tasks.BackendAsyncTask;
import com.example.catalogfilms.R;
import com.example.catalogfilms.models.DetailGenre;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.example.catalogfilms.utils.RequestUtils.checkInternetConnection;
import static com.example.catalogfilms.utils.UrlUtils.getGenerateUrl;

public class GenreDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;

    private long genreId;
    private GenreDetailSingletonService genreDetailService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParameters();
        setContentView(R.layout.activity_genre_detail);
        setTitle(R.string.app_activity_genre_detail);

        initActionBar();
        initRecycleView();
        initBottomMenu();
        subscribeOnBottomNavigationView();

        initServices();
        downloadAndShowGenreDetailListById(genreId);
        recycleViewSelectItemSubscriber();
    }

    private void initParameters() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            genreId = arguments.getLong("genreId");
        }
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void initServices() {
        genreDetailService = GenreDetailSingletonService.getInstance();
    }

    private void initBottomMenu() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.genre_detail_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.catalogue);
    }

    private void subscribeOnBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.catalogue:
                                toGenreActivity();
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
        recyclerView = (RecyclerView) findViewById(R.id.genreDetailList);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }


    private void downloadAndShowGenreDetailListById(Long id) {
        if (!checkInternetConnection(this)) {
            return;
        }

        if (id == null) {
            return;
        }

        String jsonUrl = getGenerateUrl(DetailGenre.class.getSimpleName(), id);
        BackendAsyncTask task = new BackendAsyncTask(this, this.recyclerView, DetailGenre.class.getSimpleName());
        task.execute(jsonUrl);
    }

    private void recycleViewSelectItemSubscriber() {
        Disposable disposable = genreDetailService.getSelectDetailGenreStream().subscribeWith(new DisposableObserver<DetailGenre>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onNext(DetailGenre detailGenre) {
                toMovieActivity(detailGenre.getId());
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }

    private void toFavoriteMovieActivity() {
        Intent intent = new Intent(this, FavouriteMovieActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void toMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void toGenreActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
