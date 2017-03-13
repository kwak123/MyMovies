package com.retroquack.kwak123.mymovies.data.repository;

import com.retroquack.kwak123.mymovies.model.MovieClass;

import java.util.HashMap;
import java.util.List;

/**
 * Blueprint for model
 *
 * TODO: Update documentation
 */

public interface MovieRepository {

    /**
     * Interface for invoking callback to presenter
     */
    interface onChangeListener {
        void notifyChange();
    }

    /**
     * Binds data into the repository, then compares it against the database to properly set those
     * marked as favorite.
     *
     * @param movieClasses HashMap to bind Lists from MovieQuery
     */
    void bindMovieClasses(HashMap<String, List<MovieClass>> movieClasses);

    /**
     * Refreshes the data currently bound.
     */
    void refreshMovies();

    /**
     * Fetch the desired List&lt;MovieClass&gt;
     *
     * @param type which List&lt;MovieClass&gt; is requested
     * @return above
     */
    List<MovieClass> getMovies(int type);

    /**
     * Fetch the desired movie
     * @param type which MovieClass variant
     * @param position where MovieClass is on the List&lt;MovieClass&gt; of above variant
     * @return MovieClass as defined by above.
     */
    MovieClass getMovieClass(int type, int position);

    /**
     * Check to see if movie class exists here
     */
    boolean isNull(int type, int position);

    /**
     * Adds a MovieClass to the database when favorited
     *
     * @param movieClass passed into database
     */
    void addToDatabase(MovieClass movieClass);

    /**
     * Deletes a MovieClass from the database when unfavorited
     *
     * @param movieClass removed from database
     */
    void deleteFromDatabase(MovieClass movieClass);

    /**
     * Clear the current favorites list
     */
    void clearDatabase();

    /**
     * Bind listener to repository
     *
     * @param listener presenter that is attached to a refreshing view
     */
    void setOnChangeListener(onChangeListener listener);
}
