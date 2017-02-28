package com.retroquack.kwak123.mymovies.data.repository;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.retroquack.kwak123.mymovies.data.provider.MovieContract.MovieEntry;
import com.retroquack.kwak123.mymovies.data.network.MovieQuery;
import com.retroquack.kwak123.mymovies.model.MovieClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Singleton to hold the current List<MovieClass> and control access to and from the database.
 * TODO: Refactor to allow better testing
 */

public class MovieRepositoryImpl implements MovieRepository {

    private static final String LOG_TAG = MovieRepositoryImpl.class.getSimpleName();

    private static MovieRepositoryImpl moviePresenterImpl;

    private static List<MovieClass> mPopularMovies;
    private static List<MovieClass> mRatingMovies;
    private static List<MovieClass> mFavoriteMovies;

    private ContentResolver mContentResolver;

    public static final int TYPE_POPULAR = 0;
    public static final int TYPE_RATING = 1;
    public static final int TYPE_FAVORITE = 2;

    private static final int COL_KEY = 0;
    private static final int COL_POSTER_URL = 1;
    private static final int COL_BACKDROP_URL = 2;
    private static final int COL_TITLE = 3;
    private static final int COL_RATING = 4;
    private static final int COL_POPULAR = 5;
    private static final int COL_RELEASE = 6;
    private static final int COL_OVERVIEW = 7;

    public MovieRepositoryImpl(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    // Data binding and associated helper methods
    @Override
    public void bindMovieClasses(HashMap<String, List<MovieClass>> movieClasses) {
        mPopularMovies = addFavoriteStatus(movieClasses.get(MovieQuery.GROUP_POPULAR));
        mRatingMovies = addFavoriteStatus(movieClasses.get(MovieQuery.GROUP_RATING));

        updateFavoriteMovies();
        mFavoriteMovies = getFavoriteMovies();
    }

    @Override
    public void refreshMovies() {
        mPopularMovies = addFavoriteStatus(mPopularMovies);
        mRatingMovies = addFavoriteStatus(mRatingMovies);

        updateFavoriteMovies();
        mFavoriteMovies = getFavoriteMovies();
    }

    @Override
    public List<MovieClass> addFavoriteStatus(List<MovieClass> movieClasses) {

        if (movieClasses == null) {
            return null;
        }

        Cursor cursor = mContentResolver.query(MovieEntry.buildFavoritesKeyUri(),
                null, null, null, null);

        List<MovieClass> moviesFavAssigned = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            List<String> keyList = new ArrayList<>();

            do {
                keyList.add(cursor.getString(0));
            } while (cursor.moveToNext());

            for (MovieClass movieClass : movieClasses) {
                for (String key : keyList) {
                    if (movieClass.getKey().equals(key)) {
                        movieClass.setFavorited(true);
                    }
                }
                moviesFavAssigned.add(movieClass);
            }
            cursor.close();
            return moviesFavAssigned;
        }

        if (cursor != null) {
            cursor.close();
        }

        return movieClasses;
    }

    @Override
    public void updateFavoriteMovies() {
        // Better make sure it's been instantiated
        if (mFavoriteMovies == null) {
            mFavoriteMovies = new ArrayList<>();
        }

        mFavoriteMovies.clear();

        Cursor cursor = mContentResolver.query(MovieEntry.CONTENT_URI,
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Log.v(LOG_TAG, "There are " + cursor.getCount() + " rows");

            do {
                String key = cursor.getString(COL_KEY);
                String posterUrl = cursor.getString(COL_POSTER_URL);
                String backdropUrl = cursor.getString(COL_BACKDROP_URL);
                String title = cursor.getString(COL_TITLE);
                String rating = cursor.getString(COL_RATING);
                String popular = cursor.getString(COL_POPULAR);
                String release = cursor.getString(COL_RELEASE);
                String overview = cursor.getString(COL_OVERVIEW);

                Log.v(LOG_TAG, key);

                MovieClass movieClass = new MovieClass(key, posterUrl, backdropUrl, title, rating,
                        popular, release, overview);
                movieClass.setFavorited(true);
                mFavoriteMovies.add(movieClass);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    // Database methods
    @Override
    public void addToDatabase(MovieClass movieClass) {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_KEY, movieClass.getKey());
        values.put(MovieEntry.COLUMN_POSTER_KEY, movieClass.getPosterKey());
        values.put(MovieEntry.COLUMN_BACKDROP_KEY, movieClass.getBackdropKey());
        values.put(MovieEntry.COLUMN_TITLE, movieClass.getMovieTitle());
        values.put(MovieEntry.COLUMN_RATING, movieClass.getRating());
        values.put(MovieEntry.COLUMN_POPULAR, movieClass.getPopular());
        values.put(MovieEntry.COLUMN_RELEASE, movieClass.getRelease());
        values.put(MovieEntry.COLUMN_OVERVIEW, movieClass.getOverview());

        Uri insertedUri = mContentResolver
                .insert(MovieEntry.CONTENT_URI, values);

        // No inner join
        long newRowId = ContentUris.parseId(insertedUri);
        Log.v(LOG_TAG, "Entry inserted into line " + newRowId);
    }

    @Override
    public void deleteFromDatabase(MovieClass movieClass) {
        String selection = MovieEntry.COLUMN_KEY + "=?";
        String[] selectionArgs = {movieClass.getKey()};


        int deletedRowId = mContentResolver
                .delete(MovieEntry.CONTENT_URI,
                        selection,
                        selectionArgs);
        Log.v(LOG_TAG, "Entry deleted from line " + deletedRowId);

        refreshMovies();
    }

    @Override
    public void clearDatabase() {
        int deletedRows = mContentResolver
                .delete(MovieEntry.buildFavoritesClearUri(),
                        null,
                        null);
        Log.v(LOG_TAG, "Total entries deleted: " + deletedRows);

        refreshMovies();
    }

    // Getters
    @Override
    public List<MovieClass> getPopularMovies() {
        return mPopularMovies;
    }

    @Override
    public List<MovieClass> getRatingMovies() {
        return mRatingMovies;
    }

    @Override
    public List<MovieClass> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    @Override
    public MovieClass getMovieClass(int type, int position) {
        switch (type) {
            case MovieClass.TYPE_POPULAR: {
                return mPopularMovies.get(position);
            }

            case MovieClass.TYPE_RATING: {
                return mRatingMovies.get(position);
            }

            case MovieClass.TYPE_FAVORITE: {
                return mFavoriteMovies.get(position);
            }

            default:
                return null;
        }
    }

}
