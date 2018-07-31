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
    private View view;

    private TextView titelTextView;
    private TextView standortTextView;
    private TextView wocheTextView;
    private TextView wochentagTextView;

    public MovieViewHolder(View view)
    {
        super(view);
        this.view = view;

        this.titelTextView = view.findViewById(R.id.title);
        this.standortTextView = view.findViewById(R.id.standort);
        this.wocheTextView = view.findViewById(R.id.woche);
        this.wochentagTextView = view.findViewById(R.id.wochentag);
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

        switch (movie.getStatus())
        {
            case VERFUEGBAR:
                wocheTextView.setText(movie.getStatus().getValue());
                break;
            case ENTLIEHEN:
                if(movie.getVorbestellungen() > 0)
                {
                    wocheTextView.setText(movie.getVorbestellungen() + " Vor.");
                }
                else
                {
                    String[] message = DateHelper.getWeekdayAsMessage(movie.getEntliehenBis());
                    wocheTextView.setText(message[0]);
                    if(message.length > 1)
                    {
                        wochentagTextView.setText(message[1]);
                    }
                }

                break;
            case VORBESTELLT:
                wocheTextView.setText(movie.getVorbestellungen() + " Vor.");
                break;
        }

        this.showColor(movie);
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
        this.view.setBackgroundColor(Color.parseColor("#90ff60"));
    }

    private void paintEntliehenNoVor()
    {
        this.view.setBackgroundColor(Color.parseColor("#ffff75"));
    }

    private void paintVorbestellt()
    {
        this.view.setBackgroundColor(Color.parseColor("#ffb06b"));
    }
}
