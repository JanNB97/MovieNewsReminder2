package com.yellowbite.movienewsreminder2.ui.newMovies;

import android.widget.Button;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.data.NewMoviesQueue;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.tasks.newMovies.DelLastAndAddAsyncTask;
import com.yellowbite.movienewsreminder2.tasks.newMovies.LoadNewMoviesDescendingExecutor;
import com.yellowbite.movienewsreminder2.tasks.LoadedMovieEvent;

public class NewMoviesController implements LoadedMovieEvent
{
    private NewMoviesActivity activity;

    private TextView movieTitelTextView;

    private Button addToMyMoviesButton;
    private Button nextMovieButton;

    private boolean[] movieIsLoaded;

    private int displayedMovieId;
    private Movie displayedMovie;

    public NewMoviesController(NewMoviesActivity activity)
    {
        this.activity = activity;
        addToMyMoviesButton = activity.findViewById(R.id.addToMyMoviesButton);
        nextMovieButton = activity.findViewById(R.id.nextMovieButton);
        this.movieTitelTextView = activity.findViewById(R.id.movieNameTextView);
        this.displayedMovieId = NewMoviesQueue.size(activity);

        new LoadNewMoviesDescendingExecutor(this.activity, this, NewMoviesQueue.getAll(activity));
        this.movieIsLoaded = new boolean[NewMoviesQueue.size(activity)];

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
        this.displayedMovie = NewMoviesQueue.get(activity, this.displayedMovieId);

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
