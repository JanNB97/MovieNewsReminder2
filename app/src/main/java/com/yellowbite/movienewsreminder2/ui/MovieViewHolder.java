package com.yellowbite.movienewsreminder2.ui;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class MovieViewHolder extends RecyclerView.ViewHolder
{
    private TextView textView;

    public MovieViewHolder(TextView v)
    {
        super(v);
        this.textView = v;
        v.setTextSize(20);
        v.setPadding(0, 0, 0, 100);
    }

    public TextView getTextView()
    {
        return textView;
    }
}
