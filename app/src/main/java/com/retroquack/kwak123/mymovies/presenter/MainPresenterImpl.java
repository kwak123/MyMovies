package com.retroquack.kwak123.mymovies.presenter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.retroquack.kwak123.mymovies.DetailsActivity;
import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.model.MovieClass;

/**
 * Created by kwak123 on 2/12/2017.
 *
 * Part of code refactor to have uniform codebase.
 */

public class MainPresenterImpl implements MainPresenter{

    private Activity mActivity;

    public MainPresenterImpl(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onMovieClicked(MovieClass movie) {
        try {
            Intent intent = new Intent(mActivity, DetailsActivity.class);
            intent.putExtra(MovieClass.CLASS_KEY, movie);
            mActivity.startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(mActivity, R.string.no_movie, Toast.LENGTH_SHORT).show();
        }
    }
}
