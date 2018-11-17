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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class ToolbarFragment extends Fragment
{
    private final int resource;
    private final int fragmentId;
    private static final List<ToolbarFragment> allFragments = new ArrayList<>();
    private boolean createOptionMenu = true;

    // toolbar items
    protected MenuItem undoItem;

    public ToolbarFragment(int fragmentId, @LayoutRes int resource)
    {
        this.fragmentId = fragmentId;
        this.resource = resource;
    }

    public static ToolbarFragment get(int id)
    {
        ToolbarFragment result = null;

        switch (id)
        {
            case MyMoviesFragment.FRAGMENT_ID:
                if(id >= allFragments.size() || allFragments.get(id) == null)
                {
                    result = new MyMoviesFragment();
                    allFragments.add(id, result);
                }
                else
                {
                    result = allFragments.get(id);
                }
                break;
            case AddMovieFragment.FRAGMENT_ID:
                if(id >= allFragments.size() || allFragments.get(id) == null)
                {
                    result = new AddMovieFragment();
                    allFragments.add(id, result);
                }
                else
                {
                    result = allFragments.get(id);
                }
                break;
            default:
                Logger.getGlobal().severe("No fragment with id " + id + " found");
                break;
        }

        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(this.resource, container, false);
    }

    public static List<ToolbarFragment> getAllFragments()
    {
        return allFragments;
    }

    public final void showOptionsMenu(AppCompatActivity app, Menu menu)
    {
        if(this.createOptionMenu)
        {
            this.initOptionsMenu(app, menu);
            this.modifyOptionsMenu(app, menu);
            this.createOptionMenu = false;
        }
        else
        {
            this.modifyOptionsMenu(app, menu);
        }
    }

    protected void initOptionsMenu(AppCompatActivity app, Menu menu)
    {
        this.undoItem = menu.findItem(R.id.action_undo);
    }

    protected void modifyOptionsMenu(AppCompatActivity app, Menu menu)
    {
        this.undoItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home:
                this.sendShowFragmentRequest(MainActivity.START_FRAGMENT_ID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void sendShowFragmentRequest(int fragmentId)
    {
        Intent resultIntent = new Intent(this.getActivity(), MainActivity.class);

        resultIntent.putExtra(MainActivity.SHOW_FRAGMENT_INTENT_NAME, fragmentId);

        this.getActivity().startActivity(resultIntent);
    }
}
