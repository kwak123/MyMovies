package com.retroquack.kwak123.mymovies.ui.presenter;

import com.retroquack.kwak123.mymovies.data.repository.MovieRepository;
import com.retroquack.kwak123.mymovies.model.MovieClass;

/**
 * Handles user events
 *
 * TODO: Abstract Loader into presenter?
 */

public class DetailsPresenterImpl implements DetailsPresenter {

    private static final String LOG_TAG = DetailsPresenterImpl.class.getSimpleName();

    private MovieRepository mMovieRepository;
    private int mType;

    public DetailsPresenterImpl(MovieRepository movieRepository, int type) {
        mType = type;
        mMovieRepository = movieRepository;
    }

    @Override
    public MovieClass getMovieClass(int position) {
        return mMovieRepository.getMovieClass(mType, position);
    }

    @Override
    public void onFavoritesSelected(boolean favorite, MovieClass movieClass) {

        if (favorite == movieClass.getFavorite()) {
//            Log.v(LOG_TAG, "Movie not added to db");
            return;
        }

        if (favorite && !movieClass.getFavorite()) {
            mMovieRepository.addToDatabase(movieClass);
//            Log.v(LOG_TAG, movieClass.getPosterUrl());
        } else {
            mMovieRepository.deleteFromDatabase(movieClass);
        }

        mMovieRepository.refreshMovies();
    }
}
