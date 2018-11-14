package com.yellowbite.movienewsreminder2.fragments.ui.recyclerView.listener;

import android.view.View;

@FunctionalInterface
public interface TouchListener
{
    void onTouch(View view, int position);
}
