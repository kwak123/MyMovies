package com.retroquack.kwak123.mymovies.ui.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.retroquack.kwak123.mymovies.MyMoviesApp;
import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.data.network.MovieLoader;
import com.retroquack.kwak123.mymovies.data.repository.MovieRepository;
import com.retroquack.kwak123.mymovies.data.repository.MovieRepositoryImpl;
import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.ui.adapters.MovieAdapter;
import com.retroquack.kwak123.mymovies.ui.presenter.MainPresenter;
import com.retroquack.kwak123.mymovies.ui.presenter.MainPresenterImpl;
import com.retroquack.kwak123.mymovies.ui.views.MainView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Loads a HashMap of MovieClass lists.
 *
 * TODO: Splash screen, check for internet connection, abstract Loaders to presenter
 */


public class MainFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<HashMap<String, List<MovieClass>>>,
        MainView {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private static final String SPINNER_KEY = "spinnerPosition";
    private static final String GRID_KEY = "gridPosition";

    private MovieAdapter mAdapter;
    private Spinner mSpinner;
    private int spinnerPos;
    private int gridPos;
    private int mMovieType;
    private MainPresenter mPresenter;
    private MainCallback callback;

    private GridView mGridView;

    private boolean hasStarted = false;

    // Dagger
    @Inject MovieRepository mMovieRepository;

    public MainFragment() {
    }

    public interface MainCallback {
        void onMovieClicked(int type, int position);
    }

    // Lifecycle
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MyMoviesApp) getActivity().getApplication())
                .getAndroidComponent().inject(this);
        callback = (MainCallback) getActivity();
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mPresenter = new MainPresenterImpl(this, mMovieRepository);

        if (savedInstanceState != null) {
            spinnerPos = savedInstanceState.getInt(SPINNER_KEY);
            gridPos = savedInstanceState.getInt(GRID_KEY);
            mMovieType = spinnerPos;
        } else {
            mMovieType = MovieRepositoryImpl.TYPE_POPULAR;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasStarted) {
            Log.v(LOG_TAG, "on resume");
            mPresenter.onResume();
        }
        hasStarted = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Loading GridView and GridAdapter
        mGridView = (GridView) rootView.findViewById(R.id.grid_view);

        mAdapter = new MovieAdapter(getActivity(), new ArrayList<MovieClass>());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(mOnClickListener);

        if (gridPos != 0) {
            mGridView.smoothScrollToPosition(gridPos);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMovieType = mSpinner.getSelectedItemPosition();
        gridPos = mGridView.getFirstVisiblePosition();
        outState.putInt(SPINNER_KEY, mMovieType);
        outState.putInt(GRID_KEY, gridPos);
    }

    // Handle loading movie details
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            callback.onMovieClicked(mMovieType, position);
        }
    };

    // MainView methods
    @Override
    public void refreshAdapter() {
        mAdapter.clear();
        mAdapter.onFavoritesRefresh(mPresenter.onMoviesRequested(mMovieType));
    }

    // Set up spinner
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
        MenuItem item = menu.findItem(R.id.pref_spinner);
        mSpinner = (Spinner) MenuItemCompat.getActionView(item);
        setupSpinner();
        mSpinner.setSelection(spinnerPos);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh:
                getLoaderManager().initLoader(0, null, this).forceLoad();
                break;
            case R.id.clear_favorites:
                mPresenter.clearDatabase();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSpinner() {
        ArrayAdapter queryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.preference_array, R.layout.spinner_item);
        queryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinner.setAdapter(queryAdapter);
        mSpinner.setOnItemSelectedListener(mSelectListener);
    }

    private AdapterView.OnItemSelectedListener mSelectListener =
            new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String sel = (String) parent.getItemAtPosition(position);
            if (!TextUtils.isEmpty(sel)) {
                if (sel.equals(getString(R.string.pref_popular))) {
                    mMovieType = MovieRepositoryImpl.TYPE_POPULAR;
                    startLoader();
                } else if (sel.equals(getString(R.string.pref_rating))) {
                    mMovieType = MovieRepositoryImpl.TYPE_RATING;
                    startLoader();
                } else {
                    mMovieType = MovieRepositoryImpl.TYPE_FAVORITE;
                    mAdapter.clear();
                    mAdapter.addAll(mPresenter.onMoviesRequested(mMovieType));
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    // Private method to start the loader
    private void startLoader() {
        getLoaderManager().initLoader(0, null, this);
    }

    // Loader methods
    public Loader<HashMap<String, List<MovieClass>>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(getActivity());
    }

    public void onLoadFinished(Loader<HashMap<String, List<MovieClass>>> loader,
                               HashMap<String, List<MovieClass>> data) {
        mPresenter.onMoviesLoaded(data);
        //Log.v(LOG_TAG, "Loader is done loading: " + loader.getKey());
    }

    public void onLoaderReset(Loader<HashMap<String, List<MovieClass>>> loader) {
        mAdapter.clear();
    }

}