package com.yellowbite.movienewsreminder2.ui.newMovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.MovieFileHelper;

public class NewMoviesActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.removeTitleBar();
        setContentView(R.layout.activity_new_movies);

        new NewMoviesController(this);
    }

    private void removeTitleBar()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  // will hide the title
        ActionBar actionBar = getSupportActionBar();    // will hide the title bar
        if(actionBar != null)
        {
            actionBar.hide();
        }
    }

    // start me from another activity
    public static final int REQUEST_CODE = 1;

    public static void startForResult(AppCompatActivity app)
    {
        Intent intent = new Intent(app, NewMoviesActivity.class);
        app.startActivityForResult(intent, REQUEST_CODE);
    }
}
