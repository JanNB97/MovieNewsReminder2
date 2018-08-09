package com.yellowbite.movienewsreminder2.ui.newMovies;

import android.widget.Button;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.tasks.newMovies.DelLastAndAddAsyncTask;
import com.yellowbite.movienewsreminder2.tasks.newMovies.LoadNewMoviesDescendingExecutor;
import com.yellowbite.movienewsreminder2.tasks.LoadedMovieEvent;

import java.util.List;

public class NewMoviesController implements LoadedMovieEvent
{
    private NewMoviesActivity activity;

    private List<Movie> newMovies;

    private TextView movieTitelTextView;

    private Button addToMyMoviesButton;
    private Button nextMovieButton;

    private boolean[] movieIsLoaded;

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

        new LoadNewMoviesDescendingExecutor(this.activity, this, this.newMovies);
        this.movieIsLoaded = new boolean[this.newMovies.size()];

        this.tryToShowNextMovie();

        nextMovieButton.setOnClickListener(e -> this.handleClickOnNextMovie());
        addToMyMoviesButton.setOnClickListener(e -> this.handleClickOnAddMovie());
    }

    private void handleClickOnAddMovie()
    {
        this.setButtonsEnabled(false);

        new DelLastAndAddAsyncTask(activity.getApplicationContext(), this::tryToShowNextMovie)
            .execute(this.displayedMovie);
    }

    private void handleClickOnNextMovie()
    {
        this.setButtonsEnabled(false);

        new DelLastAndAddAsyncTask(activity.getApplicationContext(),
                this::tryToShowNextMovie)
        .execute();
    }

    private void tryToShowNextMovie()
    {
        this.displayedMovieId--;
        if(this.displayedMovieId < 0)
        {
            this.activity.showMainActivity();
            return;
        }

        if(this.movieIsLoaded[this.displayedMovieId])
        {
            this.showNextMovie();
        }
    }

    private void showNextMovie()
    {
        this.displayedMovie = newMovies.get(this.displayedMovieId);

        showMovie(this.displayedMovie);
        setButtonsEnabled(true);
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

    @Override
    public void loadedMovie(int i)
    {
        this.movieIsLoaded[i] = true;

        if(this.movieIsLoaded[this.displayedMovieId] && i == this.displayedMovieId)
        {
            this.showNextMovie();
        }
    }
}
