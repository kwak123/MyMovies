package com.retroquack.kwak123.mymovies.data.network;

import android.text.TextUtils;
import android.util.Log;

import com.retroquack.kwak123.mymovies.model.DetailClass;
import com.retroquack.kwak123.mymovies.tools.UrlTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.retroquack.kwak123.mymovies.model.DetailClass.TYPE_REVIEW;
import static com.retroquack.kwak123.mymovies.model.DetailClass.TYPE_TRAILER;

/**
 * Returns DetailClasses for MovieClass based on ID. <br/>
 *
 * TODO: Query started but signal lost? Saving information? <br/>
 * Warning: Data is currently not cached. <br/>
 *
 * Udacity networking course useful for creation. Reformatted to use OkHttp.
 *
 * @see <a href="https://github.com/udacity/ud843-QuakeReport/tree/lesson-three">Network Course</a>
 *
 */

public class DetailQuery {

    private static final String LOG_TAG = DetailQuery.class.getSimpleName();

    public static final String TRAILER_HEADER = "Trailers";
    public static final String REVIEW_HEADER = "Reviews";

    private static final List<String> GROUP_TITLES = assignHeaders();

    private DetailQuery(){}

    /**
     * Retrieves nd parses data from TMDB.
     *
     * @param movieId used to parse details for particular movie
     * @return HashMap&lt;String, List&lt;DetailClass&gt;&gt;
     */

    static HashMap<String, List<DetailClass>> getDetailData(String movieId) {

        List<DetailClass> trailerDetails;
        List<DetailClass> reviewDetails;

        HashMap<String, List<DetailClass>> detailHashMap = new HashMap<>();

        String trailerResponse = httpRequest(movieId, TYPE_TRAILER);
        String reviewResponse = httpRequest(movieId, TYPE_REVIEW);

        trailerDetails = parseTrailerJson(trailerResponse);
        reviewDetails = parseReviewJson(reviewResponse);

        if (!trailerDetails.isEmpty() && !reviewDetails.isEmpty()) {
            detailHashMap.put(GROUP_TITLES.get(0), trailerDetails);
            detailHashMap.put(GROUP_TITLES.get(1), reviewDetails);
        }
        Log.v(LOG_TAG, "DetailData has fired!");

        return detailHashMap;
    }

    private static List<String> assignHeaders() {

        List<String> headerList = new ArrayList<String>(){};

        headerList.add(TRAILER_HEADER);
        headerList.add(REVIEW_HEADER);

        return headerList;
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
                requestUrl = UrlTool.buildReviewUrl(movieId);
                break;

            default:
                requestUrl = null;
                break;
        }

        if (requestUrl == null) {
            return requestResponse;
        }

//        Log.v(LOG_TAG, requestUrl.toString());

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

                // Log.v(LOG_TAG, trailerUrl.toString());

                // Prevent NPE when calling .get on HashMap
                trailerClasses.add(new DetailClass(trailerTitle, trailerKey));
            }

        } catch (JSONException ex) {
            Log.e(LOG_TAG, "Error parsing JSON object");
        }

        if (trailerClasses.isEmpty()) {
            trailerClasses.add(DetailClass.noTrailerFound());
        }

        return trailerClasses;
    }

    private static List<DetailClass> parseReviewJson(String jsonResponseString) {

        List<DetailClass> reviewClasses = new ArrayList<>();

        if (TextUtils.isEmpty(jsonResponseString)) {
            return reviewClasses;
        }

//        Log.v(LOG_TAG, "Parsing review JSON now...");

        try {
            JSONObject jsonRootObject = new JSONObject(jsonResponseString);
            JSONArray jsonReviewArray = jsonRootObject.getJSONArray("results");

            for (int i = 0; i < jsonReviewArray.length(); i++) {
                JSONObject jsonReviewObject = jsonReviewArray.getJSONObject(i);

//                Log.v(LOG_TAG, jsonReviewObject.toString());

                String reviewAuthor = jsonReviewObject.getString("author");
                String reviewSummary = truncateSummary(jsonReviewObject.getString("content"));
                String reviewUrl = jsonReviewObject.getString("url");

//                Log.v(LOG_TAG, reviewUrl);

                reviewClasses.add(new DetailClass(reviewAuthor, reviewSummary, reviewUrl));
            }
        } catch (JSONException ex) {
            Log.e(LOG_TAG, "Error parsing JSON object?");
        }

        // Prevent NPE when calling .get on HashMap
        if (reviewClasses.isEmpty()) {
            reviewClasses.add(DetailClass.noReviewFound());
        }

        return reviewClasses;
    }


    private static String truncateSummary(String summary) {
        if (summary.isEmpty()) {
            return null;
        }

        if (summary.length() < 30) {
            return summary;
        }

        return summary.substring(0, 30) + "...";
    }

}
