package com.yellowbite.movienewsreminder2.fragments.ui.recyclerview.listener;

import android.support.v7.widget.RecyclerView;

import com.yellowbite.movienewsreminder2.data.Movie;

@FunctionalInterface
public interface SwipeListener
{
    void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, Movie lastSwipedMovie);
}
