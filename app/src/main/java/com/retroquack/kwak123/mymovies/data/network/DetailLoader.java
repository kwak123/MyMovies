package com.retroquack.kwak123.mymovies.data.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.retroquack.kwak123.mymovies.MyMoviesApp;
import com.retroquack.kwak123.mymovies.model.DetailClass;

import java.util.HashMap;
import java.util.List;

/**
 * Udacity networking course useful for creation. <br/>
 *
 * @see <a href="https://github.com/udacity/ud843-QuakeReport/tree/lesson-three">Network Course</a>
 */

public class DetailLoader extends AsyncTaskLoader<HashMap<String, List<DetailClass>>> {

    private static final String LOG_TAG = DetailLoader.class.getSimpleName();

    private String mMovieId;
    private HashMap<String, List<DetailClass>> mData;

    /**
     * This is the Loader hook, TODO: Finish this documentation
     *
     * @param context TBD
     * @param movieId TBD
     */
    public DetailLoader(Context context, String movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
            //Log.v(LOG_TAG, "Loading ol    d data");
        } else {
            forceLoad();
        }
    }

    @Override
    public HashMap<String, List<DetailClass>> loadInBackground() {
        mData = DetailQuery.getDetailData(mMovieId);
        Log.v(LOG_TAG, "Loading hashmap now");
        if (mData.isEmpty()) {
            Log.v(LOG_TAG, "hashmap is null?");
        }
        return mData;
    }

    // Passes back data to the loader if it already exists, so an additional network call is not made
    @Override
    public void deliverResult(HashMap<String, List<DetailClass>> data) {
        if (isReset()) {
            onReleaseResources(data);
            return;
        }

        HashMap<String, List<DetailClass>> oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData!= null && oldData != data) {
            onReleaseResources(oldData);
        }
    }

    private void onReleaseResources(HashMap<String, List<DetailClass>> data) {}
}
