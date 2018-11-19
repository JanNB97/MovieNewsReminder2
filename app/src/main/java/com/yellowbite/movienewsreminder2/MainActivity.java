package com.yellowbite.movienewsreminder2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.yellowbite.movienewsreminder2.fragments.MyMoviesFragment;
import com.yellowbite.movienewsreminder2.fragments.ToolbarFragment;
import com.yellowbite.movienewsreminder2.fragments.WishlistFragment;
import com.yellowbite.movienewsreminder2.fragments.toolbar_navigation_activites.NavigationDrawerActivity;

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
        super.setContentViewWithoutTitleBar(R.layout.activity_main);
        this.initialize();
    }

    private void initialize()
    {
        this.fragmentManager = super.getSupportFragmentManager();
        this.startFragment = com.yellowbite.movienewsreminder2.fragments.FragmentManager.get(START_FRAGMENT_ID);
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
            ToolbarFragment fragmentToShow = com.yellowbite.movienewsreminder2.fragments.FragmentManager.get(fragmentId);
            if(fragmentToShow != null)
            {
                this.showFragment(fragmentToShow);
            }
        }
    }

    private void showFragment(ToolbarFragment fragment)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        this.setAnimation(transaction, fragment);
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        if(this.menu != null)
        {
            fragment.showOptionsMenu(this, this.menu);
        }
    }

    private void setAnimation(FragmentTransaction transaction, ToolbarFragment toolbarFragment)
    {
        int enterAnim, exitAnim;
        if(toolbarFragment.getFragmentId() == START_FRAGMENT_ID)
        {
            enterAnim = R.anim.slide_in_right;
            exitAnim = R.anim.slide_out_left;
        }
        else
        {
            enterAnim = R.anim.slide_in_left;
            exitAnim = R.anim.slide_out_right;
        }

        transaction.setCustomAnimations(enterAnim, exitAnim);
    }

    // --- --- --- Toolbar --- --- ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        this.showFragment(startFragment);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        ToolbarFragment currentFragment = com.yellowbite.movienewsreminder2.fragments.FragmentManager.getAddedFragment();
        if(currentFragment != null)
        {
            if(currentFragment.onOptionsItemSelected(item))
            {
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        ToolbarFragment currentFragment = com.yellowbite.movienewsreminder2.fragments.FragmentManager.getAddedFragment();
        if(currentFragment != null)
        {
            currentFragment.showTitleAndModifyOptionsMenu(this);
        }
    }

    // --- --- --- Navigation drawer --- --- ---
    @Override
    protected boolean handleOnNavItemClicked(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.home:
                this.showFragment(com.yellowbite.movienewsreminder2.fragments.FragmentManager.get(START_FRAGMENT_ID));
                return true;
            case R.id.wished_movies:
                this.showFragment(com.yellowbite.movienewsreminder2.fragments.FragmentManager.get(WishlistFragment.FRAGMENT_ID));
                return true;
        }

        return false;
    }
}
