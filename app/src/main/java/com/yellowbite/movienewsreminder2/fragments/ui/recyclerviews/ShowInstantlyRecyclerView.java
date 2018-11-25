package com.yellowbite.movienewsreminder2.fragments.ui.recyclerviews;

import android.app.Activity;

import com.yellowbite.movienewsreminder2.datastructures.MovieList;

public class ShowInstantlyRecyclerView extends MovieRecyclerView
{
    public ShowInstantlyRecyclerView(Activity activity, int id, boolean isSwipeable, int viewHolderLayout, MovieList movieList)
    {
        super(activity, id, isSwipeable, viewHolderLayout, movieList);
        this.showItems();
    }
}
