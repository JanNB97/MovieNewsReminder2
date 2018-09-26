package com.yellowbite.movienewsreminder2.ui.recyclerView;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.util.DateHelper;

public class MovieViewHolder extends RecyclerView.ViewHolder
{
    private static final int MAX_TITEL_LENGTH = 22;

    private View view;

    private TextView titelTextView;
    private TextView standortTextView;
    private TextView wocheTextView;
    private TextView wochentagTextView;

    // color
    private static final String RESET_COLOR = "#e5e5e5";
    private static final String VERFUEGBAR_COLOR = "#90ff60";
    private static final String ENTLIEHEN_COLOR = "#ffff75";
    private static final String VORBESTELLT_COLOR = "#ffb06b";
    private static final String TEXT_RESET_COLOR = "black";

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
        this.resetComponents();

        if(movie == null || movie.getTitel() == null)
        {
            return;
        }
        titelTextView.setText(this.cutTitel(movie.getTitel()));

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

        this.showIfHot(movie);
    }

    private void resetComponents()
    {
        this.titelTextView.setText("");
        this.standortTextView.setText("");
        this.wocheTextView.setText("");
        this.wochentagTextView.setText("");

        this.resetViewBg();

        this.titelTextView.setTextColor(Color.parseColor(TEXT_RESET_COLOR));
        this.standortTextView.setTextColor(Color.parseColor(TEXT_RESET_COLOR));
        this.wochentagTextView.setTextColor(Color.parseColor(TEXT_RESET_COLOR));
        this.wocheTextView.setTextColor(Color.parseColor(TEXT_RESET_COLOR));
    }

    private void showIfHot(Movie movie)
    {
        if(movie.isHot())
        {
            Movie.Status status = movie.getStatus();
            if(status != null)
            {
                this.switchColors(movie.getStatus(), movie.getVorbestellungen());
            }
            this.standortTextView.setText(this.standortTextView.getText() + " 🔥");
        }
    }

    private String cutTitel(String titel)
    {
        if(titel.length() > MAX_TITEL_LENGTH)
        {
            StringBuilder builder = new StringBuilder(titel);
            builder.delete(MAX_TITEL_LENGTH, titel.length());
            builder.append("...");
            titel = builder.toString();
        }

        return titel;
    }

    // --- --- --- Color showing --- --- ---

    private void showColor(Movie movie)
    {
        this.paintStatus(movie.getStatus(), movie.getVorbestellungen());
    }

    private void resetViewBg()
    {
        this.view.setBackgroundColor(Color.parseColor(RESET_COLOR));
    }

    private void paintStatus(Movie.Status status, int vorbestellungen)
    {
        this.view.setBackgroundColor(Color.parseColor(this.getColor(status, vorbestellungen)));
    }

    private void switchColors(Movie.Status status, int vorbestellungen)
    {
        String bgColor = this.getColor(status, vorbestellungen);
        if(bgColor == null)
        {
            return;
        }

        this.titelTextView.setTextColor(Color.parseColor(bgColor));
        this.standortTextView.setTextColor(Color.parseColor(bgColor));
        this.wochentagTextView.setTextColor(Color.parseColor(bgColor));
        this.wocheTextView.setTextColor(Color.parseColor(bgColor));

        this.view.setBackgroundColor(Color.parseColor(TEXT_RESET_COLOR));
    }

    private String getColor(Movie.Status status, int vorbestellungen)
    {
        switch(status)
        {
            case VERFUEGBAR:
                return VERFUEGBAR_COLOR;
            case ENTLIEHEN:
                if(vorbestellungen == 0)
                {
                    return ENTLIEHEN_COLOR;
                }

                return VORBESTELLT_COLOR;
            case VORBESTELLT:
                return VORBESTELLT_COLOR;
        }

        return null;
    }
}
