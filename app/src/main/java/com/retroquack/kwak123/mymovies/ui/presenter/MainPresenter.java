package com.retroquack.kwak123.mymovies.ui.presenter;

import com.retroquack.kwak123.mymovies.data.repository.MovieRepository;
import com.retroquack.kwak123.mymovies.model.MovieClass;

import java.util.HashMap;
import java.util.List;

/**
 * Blueprint for handling user events.
 */

public interface MainPresenter {

    /** Bind data to the movie repository after it is finished loading
     *
     * @param data HashMap returned by loader
     */
    void onMoviesLoaded(HashMap<String, List<MovieClass>> data);

    void clearDatabase();

    /** Ensure the AdapterView in case of changes made while in DetailFragment
     */
    void onResume();

    /** Returns a MovieClass List from the repository upon user selection
     *
     * @param type specifies what kind of MovieClass List to return
     * @return returns the specified MovieClass List
     */
    List<MovieClass> onMoviesRequested(int type);
}
