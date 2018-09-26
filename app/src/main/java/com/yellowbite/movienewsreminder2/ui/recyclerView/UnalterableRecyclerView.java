package com.yellowbite.movienewsreminder2.ui.recyclerView;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yellowbite.movienewsreminder2.files.datastructures.MovieList;

public class UnalterableRecyclerView extends MovieRecyclerView
{
    public UnalterableRecyclerView(AppCompatActivity activity, int id, MovieList movieList)
    {
        super(activity, id, movieList, false);
    }

    @Override
    protected void handleClickedOnMovieItem(View view, int position)
    {
        // TODO
    }

    @Override
    protected void handleClickedLongOnMovieItem(View view, int position)
    {

    }
}
