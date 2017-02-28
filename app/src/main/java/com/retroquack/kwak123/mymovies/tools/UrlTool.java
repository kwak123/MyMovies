package com.retroquack.kwak123.mymovies.tools;

import android.net.Uri;
import android.util.Log;

import com.retroquack.kwak123.mymovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Holds static factory methods for building URLs throughout the app.
 * URLs are typically converted to strings to keep in line with APIs
 */

public final class UrlTool {
    private static final String LOG_TAG = UrlTool.class.getSimpleName();

    private static final int TYPE_POPULAR = 0;
    private static final int TYPE_RATING = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_BACKDROP = 3;
    private static final int TYPE_TRAILER = 4;
    private static final int TYPE_YOUTUBE_TRAILER = 5;
    private static final int TYPE_YOUTUBE_STILL = 6;
    private static final int TYPE_REVIEW = 7;

    private UrlTool() {}

    public static URL buildPopularUrl() {
        return buildUrl(TYPE_POPULAR, null);
    }

    public static URL buildRatingUrl() {
        return buildUrl(TYPE_RATING, null);
    }

    public static URL buildPosterUrl(String posterUrl) {
        return buildUrl(TYPE_IMAGE, posterUrl);
    }

    public static URL buildBackdropUrl(String backdropUrl) {
        return buildUrl(TYPE_BACKDROP, backdropUrl);
    }

    public static URL buildTrailerUrl(String trailerKey) {
        return buildUrl(TYPE_TRAILER, trailerKey);
    }

    public static URL buildTrailerYoutube(String movieKey) {
        return buildUrl(TYPE_YOUTUBE_TRAILER, movieKey);
    }

    public static URL buildTrailerStill(String movieKey) {
        return buildUrl(TYPE_YOUTUBE_STILL, movieKey);
    }

    public static URL buildReviewUrl(String movieID) {
        return buildUrl(TYPE_REVIEW, movieID);
    }

    /**
     * URL factory. Pass in int and item spec, return URL, EZPZ.
     *
     * @param type identify the requested URL
     * @param itemSpec additional input for building
     * @return a URL. Convert to String to keep in line with APIs
     */
    private static URL buildUrl(int type, String itemSpec) {
        final String POPULAR = "popular/";
        final String RATING = "top_rated/";
        final String VIDEO = "videos";
        final String REVIEWS = "reviews";
        final String QUERY_BASE_URL= "http://api.themoviedb.org/3/movie";
        final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
        final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";
        final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch";
        final String YOUTUBE_IMAGE_URL = "https://img.youtube.com/vi/";
        final String YOUTUBE_KEY = "v";
        final String API_KEY = "api_key";

        final String STILL_TYPE_DEFAULT = "default.jpg";

        Uri baseUri;
        URL url = null;

        switch (type) {
            case TYPE_POPULAR:
                baseUri = Uri.parse(QUERY_BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(POPULAR)
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                        .build();
                break;

            case TYPE_RATING:
                baseUri = Uri.parse(QUERY_BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(RATING)
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                        .build();
                break;

            case TYPE_IMAGE:
                baseUri = Uri.parse(IMAGE_BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(itemSpec)
                        .build();
                break;

            case TYPE_BACKDROP:
                baseUri = Uri.parse(BACKDROP_BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(itemSpec)
                        .build();
                break;

            case TYPE_TRAILER:
                baseUri = Uri.parse(QUERY_BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(itemSpec)
                        .appendEncodedPath(VIDEO)
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                        .build();
                break;

            case TYPE_YOUTUBE_TRAILER:
                baseUri = Uri.parse(YOUTUBE_VIDEO_URL)
                        .buildUpon()
                        .appendQueryParameter(YOUTUBE_KEY, itemSpec)
                        .build();
                break;

            case TYPE_YOUTUBE_STILL:
                baseUri = Uri.parse(YOUTUBE_IMAGE_URL)
                        .buildUpon()
                        .appendEncodedPath(itemSpec)
                        .appendEncodedPath(STILL_TYPE_DEFAULT)
                        .build();
                break;

            case TYPE_REVIEW:
                baseUri = Uri.parse(QUERY_BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(itemSpec)
                        .appendEncodedPath(REVIEWS)
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                        .build();
                break;

            default:
                return null;
        }

        if (baseUri != null) {
            try {
                url = new URL(baseUri.toString());
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error with handling URL");
            }
        }
        return url;
    }
}
