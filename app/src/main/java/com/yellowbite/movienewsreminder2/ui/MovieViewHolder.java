package com.yellowbite.movienewsreminder2.ui;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class MovieViewHolder extends RecyclerView.ViewHolder
{
    private TextView textView;

    public MovieViewHolder(TextView v)
    {
        super(v);
        this.textView = v;
    }

    public TextView getTextView()
    {
        return textView;
    }
}
