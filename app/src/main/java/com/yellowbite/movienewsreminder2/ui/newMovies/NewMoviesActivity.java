package com.yellowbite.movienewsreminder2.ui.newMovies;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.tasks.GetMoviesTask;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;

import java.util.List;

public class NewMoviesActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.removeTitleBar();
        setContentView(R.layout.activity_new_movies);

        List<Movie> newMovies = MedZenFileMan.getNewMovies(this);
        if(newMovies.size() == 0)
        {
            this.showMainActivity();
        }

        new NewMoviesController(this, newMovies);
    }

    private void removeTitleBar()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        ActionBar actionBar = getSupportActionBar(); //hide the title bar
        if(actionBar != null)
        {
            actionBar.hide();
        }
    }

    public void showMainActivity()
    {
        this.finish();
    }
}
