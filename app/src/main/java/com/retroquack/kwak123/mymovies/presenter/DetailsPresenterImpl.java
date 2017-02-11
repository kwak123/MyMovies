package com.retroquack.kwak123.mymovies.presenter;

import com.retroquack.kwak123.mymovies.model.DetailClass;

import java.util.List;

/**
 * Created by kwak123 on 2/5/2017.
 */

public class DetailsPresenterImpl implements DetailsPresenter {

    private DetailsView mDetailsView;

    public DetailsPresenterImpl(DetailsView detailsView) {
        mDetailsView = detailsView;
    }

    @Override
    public void onTrailerSelected(List<DetailClass> trailerList) {

    }

    @Override
    public void onReviewSelected(List<DetailClass> reviewsList) {

    }
}
