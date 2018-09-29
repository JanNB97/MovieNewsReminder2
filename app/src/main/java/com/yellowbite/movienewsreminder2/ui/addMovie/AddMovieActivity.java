package com.yellowbite.movienewsreminder2.ui.addMovie;

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
import com.yellowbite.movienewsreminder2.files.datatypes.otherDatastructures.SearchMovieList;
import com.yellowbite.movienewsreminder2.tasks.loadMovieList.LoadMovieListExecutor;
import com.yellowbite.movienewsreminder2.ui.ToolbarActivity;
import com.yellowbite.movienewsreminder2.ui.recyclerView.AddMovieRecyclerView;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

public class AddMovieActivity extends ToolbarActivity
{
    private TextView searchTextView;
    private Button searchMovieButton;

    private AddMovieRecyclerView addMovieRecyclerView;

    private LoadMovieListExecutor searchExecutor;

    private ProgressBar searchProgressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_add_movie);

        this.setTitle("Filme hinzufügen");

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.searchTextView = this.findViewById(R.id.searchTextView);
        this.searchMovieButton = this.findViewById(R.id.searchMovieButton);
        this.searchProgressIndicator = this.findViewById(R.id.searchProgressIndicator);
        this.searchProgressIndicator.setVisibility(View.GONE);

        this.initSearchMovieButton();

        this.addMovieRecyclerView = new AddMovieRecyclerView(this, R.id.movieRecyclerView,
                SearchMovieList.getInstance());

        this.searchExecutor = new LoadMovieListExecutor(this,
                this::onSiteScraped, this::onScrapingFinished);
    }

    private void initSearchMovieButton()
    {
        this.searchMovieButton.setOnClickListener(
                v -> this.handleClickedOnSearchMovie(searchTextView.getText().toString()));
    }

    // --- --- --- Handle user interaction --- --- ---
    private void handleClickedOnSearchMovie(String searchText)
    {
        this.searchProgressIndicator.setVisibility(View.VISIBLE);
        this.setUserInteractionEnabled(false);
        SearchMovieList.getInstance().clear();
        this.addMovieRecyclerView.dataSetChanged(false);

        String searchURL = WebscrapingHelper.getWideSearchURL(searchText);
        this.searchExecutor.startToLoadMovieList(searchURL);
    }

    private void onSiteScraped()
    {
        this.addMovieRecyclerView.dataSetChanged(false);
    }

    private void onScrapingFinished()
    {
        this.addMovieRecyclerView.dataSetChanged(false);

        this.setUserInteractionEnabled(true);
        this.searchProgressIndicator.setVisibility(View.GONE);
        this.searchTextView.setText("");
    }

    private void setUserInteractionEnabled(boolean enabled)
    {
        this.searchMovieButton.setEnabled(enabled);
        this.searchTextView.setEnabled(enabled);
    }

    // --- --- --- on finished --- --- ---
    public void openMainActivity()
    {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    // --- --- --- toolbar interaction --- --- ---
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

    // --- --- --- start me from another activity --- --- ---
    public static final int REQUEST_CODE = 2;

    public static void startForResult(AppCompatActivity app)
    {
        Intent intent = new Intent(app, AddMovieActivity.class);
        app.startActivityForResult(intent, REQUEST_CODE);
    }
}
