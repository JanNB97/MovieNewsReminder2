package com.yellowbite.movienewsreminder2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.yellowbite.movienewsreminder2.ui.NoTitleBarActivity;
import com.yellowbite.movienewsreminder2.ui.mainActivity.MainActivityController;
import com.yellowbite.movienewsreminder2.ui.newMovies.NewMoviesActivity;
import com.yellowbite.movienewsreminder2.ui.newMovies.NewMoviesController;

public class MainActivity extends NoTitleBarActivity
{
    private MainActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_main);

        this.controller = new MainActivityController(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        this.controller.onNewMoviesActivityResult(requestCode, NewMoviesActivity.REQUEST_CODE, resultCode, data);
    }
}
