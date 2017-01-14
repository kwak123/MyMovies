package com.retroquack.kwak123.mymovies.objects;

import java.net.URL;

/**
 * Created by kwak123 on 12/26/2016.
 */

public class DetailClass {

    public static final int TYPE_TRAILER = 0;
    public static final int TYPE_REVIEW = 1;

    private String mReviewAuthor;
    private String mReviewTruncatedSummary;
    private String mReviewSummaryUrl;
    private URL mTrailerUrl;
    private String mTrailerTitle;

    /**
     * This constructor returns a DetailClass specific for trailers
     */
    public DetailClass(String trailerTitle, URL trailerUrl) {
        mTrailerTitle = trailerTitle;
        mTrailerUrl = trailerUrl;
    }

    public DetailClass(String reviewAuthor, String reviewSummary, String reviewSummaryUrl) {
        mReviewAuthor = reviewAuthor;
        mReviewTruncatedSummary = truncateSummary(reviewSummary);
        mReviewSummaryUrl = reviewSummaryUrl;
    }

    public String getReviewSummaryUrl() {
        return mReviewSummaryUrl;
    }

    public String getReviewTruncatedSummary() {
        return mReviewTruncatedSummary;
    }

    public String mReviewAuthor() {
        return mReviewAuthor;
    }

    public URL getTrailerUrl() {
        return mTrailerUrl;
    }

    public String getTrailerTitle() {
        return mTrailerTitle;
    }

    private static String truncateSummary(String summary) {
        if (summary.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(summary.substring(0, 15))
                .append("... ");

        return builder.toString();
    }

}
