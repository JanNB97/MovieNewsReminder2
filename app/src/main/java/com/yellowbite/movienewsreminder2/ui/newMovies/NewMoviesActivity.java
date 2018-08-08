package com.yellowbite.movienewsreminder2.ui.newMovies;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;

import java.util.ArrayList;
import java.util.List;

public class NewMoviesActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.removeTitleBar();
        setContentView(R.layout.activity_new_movies);

        new NewMoviesController(this, MedZenFileMan.getNewMovies(this));
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

    public void showMainActivity(List<Movie> resultMovies)
    {
        if(resultMovies != null && !resultMovies.isEmpty())
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", MedZenFileMan.toStatusLine(resultMovies));
            this.setResult(Activity.RESULT_OK, returnIntent);
        }

        this.finish();
    }
}