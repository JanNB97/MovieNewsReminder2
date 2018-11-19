package com.yellowbite.movienewsreminder2.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.fragments.toolbar_navigation_activites.NavigationDrawerActivity;

public class WishlistFragment extends ToolbarFragment
{
    public static final int FRAGMENT_ID = 3;

    public WishlistFragment()
    {
        super(FRAGMENT_ID, R.layout.activity_wishlist, "Wunschliste");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        this.findViewsById();
        super.onViewCreated(view, savedInstanceState);
    }

    private void findViewsById()
    {
        // TODO
    }

    public void handleOnAddMovieClicked(View view)
    {
        // TODO
    }
}
