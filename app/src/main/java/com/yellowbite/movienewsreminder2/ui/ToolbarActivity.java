package com.yellowbite.movienewsreminder2.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.yellowbite.movienewsreminder2.R;

public abstract class ToolbarActivity extends AppCompatActivity
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

    protected void setContentViewWithoutTitleBar(@LayoutRes int layoutResID)
    {
        this.setContentView(layoutResID);
        this.addToolbar();
        this.modifyToolbar();
    }

    private void addToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    protected void modifyToolbar(){}

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
