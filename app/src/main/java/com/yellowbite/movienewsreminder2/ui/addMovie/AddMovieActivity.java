package com.yellowbite.movienewsreminder2.ui.addMovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.otherDatastructures.SearchMovieList;
import com.yellowbite.movienewsreminder2.tasks.SimpleAsyncTask;
import com.yellowbite.movienewsreminder2.tasks.loadMovieList.LoadMovieListExecutor;
import com.yellowbite.movienewsreminder2.ui.NoTitleBarActivity;
import com.yellowbite.movienewsreminder2.ui.recyclerView.MovieRecyclerView;
import com.yellowbite.movienewsreminder2.ui.recyclerView.UnalterableRecyclerView;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

public class AddMovieActivity extends NoTitleBarActivity
{
    private TextView searchTextView;
    private Button searchMovieButton;

    private UnalterableRecyclerView searchMovieRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_add_movie);

        this.searchTextView = this.findViewById(R.id.searchTextView);
        this.searchMovieButton = this.findViewById(R.id.searchMovieButton);

        this.initSearchMovieButton();

        this.searchMovieRecyclerView = new UnalterableRecyclerView(this, R.id.movieRecyclerView,
                SearchMovieList.getInstance());
    }

    private void initSearchMovieButton()
    {
        this.searchMovieButton.setOnClickListener(
                v -> this.handleClickedOnSearchMovie(searchTextView.getText().toString()));
    }

    // --- --- --- Handle user interaction --- --- ---
    private void handleClickedOnSearchMovie(String searchText)
    {
        //this.setUserInteractionEnabled(false);
        SearchMovieList.getInstance().clear();
        this.searchMovieRecyclerView.dataSetChanged(false);

        String searchURL = WebscrapingHelper.getWideSearchURL(searchText);
        new LoadMovieListExecutor(this, searchURL,
                () -> this.searchMovieRecyclerView.dataSetChanged(false),
                this::onSiteScraped);
    }

    private void onSiteScraped()
    {
        this.searchMovieRecyclerView.dataSetChanged(false);

        this.setUserInteractionEnabled(true);
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
