package com.retroquack.kwak123.mymovies.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.presenter.MovieRepositoryImpl;

import java.util.HashMap;
import java.util.List;

/**
 * The AsyncTaskLoader concepts were taken from the Android network course offered by Udacity.
 * I chose this over the AsyncTask background thread method used in the lessons for its advantages,
 * particularly in relation to the lifecycle of activities/fragments. As one would expect the user
 * to tap into a new detail fragment, this could help reduce risk of memory leaks if the user were to
 * enter a fragment before the AsyncTask finished its load.
 */

public class MovieLoader extends AsyncTaskLoader<HashMap<String, List<MovieClass>>> {

    private static final String LOG_TAG = MovieLoader.class.getSimpleName();

    private HashMap<String, List<MovieClass>> mData;
    private Context mContext;

    public MovieLoader(Context context) {
        super(context);
        mContext = context;
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
    public HashMap<String, List<MovieClass>> loadInBackground() {
        mData = MovieQuery.getMovieData();
        return mData;
    }

    // Passes back data to the loader if it already exists, so an additional network call is not made
    @Override
    public void deliverResult(HashMap<String, List<MovieClass>> data) {
        if (isReset()) {
            onReleaseResources(data);
            return;
        }

        HashMap<String, List<MovieClass>> oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData!= null && oldData != data) {
            onReleaseResources(oldData);
        }
    }

    private void onReleaseResources(HashMap<String, List<MovieClass>> data) {}
}
