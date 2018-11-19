package com.yellowbite.movienewsreminder2.fragments.ui.recyclerview.listener;

import android.view.View;

@FunctionalInterface
public interface TouchListener
{
    void onTouch(View view, int position);
}
