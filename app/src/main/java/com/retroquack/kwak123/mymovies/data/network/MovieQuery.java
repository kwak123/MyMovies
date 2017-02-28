package com.retroquack.kwak123.mymovies.data.network;

import android.text.TextUtils;
import android.util.Log;

import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.tools.UrlTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Handles data from TMDB to make MovieClass.
 *
 * Udacity networking course useful for creation. Reformatted to use OkHttp.
 *
 * @see <a href="https://github.com/udacity/ud843-QuakeReport/tree/lesson-three">Network Course</a>
 */

public final class MovieQuery  {

    private static final String LOG_TAG = MovieQuery.class.getSimpleName();

    public static final String GROUP_POPULAR = "Popular";
    public static final String GROUP_RATING = "Top Rated";
    public static final String GROUP_FAVORITE = "Favorites";

    private static final List<String> GROUP_TITLES = assignHeaders();

    private MovieQuery() {}

    /**
     * Retrieves and parses data from TMDB.
     *
     * @return HashMap&lt;String, List&lt;MovieClass&gt;&gt;
     */

    static HashMap<String, List<MovieClass>> getData() {

        List<MovieClass> popularMovies;
        List<MovieClass> ratingMovies;

        HashMap<String, List<MovieClass>> movieHashMap = new HashMap<>();

        String popularResponse = httpRequest(MovieClass.TYPE_POPULAR);
        String ratingResponse = httpRequest(MovieClass.TYPE_RATING);

        popularMovies = parseJsonData(popularResponse);
        ratingMovies = parseJsonData(ratingResponse);

        if (!popularMovies.isEmpty() && !ratingMovies.isEmpty()) {
            movieHashMap.put(GROUP_TITLES.get(MovieClass.TYPE_POPULAR), popularMovies);
            movieHashMap.put(GROUP_TITLES.get(MovieClass.TYPE_RATING), ratingMovies);
        }
        Log.v(LOG_TAG, "MovieData has fired!");
        return movieHashMap;
    }

    // Format HashMap
    private static List<String> assignHeaders() {
        List<String> headerList = new ArrayList<>();
        headerList.add(GROUP_POPULAR);
        headerList.add(GROUP_RATING);
        headerList.add(GROUP_FAVORITE);
        return headerList;
    }

    // HTTP request and response
    private static String httpRequest(int type) {
        URL requestUrl;
        OkHttpClient httpClient = new OkHttpClient();
        String requestResponse = "";

        switch (type) {
            case MovieClass.TYPE_POPULAR: {
                requestUrl = UrlTool.buildPopularUrl();
                break;
            }

            case MovieClass.TYPE_RATING: {
                requestUrl = UrlTool.buildRatingUrl();
                break;
            }
            default:
                requestUrl = null;
                break;
        }

        if (requestUrl == null) {
            return requestResponse;
        }

        try {
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .build();

            Response response = httpClient.newCall(request).execute();
            requestResponse = response.body().string();

        } catch (IOException ex) {
            Log.e(LOG_TAG, "Error retrieving movie classes: " + ex);
        }

        if (!TextUtils.isEmpty(requestResponse)) {
            Log.v(LOG_TAG, "Response Success!");
        }
        return requestResponse;
    }

    // JSON parser
    private static List<MovieClass> parseJsonData(String jsonResponseString) {

        Log.v(LOG_TAG, jsonResponseString);
        List<MovieClass> movieClasses = new ArrayList<>();

        if (TextUtils.isEmpty(jsonResponseString)) {
            return movieClasses;
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonResponseString);
            JSONArray jsonMovieArray = jsonRootObject.getJSONArray("results");

            for (int i = 0; i < jsonMovieArray.length(); i++) {
                JSONObject jsonMovieObject = jsonMovieArray.getJSONObject(i);

                String id = jsonMovieObject.getString("id");
                String posterKey = jsonMovieObject.getString("poster_path");
                String backdropKey = jsonMovieObject.getString("backdrop_path");
                String title = jsonMovieObject.getString("title");
                String rating = formatRating(jsonMovieObject.getDouble("vote_average"));
                String popularity = formatPopularity(jsonMovieObject.getDouble("popularity"));
                String release = jsonMovieObject.getString("release_date");
                String overview = jsonMovieObject.getString("overview");

                movieClasses.add(new MovieClass(id, posterKey, backdropKey, title, rating, popularity, release, overview));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON object");
        }

        return movieClasses;
    }

    // Helper methods
    private static String formatPopularity(double pop) {
        return String.valueOf(Math.floor(pop));
    }

    private static String formatRating(double rating) {
        NumberFormat format = new DecimalFormat("0.0");
        return format.format(rating);
    }
}
