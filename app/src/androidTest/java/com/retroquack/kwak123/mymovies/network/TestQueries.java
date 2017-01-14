package com.retroquack.kwak123.mymovies.network;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.retroquack.kwak123.mymovies.objects.DetailClass;
import com.retroquack.kwak123.mymovies.objects.MovieClass;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertFalse;

/**
 * Created by kwak123 on 1/9/2017.
 *
 * Test network calls to TMDB API and ensure proper response.
 * Failure to parse a JSON should return an empty.
 * Uncommenting the log statements in the query calls should be helpful for identifying where the
 * call went wrong, should these tests fail.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestQueries {

    private static final String LOG_TAG = TestQueries.class.getSimpleName();

    private static final String TEST_ID = "328111";

    @Test
    public void testTrailerQuery() {

        // Run the query
        List<DetailClass> detailClasses;
        detailClasses = DetailQuery.getDetailData(TEST_ID, DetailClass.TYPE_TRAILER);

        // If the response is clean, this should return not null.
        Log.v(LOG_TAG, "Running trailers test...");
        assertFalse(detailClasses.isEmpty());
    }

    @Test
    public void testReviewQuery() {

        List<DetailClass> detailClasses;
        detailClasses = DetailQuery.getDetailData(TEST_ID, DetailClass.TYPE_REVIEW);

        Log.v(LOG_TAG, "Running reviews test...");
        assertFalse(detailClasses.isEmpty());
    }

    @Test
    public void testMovieQuery() {

        List<MovieClass> movieClasses;
        movieClasses = MovieQuery.getMovieData(UrlTool.buildPopularUrl().toString());

        Log.v(LOG_TAG, "Running MovieQuery Test...");
        assertFalse(movieClasses.isEmpty());
    }
}
