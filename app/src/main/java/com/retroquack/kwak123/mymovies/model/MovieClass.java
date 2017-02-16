package com.retroquack.kwak123.mymovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.retroquack.kwak123.mymovies.tools.UrlTool;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Custom MovieClass that holds the data I want from the JSON object.
 * I wanted to store everything as Strings just to keep the data simpler
 */

public class MovieClass implements Parcelable {

    private String mId;
    private String mPosterUrl;
    private String mBackdropUrl;
    private String mTitle;
    private String mRating;
    private String mPopular;
    private String mRelease;
    private String mOverview;

    // For use when parceling an instance of the object
    public static final String CLASS_KEY = "movieClass";

    public MovieClass(String posterUrl, String backdropUrl, String title, String rating,
                      String popular, String release, String overview, String id) {
        mPosterUrl = UrlTool.buildImageUrl(posterUrl).toString();
        mBackdropUrl = UrlTool.buildBackdropUrl(backdropUrl).toString();
        mTitle = title;
        mRating = formatRating(rating);
        mPopular = popular;
        mRelease = formatDate(release);
        mOverview = overview;
        mId = id;
    }

    private MovieClass(Parcel in) {
        mPosterUrl = in.readString();
        mBackdropUrl = in.readString();
        mTitle = in.readString();
        mRating = in.readString();
        mPopular = in.readString();
        mRelease = in.readString();
        mOverview = in.readString();
        mId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPosterUrl);
        parcel.writeString(mBackdropUrl);
        parcel.writeString(mTitle);
        parcel.writeString(mRating);
        parcel.writeString(mPopular);
        parcel.writeString(mRelease);
        parcel.writeString(mOverview);
        parcel.writeString(mId);
    }

    public static final Parcelable.Creator<MovieClass> CREATOR = new Parcelable.Creator<MovieClass>() {
        @Override
        public MovieClass createFromParcel(Parcel parcel) {
            return new MovieClass(parcel);
        }

        @Override
        public MovieClass[] newArray(int i) {
            return new MovieClass[i];
        }
    };

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getBackdropUrl() {
        return mBackdropUrl;
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

    // Only the year
    private String formatDate(String input) {
        return input.substring(0, 4);
    }

    // Make it obvious that the scale is out of 10
    private String formatRating(String input) {
        return input + "/10";
    }
}
