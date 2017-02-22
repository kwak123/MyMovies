package com.retroquack.kwak123.mymovies;

import android.app.Fragment;
import android.app.LoaderManager;
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

import com.retroquack.kwak123.mymovies.adapters.MovieAdapter;
import com.retroquack.kwak123.mymovies.loaders.MovieLoader;
import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.presenter.MainPresenterImpl;
import com.retroquack.kwak123.mymovies.presenter.MovieRepositoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: Splash screen, check for internet, set listener to check for changes in favorite db
 *
 * Redesign to load a hashmap of movie classes instead of separate lists
 */


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<HashMap<String, List<MovieClass>>> {

    // Various tags used throughout the activity, just to help keep things sane
    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private static final String SPINNER_KEY = "spinnerPosition";

    // Instantiating some objects that will be used
    private MovieAdapter mAdapter;
    private Spinner mSpinner;
    private int spinnerPos;
    private int movieType;
    private MainPresenterImpl mPresenter;

    private boolean hasStarted = false;

    // Required empty public constructor
    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mPresenter = new MainPresenterImpl(getActivity());

        if (savedInstanceState != null) {
            spinnerPos = savedInstanceState.getInt(SPINNER_KEY);
            movieType = spinnerPos;
        } else {
            movieType = MovieRepositoryImpl.TYPE_POPULAR;
        }
    }

    // This adds a Spinner to the action bar
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
        if (id == R.id.refresh) {
            getLoaderManager().initLoader(0, null, this).forceLoad();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasStarted) {
            Log.v(LOG_TAG, "on resume");
            mPresenter.onResume();
            mAdapter.onFavoritesRefresh(mPresenter.onMoviesRequested(movieType));
        }
        hasStarted = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Loading GridView and GridAdapter
        GridView mMovieGridView = (GridView) rootView.findViewById(R.id.grid_view);

        mAdapter = new MovieAdapter(getActivity(), new ArrayList<MovieClass>());
        mMovieGridView.setAdapter(mAdapter);
        mMovieGridView.setOnItemClickListener(mOnClickListener);

        return rootView;
    }

    // Store current state of the user's choices
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SPINNER_KEY, mSpinner.getSelectedItemPosition());
    }

    // Custom listener for the GridView, stores the MovieClass associated with the selected object and
    // passes it to the DetailsActivity to handle.
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            MovieClass movieClass = mAdapter.getItem(position);
            mPresenter.onMovieClicked(movieType, position);
        }
    };

    // Method to call the spinner, I separated it just to help me keep the methods straight in my mind
    private void setupSpinner() {
        ArrayAdapter queryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.preference_array, R.layout.spinner_item);
        queryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinner.setAdapter(queryAdapter);
        mSpinner.setOnItemSelectedListener(mSelectListener);
    }

    /**
     * This is the custom listener for what the user selects. I chose to save the user's selection
     * into loaderStored so I could call it back when the fragment is paused/resumed
     */

    private AdapterView.OnItemSelectedListener mSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String sel = (String) parent.getItemAtPosition(position);
            if (!TextUtils.isEmpty(sel)) {
                if (sel.equals(getString(R.string.pref_popular))) {
                    movieType = MovieRepositoryImpl.TYPE_POPULAR;
                    startLoader();
                } else if (sel.equals(getString(R.string.pref_rating))) {
                    movieType = MovieRepositoryImpl.TYPE_RATING;
                    startLoader();
                } else {
                    movieType = MovieRepositoryImpl.TYPE_FAVORITE;
                    mAdapter.clear();
                    mAdapter.addAll(mPresenter.onMoviesRequested(movieType));
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

    /**
     * Loader methods are listed below.
     */
    @Override
    public Loader<HashMap<String, List<MovieClass>>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(getActivity());
    }

    public void onLoadFinished(Loader<HashMap<String, List<MovieClass>>> loader, HashMap<String, List<MovieClass>> data) {
        mAdapter.clear();

        mPresenter.onMoviesLoaded(data);
        mAdapter.addAll(mPresenter.onMoviesRequested(movieType));
        //Log.v(LOG_TAG, "Loader is done loading: " + loader.getId());
    }

    public void onLoaderReset(Loader<HashMap<String, List<MovieClass>>> loader) {
        mAdapter.clear();
    }

}