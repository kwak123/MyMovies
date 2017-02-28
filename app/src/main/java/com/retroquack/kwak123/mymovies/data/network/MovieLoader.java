package com.retroquack.kwak123.mymovies.data.network;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.retroquack.kwak123.mymovies.model.MovieClass;

import java.util.HashMap;
import java.util.List;

/**
 * Udacity networking course useful for creation. <br/>
 *
 * @see <a href="https://github.com/udacity/ud843-QuakeReport/tree/lesson-three">Network Course</a>
 */

public class MovieLoader extends AsyncTaskLoader<HashMap<String, List<MovieClass>>> {

    private static final String LOG_TAG = MovieLoader.class.getSimpleName();

    private HashMap<String, List<MovieClass>> mData;

    public MovieLoader(Context context) {
        super(context);
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
        mData = MovieQuery.getData();
        return mData;
    }

    // Passes back data to the loader if it already exists
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
