package com.retroquack.kwak123.mymovies.ui.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.retroquack.kwak123.mymovies.MyMoviesApp;
import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.data.network.DetailLoader;
import com.retroquack.kwak123.mymovies.data.network.DetailQuery;
import com.retroquack.kwak123.mymovies.data.repository.MovieRepository;
import com.retroquack.kwak123.mymovies.model.DetailClass;
import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.tools.UrlTool;
import com.retroquack.kwak123.mymovies.ui.presenter.DetailsPresenter;
import com.retroquack.kwak123.mymovies.ui.presenter.DetailsPresenterImpl;
import com.retroquack.kwak123.mymovies.ui.views.DetailsView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Holds details, trailers, and reviews.
 *
 * TODO: Shift Loader to presenter?
 */

public class DetailsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<HashMap<String, List<DetailClass>>>,
        DetailsView {

    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();

    private Unbinder unbinder;
    private String mMovieKey;
    private DetailsPresenter mPresenter;
    private DetailsCallback mCallback;

    private MovieClass mMovieClass;
    private HashMap<String, List<DetailClass>> mData;

    @BindView(R.id.poster_detail_view) ImageView posterView;
    @BindView(R.id.backdrop_detail_view) ImageView backdropView;
    @BindView(R.id.movie_title) TextView titleView;
    @BindView(R.id.movie_rating) TextView ratingView;
    @BindView(R.id.movie_release) TextView releaseView;
    @BindView(R.id.movie_overview) TextView overviewView;
    @BindView(R.id.layout_favorites_confirm) RelativeLayout favoritesLayout;
    @BindView(R.id.details_yes) TextView yesTextView;
    @BindView(R.id.details_no) TextView noTextView;
    @BindView(R.id.details_message) TextView detailMessageView;
    @BindView(R.id.container_trailers) LinearLayout trailersLayout;
    @BindView(R.id.container_reviews) LinearLayout reviewsLayout;
    @BindView(R.id.checkbox_favorite) CheckBox favoriteCheckBox;

    @Inject MovieRepository mMovieRepository;

    /**
     * Used to pass parameters from DetailsActivity.
     *
     * @param type the type of MovieClass in the MovieList
     * @param position the position of the MovieClass in the MovieList
     * @return a new DetailFragment.
     *
     * If the default value of -1 is ever reached, something is very wrong.
     */
    public static DetailsFragment newInstance(int type, int position) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MovieClass.TYPE_KEY, type);
        bundle.putInt(MovieClass.POSITION_KEY, position);
        fragment.setArguments(bundle);

        return fragment;
    }

    public interface DetailsCallback {
        void onDetailsClicked(String url);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MyMoviesApp) getActivity().getApplication())
                .getAndroidComponent().inject(this);
        mCallback = (DetailsCallback) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        int mMovieType = bundle.getInt(MovieClass.TYPE_KEY);
        int mMoviePosition = bundle.getInt(MovieClass.POSITION_KEY);
        mPresenter = new DetailsPresenterImpl(this, mMovieRepository, mMovieType);
        mMovieClass = mPresenter.getMovieClass(mMoviePosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        mMovieKey = mMovieClass.getKey();

        titleView.setText(mMovieClass.getMovieTitle());
        ratingView.setText(mMovieClass.getRating());
        releaseView.setText(mMovieClass.getRelease());
        overviewView.setText(mMovieClass.getOverview());
        favoriteCheckBox.setChecked(mMovieClass.getFavorite());

        favoriteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoritesLayout.setVisibility((favoritesLayout.getVisibility()==View.VISIBLE) ?
                        View.GONE : View.VISIBLE);
                detailMessageView.setText((!mMovieClass.getFavorite()) ?
                        R.string.add_favorite : R.string.remove_favorite);
            }
        });

        favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });

        yesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onFavoritesSelected(favoriteCheckBox.isChecked(), mMovieClass);
                favoritesLayout.setVisibility(View.GONE);
            }
        });

        noTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean store = favoriteCheckBox.isChecked();
                favoriteCheckBox.setChecked(!store);
                favoritesLayout.setVisibility(View.GONE);
            }
        });

        Log.v(LOG_TAG, "Movie favorited? " + mMovieClass.getFavorite());

        Picasso.with(getActivity())
                .load(UrlTool.buildPosterUrl(mMovieClass.getPosterKey()).toString())
                .into(posterView);

        Picasso.with(getActivity())
                .load(UrlTool.buildBackdropUrl(mMovieClass.getBackdropKey()).toString())
                .into(backdropView);

        getLoaderManager().initLoader(0, null, this);

        return rootView;
    }

    // Unbind ButterKnife
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // Loader
    @Override
    public Loader<HashMap<String, List<DetailClass>>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "Loader started");
        return new DetailLoader(getActivity(), mMovieKey);
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

    @Override
    public void onFavoritesChanged(boolean status) {
        mMovieClass.setFavorited(status);
        favoriteCheckBox.setChecked(status);
    }

    // DetailClass click logic
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
                        DetailClass detailClass = (DetailClass) v.getTag();
                        mCallback.onDetailsClicked(detailClass.getTrailerUrl());
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
                    Toast.makeText(getActivity(),
                            R.string.no_trailer, Toast.LENGTH_SHORT).show();
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
                        DetailClass detailClass = (DetailClass) v.getTag();
                        mCallback.onDetailsClicked(detailClass.getReviewSummaryUrl());
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
                    Toast.makeText(getActivity(),
                            R.string.no_review, Toast.LENGTH_SHORT).show();
                }
            });

            if (reviewView.getParent() != null) {
                ((ViewGroup) reviewView.getParent()).removeView(reviewView);
            }

            reviewsLayout.addView(reviewView);
        }

    }
}