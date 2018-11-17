package com.yellowbite.movienewsreminder2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.MainActivity;
import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.MySortedMovieList;
import com.yellowbite.movienewsreminder2.files.datatypes.otherDatastructures.SearchMovieList;
import com.yellowbite.movienewsreminder2.tasks.loadMovieList.LoadMovieListExecutor;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerView.AddMovieRecyclerView;

public class AddMovieFragment extends ToolbarFragment
{
    protected static final int FRAGMENT_ID = 1;

    private TextView searchTextView;
    private Button searchMovieButton;

    private AddMovieRecyclerView addMovieRecyclerView;

    private LoadMovieListExecutor searchExecutor;

    private ProgressBar searchProgressIndicator;

    // --- --- --- Initialization --- --- ---
    public AddMovieFragment()
    {
        super(FRAGMENT_ID, R.layout.activity_add_movie, "Film hinzufügen");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        this.findViewsById();
        this.initialize();

        super.onViewCreated(view, savedInstanceState);
    }

    private void findViewsById()
    {
        this.searchTextView             = super.getView().findViewById(R.id.searchTextView);
        this.searchMovieButton          = super.getView().findViewById(R.id.searchMovieButton);
        this.searchProgressIndicator    = super.getView().findViewById(R.id.searchProgressIndicator);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        this.initialize();
        super.onActivityCreated(savedInstanceState);
    }

    private void initialize()
    {
        this.initSearchTextView();
        this.initSearchMovieButton();
        this.initRecyclerView();

        this.searchExecutor = new LoadMovieListExecutor(this.getActivity(),
                this::onSiteScraped, this::onScrapingFinished);
    }

    private void initSearchTextView()
    {
        this.searchTextView.setOnKeyListener((view, key, v3) -> {
            if(key == 66)
            {
                this.handleClickedOnSearchMovie(view);
            }
            return false;
        });
    }

    private void initSearchMovieButton()
    {
        this.searchMovieButton.setOnClickListener(this::handleClickedOnSearchMovie);
    }

    private void initRecyclerView()
    {
        this.addMovieRecyclerView = new AddMovieRecyclerView(this.getActivity(),
                R.id.movieRecyclerView, SearchMovieList.getInstance(),
                MySortedMovieList.getInstance(this.getContext()));
        this.addMovieRecyclerView.setOnClickedListener((v, position) -> super.sendShowFragmentRequest(MyMoviesFragment.FRAGMENT_ID));
    }

    // --- --- --- Handle user interaction --- --- ---
    public void handleClickedOnSearchMovie(View v)
    {
        String searchText = this.searchTextView.getText().toString();

        this.searchProgressIndicator.setVisibility(View.VISIBLE);
        this.setUserInteractionEnabled(false);
        SearchMovieList.getInstance().clear();
        this.addMovieRecyclerView.dataSetChanged();

        this.searchExecutor.startToLoadMovieList(searchText);
    }

    private void onSiteScraped(boolean gotResults)
    {
        if(gotResults)
        {
            this.addMovieRecyclerView.dataSetChanged();
        }
    }

    private void onScrapingFinished(boolean successSearch)
    {
        if(successSearch)
        {
            this.addMovieRecyclerView.dataSetChanged();
            this.searchTextView.setText("");
        }

        this.setUserInteractionEnabled(true);
        this.searchProgressIndicator.setVisibility(View.GONE);
    }

    private void setUserInteractionEnabled(boolean enabled)
    {
        this.searchMovieButton.setEnabled(enabled);
        this.searchTextView.setEnabled(enabled);
    }

    // --- --- --- Modify toolbar --- --- ---
    @Override
    public void modifyOptionsMenu()
    {
        super.modifyOptionsMenu();
        this.showBackArrow();
    }

    private void showBackArrow()
    {
        // TODO
    }
}