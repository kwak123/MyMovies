package com.retroquack.kwak123.mymovies.ui.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.retroquack.kwak123.mymovies.R;

/**
 * Dialog that holds the TMDB attribution. Thanks TMDB!
 */

public class AboutDialogFragment extends DialogFragment {

    //Required empty constructor
    public AboutDialogFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_dialog_fragment, container, false);
    }
}
