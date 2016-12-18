package com.retroquack.kwak123.mymovies.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.retroquack.kwak123.mymovies.objects.ReviewClass;

import java.util.List;

/**
 * Created by kwak123 on 12/13/2016.
 */

public class ReviewAdapter extends ArrayAdapter<ReviewClass> {

    public ReviewAdapter (Context context, List<ReviewClass> reviewClasses) {
        super(context, 0, reviewClasses);
    }

}
