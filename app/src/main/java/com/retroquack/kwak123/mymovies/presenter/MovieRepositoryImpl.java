package com.retroquack.kwak123.mymovies.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.retroquack.kwak123.mymovies.data.MovieContract.MovieEntry;
import com.retroquack.kwak123.mymovies.data.MovieDbHelper;
import com.retroquack.kwak123.mymovies.loaders.MovieQuery;
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

    public static final int TYPE_POPULAR = 0;
    public static final int TYPE_RATING = 1;
    public static final int TYPE_FAVORITE = 2;

    private static final int COL_ID = 0;
    private static final int COL_POSTER_URL = 1;
    private static final int COL_BACKDROP_URL = 2;
    private static final int COL_TITLE = 3;
    private static final int COL_RATING = 4;
    private static final int COL_POPULAR = 5;
    private static final int COL_RELEASE = 6;
    private static final int COL_OVERVIEW = 7;

    private MovieRepositoryImpl() {}

    public static MovieRepositoryImpl getInstance() {
        if (moviePresenterImpl == null) {
            moviePresenterImpl = new MovieRepositoryImpl();
        }
        return moviePresenterImpl;
    }

    // Data binding and associated helper methods
    @Override
    public void bindMovieClasses(HashMap<String, List<MovieClass>> movieClasses, Context context) {
        mPopularMovies = addFavoriteStatus(movieClasses.get(MovieQuery.GROUP_POPULAR), context);
        mRatingMovies = addFavoriteStatus(movieClasses.get(MovieQuery.GROUP_RATING), context);

        updateFavoriteMovies(context);
        mFavoriteMovies = getFavoriteMovies();
    }

    @Override
    public void refreshMovies(Context context) {
        mPopularMovies = addFavoriteStatus(mPopularMovies, context);
        mRatingMovies = addFavoriteStatus(mRatingMovies, context);

        updateFavoriteMovies(context);
        mFavoriteMovies = getFavoriteMovies();
    }

    @Override
    public List<MovieClass> addFavoriteStatus(List<MovieClass> movieClasses, Context context) {
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {MovieEntry.COLUMN_ID};
        Cursor cursor = db.query(MovieEntry.TABLE_NAME, columns, null, null, null, null, null);

        if (movieClasses == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            List<MovieClass> moviesFavAssigned = new ArrayList<>();
            List<String> idList = new ArrayList<>();

            do {
                idList.add(cursor.getString(0));
            } while (cursor.moveToNext());

            for (int i = 0; i < movieClasses.size(); i++) {
                MovieClass movieClass = movieClasses.get(i);

                for (String string : idList) {
                    if (movieClass.getId().equals(string)) {
                        movieClass.setFavorited(true);
                    }
                }
                moviesFavAssigned.add(movieClass);
            }

            cursor.close();
            db.close();
            return moviesFavAssigned;
        }

        cursor.close();
        db.close();
        return movieClasses;
    }

    @Override
    public void updateFavoriteMovies(Context context) {
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + MovieEntry.TABLE_NAME, null);
        Log.v(LOG_TAG, "There are " + cursor.getCount() + " rows");

        // Better make sure it's been instantiated!
        if (mFavoriteMovies == null) {
            mFavoriteMovies = new ArrayList<>();
        }

        mFavoriteMovies.clear();

        if (cursor.moveToFirst()) {

            do {
                String id = cursor.getString(COL_ID);
                Log.v(LOG_TAG, id);
                String posterUrl = cursor.getString(COL_POSTER_URL);
                String backdropUrl = cursor.getString(COL_BACKDROP_URL);
                String title = cursor.getString(COL_TITLE);
                String rating = cursor.getString(COL_RATING);
                String popular = cursor.getString(COL_POPULAR);
                String release = cursor.getString(COL_RELEASE);
                String overview = cursor.getString(COL_OVERVIEW);

                MovieClass movieClass = new MovieClass(id, posterUrl, backdropUrl, title, rating,
                        popular, release, overview);
                movieClass.setFavorited(true);
                mFavoriteMovies.add(movieClass);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    // Database methods
    @Override
    public void addToDatabase(Context context, MovieClass movieClass) {
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_ID, movieClass.getId());
        values.put(MovieEntry.COLUMN_POSTER_URL, movieClass.getPosterKey());
        values.put(MovieEntry.COLUMN_BACKDROP_URL, movieClass.getBackdropKey());
        values.put(MovieEntry.COLUMN_TITLE, movieClass.getMovieTitle());
        values.put(MovieEntry.COLUMN_RATING, movieClass.getRating());
        values.put(MovieEntry.COLUMN_POPULAR, movieClass.getPopular());
        values.put(MovieEntry.COLUMN_RELEASE, movieClass.getRelease());
        values.put(MovieEntry.COLUMN_OVERVIEW, movieClass.getOverview());

        long newRowId = db.insert(MovieEntry.TABLE_NAME, null, values);
        Log.v(LOG_TAG, "Entry inserted into line " + newRowId);
        db.close();
    }

    @Override
    public void deleteFromDatabase(Context context, MovieClass movieClass) {
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = MovieEntry.COLUMN_ID + "=?";
        String[] selectionArgs = {movieClass.getId()};

        int deletedRowId = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
        Log.v(LOG_TAG, "Entry deleted from line " + deletedRowId);
        db.close();

        mPopularMovies = addFavoriteStatus(mPopularMovies, context);
        mRatingMovies = addFavoriteStatus(mRatingMovies, context);
    }

    @Override
    public void deleteDatabase(Context context) {
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.close();
        context.deleteDatabase(dbHelper.getDatabaseName());
        refreshMovies(context);
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
