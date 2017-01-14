package com.retroquack.kwak123.mymovies.network;

import android.text.TextUtils;
import android.util.Log;

import com.retroquack.kwak123.mymovies.objects.MovieClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwak123 on 10/18/2016.
 */


/**
 * A class meant to hold helper methods related to requesting and parsing data from TMDB.
 * I used the Udacity network course to aid with making this utility, as I found it much cleaner and
 * better thought out than what was covered in project one. Much of this code is taken near verbatim,
 * with the necessary refactoring being done here.
 */

public final class MovieQuery {

    private static final String LOG_TAG = MovieQuery.class.getSimpleName();

    // Private constructor to protect the class
    private MovieQuery() {}

    // The summary method used by other classes when attempting to retrieve data from TMDB.
    public static List<MovieClass> getMovieData(String requestUrl) {
        URL url = makeURL(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = httpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error parsing JSON", e);
        }

        List<MovieClass> movieClasses = parseJsonData(jsonResponse);

        return movieClasses;
    }

    // Makes the HTTP request and returns the response as a string
    private static String httpRequest(URL requestUrl) throws IOException {
        String jsonResponse = "";

        if (requestUrl == null) {
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error with connection, response code: " + connection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error retrieving JSON results", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    // Parses data from the JSON request, input as a string
    private static List<MovieClass> parseJsonData(String jsonResponseString) {

        List<MovieClass> movieClasses = new ArrayList<>();

        if (TextUtils.isEmpty(jsonResponseString)) {
            return movieClasses;
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonResponseString);
            JSONArray jsonMovieArray = jsonRootObject.getJSONArray("results");

            for (int i = 0; i < jsonMovieArray.length(); i++) {
                JSONObject jsonMovieObject = jsonMovieArray.getJSONObject(i);

                String posterUrl = jsonMovieObject.getString("poster_path");
                String backdropUrl = jsonMovieObject.getString("backdrop_path");
                String title = jsonMovieObject.getString("title");
                String rating = formatRating(jsonMovieObject.getDouble("vote_average"));
                String popularity = formatPopularity(jsonMovieObject.getDouble("popularity"));
                String release = jsonMovieObject.getString("release_date");
                String overview = jsonMovieObject.getString("overview");
                String id = jsonMovieObject.getString("id");

                movieClasses.add(new MovieClass(posterUrl, backdropUrl, title, rating, popularity, release, overview, id));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON object");
        }

        return movieClasses;
    }

    // Some private helper methods to keep consistent input, saving all data as Strings
    private static String formatPopularity(double pop) {
        return String.valueOf(Math.floor(pop));
    }

    private static String formatRating(double rating) {
        NumberFormat format = new DecimalFormat("0.0");
        return format.format(rating);
    }

    // Checks the URL string to make sure it is valid
    private static URL makeURL(String urlString) {
        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Unable to parse URL: " + urlString, e);
        }

        return url;
    }

    // Tool to receive input from the server and store it as a String
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }

        return builder.toString();
    }
}
