package xyz.michaelobi.popcorn.data.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import xyz.michaelobi.popcorn.data.Movie;

/**
 * Popcorn
 * Michael Obi
 * 16 05 2017 11:45 PM
 */

public class DbMovieRepository implements MovieRepository {

    private final Context context;

    public DbMovieRepository(Context context) {
        this.context = context;
    }

    @Override
    public Movie findByMovieId(int movieId) {
        Cursor cursor = context.getContentResolver().query(MovieContract.CONTENT_URI, null,
                MovieContract.MovieColumns.COLUMN_MOVIE_ID + "=" + movieId, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return getMovie(cursor);
        }
        return null;
    }

    @Override
    public boolean deleteMovie(int movieId) {
        int deletedRows = context.getContentResolver().delete(MovieContract.CONTENT_URI,
                MovieContract.MovieColumns.COLUMN_MOVIE_ID + "=" + movieId, null);
        return deletedRows > 0;
    }


    @Override
    public List<Movie> getAll() {
        Cursor cursor = context.getContentResolver().query(MovieContract.CONTENT_URI, null, null, null, null);

        List<Movie> movies = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Movie movie = getMovie(cursor);
                movies.add(movie);
            }
        }
        return movies;
    }

    @Override
    public boolean addMovie(Movie movie) {
        ContentValues contentValues = getContentValues(movie);
        Uri newRecord = context.getContentResolver().insert(MovieContract.CONTENT_URI, contentValues);
        return newRecord != null;
    }

    @NonNull
    private Movie getMovie(Cursor cursor) {
        Movie movie = new Movie();
        movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieColumns.COLUMN_MOVIE_ID)));
        movie.setPosterUrl(cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.COLUMN_POSTER_PATH)));
        movie.setBackdropUrl(cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.COLUMN_BACKDROP_PATH)));
        movie.setFavorite(true);
        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.COLUMN_OVERVIEW)));
        movie.setRating(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieColumns.COLUMN_VOTE_RATING)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.COLUMN_TITLE)));
        movie.setReleaseYear(cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.COLUMN_RELEASE_YEAR)));
        return movie;
    }

    @NonNull
    private ContentValues getContentValues(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieColumns.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieColumns.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieColumns.COLUMN_BACKDROP_PATH, movie.getBackdropUrl());
        contentValues.put(MovieContract.MovieColumns.COLUMN_POSTER_PATH, movie.getPosterUrl());
        contentValues.put(MovieContract.MovieColumns.COLUMN_VOTE_RATING, movie.getRating());
        contentValues.put(MovieContract.MovieColumns.COLUMN_RELEASE_YEAR, movie.getReleaseYear());
        contentValues.put(MovieContract.MovieColumns.COLUMN_TITLE, movie.getTitle());
        return contentValues;
    }
}
