package com.yellowbite.movienewsreminder2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.NotificationMan;
import com.yellowbite.movienewsreminder2.ui.newMovies.NewMoviesActivity;
import com.yellowbite.movienewsreminder2.ui.recycler.RecyclerTouchListener;
import com.yellowbite.movienewsreminder2.ui.recycler.SwipeCallback;
import com.yellowbite.movienewsreminder2.ui.tasks.GetMoviesDescendingNotifier;
import com.yellowbite.movienewsreminder2.ui.tasks.GetMoviesTask;
import com.yellowbite.movienewsreminder2.ui.recycler.MovieAdapter;
import com.yellowbite.movienewsreminder2.ui.tasks.LoadedMovieEvent;
import com.yellowbite.movienewsreminder2.ui.tasks.MovieRunnable;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements LoadedMovieEvent
{
    private static final int REQUEST_CODE = 1;

    private RecyclerView movieRecyclerView;
    private TextView urlTextView;
    private Button addMovieButton;
    private ProgressBar loadingProgressBar;
    private TextView moviesUpdateTextView;
    private AtomicInteger loadedMovies = new AtomicInteger(0);

    private List<Movie> myMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.removeTitleBar();
        setContentView(R.layout.activity_main);

        this.launchNewMoviesActivity();

        this.myMovies = new ArrayList<>();

        this.loadingProgressBar = this.findViewById(R.id.loadingProgressBar);
        this.moviesUpdateTextView = this.findViewById(R.id.moviesUpdateTextView);
        this.initAddMovieButton();
        this.initURLTextView();
        this.initRecyclerView();

        this.loadMyMovies();

        NewsService.start(this);
    }

    // --- --- --- Open NewMoviesActivity --- --- ---

    private void launchNewMoviesActivity()
    {
        if(!MedZenFileMan.newMoviesIsEmpty(this))
        {
            Intent intent = new Intent(this, NewMoviesActivity.class);
            this.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String result = data.getStringExtra("result");
                List<Movie> resultMovies = MedZenFileMan.toStatusMovies(result);
                if(resultMovies != null && !resultMovies.isEmpty())
                {
                    ((MovieAdapter)movieRecyclerView.getAdapter()).addItems(resultMovies, false);
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                // TODO
            }
        }
    }

    // --- --- --- Initialization of UI --- --- ---

    private void removeTitleBar()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        ActionBar actionBar = getSupportActionBar(); //hide the title bar
        if(actionBar != null)
        {
            actionBar.hide();
        }
    }

    private void initAddMovieButton()
    {
        this.addMovieButton = findViewById(R.id.addMovieButton);
        this.addMovieButton.setOnClickListener(this::handleOnAddMovieClicked);
    }

    private void initURLTextView()
    {
        this.urlTextView = findViewById(R.id.urlTextView);
        this.urlTextView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                addMovieButton.setEnabled(charSequence.length() != 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void initRecyclerView()
    {
        this.movieRecyclerView = (RecyclerView) findViewById(R.id.movieRecyclerView);
        this.movieRecyclerView.setHasFixedSize(true);

        // use linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.movieRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadMyMovies()
    {
        // load out of file
        MedZenFileMan.getMyMovies(this, this.myMovies);
        this.loadingProgressBar.setMax(this.myMovies.size());

        // download status
        new GetMoviesDescendingNotifier(this, this, this.myMovies);
    }

    @Override
    public void loadedMovie(int id)
    {
        int l = this.loadedMovies.incrementAndGet();
        this.loadingProgressBar.setProgress(l);

        if(l >= this.myMovies.size())
        {
            this.onLoadingFinished();
        }
    }

    private void onLoadingFinished()
    {
        Collections.sort(this.myMovies);

        this.loadingProgressBar.setVisibility(View.GONE);
        this.moviesUpdateTextView.setVisibility(View.GONE);
        this.addAdapterToRecyclerView(myMovies);
        this.urlTextView.setEnabled(true);
    }

    private void addAdapterToRecyclerView(List<Movie> myMovies)
    {
        // specify adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, myMovies);
        this.movieRecyclerView.setAdapter(movieAdapter);

        new ItemTouchHelper(new SwipeCallback(movieAdapter)).attachToRecyclerView(this.movieRecyclerView);

        this.movieRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, this.movieRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position)
            {
                movieAdapter.handleClickedOnMovieItem(view, position);
            }

            @Override
            public void onLongClick(View view, int position)
            {
                movieAdapter.handleClickedLongOnMovieItem(view, position);
            }
        }));
    }

    // --- --- --- Interaction with user --- --- ---

    private void handleOnAddMovieClicked(View view)
    {
        Context context = this;
        new GetMoviesTask(new MovieRunnable()
        {
            @Override
            public void run(Movie movie)
            {
                urlTextView.setText("");

                if(movie == null)
                {
                    NotificationMan.showShortToast(context, "Falsche URL oder keine Internetverbindung");
                    return;
                }

                ((MovieAdapter)movieRecyclerView.getAdapter()).addItem(movie, true);
            }
        })
        .execute(new Movie(urlTextView.getText().toString()));
    }
}
