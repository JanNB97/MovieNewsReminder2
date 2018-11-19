package com.yellowbite.movienewsreminder2.fragments.ui.recyclerview;

import android.app.Activity;

import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;

public class UnalterableRecyclerView extends MovieRecyclerView
{
    public UnalterableRecyclerView(Activity activity, int id, MovieList movieList, int viewHolderLayout)
    {
        super(activity, id, movieList, false, viewHolderLayout);
        this.showItems();
    }
}
