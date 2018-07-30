package com.yellowbite.movienewsreminder2.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.util.DateHelper;

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

        if(movie.getStatus() == null)
        {
            return;
        }

        switch (movie.getStatus())
        {
            case VERFUEGBAR:
                statusTextView.setText(movie.getStatus().getValue());
                break;
            case ENTLIEHEN:
                if(movie.getVorbestellungen() > 0)
                {
                    statusTextView.setText(movie.getVorbestellungen() + " Vor.");
                }
                else
                {
                    statusTextView.setText(DateHelper.toString(movie.getEntliehenBis()));
                }

                break;
            case VORBESTELLT:
                statusTextView.setText(movie.getVorbestellungen() + " Vor.");
                break;
        }
    }
}
