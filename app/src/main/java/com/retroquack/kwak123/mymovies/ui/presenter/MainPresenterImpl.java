package com.retroquack.kwak123.mymovies.ui.presenter;

import android.util.Log;

import com.retroquack.kwak123.mymovies.data.repository.MovieRepository;
import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.ui.views.MainView;

import java.util.HashMap;
import java.util.List;

public class MainPresenterImpl implements MainPresenter, MovieRepository.onChangeListener {

    private static final String LOG_TAG = MainPresenterImpl.class.getSimpleName();

    private MovieRepository mMovieRepository;
    private MainView mView;

    public MainPresenterImpl(MainView mainView, MovieRepository movieRepository) {
        mView = mainView;
        mMovieRepository = movieRepository;
        mMovieRepository.setOnChangeListener(this);
    }

    @Override
    public void onMoviesLoaded(HashMap<String, List<MovieClass>> data) {
        Log.v(LOG_TAG, "Binding " + data.size() + " elements");
        mMovieRepository.bindMovieClasses(data);
    }

    @Override
    public void clearDatabase() {
        mMovieRepository.clearDatabase();
    }

    @Override
    public void onResume() {
        mMovieRepository.refreshMovies();
    }

    @Override
    public List<MovieClass> onMoviesRequested(int type) {
        return mMovieRepository.getMovies(type);
    }

    @Override
    public void notifyChange() {
        mView.refreshAdapter();
    }
}
