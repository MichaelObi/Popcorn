package xyz.michaelobi.popcorn.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static xyz.michaelobi.popcorn.data.provider.MovieContract.MovieColumns;
import static xyz.michaelobi.popcorn.data.provider.MovieContract.TABLE_NAME;

/**
 * Popcorn
 * Michael Obi
 * 16 05 2017 10:26 PM
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                MovieColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieColumns.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE, " +
                MovieColumns.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieColumns.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieColumns.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieColumns.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieColumns.COLUMN_RELEASE_YEAR + " TEXT NOT NULL, " +
                MovieColumns.COLUMN_VOTE_RATING + " REAL NOT NULL" +
                ");";

        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
