package com.retroquack.kwak123.mymovies;

import android.app.Application;

import com.retroquack.kwak123.mymovies.dagger.components.AndroidComponent;
import com.retroquack.kwak123.mymovies.dagger.components.DaggerAndroidComponent;
import com.retroquack.kwak123.mymovies.dagger.modules.AndroidModule;
import com.retroquack.kwak123.mymovies.dagger.modules.DataModule;

/**
 * Load dependencies
 */

public class MyMoviesApp extends Application {

    private AndroidComponent mAndroidComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAndroidComponent = DaggerAndroidComponent.builder()
                .androidModule(new AndroidModule(this))
                .dataModule(new DataModule())
                .build();
    }

    public AndroidComponent getAndroidComponent() {
        return mAndroidComponent;
    }

}
