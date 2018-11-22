package com.yellowbite.movienewsreminder2.fragments.ui.recyclerviews;

import android.app.Activity;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.datastructures.MovieList;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.tasks.mainactivity.GetMovieAsyncTask;
import com.yellowbite.movienewsreminder2.notifications.NotificationMan;

public class AddMovieRecyclerView extends ShowInstantlyRecyclerView
{
    private MovieList destinationList;
    private MovieList selectableList;

    public AddMovieRecyclerView(Activity activity, int id, MovieList selectableList,
                                MovieList destinationList)
    {
        super(activity, id, selectableList, false, R.layout.simple_movie_list_row);
        this.destinationList = destinationList;
        this.selectableList = selectableList;
    }

    @Override
    protected void handleClickedOnMovieItem(View view, int position)
    {
        this.recyclerView.setEnabled(false);

        Movie movieToAdd = selectableList.get(position);

        GetMovieAsyncTask.getMovie(movieToAdd,
            (Movie movie) -> {
                if(movie == null)
                {
                    NotificationMan.showShortToast(this.activity, "Keine Internetverbindung");
                    this.recyclerView.setEnabled(true);
                }
                else
                {
                    this.destinationList.add(movie);

                    super.handleClickedOnMovieItem(view, position);
                }
        });
    }
}
