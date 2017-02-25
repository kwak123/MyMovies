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

    /** Binds data into the repository, then compares it against the database to properly set those
     * marked as favorite.
     *
     * @param movieClasses HashMap to bind Lists from MovieQuery
     */
    void bindMovieClasses(HashMap<String, List<MovieClass>> movieClasses);

    /**
     *  Refreshes the data currently bound.
     */
    void refreshMovies();

    /** Compares the MovieClasses and the Favorites database, applying favorited tags to those in
     * both
     *
     * @param movieClasses List of MovieClass used to
     * @return List of MovieClasses tagged with favorites
     */
    List<MovieClass> addFavoriteStatus(List<MovieClass> movieClasses);

    /** Refreshes the Favorites Movies list
     */
    void updateFavoriteMovies();

    /** Adds a MovieClass to the database when favorited
     *
     * @param movieClass passed into database
     */
    void addToDatabase(MovieClass movieClass);

    /** Deletes a MovieClass from the database when unfavorited
     *
     * @param movieClass removed from database
     */
    void deleteFromDatabase(MovieClass movieClass);

    /** Clear the current favorites list
     *
     */
    void clearDatabase();

    /** Fetch the popular movies list
     *
     * @return List of top 20 popular movies.
     */
    List<MovieClass> getPopularMovies();

    /** Fetch the top rated movies list
     *
     * @return List of 20 highest-rated movies
     */
    List<MovieClass> getRatingMovies();

    /** Fetch the user's favorite movies
     *
     * @return List of the user's current favorited movies.
     */
    List<MovieClass> getFavoriteMovies();

    /** Returns the MovieClass associated with the list
     *
     * @param type specifies which list to get MovieClass from
     * @param position specifies where in list MovieClass is located
     * @return MovieClass as specified
     */
    MovieClass getMovieClass(int type, int position);

}