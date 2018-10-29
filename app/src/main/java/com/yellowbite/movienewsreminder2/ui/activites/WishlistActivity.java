package com.yellowbite.movienewsreminder2.ui.activites;

import android.content.Context;
import android.content.Intent;

public class WishlistActivity extends NavigationDrawerActivity
{
    public static void start(Context context)
    {
        Intent intent = new Intent(context, WishlistActivity.class);
        context.startActivity(intent);
    }
}
