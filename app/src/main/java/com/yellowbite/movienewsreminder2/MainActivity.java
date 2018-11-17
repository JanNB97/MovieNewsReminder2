package com.yellowbite.movienewsreminder2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.yellowbite.movienewsreminder2.fragments.MyMoviesFragment;
import com.yellowbite.movienewsreminder2.fragments.ToolbarFragment;
import com.yellowbite.movienewsreminder2.fragments.toolbar_navigation_activites.NavigationDrawerActivity;
import com.yellowbite.movienewsreminder2.newsService.NewsService;

public class MainActivity extends NavigationDrawerActivity
{
    public static final String SHOW_FRAGMENT_INTENT_NAME = "Show fragment";

    public static final int START_FRAGMENT_ID = MyMoviesFragment.FRAGMENT_ID;

    private FragmentManager fragmentManager;

    private Menu menu;

    private ToolbarFragment startFragment;

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
        this.startFragment = ToolbarFragment.get(START_FRAGMENT_ID);
        this.showFragment(startFragment);
    }

    // --- --- --- Interaction with fragments --- --- ---
    @Override
    protected void onNewIntent(Intent intent)
    {
        if(intent.hasExtra(SHOW_FRAGMENT_INTENT_NAME))
        {
            this.handleShowFragmentIntent(intent);
        }
        super.onNewIntent(intent);
    }

    private void handleShowFragmentIntent(Intent intent)
    {
        final int DEFAULT_VALUE = -1;
        int fragmentId = intent.getIntExtra(SHOW_FRAGMENT_INTENT_NAME, DEFAULT_VALUE);

        if(fragmentId != DEFAULT_VALUE)
        {
            ToolbarFragment fragmentToShow = ToolbarFragment.get(fragmentId);
            if(fragmentToShow != null)
            {
                this.showFragment(fragmentToShow);
            }
        }
    }

    private void showFragment(ToolbarFragment fragment)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        // TODO - Set Animation
        // transaction.setTransition();
        transaction.commit();

        if(this.menu != null)
        {
            fragment.showOptionsMenu(this, this.menu);
        }
    }

    // --- --- --- Toolbar --- --- ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        this.startFragment.showOptionsMenu(this, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        for(ToolbarFragment toolbarFragment : ToolbarFragment.getAllFragments())
        {
            if(toolbarFragment.isAdded())
            {
                if(toolbarFragment.onOptionsItemSelected(item))
                {
                    return true;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // --- --- --- Results --- --- ---
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO
        ((MyMoviesFragment)startFragment).getMyMovieRecyclerView().dataSetChanged();
        NewsService.start(this);
    }
}
