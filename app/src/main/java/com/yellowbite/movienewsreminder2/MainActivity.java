package com.yellowbite.movienewsreminder2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

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

        this.myMoviesFragment = new MyMoviesFragment();
        this.myMoviesFragment.setUndoItem(super.undoItem);
        this.myMoviesFragment.start(this);

        FragmentTransaction transaction = super.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, this.myMoviesFragment);
        transaction.addToBackStack(null);
        transaction.commit();

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
