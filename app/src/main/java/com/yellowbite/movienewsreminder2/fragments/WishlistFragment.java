package com.yellowbite.movienewsreminder2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;

public class WishlistFragment extends ToolbarFragment
{
    public static final int FRAGMENT_ID = 3;

    public WishlistFragment()
    {
        super(FRAGMENT_ID, R.layout.fragment_wishlist, 1, "Wunschliste");
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
