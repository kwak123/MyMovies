package com.retroquack.kwak123.mymovies.network;

import android.text.TextUtils;
import android.util.Log;

import com.retroquack.kwak123.mymovies.objects.DetailClass;

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

import static com.retroquack.kwak123.mymovies.objects.DetailClass.TYPE_REVIEW;
import static com.retroquack.kwak123.mymovies.objects.DetailClass.TYPE_TRAILER;

/**
 * Created by kwak123 on 12/13/2016.
 *
 * Runs the query on a specific movie based on it's ID.
 *
 * I am NOT saving the information this query gets, the user would require internet connectivity for
 * most effective use of this function, the other details information would be saved as it is already
 * attached to the Movie Object.
 *
 * Log statements that are commented out were used during debugging. I left them to save the time of
 * rewriting then, should I need them again
 *
 * This is also practice with using OkHTTP
 *
 * TODO: What if the trailer query and review query are legitimate but return no results? e.g. movie_id 550
 */

public class DetailQuery {

    private static final String LOG_TAG = DetailQuery.class.getSimpleName();

    private DetailQuery(){}

    /**
     *
     *
     * @param movieId
     * @param type tells what type of list to return, check DetailClass for types.
     * @return
     */

    public static List<DetailClass> getDetailData(String movieId, int type) {

        List<DetailClass> detailClasses;

        if (type == TYPE_TRAILER) {
            String trailerResponse = httpRequest(movieId, TYPE_TRAILER);
            detailClasses = parseTrailerJson(trailerResponse);
        } else if (type == TYPE_REVIEW) {
            String reviewResponse = httpRequest(movieId, TYPE_REVIEW);
            detailClasses = parseReviewJson(reviewResponse);
        } else {
            detailClasses = null;
        }

        return detailClasses;

    }

    private static String httpRequest(String movieId, int type) {
        URL requestUrl;
        OkHttpClient httpClient = new OkHttpClient();
        String requestResponse = "";

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
            return requestResponse;
        }

        Log.v(LOG_TAG, requestUrl.toString());

        try {
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .build();

            Response response = httpClient.newCall(request).execute();
            requestResponse = response.body().string();

            // Log.v(LOG_TAG, requestResponse);
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Error handling http request? : ", ex);
        }

        if (!TextUtils.isEmpty(requestResponse)) {
            Log.v(LOG_TAG, "Response success!");
        }

        return requestResponse;
    }

    private static List<DetailClass> parseTrailerJson(String jsonResponseString) {

        List<DetailClass> trailerClasses = new ArrayList<>();

        if (TextUtils.isEmpty(jsonResponseString)) {
            return trailerClasses;
        }

        // Log.v(LOG_TAG, "Parsing trailer JSON now");

        try {
            JSONObject jsonRootObject = new JSONObject(jsonResponseString);
            JSONArray jsonTrailerArray = jsonRootObject.getJSONArray("results");

            // Log.v(LOG_TAG, "Iterating through JSON array");

            for (int i = 0; i < jsonTrailerArray.length(); i++) {

                JSONObject jsonTrailerObject = jsonTrailerArray.getJSONObject(i);

                // Log.v(LOG_TAG, jsonTrailerObject.toString());

                String trailerKey = jsonTrailerObject.getString("key");
                String trailerTitle = jsonTrailerObject.getString("name");

                URL trailerUrl = UrlTool.buildTrailerYoutube(trailerKey);

                // Log.v(LOG_TAG, trailerUrl.toString());

                trailerClasses.add(new DetailClass(trailerTitle, trailerUrl));
            }

        } catch (JSONException ex) {
            Log.e(LOG_TAG, "Error parsing JSON object");
        }

        return trailerClasses;
    }

    private static List<DetailClass> parseReviewJson(String jsonResponseString) {

        List<DetailClass> reviewClasses = new ArrayList<>();

        if (TextUtils.isEmpty(jsonResponseString)) {
            return reviewClasses;
        }

        Log.v(LOG_TAG, "Parsing review JSON now...");

        try {
            JSONObject jsonRootObject = new JSONObject(jsonResponseString);
            JSONArray jsonReviewArray = jsonRootObject.getJSONArray("results");

            for (int i = 0; i < jsonReviewArray.length(); i++) {
                JSONObject jsonReviewObject = jsonReviewArray.getJSONObject(i);

                Log.v(LOG_TAG, jsonReviewObject.toString());

                String reviewAuthor = jsonReviewObject.getString("author");
                String reviewSummary = jsonReviewObject.getString("content");
                String reviewUrl = jsonReviewObject.getString("url");

                Log.v(LOG_TAG, reviewUrl);

                reviewClasses.add(new DetailClass(reviewAuthor, reviewSummary, reviewUrl));
            }
        } catch (JSONException ex) {
            Log.e(LOG_TAG, "Error parsing JSON object?");
        }

        return reviewClasses;
    }

}
