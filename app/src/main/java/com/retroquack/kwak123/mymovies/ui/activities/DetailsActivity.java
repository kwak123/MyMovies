package com.retroquack.kwak123.mymovies.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.ui.fragments.DetailsFragment;
import com.retroquack.kwak123.mymovies.model.MovieClass;

/**
 * Receives intent, creates new fragments with parameters
 */
public class DetailsActivity extends AppCompatActivity implements DetailsFragment.DetailsCallback{

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Takes the MovieClass object from the intent and passes it into the DetailsFragment
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
