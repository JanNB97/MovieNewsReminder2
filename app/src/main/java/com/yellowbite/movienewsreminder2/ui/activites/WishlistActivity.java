package com.yellowbite.movienewsreminder2.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;

public class WishlistActivity extends NavigationDrawerActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentViewWithoutTitleBar(R.layout.activity_wishlist);
    }

    public void handleOnAddMovieClicked(View view)
    {
        // TODO
    }

    public static void start(Context context)
    {
        Intent intent = new Intent(context, WishlistActivity.class);
        context.startActivity(intent);
    }
}
