package com.yellowbite.movienewsreminder2.ui.recyclerView.listener;

import android.support.v7.widget.RecyclerView;

import com.yellowbite.movienewsreminder2.model.Movie;

@FunctionalInterface
public interface SwipeListener
{
    void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, Movie lastSwipedMovie);
}
