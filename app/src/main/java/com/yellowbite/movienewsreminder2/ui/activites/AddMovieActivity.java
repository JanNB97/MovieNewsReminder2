package com.yellowbite.movienewsreminder2.ui.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.MySortedMovieList;
import com.yellowbite.movienewsreminder2.files.datatypes.otherDatastructures.SearchMovieList;
import com.yellowbite.movienewsreminder2.tasks.loadMovieList.LoadMovieListExecutor;
import com.yellowbite.movienewsreminder2.ui.recyclerView.AddMovieRecyclerView;

public class AddMovieActivity extends ToolbarActivity
{
    private TextView searchTextView;
    private Button searchMovieButton;

    private AddMovieRecyclerView addMovieRecyclerView;

    private LoadMovieListExecutor searchExecutor;

    private ProgressBar searchProgressIndicator;

    // --- --- --- Initialization --- --- ---
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_add_movie);

        this.findViewsById();
        this.initialize();
    }

    private void initialize()
    {
        this.showBackArrow();

        this.initSearchTextView();
        this.initRecyclerView();

        this.searchExecutor = new LoadMovieListExecutor(this,
                this::onSiteScraped, this::onScrapingFinished);
    }

    private void findViewsById()
    {
        this.searchTextView             = this.findViewById(R.id.searchTextView);
        this.searchMovieButton          = this.findViewById(R.id.searchMovieButton);
        this.searchProgressIndicator    = this.findViewById(R.id.searchProgressIndicator);
    }

    private void initRecyclerView()
    {
        this.addMovieRecyclerView = new AddMovieRecyclerView(this,
                R.id.movieRecyclerView, SearchMovieList.getInstance(),
                MySortedMovieList.getInstance(this));
        this.addMovieRecyclerView.setOnClickedListener((v, position) -> this.openMainActivity());
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

    private void showBackArrow()
    {
        if(this.getSupportActionBar() != null)
        {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    // --- --- --- On finished --- --- ---
    public void openMainActivity()
    {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    // --- --- --- Toolbar interaction --- --- ---
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // --- --- --- Start me from another activity --- --- ---
    public static final int REQUEST_CODE = 2;

    public static void startForResult(AppCompatActivity app)
    {
        Intent intent = new Intent(app, AddMovieActivity.class);
        app.startActivityForResult(intent, REQUEST_CODE);
    }
}
