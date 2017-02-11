package com.retroquack.kwak123.mymovies.model;

import com.retroquack.kwak123.mymovies.tools.UrlTool;

/**
 * Created by kwak123 on 12/26/2016.
 */

public class DetailClass {

    public static final int TYPE_TRAILER = 0;
    public static final int TYPE_REVIEW = 1;
    public static final String NO_TRAILER = "No trailers found!";
    public static final String NO_AUTHOR = "No reviews found!";
    public static final String NO_SUMMARY = "Try again later";

    private static final String NO_URL = "";

    private String mReviewAuthor;
    private String mReviewTruncatedSummary;
    private String mReviewSummaryUrl;
    private String mTrailerUrl;
    private String mTrailerStillUrl;
    private String mTrailerTitle;

    // Details for a trailer
    public DetailClass(String trailerTitle, String trailerKey) {
        mTrailerTitle = trailerTitle;
        mTrailerUrl = UrlTool.buildTrailerYoutube(trailerKey).toString();
        mTrailerStillUrl = UrlTool.buildTrailerStill(trailerKey).toString();
    }

    // Details for a review
    public DetailClass(String reviewAuthor, String reviewSummary, String reviewSummaryUrl) {
        mReviewAuthor = reviewAuthor;
        mReviewTruncatedSummary = reviewSummary;
        mReviewSummaryUrl = reviewSummaryUrl;
    }

    public String getReviewSummaryUrl() {
        return mReviewSummaryUrl;
    }

    public String getReviewTruncatedSummary() {
        return mReviewTruncatedSummary;
    }

    public String getReviewAuthor() {
        return mReviewAuthor;
    }

    public String getTrailerUrl() {
        return mTrailerUrl;
    }

    public String getTrailerStillUrl() {
        return mTrailerStillUrl;
    }

    public String getTrailerTitle() {
        return mTrailerTitle;
    }

    public static DetailClass noTrailerFound() {
        return new DetailClass(NO_TRAILER, NO_URL);
    }

    public static DetailClass noReviewFound() {
        return new DetailClass(NO_AUTHOR, NO_SUMMARY, NO_URL);
    }
}
