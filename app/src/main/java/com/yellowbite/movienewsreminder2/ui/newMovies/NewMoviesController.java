package com.yellowbite.movienewsreminder2.ui.newMovies;

import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.data.NewMoviesQueue;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.tasks.newMovies.DelLastAndAddAsyncTask;
import com.yellowbite.movienewsreminder2.tasks.newMovies.LoadNewMoviesDescendingExecutor;
import com.yellowbite.movienewsreminder2.tasks.LoadedMovieEvent;

import java.net.URLEncoder;

public class NewMoviesController implements LoadedMovieEvent
{
    private NewMoviesActivity activity;

    private TextView movieTitelTextView;
    private TextView einheitstitelTextView;

    private Button addToMyMoviesButton;
    private Button nextMovieButton;

    private ImageView movieImageView;

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
        this.movieImageView = this.activity.findViewById(R.id.movieImageView);
        this.einheitstitelTextView = this.activity.findViewById(R.id.einheitstitelTextView);

        new LoadNewMoviesDescendingExecutor(this.activity, this, NewMoviesQueue.getAll(activity));
        this.movieIsLoaded = new boolean[NewMoviesQueue.size(activity)];

        this.tryToShowNextMovie();

        nextMovieButton.setOnClickListener(e -> this.handleClickOnNextMovie());
        addToMyMoviesButton.setOnClickListener(e -> this.handleClickOnAddMovie());

        movieTitelTextView.setOnClickListener(e -> this.handleClickOnTitel());
        einheitstitelTextView.setOnClickListener(e -> this.handleClickOnTitel());
    }

    // --- --- --- handle clicks --- --- ---

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

    private void handleClickOnTitel()
    {
        String searchword = movieTitelTextView.getText().toString();
        String einheitssachtitel = einheitstitelTextView.getText().toString();
        if(!einheitssachtitel.isEmpty())
        {
            searchword = einheitssachtitel;
        }
        Uri uri = Uri.parse("https://www.google.com/#q=" + searchword + " wikipedia english");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
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
        this.movieImageView.setImageBitmap(movie.getImageBitmap());

        String einheitstitel = movie.getEinheitstitel();
        if(einheitstitel != null)
        {
            this.einheitstitelTextView.setText(einheitstitel);
        }
        else
        {
            this.einheitstitelTextView.setText("");
        }
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
