package com.yellowbite.movienewsreminder2.fragments.ui.recyclerviews;

import android.app.Activity;

import com.yellowbite.movienewsreminder2.datastructures.MovieList;

public class ShowInstantlyRecyclerView extends MovieRecyclerView
{
    public ShowInstantlyRecyclerView(Activity activity, int id, boolean isSwipeable, int viewHolderLayout, MovieList...movieLists)
    {
        super(activity, id, isSwipeable, viewHolderLayout, movieLists);
        this.showItems();
    }
}
