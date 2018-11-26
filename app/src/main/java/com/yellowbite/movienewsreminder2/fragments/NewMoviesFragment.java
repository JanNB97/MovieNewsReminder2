package com.yellowbite.movienewsreminder2.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted.SortedMyMoviesList;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted.NewMovieQueue;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted.SortedBookedMovieList;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.newsservice.NewsService;
import com.yellowbite.movienewsreminder2.tasks.functionalinterfaces.LoadedMovieEvent;
import com.yellowbite.movienewsreminder2.tasks.newmovies.DelLastAndAddAsyncTask;
import com.yellowbite.movienewsreminder2.tasks.newmovies.LoadNewMoviesDescendingExecutor;

public class NewMoviesFragment extends ToolbarFragment implements LoadedMovieEvent
{
    public static final int FRAGMENT_ID = 2;

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

    // --- --- --- Initialization --- --- ---
    public NewMoviesFragment()
    {
        super(FRAGMENT_ID, R.layout.fragment_new_movies, -1, "Neue Filme");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        this.findViewsById();
        super.onViewCreated(view, savedInstanceState);
    }

    private void findViewsById()
    {
        this.addToMyMoviesButton    = this.getView().findViewById(R.id.addToMyMoviesButton);
        this.nextMovieButton        = this.getView().findViewById(R.id.nextMovieButton);
        this.movieTitelTextView     = this.getView().findViewById(R.id.movieNameTextView);
        this.movieImageView         = this.getView().findViewById(R.id.movieImageView);
        this.einheitstitelTextView  = this.getView().findViewById(R.id.einheitstitelTextView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        this.initialize();
        super.onActivityCreated(savedInstanceState);
    }

    private void initialize()
    {
        this.nextMovieLabel = this.nextMovieButton.getText().toString();

        this.displayedMovieId = NewMovieQueue.getInstance(this.getContext()).size();

        new LoadNewMoviesDescendingExecutor(this.getActivity(), this, NewMovieQueue.getInstance(this.getContext()).getAll());
        this.movieIsLoaded = new boolean[NewMovieQueue.getInstance(this.getContext()).size()];

        this.tryToShowNextMovie();

        this.initUiListeners();
    }

    private void initUiListeners()
    {
        this.movieTitelTextView.setOnClickListener(this::handleClickOnTitel);
        this.movieImageView.setOnClickListener(this::handleClickOnTitel);
        this.einheitstitelTextView.setOnClickListener(this::handleClickOnTitel);

        this.addToMyMoviesButton.setOnClickListener(this::handleClickOnAddMovie);
        this.nextMovieButton.setOnClickListener(this::handleClickOnNextMovie);
    }

    // --- --- --- handle clicks --- --- ---
    public void handleClickOnAddMovie(View v)
    {
        this.setButtonsEnabled(false);

        DelLastAndAddAsyncTask.delLastAndAdd(this.getContext(),
                /* Movie to add: */ this.displayedMovie,
                /* Executed after tasks finished: */this::tryToShowNextMovie);
    }

    public void handleClickOnNextMovie(View v)
    {
        this.setButtonsEnabled(false);
        DelLastAndAddAsyncTask.delLast(this.getContext(), this::tryToShowNextMovie);
    }

    public void handleClickOnTitel(View v)
    {
        String searchword = movieTitelTextView.getText().toString();
        String einheitssachtitel = einheitstitelTextView.getText().toString();

        if(!einheitssachtitel.isEmpty())
        {
            searchword = einheitssachtitel;
        }

        this.openInternetBrowserToSearch(searchword);
    }

    private void openInternetBrowserToSearch(String searchword)
    {
        Uri uri = Uri.parse("https://www.google.com/#q=" + searchword + " wikipedia english");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }

    // --- --- --- Show next movie --- --- ---
    private void tryToShowNextMovie()
    {
        this.displayedMovieId--;
        if(this.displayedMovieId < 0)
        {
            this.sendShowFragmentRequest(MyMoviesFragment.FRAGMENT_ID);
            return;
        }

        if(this.movieIsLoaded[this.displayedMovieId])
        {
            this.showNextMovie();
        }
    }

    private void showNextMovie()
    {
        this.displayedMovie = NewMovieQueue.getInstance(this.getContext()).get(this.displayedMovieId);

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

        boolean movieAlreadyInMyMovies = SortedMyMoviesList.getInstance(this.getContext()).contains(movie);

        if(movie.getStatus() == Movie.Status.IN_BEARBEITUNG)
        {
            this.setDecisionEnabled(!movieAlreadyInMyMovies);
        }
        else
        {
            boolean bookedMovieArrived = SortedBookedMovieList.getInstance(this.getContext()).remove(movie);
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

    protected void sendShowFragmentRequest(int fragmentId)
    {
        NewsService.start(super.getActivity());
        FragmentMaster.sendShowFragmentRequest(super.getContext(), fragmentId);
    }
}
