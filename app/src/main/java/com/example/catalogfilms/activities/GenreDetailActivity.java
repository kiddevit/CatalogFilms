package com.example.catalogfilms.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.services.GenreDetailSingletonService;
import com.example.catalogfilms.tasks.BackendAsyncTask;
import com.example.catalogfilms.R;
import com.example.catalogfilms.models.DetailGenre;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.example.catalogfilms.utils.RequestUtils.checkInternetConnection;
import static com.example.catalogfilms.utils.UrlUtils.getGenerateUrl;

public class GenreDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String activityTitle;
    private long genreId;
    private GenreDetailSingletonService genreDetailService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParameters();
        setContentView(R.layout.activity_genre_detail);
        setTitle(activityTitle);

        initServices();
        initRecycleView();
        downloadAndShowGenreDetailListById(genreId);
        recycleViewSelectItemSubscriber();
    }

    private void initParameters() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            genreId = arguments.getLong("genreId");
            activityTitle = arguments.getString("genreName");
        }
    }

    private void initServices() {
        genreDetailService = GenreDetailSingletonService.getInstance();
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
                toMovieActivity(detailGenre.getId(), detailGenre.getTitle());
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

    private void toMovieActivity(long movieId, String movieName) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("movieId", movieId);
        intent.putExtra("movieName", movieName);
        startActivity(intent);
    }
}
