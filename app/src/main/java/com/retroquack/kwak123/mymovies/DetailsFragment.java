package com.retroquack.kwak123.mymovies;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.retroquack.kwak123.mymovies.network.DetailLoader;
import com.retroquack.kwak123.mymovies.network.DetailQuery;
import com.retroquack.kwak123.mymovies.model.DetailClass;
import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.presenter.DetailsPresenterImpl;
import com.retroquack.kwak123.mymovies.presenter.DetailsView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * This is the screen for holding the user's interactions.
 * I apologize for the layout being incomplete, I am still learning better layout practices
 *
 * TODO: Replace Trailer/Review adapter with correct ExpandableListAdapter
 */

public class DetailsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<HashMap<String, List<DetailClass>>>, DetailsView {

    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();
    private Unbinder unbinder;
    private String mMovieId;
    private DetailsPresenterImpl mPresenter;

    private HashMap<String, List<DetailClass>> mData;
    private List<DetailClass> mTrailersList;
    private List<DetailClass> mReviewsList;

    @BindView(R.id.poster_detail_view) ImageView posterView;
    @BindView(R.id.backdrop_view) ImageView backdropView;
    @BindView(R.id.movie_title) TextView titleView;
    @BindView(R.id.movie_rating) TextView ratingView;
    @BindView(R.id.movie_release) TextView releaseView;
    @BindView(R.id.movie_overview) TextView overviewView;
    @BindView(R.id.container_trailers) LinearLayout trailersLayout;
    @BindView(R.id.container_reviews) LinearLayout reviewsLayout;

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

        Bundle bundle = getArguments();
        MovieClass mMovieClass = bundle.getParcelable(MovieClass.CLASS_KEY);
        mPresenter = new DetailsPresenterImpl(this);


        unbinder = ButterKnife.bind(this, rootView);

        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.detail_progress_bar);

        mMovieId = mMovieClass.getId();

        titleView.setText(mMovieClass.getMovieTitle());
        ratingView.setText(mMovieClass.getRating());
        releaseView.setText(mMovieClass.getRelease());
        overviewView.setText(mMovieClass.getOverview());

        Picasso.with(getActivity())
                .load(mMovieClass.getPosterUrl())
                .into(posterView);

        Picasso.with(getActivity())
                .load(mMovieClass.getBackdropUrl())
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

        getLoaderManager().initLoader(0, null, this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // Loader is below

    @Override
    public Loader<HashMap<String, List<DetailClass>>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "Loader started");
        return new DetailLoader(getActivity(), mMovieId);
    }

    @Override
    public void onLoadFinished(Loader<HashMap<String, List<DetailClass>>> loader,
                               HashMap<String, List<DetailClass>> data) {

        mData = data;

        mTrailersList = mData.get(DetailQuery.TRAILER_HEADER);
        mReviewsList = mData.get(DetailQuery.REVIEW_HEADER);

        updateTrailers(mTrailersList);
        updateReviews(mReviewsList);
    }

    @Override
    public void onLoaderReset(Loader<HashMap<String, List<DetailClass>>> loader) {
        mData = null;
    }

    @Override
    public void onDetailsLoaded() {
        mPresenter.onTrailerSelected(mTrailersList);
        mPresenter.onReviewSelected(mReviewsList);
    }

    private void updateTrailers(List<DetailClass> trailersList) {

        Log.v(LOG_TAG, "Updating trailers!");

        final LayoutInflater inflater = LayoutInflater.from(getActivity());

        if (!trailersList.get(0).getTrailerTitle().equals(DetailClass.NO_TRAILER)) {

            for (DetailClass trailer : trailersList) {

                final View trailerView = inflater.inflate(R.layout.list_item_trailer, trailersLayout, false);
                final TextView trailerTitleView = (TextView) trailerView.findViewById(R.id.trailer_title_view);
                final ImageView trailerStillView = (ImageView) trailerView.findViewById(R.id.trailer_image_view);

                trailerTitleView.setText(trailer.getTrailerTitle());
                trailerView.setTag(trailer);

                Picasso.with(getActivity())
                        .load(trailer.getTrailerStillUrl())
                        .into(trailerStillView);

                if (trailerView.getParent() != null) {
                    ((ViewGroup) trailerView.getParent()).removeView(trailerView);
                }

                trailersLayout.addView(trailerView);
            }
        } else {

            final View trailerView = inflater.inflate(R.layout.list_item_trailer, trailersLayout, false);
            final TextView trailerTitleView = (TextView) trailerView.findViewById(R.id.trailer_title_view);
            final ImageView trailerStillView = (ImageView) trailerView.findViewById(R.id.trailer_image_view);

            final DetailClass noTrailerClass = DetailClass.noTrailerFound();
            // I was going to use a lambda here, opted against it to keep it in line with older versions of java

            trailerTitleView.setText(DetailClass.NO_TRAILER);
            trailerStillView.setImageResource(R.mipmap.no_pic);
            trailerView.setTag(noTrailerClass);

            if (trailerView.getParent() != null) {
                ((ViewGroup) trailerView.getParent()).removeView(trailerView);
            }

            trailersLayout.addView(trailerView);
        }
    }

    private void updateReviews(List<DetailClass> reviewsList) {
        Log.v(LOG_TAG, "Updating reviews!");

        final LayoutInflater inflater = LayoutInflater.from((getActivity()));

        if (!reviewsList.get(0).getReviewAuthor().equals(DetailClass.NO_AUTHOR)) {

            for (DetailClass review : reviewsList) {
                final View reviewView = inflater.inflate(R.layout.list_item_review, reviewsLayout, false);
                final TextView reviewAuthorView = (TextView) reviewView.findViewById(R.id.review_author_view);
                final TextView reviewSummaryView = (TextView) reviewView.findViewById(R.id.review_summary_view);

                reviewAuthorView.setText(review.getReviewAuthor());
                reviewSummaryView.setText(review.getReviewTruncatedSummary());
                reviewView.setTag(review);

                if (reviewView.getParent() != null) {
                    ((ViewGroup) reviewView.getParent()).removeView(reviewView);
                }

                reviewsLayout.addView(reviewView);
            }
        } else {
            final View reviewView = inflater.inflate(R.layout.list_item_review, reviewsLayout, false);
            final TextView reviewAuthorView = (TextView) reviewView.findViewById(R.id.review_author_view);
            final TextView reviewSummaryView = (TextView) reviewView.findViewById(R.id.review_summary_view);

            reviewAuthorView.setText(DetailClass.NO_AUTHOR);
            reviewSummaryView.setText(DetailClass.NO_SUMMARY);

            DetailClass noReviewFound = DetailClass.noReviewFound();

            reviewView.setTag(noReviewFound);

            if (reviewView.getParent() != null) {
                ((ViewGroup) reviewView.getParent()).removeView(reviewView);
            }

            reviewsLayout.addView(reviewView);
        }

    }

}