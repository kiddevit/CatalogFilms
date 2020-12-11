package com.example.catalogfilms;

import com.example.catalogfilms.models.Genre;
import com.example.catalogfilms.models.Movie;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GenreUnitTest {
    private Genre genre;

    @Before
    public void createGenre() {
        this.genre = Genre.builder()
                .id((long) 1)
                .name("Action")
                .amount(0)
                .movies(new ArrayList<Movie>())
                .build();
    }

    @Test
    public void additionMovieToGenre_isCorrect() {
        Movie addedMovie = Movie.builder()
                .id((long) 1)
                .genre("Action")
                .title("Incredible")
                .description("In search of incredible")
                .actors("mr. Incredible, mrs. Impossible")
                .director("mr. Supernatural")
                .poster("fdsf12e1265461edsf")
                .year(2020)
                .rating((double) 10)
                .genreId((long) 1)
                .build();

        this.genre.getMovies().add(addedMovie);

        assertEquals(1, this.genre.getMovies().size());
        assertTrue("expected movies contains added movie", this.genre.getMovies().contains(addedMovie));
    }

    @Test
    public void renameGenre_isCorrect() {
        this.genre.setName("Horror");

        assertEquals("Horror", this.genre.getName());
    }
}
