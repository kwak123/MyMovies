package com.retroquack.kwak123.mymovies.presenter;

import com.retroquack.kwak123.mymovies.model.DetailClass;
import com.retroquack.kwak123.mymovies.model.MovieClass;

/** Blueprint for interacting with repository.
 * When implementing, be sure to pass a context into constructor to interact with MovieRepository.
 */

interface DetailsPresenter {

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

    /** Loads the trailer upon user selection
     *
     * @param trailer desired DetailClass
     */
    void onTrailerSelected(DetailClass trailer);

    /** Loads the review upon user selection
     *
     * @param review desired DetailClass
     */
    void onReviewSelected(DetailClass review);

    /** Failed to pull trailer data from MovieRepository
     */
    void noTrailerAvailable();

    /** Failed to pull review data from MovieRepository
     */
    void noReviewAvailable();

}
