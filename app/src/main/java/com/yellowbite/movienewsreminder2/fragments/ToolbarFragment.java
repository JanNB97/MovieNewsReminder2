package com.yellowbite.movienewsreminder2.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.yellowbite.movienewsreminder2.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class ToolbarFragment extends Fragment
{
    private final int fragmentId;
    private static final List<ToolbarFragment> allFragments = new ArrayList<>();
    private boolean createOptionMenu = true;

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

    protected abstract void initOptionsMenu(AppCompatActivity app, Menu menu);

    protected abstract void modifyOptionsMenu(AppCompatActivity app, Menu menu);

    public abstract boolean onOptionsItemSelected(MenuItem item);

    protected void sendShowFragmentRequest(int fragmentId)
    {
        Intent resultIntent = new Intent(this.getActivity(), MainActivity.class);

        resultIntent.putExtra(MainActivity.SHOW_FRAGMENT_INTENT_NAME, fragmentId);

        this.getActivity().startActivity(resultIntent);
    }
}
