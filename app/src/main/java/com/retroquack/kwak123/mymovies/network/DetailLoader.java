package com.retroquack.kwak123.mymovies.network;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.retroquack.kwak123.mymovies.objects.DetailClass;

import java.util.List;

/**
 * Created by kwak123 on 12/17/2016.
 */

public class DetailLoader extends AsyncTaskLoader<List<DetailClass>> {

    private static final String LOG_TAG = DetailLoader.class.getSimpleName();

    private String mMovieId;
    private List<DetailClass> mData;
    private int mLoaderType;

    /**
     * This is the Loader hook, TODO: Finish this doucmentation
     *
     * @param context
     * @param movieId
     * @param loaderType use the static ints in DetailQuery to properly identify what you wish to load
     */
    public DetailLoader(Context context, String movieId, int loaderType) {
        super(context);
        mMovieId = movieId;
        mLoaderType = loaderType;
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
    public List<DetailClass> loadInBackground() {
        if (mMovieId.isEmpty() || mMovieId == null) {
            return null;
        }

        mData = DetailQuery.getDetailData(mMovieId, mLoaderType);
        return mData;
    }

    // Passes back data to the loader if it already exists, so an additional network call is not made
    @Override
    public void deliverResult(List<DetailClass> data) {
        if (isReset()) {
            onReleaseResources(data);
            return;
        }

        List<DetailClass> oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData!= null && oldData != data) {
            onReleaseResources(oldData);
        }
    }

    private void onReleaseResources(List<DetailClass> data) {}
}
