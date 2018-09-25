package com.yellowbite.movienewsreminder2.ui.newMovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.MovieFileHelper;
import com.yellowbite.movienewsreminder2.files.data.NewMoviesQueue;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.tasks.LoadedMovieEvent;
import com.yellowbite.movienewsreminder2.tasks.newMovies.DelLastAndAddAsyncTask;
import com.yellowbite.movienewsreminder2.tasks.newMovies.LoadNewMoviesDescendingExecutor;
import com.yellowbite.movienewsreminder2.ui.NoTitleBarActivity;

public class NewMoviesActivity extends NoTitleBarActivity implements LoadedMovieEvent
{
    private TextView movieTitelTextView;
    private TextView einheitstitelTextView;

    private Button addToMyMoviesButton;
    private Button nextMovieButton;

    private ImageView movieImageView;

    private boolean[] movieIsLoaded;

    private int displayedMovieId;
    private Movie displayedMovie;

    // --- --- --- initialization --- --- ---
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_new_movies);

        this.initialize();
    }

    public void initialize()
    {
        addToMyMoviesButton = this.findViewById(R.id.addToMyMoviesButton);
        nextMovieButton = this.findViewById(R.id.nextMovieButton);
        this.movieTitelTextView = this.findViewById(R.id.movieNameTextView);
        this.displayedMovieId = NewMoviesQueue.size(this);
        this.movieImageView = this.findViewById(R.id.movieImageView);
        this.einheitstitelTextView = this.findViewById(R.id.einheitstitelTextView);

        new LoadNewMoviesDescendingExecutor(this, this, NewMoviesQueue.getAll(this));
        this.movieIsLoaded = new boolean[NewMoviesQueue.size(this)];

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

        new DelLastAndAddAsyncTask(this.getApplicationContext(), this::tryToShowNextMovie)
                .execute(this.displayedMovie);
    }

    private void handleClickOnNextMovie()
    {
        this.setButtonsEnabled(false);

        new DelLastAndAddAsyncTask(this.getApplicationContext(),
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
        this.startActivity(intent);
    }

    private void tryToShowNextMovie()
    {
        this.displayedMovieId--;
        if(this.displayedMovieId < 0)
        {
            this.showMainActivity();
            return;
        }

        if(this.movieIsLoaded[this.displayedMovieId])
        {
            this.showNextMovie();
        }
    }

    private void showNextMovie()
    {
        this.displayedMovie = NewMoviesQueue.get(this, this.displayedMovieId);

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

    // --- --- --- switching between activities --- --- ---
    private void showMainActivity()
    {
        Intent returnIntent = new Intent();
        this.setResult(Activity.RESULT_OK, returnIntent);

        this.finish();
    }

    public static final int REQUEST_CODE = 1;

    public static void startForResult(AppCompatActivity app)
    {
        Intent intent = new Intent(app, NewMoviesActivity.class);
        app.startActivityForResult(intent, REQUEST_CODE);
    }
}
