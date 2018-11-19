package com.yellowbite.movienewsreminder2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yellowbite.movienewsreminder2.MainActivity;
import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.util.UnremovableSparseArray;

import java.util.Collection;

public abstract class ToolbarFragment extends Fragment
{
    private final int resource;
    private final int fragmentId;
    private final String titleInToolbar;

    private boolean createOptionMenu = true;

    // toolbar items
    protected MenuItem undoItem;
    protected MenuItem homeItem;

    public ToolbarFragment(int fragmentId, @LayoutRes int resource, String titleInToolbar)
    {
        this.fragmentId = fragmentId;
        this.resource = resource;
        this.titleInToolbar = titleInToolbar;
    }

    // --- --- --- Initialization --- --- ---
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(this.resource, container, false);
    }

    // --- --- --- Toolbar --- --- ---
    public final void showOptionsMenu(AppCompatActivity app, Menu menu)
    {
        if(this.createOptionMenu)
        {
            this.initOptionsMenu(menu);
            this.showTitleAndModifyOptionsMenu(app);
            this.createOptionMenu = false;
        }
        else
        {
            this.showTitleAndModifyOptionsMenu(app);
        }
    }

    private void initOptionsMenu(Menu menu)
    {
        this.undoItem = menu.findItem(R.id.action_undo);
        this.homeItem = menu.findItem(R.id.action_home);
    }

    public final void showTitleAndModifyOptionsMenu(AppCompatActivity app)
    {
        app.getSupportActionBar().setTitle(this.titleInToolbar);
        this.modifyOptionsMenu();
    }

    protected void modifyOptionsMenu()
    {
        this.homeItem.setVisible(true);
        this.undoItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home:
                FragmentManager.sendShowFragmentRequest(this.getContext(), MainActivity.START_FRAGMENT_ID);
                return true;
        }

        return false;
    }

    // --- --- --- Getter and Setter --- --- ---
    public int getFragmentId()
    {
        return this.fragmentId;
    }
}
