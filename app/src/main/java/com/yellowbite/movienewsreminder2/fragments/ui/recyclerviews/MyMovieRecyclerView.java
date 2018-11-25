package com.yellowbite.movienewsreminder2.fragments.ui.recyclerviews;

import android.app.Activity;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.datastructures.MovieList;
import com.yellowbite.movienewsreminder2.data.Movie;

public class MyMovieRecyclerView extends MovieRecyclerView
{
    public MyMovieRecyclerView(Activity activity, int id, MovieList movieList)
    {
        super(activity, id, true, R.layout.movie_list_row, movieList);
    }

    @Override
    protected void handleClickedLongOnMovieItem(View view, int position)
    {
        if(super.recentlySwiped)
        {
            return;
        }

        Movie movie = super.movieLists[super.currentMovieListId].get(position);
        movie.setHot(super.activity, !movie.isHot());
        this.dataSetChanged();

        super.handleClickedLongOnMovieItem(view, position);
    }
}
