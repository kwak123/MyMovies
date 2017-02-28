package com.retroquack.kwak123.mymovies.ui.adapters;

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
 * Custom ArrayAdapter for binding images to the GridView. Progress bars removed for smoother UI.
 *
 * Added static ViewHolder class for AdapterView standards.
 */

public class MovieAdapter extends ArrayAdapter<MovieClass> {

    public MovieAdapter(Context context, List<MovieClass> movieClasses) {
        super(context, 0, movieClasses);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

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
