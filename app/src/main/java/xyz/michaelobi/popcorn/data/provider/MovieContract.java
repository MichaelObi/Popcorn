package xyz.michaelobi.popcorn.data.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Popcorn
 * Michael Obi
 * 16 05 2017 10:32 PM
 */

public class MovieContract {

    public static final String PROVIDER_AUTHORITY = "xyz.michaelobi.popcorn";
    public static final String TABLE_NAME = "movies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_AUTHORITY)
            .buildUpon().appendPath(TABLE_NAME).build();

    private MovieContract() {
    }

    public static class MovieColumns implements BaseColumns {

        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_YEAR = "release_year";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_RATING = "rating";
    }
}