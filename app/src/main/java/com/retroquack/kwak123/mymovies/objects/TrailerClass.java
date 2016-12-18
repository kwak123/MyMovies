package com.retroquack.kwak123.mymovies.objects;

import java.net.URL;

/**
 * Created by kwak123 on 12/15/2016.
 */

public class TrailerClass {
    private URL mTrailerUrl;
    private String mTrailerTitle;

    public TrailerClass(String trailerTitle, URL trailerUrl) {
        mTrailerUrl = trailerUrl;
        mTrailerTitle = trailerTitle;
    }

    public URL getTrailerUrl() {
        return mTrailerUrl;
    }

    public String getTrailerTitle() {
        return mTrailerTitle;
    }
}
