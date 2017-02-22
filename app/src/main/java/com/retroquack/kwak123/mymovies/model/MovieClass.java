package com.retroquack.kwak123.mymovies.model;

/**
 * Custom MovieClass that holds the data I want from the JSON object.
 * I wanted to store everything as Strings just to keep the data simpler
 *
 * Don't need to be parcelable with new data structure!
 *
 * Methods rewritten for encapsulation
 */

public class MovieClass {

    private String mId;
    private String mPosterKey;
    private String mBackdropKey;
    private String mTitle;
    private String mRating;
    private String mPopular;
    private String mRelease;
    private String mOverview;
    private boolean mFavorited = false;

    // For use when sending movie class reference
    public static final String POSITION_KEY = "position";
    public static final String TYPE_KEY = "type";

    // Statics for use when specifying movie classes
    public static final int TYPE_POPULAR = 0;
    public static final int TYPE_RATING = 1;
    public static final int TYPE_FAVORITE = 2;

    public MovieClass(String id, String posterKey, String backdropKey, String title, String rating,
                      String popular, String release, String overview) {
        mId = id;
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

    public String getId() {
        return mId;
    }

    public boolean getFavorite() {
        return mFavorited;
    }

    // Only the year
    private String formatDate(String input) {
        return input.substring(0, 4);
    }

    // Make it obvious that the scale is out of 10
    private String formatRating(String input) {
        return input + "/10";
    }
}
