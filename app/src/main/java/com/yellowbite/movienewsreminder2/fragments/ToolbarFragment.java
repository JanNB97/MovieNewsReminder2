package com.yellowbite.movienewsreminder2.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class ToolbarFragment extends Fragment
{
    private final int fragmentId;
    private static final List<ToolbarFragment> allFragments = new ArrayList<>();

    public ToolbarFragment(int fragmentId)
    {
        this.fragmentId = fragmentId;
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

    public static List<ToolbarFragment> getAllFragments()
    {
        return allFragments;
    }

    public abstract void modifyOptionsMenu(AppCompatActivity appCompatActivity, Menu menu);

    public abstract boolean onOptionsItemSelected(MenuItem item);
}
