package com.retroquack.kwak123.mymovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.model.MovieClass;
import com.retroquack.kwak123.mymovies.tools.UrlTool;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kwak123 on 10/16/2016.
 * Custom ArrayAdapter for binding images to the GridView. I added some rudimentary progress bar
 * interactions just to play around some with Picasso.
 *
 * Added static ViewHolder class, having some second thoughts about implementing progress bar...
 * The logic behind accessing the progress bar is backwards I'll give it more thought
 */

public class MovieAdapter extends ArrayAdapter<MovieClass> {

    public MovieAdapter(Context context, List<MovieClass> movieClasses) {
        super(context, 0, movieClasses);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.grid_layout_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        final MovieClass movie = getItem(position);

        // ImageView loading for movie posters on the main fragment

        Picasso.with(getContext())
                .load(UrlTool.buildPosterUrl(movie.getPosterKey()).toString())
                .into(holder.posterView);

        return convertView;
    }

    public void onFavoritesRefresh(List<MovieClass> movieClasses) {
        clear();
        addAll(movieClasses);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.poster_view) ImageView posterView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
