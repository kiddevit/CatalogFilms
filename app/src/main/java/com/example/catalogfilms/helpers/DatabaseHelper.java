package com.example.catalogfilms.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";
    private static final int SCHEMA = 1;

    public static final String MOVIE_TABLE = "movie";
    public static final String MOVIE_COLUMN_ID = "id";
    public static final String MOVIE_COLUMN_TITLE = "title";
    public static final String MOVIE_COLUMN_DESCRIPTION = "description";
    public static final String MOVIE_COLUMN_DIRECTOR = "director";
    public static final String MOVIE_COLUMN_ACTORS = "actors";
    public static final String MOVIE_COLUMN_RATING = "rating";
    public static final String MOVIE_COLUMN_YEAR = "year";
    public static final String MOVIE_COLUMN_GENRE_ID = "genreId";
    public static final String MOVIE_COLUMN_GENRE = "genre";
    public static final String MOVIE_COLUMN_POSTER = "poster";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getSqlCreateMovieTable());
    }

    private String getSqlCreateMovieTable() {
        return "CREATE TABLE " + MOVIE_TABLE + " (" +
                MOVIE_COLUMN_ID + " INTEGER," +
                MOVIE_COLUMN_TITLE + " TEXT, " +
                MOVIE_COLUMN_DESCRIPTION + " TEXT, " +
                MOVIE_COLUMN_DIRECTOR + " TEXT, " +
                MOVIE_COLUMN_ACTORS + " TEXT, " +
                MOVIE_COLUMN_RATING + " REAL, " +
                MOVIE_COLUMN_YEAR + " INTEGER, " +
                MOVIE_COLUMN_GENRE_ID + " INTEGER, " +
                MOVIE_COLUMN_GENRE + " TEXT, " +
                MOVIE_COLUMN_POSTER + " TEXT);";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(getSqlDeleteMovieTable());
        onCreate(db);
    }

    private String getSqlDeleteMovieTable() {
        return "DROP TABLE IF EXISTS " + MOVIE_TABLE;
    }
}
