package com.retroquack.kwak123.mymovies.data;

import android.provider.BaseColumns;

/**
 * Database contract for saving user's favorite movies
 */

public final class MovieContract {

    // Private constructor
    private MovieContract() {}

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_BACKDROP_URL = "backdrop_url";
        public static final String COLUMN_TITLE = "movie_title";
        public static final String COLUMN_RATING = "moving_rating";
        public static final String COLUMN_POPULAR = "movie_popular";
        public static final String COLUMN_RELEASE = "movie_release";
        public static final String OOLUMN_OVERVIEW = "movie_overview";
        public static final String COLUMN_ID = "movie_id";

    }

}