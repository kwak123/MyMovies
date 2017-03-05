package com.retroquack.kwak123.mymovies.ui.presenter;

import android.util.Log;

import com.retroquack.kwak123.mymovies.data.repository.MovieRepository;
import com.retroquack.kwak123.mymovies.data.repository.MovieRepositoryImpl;
import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.ui.views.MainView;

import java.util.HashMap;
import java.util.List;

/**
 * Blueprint for handling user events.
 *
 * Extra level of abstraction
 */

public class MainPresenterImpl implements MainPresenter{

    private static final String LOG_TAG = MainPresenterImpl.class.getSimpleName();

    private MovieRepository mMovieRepository;
    private MainView mView;

    public MainPresenterImpl(MainView mainView, MovieRepository movieRepository) {
        mView = mainView;
        mMovieRepository = movieRepository;
    }

    @Override
    public void onMoviesLoaded(HashMap<String, List<MovieClass>> data) {
        Log.v(LOG_TAG, "Binding " + data.size() + " elements");
        mMovieRepository.bindMovieClasses(data);
    }

    @Override
    public void clearDatabase() {
        mMovieRepository.clearDatabase();
        mView.refreshAdapter();
    }

    @Override
    public void onResume() {
        mMovieRepository.refreshMovies();
        mView.refreshAdapter();
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



}
