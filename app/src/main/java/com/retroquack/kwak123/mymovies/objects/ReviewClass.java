package com.retroquack.kwak123.mymovies.objects;

/**
 * Created by kwak123 on 12/15/2016.
 */

public class ReviewClass {

    private String mReviewAuthor;
    private String mReviewTruncatedSummary;
    private String mReviewSummaryUrl;

    public ReviewClass(String reviewAuthor, String reviewSummary, String reviewSummaryUrl) {
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
