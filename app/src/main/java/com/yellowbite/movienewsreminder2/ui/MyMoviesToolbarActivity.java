package com.yellowbite.movienewsreminder2.ui;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.yellowbite.movienewsreminder2.R;

public abstract class MyMoviesToolbarActivity extends NavigationDrawerActivity
{
    protected MenuItem undoItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_items, menu);

        this.undoItem = menu.findItem(R.id.action_undo);
        this.undoItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_undo:
                this.handleOnUndoClicked();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected abstract void handleOnUndoClicked();
}
