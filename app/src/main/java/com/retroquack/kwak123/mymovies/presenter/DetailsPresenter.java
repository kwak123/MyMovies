package com.retroquack.kwak123.mymovies.presenter;

import com.retroquack.kwak123.mymovies.model.DetailClass;

import java.util.List;

/**
 * Created by kwak123 on 2/5/2017.
 */

public interface DetailsPresenter {

    void onTrailerSelected(List<DetailClass> trailerList);

    void onReviewSelected(List<DetailClass> reviewsList);

}
