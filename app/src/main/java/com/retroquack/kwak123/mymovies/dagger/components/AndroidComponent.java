package com.retroquack.kwak123.mymovies.dagger.components;

import com.retroquack.kwak123.mymovies.dagger.modules.AndroidModule;
import com.retroquack.kwak123.mymovies.dagger.modules.DataModule;
import com.retroquack.kwak123.mymovies.ui.activities.MainActivity;
import com.retroquack.kwak123.mymovies.ui.fragments.DetailsFragment;
import com.retroquack.kwak123.mymovies.ui.fragments.MainFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Playing with Dagger 2
 *
 * Starting with Singleton management
 */

@Component(modules = {AndroidModule.class, DataModule.class})
@Singleton
public interface AndroidComponent {
    void inject(MainActivity activity);
    void inject(MainFragment fragment);
    void inject(DetailsFragment fragment);
}
