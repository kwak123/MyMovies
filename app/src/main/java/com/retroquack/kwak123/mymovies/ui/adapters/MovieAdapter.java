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
import com.retroquack.kwak123.mymovies.ui.activities.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        if (movie != null) {
            Picasso.with(getContext())
                    .load(UrlTool.buildPosterUrl(movie.getPosterKey()).toString())
                    .into(holder.posterView);

            Picasso.with(getContext())
                    .load(UrlTool.buildBackdropUrl(movie.getBackdropKey()).toString())
                    .into(holder.backdropView);
        } else {
            holder.posterView.setImageResource(R.mipmap.no_pic);
        }

        return convertView;
    }

    public void onFavoritesRefresh(List<MovieClass> movieClasses) {
        if (movieClasses == null) {
            ((MainActivity) getContext()).showSnackBar();
            return;
        }
        addAll(movieClasses);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.poster_view) ImageView posterView;
        @BindView(R.id.backdrop_view) ImageView backdropView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
