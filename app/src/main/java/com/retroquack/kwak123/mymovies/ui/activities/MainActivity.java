package com.retroquack.kwak123.mymovies.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.retroquack.kwak123.mymovies.MyMoviesApp;
import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.data.repository.MovieRepository;
import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.ui.fragments.AboutDialogFragment;
import com.retroquack.kwak123.mymovies.ui.fragments.DetailsFragment;
import com.retroquack.kwak123.mymovies.ui.fragments.MainFragment;

import javax.inject.Inject;

/**
 * Holds the main fragment as well as the options menu for the main page.
 */

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    @Inject MovieRepository mMovieRepository;

    private static final String DETAIL_TAG = "DF";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_details_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, new DetailsFragment(), DETAIL_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        ((MyMoviesApp) getApplication()).getAndroidComponent().inject(this);
    }

    @Override
    public void onMovieClicked(int type, int position) {
        if (mMovieRepository.isNull(type, position)) {
            Toast.makeText(this, R.string.no_movie, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MovieClass.TYPE_KEY, type);
        intent.putExtra(MovieClass.POSITION_KEY, position);
        startActivity(intent);
    }

    // Options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this, getString(R.string.default_toast), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_about:
                showAboutDialog();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        FragmentManager fm = getFragmentManager();
        AboutDialogFragment fragment = new AboutDialogFragment();
        fragment.show(fm, "about_fragment");
    }



}
