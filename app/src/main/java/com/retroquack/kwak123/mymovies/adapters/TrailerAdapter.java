package com.retroquack.kwak123.mymovies.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.retroquack.kwak123.mymovies.objects.TrailerClass;

import java.util.List;

/**
 * Created by kwak123 on 12/13/2016.
 */

public class TrailerAdapter extends ArrayAdapter<TrailerClass> {

    public TrailerAdapter(Context context, List<TrailerClass> trailerClasses) {
        super(context, 0, trailerClasses);
    }

}
