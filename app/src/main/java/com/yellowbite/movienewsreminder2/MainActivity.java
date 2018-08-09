package com.yellowbite.movienewsreminder2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.yellowbite.movienewsreminder2.ui.MainActivityController;
import com.yellowbite.movienewsreminder2.ui.newMovies.NewMoviesActivity;
import com.yellowbite.movienewsreminder2.files.MedZenFileMan;

public class MainActivity extends AppCompatActivity
{
    private static final int REQUEST_CODE = 1;

    private MainActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.removeTitleBar();
        setContentView(R.layout.activity_main);

        this.controller = new MainActivityController(this);

        this.launchNewMoviesActivity();
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
        this.controller.onNewMoviesActivityResult(requestCode, REQUEST_CODE, resultCode, data);
    }
}
