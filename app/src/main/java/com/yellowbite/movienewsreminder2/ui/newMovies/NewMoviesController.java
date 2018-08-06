package com.yellowbite.movienewsreminder2.ui.newMovies;

import android.widget.Button;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.tasks.DeleteLastNewMovieTask;

import java.util.List;

public class NewMoviesController
{
    NewMoviesActivity activity;

    private List<Movie> newMovies;

    private TextView movieTitelTextView;

    private int displayedMovieId;
    private Movie displayedMovie;

    public NewMoviesController(NewMoviesActivity activity, List<Movie> newMovies)
    {
        this.activity = activity;

        this.newMovies = newMovies;

        Button addToMyMoviesButton = activity.findViewById(R.id.addToMyMoviesButton);
        addToMyMoviesButton.setOnClickListener(e -> this.handleClickOnAddMovie());

        Button nextMovieButton = activity.findViewById(R.id.nextMovieButton);
        nextMovieButton.setOnClickListener(e -> this.handleClickOnNextMovie());

        this.movieTitelTextView = activity.findViewById(R.id.movieNameTextView);

        this.displayedMovieId = newMovies.size();
        this.showNextMovie();
    }

    private void handleClickOnAddMovie()
    {
        // TODO - execute add movie task
        this.showNextMovie();
    }

    private void handleClickOnNextMovie()
    {
        new DeleteLastNewMovieTask(activity.getApplicationContext(),
                this::showNextMovie).execute();
    }

    private void showNextMovie()
    {
        this.displayedMovieId--;
        if(this.displayedMovieId < 0)
        {
            activity.showMainActivity();
        }

        this.displayedMovie = newMovies.get(this.displayedMovieId);
        this.showMovie(displayedMovie);
    }

    private void showMovie(Movie movie)
    {
        this.movieTitelTextView.setText(movie.getTitel());
    }
}
