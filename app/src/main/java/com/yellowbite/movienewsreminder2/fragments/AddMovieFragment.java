package com.yellowbite.movienewsreminder2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted.SortedMyMoviesList;
import com.yellowbite.movienewsreminder2.datastructures.other.SearchMovieList;
import com.yellowbite.movienewsreminder2.fragments.ui.toolbar.NavigationDrawerActivity;
import com.yellowbite.movienewsreminder2.tasks.loadmovielist.LoadMovieListExecutor;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerviews.AddMovieRecyclerView;

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
        super(FRAGMENT_ID,
                R.layout.fragment_add_movie,
                NavigationDrawerActivity.NO_ID,
                "Film hinzufügen");
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
        this.addMovieRecyclerView = new AddMovieRecyclerView(this.getActivity(), R.id.movieRecyclerView,
                SearchMovieList.getInstance(), SortedMyMoviesList.getInstance(this.getContext()));
        this.addMovieRecyclerView.setOnClickedListener((v, position)
                -> this.sendShowFragmentRequest(MyMoviesFragment.FRAGMENT_ID));
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

    // --- --- --- Site scraping callbacks --- --- ---
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

    // --- --- --- Ui modification --- --- ---
    private void setUserInteractionEnabled(boolean enabled)
    {
        this.searchMovieButton.setEnabled(enabled);
        this.searchTextView.setEnabled(enabled);
    }
}
