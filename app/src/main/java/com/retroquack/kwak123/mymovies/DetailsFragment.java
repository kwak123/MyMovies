package com.retroquack.kwak123.mymovies;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.retroquack.kwak123.mymovies.model.DetailClass;
import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.network.DetailLoader;
import com.retroquack.kwak123.mymovies.network.DetailQuery;
import com.retroquack.kwak123.mymovies.presenter.DetailsPresenterImpl;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * This is the screen for holding the user's interactions.
 * No lambdas because I didn't want to lose Instant Run.
 *
 * TODO: Add favorites option
 * TODO: Add menu button to see other favorites, etc.
 */

public class DetailsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<HashMap<String, List<DetailClass>>> {

    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();

    private DisplayMetrics mDisplayMetrics;

    private Unbinder unbinder;
    private String mMovieId;
    private DetailsPresenterImpl mPresenter;

    private MovieClass mMovieClass;

    private HashMap<String, List<DetailClass>> mData;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mMovieClass = bundle.getParcelable(MovieClass.CLASS_KEY);

        mDisplayMetrics = new DisplayMetrics();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

        mPresenter = new DetailsPresenterImpl(getActivity());

        unbinder = ButterKnife.bind(this, rootView);

        mMovieId = mMovieClass.getId();

        titleView.setText(mMovieClass.getMovieTitle());
        ratingView.setText(mMovieClass.getRating());
        releaseView.setText(mMovieClass.getRelease());
        overviewView.setText(mMovieClass.getOverview());

        backdropView.getLayoutParams().height = mDisplayMetrics.widthPixels * 104 / 185;
        Log.v(LOG_TAG, Integer.toString(mDisplayMetrics.widthPixels * 104 / 185));

        Picasso.with(getActivity())
                .load(mMovieClass.getPosterUrl())
                .into(posterView);

        Glide.with(getActivity())
                .load(mMovieClass.getBackdropUrl())
                .into(backdropView);

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

        List<DetailClass> mTrailersList = mData.get(DetailQuery.TRAILER_HEADER);
        List<DetailClass> mReviewsList = mData.get(DetailQuery.REVIEW_HEADER);

        updateTrailers(mTrailersList);
        updateReviews(mReviewsList);
    }

    @Override
    public void onLoaderReset(Loader<HashMap<String, List<DetailClass>>> loader) {
        mData = null;
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

                trailerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.onTrailerSelected((DetailClass) trailerView.getTag());
                    }
                });

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

            trailerTitleView.setText(DetailClass.NO_TRAILER);
            trailerStillView.setImageResource(R.mipmap.no_pic);
            trailerView.setTag(noTrailerClass);

            trailerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.noTrailerAvailable();
                }
            });

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

                reviewView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.onReviewSelected((DetailClass) reviewView.getTag());
                    }
                });

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

            reviewView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.noReviewAvailable();
                }
            });

            if (reviewView.getParent() != null) {
                ((ViewGroup) reviewView.getParent()).removeView(reviewView);
            }

            reviewsLayout.addView(reviewView);
        }

    }

}