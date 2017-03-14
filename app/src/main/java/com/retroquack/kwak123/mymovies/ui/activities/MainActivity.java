package com.retroquack.kwak123.mymovies.ui.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.retroquack.kwak123.mymovies.MyMoviesApp;
import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.data.repository.MovieRepository;
import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.ui.fragments.AboutDialogFragment;
import com.retroquack.kwak123.mymovies.ui.fragments.DetailsFragment;
import com.retroquack.kwak123.mymovies.ui.fragments.MainFragment;

import javax.inject.Inject;

import static android.support.design.widget.Snackbar.make;

/**
 * Holds the main fragment as well as the options menu for the main page.
 */

public class MainActivity extends AppCompatActivity implements MainFragment.MainCallback,
        DetailsFragment.DetailsCallback {

    @Inject MovieRepository mMovieRepository;

    public static final String DETAIL_TAG = "DF";
    private boolean mTwoPane;
    private ConnectivityManager mConnMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        setContentView(R.layout.activity_main);

        mTwoPane = (findViewById(R.id.movie_details_container) != null);

        ((MyMoviesApp) getApplication()).getAndroidComponent().inject(this);
    }

    @Override
    public void onMovieClicked(int type, int position) {
        if (mMovieRepository.isNull(type, position)) {
            Toast.makeText(this, R.string.no_movie, Toast.LENGTH_SHORT).show();
            return;
        }

        if (mTwoPane) {
            ((FrameLayout) findViewById(R.id.movie_details_container)).removeAllViews();
            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container,
                            DetailsFragment.newInstance(type, position),
                            DETAIL_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(MovieClass.TYPE_KEY, type);
            intent.putExtra(MovieClass.POSITION_KEY, position);
            startActivity(intent);
        }
    }

    @Override
    public void onDetailsClicked(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        try {
            startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(this, R.string.fail_connection, Toast.LENGTH_SHORT).show();
        }
    }

    public void showSnackBar() {
        Snackbar sb = Snackbar.make(findViewById(R.id.main_fragment),
                getString(R.string.error_data), Snackbar.LENGTH_LONG);
        sb.getView().setBackgroundColor(getResources().getColor(R.color.background_red));
        if (mConnMan.getActiveNetworkInfo() == null) {
            sb.setText(R.string.error_network);
        }
        sb.show();
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
