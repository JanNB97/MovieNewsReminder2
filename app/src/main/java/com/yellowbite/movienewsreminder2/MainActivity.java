package com.yellowbite.movienewsreminder2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yellowbite.movienewsreminder2.fragments.MyMoviesFragment;
import com.yellowbite.movienewsreminder2.fragments.toolbar_navigation_activites.NavigationDrawerActivity;

public class MainActivity extends NavigationDrawerActivity
{
    MyMoviesFragment myMoviesFragment;

    // --- --- --- Initialization --- --- ---
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_main);

        this.myMoviesFragment = (MyMoviesFragment)super.getSupportFragmentManager().findFragmentById(R.id.main_fragment);
    }

    // --- --- --- Toolbar --- --- ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        this.myMoviesFragment.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        this.myMoviesFragment.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    // --- --- --- Results --- --- ---
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        this.myMoviesFragment.getMyMovieRecyclerView().dataSetChanged();
    }
}
