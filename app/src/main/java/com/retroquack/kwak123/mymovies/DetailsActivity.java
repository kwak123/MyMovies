package com.retroquack.kwak123.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.retroquack.kwak123.mymovies.objects.MovieClass;

public class DetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Takes the MovieClass object from the intent and passes it into the DetailsFragment
        Intent intent = getIntent();
        MovieClass movieClass = intent.getParcelableExtra(MovieClass.CLASS_KEY);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container_details, DetailsFragment.newInstance(movieClass))
                    .commit();
        }
    }
}
