package com.retroquack.kwak123.mymovies.ui.presenter;

import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.data.repository.MovieRepository;
import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.ui.views.DetailsView;

import static android.R.attr.id;
import static com.retroquack.kwak123.mymovies.R.string.favorite;

/**
 * Handles user events
 *
 * TODO: Abstract Loader into presenter?
 */

public class DetailsPresenterImpl implements DetailsPresenter {

    private static final String LOG_TAG = DetailsPresenterImpl.class.getSimpleName();

    private DetailsView mView;
    private MovieRepository mMovieRepository;
    private int mType;

    public DetailsPresenterImpl(DetailsView view, MovieRepository movieRepository, int type) {
        mView = view;
        mType = type;
        mMovieRepository = movieRepository;
    }

    @Override
    public MovieClass getMovieClass(int position) {
        return mMovieRepository.getMovieClass(mType, position);
    }

    @Override
    public void onFavoritesSelected(boolean isChecked, MovieClass movieClass) {
        if (!isChecked) {
            mMovieRepository.deleteFromDatabase(movieClass);
        } else {
            mMovieRepository.addToDatabase(movieClass);
        }
        mView.onFavoritesChanged(isChecked);
        mMovieRepository.refreshMovies();
    }
}
