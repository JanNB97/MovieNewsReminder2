package com.yellowbite.movienewsreminder2.ui.newMovies;

import android.widget.Button;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.tasks.DeleteLastAndAddTask;
import com.yellowbite.movienewsreminder2.ui.tasks.GetMoviesDescendingNotifier;
import com.yellowbite.movienewsreminder2.ui.tasks.LoadedMovieEvent;

import java.util.ArrayList;
import java.util.List;

public class NewMoviesController implements LoadedMovieEvent
{
    private NewMoviesActivity activity;

    private List<Movie> newMovies;
    private List<Movie> addToMyMovies;

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

        this.addToMyMovies = new ArrayList<>();

        new GetMoviesDescendingNotifier(this.activity, this, this.newMovies);
        this.movieIsLoaded = new boolean[this.newMovies.size()];

        this.tryToShowNextMovie();

        nextMovieButton.setOnClickListener(e -> this.handleClickOnNextMovie());
        addToMyMoviesButton.setOnClickListener(e -> this.handleClickOnAddMovie());
    }

    private void handleClickOnAddMovie()
    {
        this.setButtonsEnabled(false);

        new DeleteLastAndAddTask(activity.getApplicationContext(), () -> {
                    addToMyMovies.add(displayedMovie);
                    tryToShowNextMovie();
        })
        .execute(this.displayedMovie);
    }

    private void handleClickOnNextMovie()
    {
        this.setButtonsEnabled(false);

        new DeleteLastAndAddTask(activity.getApplicationContext(),
                this::tryToShowNextMovie)
        .execute();
    }

    private void tryToShowNextMovie()
    {
        this.displayedMovieId--;
        if(this.displayedMovieId < 0)
        {
            activity.showMainActivity(this.addToMyMovies);
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
    public void loadedMovie(int id)
    {
        this.movieIsLoaded[id] = true;

        if(this.movieIsLoaded[this.displayedMovieId] && id == this.displayedMovieId)
        {
            this.showNextMovie();
        }
    }
}
