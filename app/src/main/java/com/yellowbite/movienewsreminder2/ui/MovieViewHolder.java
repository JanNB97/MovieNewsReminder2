package com.yellowbite.movienewsreminder2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;

public class MovieViewHolder extends RecyclerView.ViewHolder
{
    private TextView titelTextView;
    private TextView statusTextView;

    public MovieViewHolder(View view)
    {
        super(view);

        this.titelTextView = view.findViewById(R.id.title);
        this.statusTextView = view.findViewById(R.id.status);
    }

    public void showMovie(Movie movie)
    {
        titelTextView.setText(movie.getTitel());
        statusTextView.setText(movie.getStatus().getValue());
    }
}
