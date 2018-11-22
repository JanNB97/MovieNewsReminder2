package com.yellowbite.movienewsreminder2.fragments.ui.recyclerviews;

import android.app.Activity;

import com.yellowbite.movienewsreminder2.datastructures.MovieList;

public class ShowInstantlyRecyclerView extends MovieRecyclerView
{
    public ShowInstantlyRecyclerView(Activity activity, int id, MovieList movieList, boolean isSwipeable, int viewHolderLayout)
    {
        super(activity, id, movieList, isSwipeable, viewHolderLayout);
        this.showItems();
    }
}
