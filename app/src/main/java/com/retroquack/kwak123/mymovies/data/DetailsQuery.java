package com.retroquack.kwak123.mymovies.data;

import android.text.TextUtils;
import android.util.Log;

import com.retroquack.kwak123.mymovies.objects.ReviewClass;
import com.retroquack.kwak123.mymovies.objects.TrailerClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kwak123 on 12/13/2016.
 *
 * Runs the query on a specific movie based on it's ID.
 *
 * I am NOT saving the information this query gets, the user would require internet connectivity for
 * most effective use of this function, the other details information would be saved as it is already
 * attached to the Movie Object.
 *
 * This is also practice with using OkHTTP
 */

public class DetailsQuery {

    private static final String LOG_TAG = DetailsQuery.class.getSimpleName();
    private static final int TYPE_TRAILER = 0;
    private static final int TYPE_REVIEW = 1;

    private DetailsQuery(){}

    public static List<TrailerClass> getTrailerData(String movieId) {
        String jsonResponse = httpRequest(movieId, TYPE_TRAILER);

        List<TrailerClass> trailerClasses = parseTrailerJson(jsonResponse);

        return trailerClasses;
    }

    public static List<ReviewClass> getReviewData(String movieId) {
        String jsonResponse = httpRequest(movieId, TYPE_REVIEW);

        List<ReviewClass> reviewClasses = parseReviewJson(jsonResponse);

        return reviewClasses;
    }

    private static String httpRequest(String movieId, int type) {
        URL requestUrl;
        OkHttpClient httpClient = new OkHttpClient();

        switch (type) {
            case TYPE_TRAILER:
                requestUrl = UrlTool.buildTrailerUrl(movieId);
                break;

            case TYPE_REVIEW:
                requestUrl = UrlTool.buildReviewsUrl(movieId);
                break;

            default:
                requestUrl = null;
        }

        if (requestUrl == null) {
            return null;
        }

        try {
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .build();

            Response response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Error handling http request? : ", ex);
            return null;
        }
    }

    private static List<TrailerClass> parseTrailerJson(String jsonResponseString) {

        if (TextUtils.isEmpty(jsonResponseString)) {
            return null;
        }

        List<TrailerClass> trailerClasses = new ArrayList<>();

        try {
            JSONObject jsonRootObject = new JSONObject(jsonResponseString);
            JSONArray jsonTrailerArray = jsonRootObject.getJSONArray("results");

            for (int i = 0; i < jsonTrailerArray.length(); i++) {
                JSONObject jsonTrailerObject = jsonTrailerArray.getJSONObject(i);

                String trailerKey = Integer.toString(jsonTrailerObject.getInt("key"));
                String trailerTitle = jsonTrailerObject.getString("name");

                URL trailerUrl = UrlTool.buildTrailerYoutube(trailerKey);

                trailerClasses.add(new TrailerClass(trailerTitle, trailerUrl));
            }

        } catch (JSONException ex) {
            Log.e(LOG_TAG, "Error parsing JSON object");
        }

        return trailerClasses;
    }

    private static List<ReviewClass> parseReviewJson(String jsonResponseString) {

        if (TextUtils.isEmpty(jsonResponseString)) {
            return null;
        }

        List<ReviewClass> reviewClasses = new ArrayList<>();

        try {
            JSONObject jsonRootObject = new JSONObject(jsonResponseString);
            JSONArray jsonReviewArray = jsonRootObject.getJSONArray("results");

            for (int i = 0; i < jsonReviewArray.length(); i++) {
                JSONObject jsonReviewObject = jsonReviewArray.getJSONObject(i);

                String reviewAuthor = jsonReviewObject.getString("author");
                String reviewSummary = jsonReviewObject.getString("content");
                String reviewUrl = jsonReviewObject.getString("url");

                reviewClasses.add(new ReviewClass(reviewAuthor, reviewSummary, reviewUrl));
            }
        } catch (JSONException ex) {
            Log.e(LOG_TAG, "Error parsing JSON object?");
        }

        return reviewClasses;
    }

}
