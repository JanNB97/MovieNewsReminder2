package com.yellowbite.movienewsreminder2.ui.recyclerView;

import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.files.datatypes.otherDatastructures.SearchMovieList;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.tasks.mainActivity.GetMovieAsyncTask;
import com.yellowbite.movienewsreminder2.ui.activites.AddMovieActivity;
import com.yellowbite.movienewsreminder2.ui.notifications.NotificationMan;

public class AddMovieRecyclerView extends UnalterableRecyclerView
{
    private AddMovieActivity addMovieActivity;
    private MovieList movieListToAdd;

    public AddMovieRecyclerView(AddMovieActivity addMovieActivity, int id, MovieList movieList,
                                MovieList movieListToAdd)
    {
        super(addMovieActivity, id, movieList, R.layout.simple_movie_list_row);
        this.addMovieActivity = addMovieActivity;
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
                    NotificationMan.showShortToast(this.addMovieActivity, "Keine Internetverbindung");
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
