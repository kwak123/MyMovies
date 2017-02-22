package com.retroquack.kwak123.mymovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This is modeled off the Udacity WeatherContract found in their example SunshineMaster app.
 * Database contract for saving user's favorite movies.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.retroquack.kwak123.mymovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE = "favorite";

    // Inner class to define tablet contents
    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;

        // Table name
        public static final String TABLE_NAME = "favorites";

        // Table contents
        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_BACKDROP_URL = "backdrop_url";
        public static final String COLUMN_TITLE = "movie_title";
        public static final String COLUMN_RATING = "movie_rating";
        public static final String COLUMN_POPULAR = "movie_popular";
        public static final String COLUMN_RELEASE = "movie_release";
        public static final String COLUMN_OVERVIEW = "movie_overview";

    }

}