package com.retroquack.kwak123.mymovies.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.retroquack.kwak123.mymovies.data.provider.MovieContract.MovieEntry;

/**
 * This is modeled off the Udacity WeatherDbHelper found in their example SunshineMaster app
 *
 * @see <a href="https://github.com/udacity/Sunshine-Version-2">Udacity Sunshine App</a>
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE = ("CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry.COLUMN_KEY + " TEXT PRIMARY KEY, " +
                MovieEntry.COLUMN_POSTER_KEY + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_BACKDROP_KEY + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POPULAR + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL" + ");");

        db.execSQL(SQL_CREATE_FAVORITES_TABLE);

    }

    // Alter as needed for future updates.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }

}
