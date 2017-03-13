package com.retroquack.kwak123.mymovies.model;

/**
 * MovieClass, holds all information as Strings.
 * Parcelable reimplemented to reduce overhead
 *
 * Methods rewritten for encapsulation
 */

public class MovieClass {

    private String mKey;
    private String mPosterKey;
    private String mBackdropKey;
    private String mTitle;
    private String mRating;
    private String mPopular;
    private String mRelease;
    private String mOverview;

    // Default is false, see MovieRepositoryImpl
    private boolean mFavorited = false;

    // For use when sending movie class reference
    public static final String POSITION_KEY = "position";
    public static final String TYPE_KEY = "type";

    // Statics for use when specifying movie classes
    public static final int TYPE_POPULAR = 0;
    public static final int TYPE_RATING = 1;
    public static final int TYPE_FAVORITE = 2;

    public MovieClass(String key, String posterKey, String backdropKey, String title, String rating,
                      String popular, String release, String overview) {
        mKey = key;
        mPosterKey = posterKey;
        mBackdropKey = backdropKey;
        mTitle = title;
        mRating = formatRating(rating);
        mPopular = popular;
        mRelease = formatDate(release);
        mOverview = overview;
    }

    public void setFavorited(Boolean favorited) {
        mFavorited = favorited;
    }

    public String getPosterKey() {
        return mPosterKey;
    }

    public String getBackdropKey() {
        return mBackdropKey;
    }

    public String getMovieTitle() {
        return mTitle;
    }

    public String getRating() {
        return mRating;
    }

    public String getPopular() {
        return mPopular;
    }

    public String getRelease() {
        return mRelease;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getKey() {
        return mKey;
    }

    public boolean getFavorite() {
        return mFavorited;
    }

    // Only the year
    private String formatDate(String input) {
        return input.substring(0, 4);
    }

    // Format score to #/10
    private String formatRating(String input) {
        return input + "/10";
    }
}
