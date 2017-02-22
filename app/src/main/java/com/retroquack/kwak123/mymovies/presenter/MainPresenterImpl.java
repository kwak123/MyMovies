package com.retroquack.kwak123.mymovies.presenter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.retroquack.kwak123.mymovies.DetailsActivity;
import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.model.MovieClass;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kwak123 on 2/12/2017.
 *
 * Part of code refactor to have uniform codebase.
 */

public class MainPresenterImpl implements MainPresenter{

    private static final String LOG_TAG = MainPresenterImpl.class.getSimpleName();

    private Activity mActivity;
    private MovieRepositoryImpl mMovieRepository;

    public MainPresenterImpl(Activity activity) {
        mActivity = activity;
        mMovieRepository = MovieRepositoryImpl.getInstance();
    }

    @Override
    public void onMoviesLoaded(HashMap<String, List<MovieClass>> data) {
        Log.v(LOG_TAG, "Binding " + data.size() + " elements");
        mMovieRepository.bindMovieClasses(data, mActivity);
    }

    @Override
    public void onResume() {
        mMovieRepository.refreshMovies(mActivity);
    }

    @Override
    public List<MovieClass> onMoviesRequested(int type) {
        switch (type) {
            case MovieRepositoryImpl.TYPE_POPULAR:
                return mMovieRepository.getPopularMovies();
            case MovieRepositoryImpl.TYPE_RATING:
                return mMovieRepository.getRatingMovies();
            case MovieRepositoryImpl.TYPE_FAVORITE:
                return mMovieRepository.getFavoriteMovies();
            default:
                return null;
        }
    }

    @Override
    public void onMovieClicked(int type, int position) {
        MovieClass movieClass = mMovieRepository.getMovieClass(type, position);
        Log.v(LOG_TAG, "Clicked position at: " + position);

        if (movieClass == null) {
            Toast.makeText(mActivity, R.string.no_movie, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(mActivity, DetailsActivity.class);
        intent.putExtra(MovieClass.TYPE_KEY, type);
        intent.putExtra(MovieClass.POSITION_KEY, position);
        mActivity.startActivity(intent);
    }



}
