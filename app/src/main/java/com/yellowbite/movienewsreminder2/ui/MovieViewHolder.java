package com.yellowbite.movienewsreminder2.ui;

import android.graphics.Color;
import android.net.LinkAddress;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.model.Movie;

public class MovieViewHolder extends RecyclerView.ViewHolder
{
    private LinearLayout linearLayout;

    private TextView titelTextView;
    private TextView statusTextView;

    private Button hotMovieButton;

    public MovieViewHolder(LinearLayout linearLayout)
    {
        super(linearLayout);

        this.initLinearLayout(linearLayout);

        this.initTitelTextView();
        this.initStatusTextView();
        this.initHotMovieButton();
    }

    private void initLinearLayout(LinearLayout linearLayout)
    {
        this.linearLayout = linearLayout;
        linearLayout.setPadding(0, 0, 0, 100);
    }

    private void initTitelTextView()
    {
        this.titelTextView = new TextView(linearLayout.getContext());
        initTextView(titelTextView);
        linearLayout.addView(titelTextView);
    }

    private void initStatusTextView()
    {
        this.statusTextView = new TextView(linearLayout.getContext());

        initTextView(this.statusTextView);

        linearLayout.addView(statusTextView);
    }

    private void initHotMovieButton()
    {
        this.hotMovieButton = new Button(linearLayout.getContext());
        this.hotMovieButton.setText("hot");
        this.initView(this.hotMovieButton);
        this.linearLayout.addView(hotMovieButton);
    }

    private void initTextView(TextView textView)
    {
        textView.setTextSize(20);
        this.initView(textView);
    }

    private void initView(View view)
    {

    }

    public void showMovie(Movie movie)
    {
        titelTextView.setText(movie.getTitel());
        statusTextView.setText(movie.getStatus().getValue());
    }
}
