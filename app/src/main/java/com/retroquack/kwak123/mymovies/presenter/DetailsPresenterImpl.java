package com.retroquack.kwak123.mymovies.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.model.DetailClass;

/**
 * Created by kwak123 on 2/5/2017.
 * 2/11: Provides support to view for how to handle click events
 */

public class DetailsPresenterImpl implements DetailsPresenter {

    private Activity mActivity;

    public DetailsPresenterImpl(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onTrailerSelected(DetailClass trailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerUrl()));
        try {
            mActivity.startActivity(intent);
        } catch (Exception ex){
            Toast.makeText(mActivity, R.string.fail_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReviewSelected(DetailClass review) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getReviewSummaryUrl()));
        try {
            mActivity.startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(mActivity, R.string.fail_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void noTrailerAvailable() {
        Toast.makeText(mActivity, R.string.no_trailer, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void noReviewAvailable() {
        Toast.makeText(mActivity, R.string.no_review, Toast.LENGTH_SHORT).show();
    }
}
