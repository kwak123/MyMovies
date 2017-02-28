package com.retroquack.kwak123.mymovies.dagger.modules;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

import com.retroquack.kwak123.mymovies.MyMoviesApp;
import com.retroquack.kwak123.mymovies.dagger.scopes.AndroidScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * v1.2.0 - Provider and Dagger2 Update
 *
 * Dependencies used application wide
 */

@Module
public class AndroidModule {

    private final MyMoviesApp myMovieApplication;

    public AndroidModule(MyMoviesApp application) {
        myMovieApplication = application;
    }

    @Provides
    @Singleton
    MyMoviesApp providesApplication() {
        return myMovieApplication;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return myMovieApplication;
    }

    @Provides
    @Singleton
    ContentResolver providesContentResolver() {
        return myMovieApplication.getContentResolver();
    }
}
