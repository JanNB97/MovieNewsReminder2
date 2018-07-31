package com.yellowbite.movienewsreminder2.ui;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.util.DateHelper;

public class MovieViewHolder extends RecyclerView.ViewHolder
{
    private TextView titelTextView;
    private TextView standortTextView;
    private TextView statusTextView;

    public MovieViewHolder(View view)
    {
        super(view);

        this.titelTextView = view.findViewById(R.id.title);
        this.standortTextView = view.findViewById(R.id.standort);
        this.statusTextView = view.findViewById(R.id.status);
    }

    public void showMovie(Movie movie)
    {
        if(movie == null || movie.getTitel() == null)
        {
            return;
        }
        titelTextView.setText(movie.getTitel());

        if(movie.getStandort() != null)
        {
            standortTextView.setText(movie.getStandort());
        }

        if(movie.getStatus() == null)
        {
            return;
        }
        this.showColor(movie);

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
                    statusTextView.setText(DateHelper.getWeekdayAsMessage(movie.getEntliehenBis()));
                }

                break;
            case VORBESTELLT:
                statusTextView.setText(movie.getVorbestellungen() + " Vor.");
                break;
        }
    }

    private void showColor(Movie movie)
    {
        switch (movie.getStatus())
        {

            case VERFUEGBAR:
                paintVerfuegbar();
                break;
            case ENTLIEHEN:
                if(movie.getVorbestellungen() == 0)
                {
                    this.paintEntliehenNoVor();
                }
                else
                {
                    this.paintVorbestellt();
                }
                break;
            case VORBESTELLT:
                this.paintVorbestellt();
                break;
        }
    }

    private void paintVerfuegbar()
    {
        this.statusTextView.setBackgroundColor(Color.parseColor("green"));
    }

    private void paintEntliehenNoVor()
    {
        this.statusTextView.setBackgroundColor(Color.parseColor("yellow"));
    }

    private void paintVorbestellt()
    {
        this.statusTextView.setBackgroundColor(Color.parseColor("red"));
    }
}
