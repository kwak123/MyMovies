package com.retroquack.kwak123.mymovies.presenter;

import com.retroquack.kwak123.mymovies.model.DetailClass;

/**
 * Created by kwak123 on 2/5/2017.
 *
 * Contract for the presenter to follow
 * Used MVP for testing purpose
 */

interface DetailsPresenter {

    void onTrailerSelected(DetailClass trailer);

    void onReviewSelected(DetailClass review);

    void noTrailerAvailable();

    void noReviewAvailable();

}
