package com.retroquack.kwak123.mymovies;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
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
import com.retroquack.kwak123.mymovies.data.MovieLoader;
import com.retroquack.kwak123.mymovies.data.UrlTool;
import com.retroquack.kwak123.mymovies.objects.MovieClass;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<MovieClass>>{

    // Various tags used throughout the activity, just to help keep things sane
    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    private static final int POPULAR_LOADER = 0;
    private static final int RATINGS_LOADER = 1;
    private static final String SPINNER_KEY = "spinnerPosition";
    private static final String LOADER_KEY = "loaderId";

    // Instantiating some objects that will be used
    private MovieAdapter mAdapter;
    private Spinner mSpinner;
    private int spinnerPos;
    private int loaderStored;

    // Required empty public constructor
    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            spinnerPos = savedInstanceState.getInt(SPINNER_KEY);
            loaderStored = savedInstanceState.getInt(LOADER_KEY);
        }

        Log.v(LOG_TAG, "Fragment made");
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
            getLoaderManager().initLoader(loaderStored, null, this).forceLoad();
        }
        return super.onOptionsItemSelected(item);
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

    // I was playing around with the fragment lifecycle, I left this here as I know I'll be using it later
    @Override
    public void onStart() {
        super.onStart();
    }

    // Attempt to preserve the current state of the user's choices, so I can call it back when the
    // fragment is called back
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SPINNER_KEY, mSpinner.getSelectedItemPosition());
        outState.putInt(LOADER_KEY, loaderStored);
    }

    // Custom listener for the GridView, stores the MovieClass associated with the selected object and
    // passes it to the DetailsActivity to handle.
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MovieClass movieClass = mAdapter.getItem(position);

            if (movieClass != null) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(MovieClass.CLASS_KEY, movieClass);
                startActivity(intent);
            } else {
                Log.e(LOG_TAG, "No movie object was found?");
            }
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
                    loaderStored = POPULAR_LOADER;
                    startLoader(loaderStored);
                } else {
                    loaderStored = RATINGS_LOADER;
                    startLoader(loaderStored);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    // Private method to start the loader, just for my own sanity
    private void startLoader(int id) {
        getLoaderManager().initLoader(id, null, this);
    }

    /**
     * Loader methods are listed below.
     */
    @Override
    public Loader<List<MovieClass>> onCreateLoader(int id, Bundle args) {
        //Log.v(LOG_TAG, "Loader is created");
        if (id == POPULAR_LOADER) {
            return new MovieLoader(getActivity(), UrlTool.buildPopularUrl().toString());
        } else if (id == RATINGS_LOADER) {
            return new MovieLoader(getActivity(), UrlTool.buildRatingUrl().toString());
        } else {
            Log.e(LOG_TAG, "Error with query type?" + id);
            return null;
        }
    }

    public void onLoadFinished(Loader<List<MovieClass>> loader, List<MovieClass> data) {
        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }

        //Log.v(LOG_TAG, "Loader is done loading: " + loader.getId());
    }

    public void onLoaderReset(Loader<List<MovieClass>> loader) {
        mAdapter.clear();
    }

}
