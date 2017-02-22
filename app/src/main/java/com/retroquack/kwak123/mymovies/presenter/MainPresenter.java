package com.retroquack.kwak123.mymovies.presenter;

import com.retroquack.kwak123.mymovies.model.MovieClass;

import java.util.HashMap;
import java.util.List;

/** Blueprint for interacting with repository.
 * When implementing, be sure to pass a context into constructor to interact with MovieRepository.
 */

interface MainPresenter {

    /** Bind data to the movie repository after it is finished loading
     *
     * @param data HashMap returned by loader
     */
    void onMoviesLoaded(HashMap<String, List<MovieClass>> data);

    /** Ensure the AdapterView in case of changes made while in DetailFragment
     */
    void onResume();

    /** Returns a MovieClass List from the repository upon user selection
     *
     * @param type specifies what kind of MovieClass List to return
     * @return returns the specified MovieClass List
     */
    List<MovieClass> onMoviesRequested(int type);

    /** Opens up the correct DetailsFragment upon user selection
     *
     * @param type specifies which list MovieClass is located in
     * @param position specifies which index MovieClass is in
     */
    void onMovieClicked(int type, int position);
}
