package com.yellowbite.movienewsreminder2.fragments.ui.recyclerView;

import android.app.Activity;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.SortedHotMovieList;
import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.data.Movie;

public class MyMovieRecyclerView extends MovieRecyclerView
{
    public MyMovieRecyclerView(Activity activity, int id, MovieList movieList)
    {
        super(activity, id, movieList, true, R.layout.movie_list_row);
    }

    @Override
    protected void handleClickedLongOnMovieItem(View view, int position)
    {
        if(super.recentlySwiped)
        {
            return;
        }

        Movie movie = super.movieList.get(position);
        if(SortedHotMovieList.getInstance(super.activity).switchHot(movie))
        {
            this.dataSetChanged();
        }
        super.handleClickedLongOnMovieItem(view, position);
    }
}
