package com.retroquack.kwak123.mymovies.ui.presenter;

import com.retroquack.kwak123.mymovies.model.DetailClass;
import com.retroquack.kwak123.mymovies.model.MovieClass;

/**
 * Blueprint to handle user events
 */

public interface DetailsPresenter {

    /** Fetches MovieClass from the repository
     *
     * @param position which index the MovieClass is located in
     * @return the desired MovieClass
     */
    MovieClass getMovieClass(int position);

    /** Update MovieRepository if the list of Favorites has changed
     *
     * @param favorite the new favorited status of the MovieClass
     * @param movieClass the MovieClass being modified
     */
    void onFavoritesSelected(boolean favorite, MovieClass movieClass);

}
