package com.retroquack.kwak123.mymovies;

import android.app.Fragment;
import android.content.Loader;
import android.os.Bundle;
import android.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.retroquack.kwak123.mymovies.objects.MovieClass;
import com.retroquack.kwak123.mymovies.objects.ReviewClass;
import com.retroquack.kwak123.mymovies.objects.TrailerClass;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * This is the screen for holding the user's interactions.
 * I apologize for the layout being incomplete, I am still learning better layout practices
 */

public class DetailsFragment extends Fragment {

    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();
    private Unbinder unbinder;

    private static final int TRAILER_LOADER = 0;
    private static final int REVIEW_LOADER = 1;

    private String movieId;

    @BindView(R.id.poster_detail_view) ImageView posterView;
    @BindView(R.id.backdrop_view) ImageView backdropView;
    @BindView(R.id.movie_title) TextView titleView;
    @BindView(R.id.movie_rating) TextView ratingView;
    @BindView(R.id.movie_release) TextView releaseView;
    @BindView(R.id.movie_overview) TextView overviewView;

    // Factory method to be called in DetailsActivity so that a movieClass object
    // can be passed into the fragment
    public static DetailsFragment newInstance(MovieClass movieClass) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MovieClass.CLASS_KEY, movieClass);
        fragment.setArguments(bundle);

        return fragment;
    }

    // View creation, mostly done dynamically so that changing XML components will not
    // break the fragment, as long as IDs are retained
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.detail_progress_bar);

        Bundle bundle = getArguments();
        MovieClass movieClass = bundle.getParcelable(MovieClass.CLASS_KEY);

        titleView.setText(movieClass.getMovieTitle());
        ratingView.setText(movieClass.getRating());
        releaseView.setText(movieClass.getRelease());
        overviewView.setText(movieClass.getOverview());

        Picasso.with(getActivity())
                .load(movieClass.getPosterUrl())
                .into(posterView);

        Picasso.with(getActivity())
                .load(movieClass.getBackdropUrl())
                .into(backdropView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                        backdropView.setImageResource(R.mipmap.no_pic);
                    }
                });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private LoaderManager.LoaderCallbacks<List<TrailerClass>> trailerResultsLoader = new LoaderManager.LoaderCallbacks<List<TrailerClass>>() {
        @Override
        public Loader<List<TrailerClass>> onCreateLoader(int id, Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(Loader<List<TrailerClass>> loader, List<TrailerClass> data) {

        }

        @Override
        public void onLoaderReset(Loader<List<TrailerClass>> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<List<ReviewClass>> reviewResultsLoader = new LoaderManager.LoaderCallbacks<List<ReviewClass>>() {
        @Override
        public Loader<List<ReviewClass>> onCreateLoader(int id, Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(Loader<List<ReviewClass>> loader, List<ReviewClass> data) {

        }

        @Override
        public void onLoaderReset(Loader<List<ReviewClass>> loader) {

        }
    };
}