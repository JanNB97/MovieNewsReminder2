package com.yellowbite.movienewsreminder2.fragments.ui.recyclerView;

import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;

public class UnalterableRecyclerView extends MovieRecyclerView
{
    public UnalterableRecyclerView(AppCompatActivity activity, int id, MovieList movieList, int viewHolderLayout)
    {
        super(activity, id, movieList, false, viewHolderLayout);
        this.showItems();
    }
}
