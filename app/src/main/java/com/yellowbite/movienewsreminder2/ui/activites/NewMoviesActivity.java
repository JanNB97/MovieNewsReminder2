package com.yellowbite.movienewsreminder2.ui.activites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.MyMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.NewMoviesQueue;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.SortedBookedMoviesList;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.tasks.functionalInterfaces.LoadedMovieEvent;
import com.yellowbite.movienewsreminder2.tasks.newMovies.DelLastAndAddAsyncTask;
import com.yellowbite.movienewsreminder2.tasks.newMovies.LoadNewMoviesDescendingExecutor;

public class NewMoviesActivity extends ToolbarActivity implements LoadedMovieEvent
{
    private TextView movieTitelTextView;
    private TextView einheitstitelTextView;

    private Button addToMyMoviesButton;
    private Button nextMovieButton;

    private ImageView movieImageView;

    private boolean[] movieIsLoaded;

    private int displayedMovieId;
    private Movie displayedMovie;

    private String nextMovieLabel;

    private static final String DECISION_DISABLED_LABEL = "OK";
    private static final String IN_BEARBEITUNG_ARRIVED_LABEL = "war " + Movie.Status.IN_BEARBEITUNG.getValue();

    // --- --- --- initialization --- --- ---
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_new_movies);

        this.findViewsById();
        this.initialize();
    }

    public void initialize()
    {
        nextMovieLabel = this.nextMovieButton.getText().toString();

        this.displayedMovieId = NewMoviesQueue.getInstance(this).size();

        new LoadNewMoviesDescendingExecutor(this, this, NewMoviesQueue.getInstance(this).getAll());
        this.movieIsLoaded = new boolean[NewMoviesQueue.getInstance(this).size()];

        this.tryToShowNextMovie();
    }

    private void findViewsById()
    {
        this.addToMyMoviesButton    = this.findViewById(R.id.addToMyMoviesButton);
        this.nextMovieButton        = this.findViewById(R.id.nextMovieButton);
        this.movieTitelTextView     = this.findViewById(R.id.movieNameTextView);
        this.movieImageView         = this.findViewById(R.id.movieImageView);
        this.einheitstitelTextView  = this.findViewById(R.id.einheitstitelTextView);
    }

    // --- --- --- handle clicks --- --- ---

    public void handleClickOnAddMovie(View v)
    {
        this.setButtonsEnabled(false);

        DelLastAndAddAsyncTask.delLastAndAdd(this.getApplicationContext(),
                /* Movie to add: */ this.displayedMovie,
                /* Executed after tasks finished: */this::tryToShowNextMovie);
    }

    public void handleClickOnNextMovie(View v)
    {
        this.setButtonsEnabled(false);
        DelLastAndAddAsyncTask.delLast(this.getApplicationContext(), this::tryToShowNextMovie);
    }

    public void handleClickOnTitel(View v)
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
        this.displayedMovie = NewMoviesQueue.getInstance(this).get(this.displayedMovieId);

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

        boolean movieAlreadyInMyMovies = MyMoviesSortedList.getInstance(this).contains(movie);

        if(movie.getStatus() == Movie.Status.IN_BEARBEITUNG)
        {
            this.setDecisionEnabled(!movieAlreadyInMyMovies);
        }
        else
        {
            boolean bookedMovieArrived = SortedBookedMoviesList.getInstance(this).containsAndRemove(movie);
            this.setDecisionEnabled(!bookedMovieArrived && !movieAlreadyInMyMovies);
            if(bookedMovieArrived)
            {
                this.movieTitelTextView.append(" (" + IN_BEARBEITUNG_ARRIVED_LABEL + ")");
            }
        }
    }

    private void setDecisionEnabled(boolean movieBooked)
    {
        this.addToMyMoviesButton.setVisibility(movieBooked ? View.VISIBLE : View.GONE);
        this.nextMovieButton.setText(movieBooked ? nextMovieLabel : DECISION_DISABLED_LABEL);
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
