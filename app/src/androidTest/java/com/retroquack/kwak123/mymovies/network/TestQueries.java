package com.retroquack.kwak123.mymovies.network;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;

/**
 * Created by kwak123 on 1/9/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestQueries {

    private static final String LOG_TAG = TestQueries.class.getSimpleName();

    private static final String TEST_ID = "328111";

//    @Test
//    public void testTrailerQuery() {
//
//        // Run the query
//        List<DetailClass> detailClasses;
//        detailClasses = DetailQuery.getDetailData(TEST_ID, DetailClass.TYPE_TRAILER);
//
//        // If the response is clean, this should return not null.
//        Log.v(LOG_TAG, "Running trailers test...");
//        assertFalse(detailClasses.isEmpty());
//    }
//
//    @Test
//    public void testReviewQuery() {
//
//        List<DetailClass> detailClasses;
//        detailClasses = DetailQuery.getDetailData(TEST_ID, DetailQuery.TYPE_REVIEW);
//
//        Log.v(LOG_TAG, "Running reviews test...");
//        assertFalse(detailClasses.isEmpty());
//    }
//
//    @Test
//    public void testMovieQuery() {
//
//        List<MovieClass> movieClasses;
//        movieClasses = MovieQuery.getMovieData(UrlTool.buildPopularUrl().toString());
//
//        Log.v(LOG_TAG, "Running MovieQuery Test...");
//        assertFalse(movieClasses.isEmpty());
//    }
}
