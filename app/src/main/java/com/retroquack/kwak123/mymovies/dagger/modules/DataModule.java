package com.retroquack.kwak123.mymovies.dagger.modules;

import android.content.ContentResolver;

import com.retroquack.kwak123.mymovies.data.repository.MovieRepository;
import com.retroquack.kwak123.mymovies.data.repository.MovieRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *  Dependencies for data tools
 */

@Module
public class DataModule {

    public DataModule(){}

    @Provides
    @Singleton
    MovieRepository provideMovieRepository(ContentResolver contentResolver) {
        return new MovieRepositoryImpl(contentResolver);
    }
}
