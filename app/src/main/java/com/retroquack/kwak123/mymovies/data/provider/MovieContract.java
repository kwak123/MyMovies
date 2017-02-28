package com.retroquack.kwak123.mymovies.data.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Database contract for saving user's favorite movies. <br/>
 * Modeled off of Udacity's sunshine app.
 *
 * @see <a href="https://github.com/udacity/Sunshine-Version-2">Udacity Sunshine App</a>
 */

public class MovieContract {

    private static final String LOG_TAG = MovieContract.class.getSimpleName();

    static final String CONTENT_AUTHORITY = "com.retroquack.kwak123.mymovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_FAVORITE = "favorite";
    static final String PATH_KEY = "key";
    static final String PATH_DELETE = "delete";


    // Inner class to define tablet contents
    public static class MovieEntry implements BaseColumns {

        /**
         *  Uri for entire database
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                        CONTENT_AUTHORITY + "/" +
                        PATH_FAVORITE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                        CONTENT_AUTHORITY + "/" +
                        PATH_FAVORITE;

        static Uri buildFavoritesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavoritesKeyUri() {
            return CONTENT_URI.buildUpon().appendPath(PATH_KEY).build();
        }

        public static Uri buildFavoritesClearUri() {
            return CONTENT_URI.buildUpon().appendPath(PATH_DELETE).build();
        }

        // Table name
        public static final String TABLE_NAME = "favorites";

        // Table contents
        public static final String COLUMN_KEY = "movie_key";
        public static final String COLUMN_POSTER_KEY = "poster_key";
        public static final String COLUMN_BACKDROP_KEY = "backdrop_key";
        public static final String COLUMN_TITLE = "movie_title";
        public static final String COLUMN_RATING = "movie_rating";
        public static final String COLUMN_POPULAR = "movie_popular";
        public static final String COLUMN_RELEASE = "movie_release";
        public static final String COLUMN_OVERVIEW = "movie_overview";

    }

}