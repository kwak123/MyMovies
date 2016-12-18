package com.retroquack.kwak123.mymovies.data;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.retroquack.kwak123.mymovies.objects.MovieClass;

import java.util.List;

/**
 * Created by kwak123 on 10/18/2016.
 */

/**
 * The AsyncTaskLoader concepts were taken from the Android network course offered by Udacity.
 * I chose this over the AsyncTask background thread method used in the lessons for its advantages,
 * particularly in relation to the lifecycle of activities/fragments. As one would expect the user
 * to tap into a new detail fragment, this could help reduce risk of memory leaks if the user were to
 * enter a fragment before the AsyncTask finished its load.
 */

public class MovieLoader extends AsyncTaskLoader<List<MovieClass>> {

    private static final String LOG_TAG = MovieLoader.class.getSimpleName();

    private String mUrl;
    private List<MovieClass> mData;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
            //Log.v(LOG_TAG, "Loading old data");
        } else {
            forceLoad();
        }
    }

    @Override
    public List<MovieClass> loadInBackground() {
        if (mUrl.isEmpty() || mUrl == null) {
            return null;
        }

        mData = MovieQuery.getMovieData(mUrl);
        return mData;
    }

    // Passes back data to the loader if it already exists, so an additional network call is not made
    @Override
    public void deliverResult(List<MovieClass> data) {
        if (isReset()) {
            onReleaseResources(data);
            return;
        }

        List<MovieClass> oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData!= null && oldData != data) {
            onReleaseResources(oldData);
        }
    }

    private void onReleaseResources(List<MovieClass> data) {}
}
