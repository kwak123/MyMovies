package com.retroquack.kwak123.mymovies.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.model.DetailClass;
import com.retroquack.kwak123.mymovies.model.MovieClass;

import java.util.List;

/**
 * Created by kwak123 on 2/5/2017.
 * 2/11: Provides support to view for how to handle click events
 *
 * TODO: Collate logic still in view into presenter
 */

public class DetailsPresenterImpl implements DetailsPresenter {

    private static final String LOG_TAG = DetailsPresenterImpl.class.getSimpleName();

    private Activity mActivity;
    private MovieRepositoryImpl mMovieRepository;
    private int mType;

    public DetailsPresenterImpl(Activity activity, int type) {
        mActivity = activity;
        mType = type;
        mMovieRepository = MovieRepositoryImpl.getInstance();
    }

    @Override
    public MovieClass getMovieClass(int position) {
        return MovieRepositoryImpl.getInstance().getMovieClass(mType, position);
    }

    @Override
    public void onTrailerSelected(DetailClass trailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerUrl()));
        try {
            mActivity.startActivity(intent);
        } catch (Exception ex){
            Toast.makeText(mActivity, R.string.fail_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReviewSelected(DetailClass review) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getReviewSummaryUrl()));
        try {
            mActivity.startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(mActivity, R.string.fail_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFavoritesSelected(boolean favorite, MovieClass movieClass) {

        if (favorite == movieClass.getFavorite()) {
//            Log.v(LOG_TAG, "Movie not added to db");
            return;
        }

        if (favorite && !movieClass.getFavorite()) {
            mMovieRepository.addToDatabase(mActivity, movieClass);
//            Log.v(LOG_TAG, movieClass.getPosterUrl());
        } else {
            mMovieRepository.deleteFromDatabase(mActivity, movieClass);
        }

        mMovieRepository.refreshMovies(mActivity);
    }

    @Override
    public void noTrailerAvailable() {
        Toast.makeText(mActivity, R.string.no_trailer, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void noReviewAvailable() {
        Toast.makeText(mActivity, R.string.no_review, Toast.LENGTH_SHORT).show();
    }
}
