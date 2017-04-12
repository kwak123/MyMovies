package com.retroquack.kwak123.mymovies.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.retroquack.kwak123.mymovies.MyMoviesApp;
import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.data.repository.MovieRepository;
import com.retroquack.kwak123.mymovies.ui.fragments.DetailsFragment;
import com.retroquack.kwak123.mymovies.model.MovieClass;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.DetailsCallback{

    @BindView(R.id.toolbar_backdrop_view) ImageView backdropView;

    @Inject MovieRepository mMovieRepository;

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        ((MyMoviesApp) getApplication()).getAndroidComponent().inject(this);

        Intent intent = getIntent();
        int type = intent.getIntExtra(MovieClass.TYPE_KEY, -1);
        int position = intent.getIntExtra(MovieClass.POSITION_KEY, -1);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container_details, DetailsFragment.newInstance(type, position))
                    .commit();
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
}
