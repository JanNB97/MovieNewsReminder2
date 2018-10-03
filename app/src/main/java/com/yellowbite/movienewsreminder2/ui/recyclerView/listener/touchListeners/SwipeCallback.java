package com.yellowbite.movienewsreminder2.ui.recyclerView.listener.touchListeners;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public abstract class SwipeCallback extends ItemTouchHelper.SimpleCallback
{
    public SwipeCallback()
    {
        super(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return false;
    }
}