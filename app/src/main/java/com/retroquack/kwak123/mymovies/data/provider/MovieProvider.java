package com.retroquack.kwak123.mymovies.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * TODO: Consider implementing update? <br/> <br/>
 *
 * This is modeled off the WeatherProvider in the Udacity Sunshine app
 *
 * @see <a href="https://github.com/udacity/Sunshine-Version-2">Udacity Sunshine App</a>
 */

public final class MovieProvider extends ContentProvider {

    private static final String LOG_TAG = MovieProvider.class.getSimpleName();

    private static final UriMatcher mUriMatcher = buildUriMatcher();

    private static final int FAVORITES_ALL = 101;
    private static final int FAVORITES_KEY = 102;
    private static final int DELETE = 200;

    private MovieDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case FAVORITES_ALL:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case FAVORITES_KEY:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Failed to match uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch(mUriMatcher.match(uri)) {
            case FAVORITES_ALL:
                retCursor = getFavorites();
                break;

            case FAVORITES_KEY:
                retCursor = getFavoritesWithKey();
                break;

            default:
                throw new UnsupportedOperationException("Failed to match uri: " + uri);
        }

        return retCursor;
    }

    // Favorites should not be updated?

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        Uri retUri;

        switch (match) {
            case FAVORITES_ALL:
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);

                if (_id != -1) {
                    retUri = MovieContract.MovieEntry.buildFavoritesUri(_id);
                    Log.v(LOG_TAG, "Entry inserted into row " + _id);
                } else {
                    throw new android.database.SQLException("Unknown uri: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Failed to match uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowDeleted;

        switch (match) {
            case FAVORITES_ALL:
                rowDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                Log.v(LOG_TAG, "Deleted row " + rowDeleted);
                break;
            case DELETE:
                rowDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, null, null);
                Log.v(LOG_TAG, "Deleted " + rowDeleted + " rows");
                break;
            default:
                throw new UnsupportedOperationException("Failed to match uri: " + uri);
        }

        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    // Cursors
    private Cursor getFavorites() {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + MovieContract.MovieEntry.TABLE_NAME,
                null);
    }

    private Cursor getFavoritesWithKey() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {MovieContract.MovieEntry.COLUMN_KEY};
        return db.query(MovieContract.MovieEntry.TABLE_NAME, columns,
                null, null, null, null, null);
    }

    // UriMatcher
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,
                MovieContract.PATH_FAVORITE,
                FAVORITES_ALL);

        matcher.addURI(authority,
                MovieContract.PATH_FAVORITE + "/" + MovieContract.PATH_KEY,
                FAVORITES_KEY);

        matcher.addURI(authority,
                MovieContract.PATH_FAVORITE + "/" + MovieContract.PATH_DELETE,
                DELETE);

        return matcher;
    }
}
