package com.yellowbite.movienewsreminder2.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Adapter;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;

import java.util.List;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback
{
    private MovieAdapter movieAdapter;

    public SwipeCallback(MovieAdapter movieAdapter)
    {
        super(0, ItemTouchHelper.LEFT);
        this.movieAdapter = movieAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        int position = viewHolder.getAdapterPosition();
        movieAdapter.removeItem(position);
    }
}
