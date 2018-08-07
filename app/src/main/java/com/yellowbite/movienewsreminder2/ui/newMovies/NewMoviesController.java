package com.yellowbite.movienewsreminder2.ui.newMovies;

import android.widget.Button;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.tasks.DeleteLastAndAddTask;
import com.yellowbite.movienewsreminder2.ui.tasks.GetMoviesTask;
import com.yellowbite.movienewsreminder2.ui.tasks.MovieRunnable;

import java.util.ArrayList;
import java.util.List;

public class NewMoviesController
{
    private NewMoviesActivity activity;

    private List<Movie> newMovies;
    private List<Movie> addToMyMovies;

    private TextView movieTitelTextView;

    private Button addToMyMoviesButton;
    private Button nextMovieButton;

    private int displayedMovieId;
    private Movie displayedMovie;

    public NewMoviesController(NewMoviesActivity activity, List<Movie> newMovies)
    {
        this.activity = activity;
        this.newMovies = newMovies;
        addToMyMoviesButton = activity.findViewById(R.id.addToMyMoviesButton);
        nextMovieButton = activity.findViewById(R.id.nextMovieButton);
        this.movieTitelTextView = activity.findViewById(R.id.movieNameTextView);
        this.displayedMovieId = newMovies.size();

        this.addToMyMovies = new ArrayList<>();

        this.showNextMovie();

        nextMovieButton.setOnClickListener(e -> this.handleClickOnNextMovie());
        addToMyMoviesButton.setOnClickListener(e -> this.handleClickOnAddMovie());
    }

    private void handleClickOnAddMovie()
    {
        this.setButtonsEnabled(false);

        new DeleteLastAndAddTask(activity.getApplicationContext(), () -> {
                    addToMyMovies.add(displayedMovie);
                    showNextMovie();
        })
        .execute(this.displayedMovie);
    }

    private void handleClickOnNextMovie()
    {
        this.setButtonsEnabled(false);

        new DeleteLastAndAddTask(activity.getApplicationContext(),
                this::showNextMovie)
        .execute();
    }

    private void showNextMovie()
    {
        this.displayedMovieId--;
        if(this.displayedMovieId < 0)
        {
            activity.showMainActivity(this.addToMyMovies);
            return;
        }

        this.displayedMovie = newMovies.get(this.displayedMovieId);
        new GetMoviesTask(new MovieRunnable()
        {
            @Override
            public void run(Movie movie)
            {
                showMovie(movie);
                setButtonsEnabled(true);
            }
        })
        .execute(this.displayedMovie);
    }

    private void showMovie(Movie movie)
    {
        this.movieTitelTextView.setText(movie.getTitel());
    }

    private void setButtonsEnabled(boolean b)
    {
        this.nextMovieButton.setEnabled(b);
        this.addToMyMoviesButton.setEnabled(b);
    }
}
