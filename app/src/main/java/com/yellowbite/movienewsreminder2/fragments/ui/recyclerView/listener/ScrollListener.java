package com.yellowbite.movienewsreminder2.fragments.ui.recyclerView.listener;

import android.support.v7.widget.RecyclerView;

@FunctionalInterface
public interface ScrollListener
{
    void handleScroll(RecyclerView recyclerView, int dx, int dy);
}
