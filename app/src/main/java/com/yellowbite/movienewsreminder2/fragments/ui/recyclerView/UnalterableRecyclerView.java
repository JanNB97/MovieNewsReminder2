package com.yellowbite.movienewsreminder2.fragments.ui.recyclerView;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;

public class UnalterableRecyclerView extends MovieRecyclerView
{
    public UnalterableRecyclerView(Activity activity, int id, MovieList movieList, int viewHolderLayout)
    {
        super(activity, id, movieList, false, viewHolderLayout);
        this.showItems();
    }
}
