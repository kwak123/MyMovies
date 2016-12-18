package com.retroquack.kwak123.mymovies;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Dialog made when the user selects the "about" option in the settings menu
 * Holds the TMDB attribution to stay in line with the API guidelines
 */

public class AboutDialogFragment extends DialogFragment {

    //Required empty constructor
    public AboutDialogFragment() {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_dialog_fragment, container, false);
        return rootView;
    }
}
