package com.yellowbite.movienewsreminder2.ui.addMovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.otherDatastructures.SearchMovieList;
import com.yellowbite.movienewsreminder2.tasks.loadMovieList.LoadMovieListExecutor;
import com.yellowbite.movienewsreminder2.ui.NoTitleBarActivity;
import com.yellowbite.movienewsreminder2.ui.recyclerView.UnalterableRecyclerView;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

public class AddMovieActivity extends NoTitleBarActivity
{
    private TextView searchTextView;
    private Button searchMovieButton;

    private UnalterableRecyclerView searchMovieRecyclerView;

    private LoadMovieListExecutor searchExecutor;

    private ProgressBar searchProgressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_add_movie);

        this.searchTextView = this.findViewById(R.id.searchTextView);
        this.searchMovieButton = this.findViewById(R.id.searchMovieButton);
        this.searchProgressIndicator = this.findViewById(R.id.searchProgressIndicator);
        this.searchProgressIndicator.setVisibility(View.GONE);

        this.initSearchMovieButton();

        this.searchMovieRecyclerView = new UnalterableRecyclerView(this, R.id.movieRecyclerView,
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
        this.searchMovieRecyclerView.dataSetChanged(false);

        String searchURL = WebscrapingHelper.getWideSearchURL(searchText);
        this.searchExecutor.startToLoadMovieList(searchURL);
    }

    private void onSiteScraped()
    {
        this.searchMovieRecyclerView.dataSetChanged(false);
    }

    private void onScrapingFinished()
    {
        this.searchMovieRecyclerView.dataSetChanged(false);

        this.setUserInteractionEnabled(true);
        this.searchProgressIndicator.setVisibility(View.GONE);
        this.searchTextView.setText("");
    }

    private void setUserInteractionEnabled(boolean enabled)
    {
        this.searchMovieButton.setEnabled(enabled);
        this.searchTextView.setEnabled(enabled);
    }

    // --- --- --- start me from another activity --- --- ---
    public static int REQUEST_CODE = 2;

    public static void startForResult(AppCompatActivity app)
    {
        Intent intent = new Intent(app, AddMovieActivity.class);
        app.startActivityForResult(intent, REQUEST_CODE);
    }
}
