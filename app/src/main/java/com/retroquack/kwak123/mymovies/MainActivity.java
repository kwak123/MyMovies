package com.retroquack.kwak123.mymovies;

import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.retroquack.kwak123.mymovies.data.MovieDbHelper;
import com.retroquack.kwak123.mymovies.presenter.MainPresenterImpl;
import com.retroquack.kwak123.mymovies.presenter.MovieRepositoryImpl;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Holds the main fragment as well as the options menu for the main page.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container_main, new MainFragment())
                    .commit();
        }
    }

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
            case R.id.action_delete:
                MovieRepositoryImpl.getInstance().deleteDatabase(this);
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
