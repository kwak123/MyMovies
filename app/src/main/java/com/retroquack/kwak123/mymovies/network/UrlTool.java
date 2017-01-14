package com.retroquack.kwak123.mymovies.network;

/**
 * Created by kwak123 on 10/19/2016.
 */

import android.net.Uri;
import android.util.Log;

import com.retroquack.kwak123.mymovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Holds static factory methods for building URLs throughout the app.
 * I did not add in safety features yet,
 *
 * TODO: I need to make sure I add sanity checking to this at some point
 */

public class UrlTool {
    private static final String LOG_TAG = UrlTool.class.getSimpleName();

    private static final int TYPE_POPULAR = 0;
    private static final int TYPE_RATING = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_BACKDROP = 3;
    private static final int TYPE_TRAILER = 4;
    private static final int TYPE_YOUTUBE_TRAILER = 5;
    private static final int TYPE_REVIEW = 6;

    /**
     * For types, 0 = popular, 1 = top-rated, 2 = poster, 3 = backdrop
     *
     * Note that buildPopularUrl and buildRatingUrl take null as the string input, because
     * those are generic queries
     */

    //Constructor here, marked private to protect class
    private UrlTool() {}

    public static URL buildPopularUrl() {
        return buildUrl(TYPE_POPULAR, null);
    }

    public static URL buildRatingUrl() {
        return buildUrl(TYPE_RATING, null);
    }

    public static URL buildImageUrl(String imageUrl) {
        return buildUrl(TYPE_IMAGE, imageUrl);
    }

    public static URL buildBackdropUrl(String backdropUrl) {
        return buildUrl(TYPE_BACKDROP, backdropUrl);
    }

    public static URL buildTrailerUrl(String movieID) {
        return buildUrl(TYPE_TRAILER, movieID);
    }

    public static URL buildTrailerYoutube(String movieKey) {
        return buildUrl(TYPE_YOUTUBE_TRAILER, movieKey);
    }

    public static URL buildReviewsUrl(String movieID) {
        return buildUrl(TYPE_REVIEW, movieID);
    }

    // The tool used in the static factory methods to create the URL based on input.
    private static URL buildUrl(int type, String itemSpec) {
        final String POPULAR = "popular/";
        final String RATING = "top_rated/";
        final String VIDEO = "videos";
        final String REVIEWS = "reviews";
        final String QUERY_BASE_URL= "http://api.themoviedb.org/3/movie";
        final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
        final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";
        final String YOUTUBE_URL = "http://www.youtube.com/watch";
        final String YOUTUBE_KEY = "v";
        final String API_KEY = "api_key";

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
                baseUri = Uri.parse(YOUTUBE_URL)
                        .buildUpon()
                        .appendQueryParameter(YOUTUBE_KEY, itemSpec)
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
