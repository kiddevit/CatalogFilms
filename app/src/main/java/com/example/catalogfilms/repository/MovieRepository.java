package com.example.catalogfilms.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.catalogfilms.helpers.DatabaseHelper;
import com.example.catalogfilms.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public MovieRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public MovieRepository open() {
        this.database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private Cursor getAllEntries() {
        String[] movieColumns = new String[]{
                DatabaseHelper.MOVIE_COLUMN_ID,
                DatabaseHelper.MOVIE_COLUMN_TITLE,
                DatabaseHelper.MOVIE_COLUMN_DESCRIPTION,
                DatabaseHelper.MOVIE_COLUMN_DIRECTOR,
                DatabaseHelper.MOVIE_COLUMN_ACTORS,
                DatabaseHelper.MOVIE_COLUMN_RATING,
                DatabaseHelper.MOVIE_COLUMN_YEAR,
                DatabaseHelper.MOVIE_COLUMN_GENRE_ID,
                DatabaseHelper.MOVIE_COLUMN_GENRE,
                DatabaseHelper.MOVIE_COLUMN_POSTER
        };

        return database.query(
                DatabaseHelper.MOVIE_TABLE,
                movieColumns,
                null,
                null,
                null,
                null,
                null
        );
    }

    public List<Movie> getMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if (cursor.moveToFirst()) {
            do {
                movies.add(getMovieByCursor(cursor));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return movies;
    }

    public long getCount() {
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.MOVIE_TABLE);
    }

    public Movie getMovieById(long id) {
        Movie movie = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", DatabaseHelper.MOVIE_TABLE, DatabaseHelper.MOVIE_COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            movie = getMovieByCursor(cursor);
        }
        cursor.close();

        return movie;
    }

    public Movie getMovieByTitle(String title) {
        Movie movie = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", DatabaseHelper.MOVIE_TABLE, DatabaseHelper.MOVIE_COLUMN_TITLE);
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(title)});
        if (cursor.moveToFirst()) {
            movie = getMovieByCursor(cursor);
        }
        cursor.close();

        return movie;
    }

    private Movie getMovieByCursor(Cursor cursor) {
        return Movie.builder()
                .id(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.MOVIE_COLUMN_ID)))
                .title(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOVIE_COLUMN_TITLE)))
                .description(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOVIE_COLUMN_DESCRIPTION)))
                .director(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOVIE_COLUMN_DIRECTOR)))
                .actors(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOVIE_COLUMN_ACTORS)))
                .rating(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.MOVIE_COLUMN_RATING)))
                .year(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MOVIE_COLUMN_YEAR)))
                .genreId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.MOVIE_COLUMN_GENRE_ID)))
                .genre(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOVIE_COLUMN_GENRE)))
                .poster(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOVIE_COLUMN_POSTER)))
                .build();
    }

    public long add(Movie movie) {
        return database.insert(DatabaseHelper.MOVIE_TABLE, null, getMovieContentValues(movie));
    }

    public long deleteById(long id) {
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};

        return database.delete(DatabaseHelper.MOVIE_TABLE, whereClause, whereArgs);
    }

    public long update(Movie movie) {
        String whereClause = DatabaseHelper.MOVIE_COLUMN_ID + "=" + String.valueOf(movie.getId());

        return database.update(DatabaseHelper.MOVIE_TABLE, getMovieContentValues(movie), whereClause, null);
    }

    private ContentValues getMovieContentValues(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.MOVIE_COLUMN_ID, movie.getId());
        cv.put(DatabaseHelper.MOVIE_COLUMN_TITLE, movie.getTitle());
        cv.put(DatabaseHelper.MOVIE_COLUMN_DESCRIPTION, movie.getDescription());
        cv.put(DatabaseHelper.MOVIE_COLUMN_DIRECTOR, movie.getDirector());
        cv.put(DatabaseHelper.MOVIE_COLUMN_ACTORS, movie.getActors());
        cv.put(DatabaseHelper.MOVIE_COLUMN_RATING, movie.getRating());
        cv.put(DatabaseHelper.MOVIE_COLUMN_YEAR, movie.getYear());
        cv.put(DatabaseHelper.MOVIE_COLUMN_GENRE_ID, movie.getGenreId());
        cv.put(DatabaseHelper.MOVIE_COLUMN_GENRE, movie.getGenre());
        cv.put(DatabaseHelper.MOVIE_COLUMN_POSTER, movie.getPoster());

        return cv;
    }
}
