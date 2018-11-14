package com.yellowbite.movienewsreminder2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.yellowbite.movienewsreminder2.ui.activites.MyMoviesFragment;
import com.yellowbite.movienewsreminder2.ui.activites.MyMoviesToolbarActivity;

public class MainActivity extends MyMoviesToolbarActivity
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        this.myMoviesFragment.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        this.myMoviesFragment.getMyMovieRecyclerView().dataSetChanged();
    }

    @Override
    protected void handleOnUndoClicked()
    {
        this.myMoviesFragment.handleOnUndoClicked();
    }
}
