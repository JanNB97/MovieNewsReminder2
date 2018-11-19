package com.yellowbite.movienewsreminder2.fragments.ui.recyclerview;

import android.app.Activity;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.files.datatypes.otherdatastructures.SearchMovieList;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.tasks.mainactivity.GetMovieAsyncTask;
import com.yellowbite.movienewsreminder2.notifications.NotificationMan;

public class AddMovieRecyclerView extends UnalterableRecyclerView
{
    private MovieList movieListToAdd;

    public AddMovieRecyclerView(Activity activity, int id, MovieList movieList,
                                MovieList movieListToAdd)
    {
        super(activity, id, movieList, R.layout.simple_movie_list_row);
        this.movieListToAdd = movieListToAdd;
    }

    @Override
    protected void handleClickedOnMovieItem(View view, int position)
    {
        this.recyclerView.setEnabled(false);

        Movie movieToAdd = SearchMovieList.getInstance().get(position);

        GetMovieAsyncTask.getMovie(movieToAdd,
            (Movie movie) -> {
                if(movie == null)
                {
                    NotificationMan.showShortToast(this.activity, "Keine Internetverbindung");
                    this.recyclerView.setEnabled(true);
                }
                else
                {
                    this.movieListToAdd.add(movie);

                    super.handleClickedOnMovieItem(view, position);
                }
        });
    }
}
