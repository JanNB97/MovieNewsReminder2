package com.yellowbite.movienewsreminder2.ui.recycler;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.yellowbite.movienewsreminder2.ui.recycler.MovieAdapter;

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