package com.example.catalogfilms;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ActivityTestRule;


import com.example.catalogfilms.activities.GenreDetailActivity;
import com.example.catalogfilms.adapters.GenreDetailAdapter;
import com.example.catalogfilms.models.DetailGenre;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class GenreInstrumentedTest {

    @Rule
    public ActivityTestRule<GenreDetailActivity> rule = new ActivityTestRule<>(GenreDetailActivity.class);

    @Test
    public void recycleViewIsPresent() {
        GenreDetailActivity activity = rule.getActivity();
        View recyclerView = activity.findViewById(R.id.genreDetailList);

        assertThat(recyclerView, notNullValue());
        assertThat(recyclerView, instanceOf(RecyclerView.class));
    }


    @Test
    public void recycleViewIsCorrectInit() {
        GenreDetailActivity activity = rule.getActivity();
        RecyclerView recyclerView = activity.findViewById(R.id.genreDetailList);

        List<DetailGenre> detailGenres = Arrays.asList(
                DetailGenre.builder()
                        .id((long) 1)
                        .title("Action")
                        .description("The best genre")
                        .rating((double) 10)
                        .build(),
                DetailGenre.builder()
                        .id((long) 2)
                        .title("Horror")
                        .description("The fear genre")
                        .rating((double) 9.5)
                        .build()
        );

        GenreDetailAdapter adapter = new GenreDetailAdapter(activity.getBaseContext(), detailGenres);
        recyclerView.setAdapter(adapter);

        assertEquals(detailGenres.size(), adapter.getItemCount());
        assertEquals(detailGenres.get(0), adapter.getDetailGenreList().get(0));
    }
}
