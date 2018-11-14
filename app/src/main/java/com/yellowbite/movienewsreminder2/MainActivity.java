package com.yellowbite.movienewsreminder2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.yellowbite.movienewsreminder2.fragments.AddMovieFragment;
import com.yellowbite.movienewsreminder2.fragments.MyMoviesFragment;
import com.yellowbite.movienewsreminder2.fragments.toolbar_navigation_activites.NavigationDrawerActivity;

public class MainActivity extends NavigationDrawerActivity
{
    FragmentManager fragmentManager;

    MyMoviesFragment myMoviesFragment;
    AddMovieFragment addMovieFragment;

    // --- --- --- Initialization --- --- ---
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_main);
        this.initialize();

        this.startMainFragment();
    }

    private void initialize()
    {
        this.fragmentManager = super.getSupportFragmentManager();
    }

    private void startMainFragment()
    {
        this.myMoviesFragment = new MyMoviesFragment();
        this.showFragment(this.myMoviesFragment);
    }

    // --- --- --- Interaction with fragments --- --- ---
    @Override
    protected void onNewIntent(Intent intent)
    {
        // TODO
        int message = intent.getIntExtra("fragment", -1);
        switch (message)
        {
            case -1:
                // default
                break;
            case 0:
                // main
                this.showFragment(this.myMoviesFragment);
                break;
            case 1:
                // add
                if(this.addMovieFragment == null)
                {
                    this.addMovieFragment = new AddMovieFragment();
                }
                this.showFragment(this.addMovieFragment);
                break;
        }

        super.onNewIntent(intent);
    }

    private void showFragment(Fragment fragment)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        // TODO - Set Animation
        // transaction.setTransition();
        transaction.commit();
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
