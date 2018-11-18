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
    private final String titleInToolbar;

    private static final List<ToolbarFragment> allFragments = new ArrayList<>();
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

    public static ToolbarFragment get(int id)
    {
        ToolbarFragment result = null;

        // FIXME
        switch (id)
        {
            case NewMoviesFragment.FRAGMENT_ID:
                if(id >= allFragments.size() || allFragments.get(id) == null)
                {
                    result = new NewMoviesFragment();
                    if(allFragments.size() <= 1)
                    {
                        if(allFragments.size() == 0)
                        {
                            allFragments.add(null);
                        }
                        allFragments.add(null);
                    }
                    allFragments.add(id, result);
                }
                else
                {
                    result = allFragments.get(id);
                }
                break;
            case MyMoviesFragment.FRAGMENT_ID:
                if(id >= allFragments.size() || allFragments.get(id) == null)
                {
                    result = new MyMoviesFragment();
                    if(allFragments.size() == 0)
                    {
                        allFragments.add(id, result);
                    }
                    else
                    {
                        allFragments.set(id, result);
                    }
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
                    allFragments.set(id, result);
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

    public static ToolbarFragment getAddedFragment()
    {
        for(ToolbarFragment toolbarFragment : allFragments)
        {
            if(toolbarFragment.isAdded())
            {
                return toolbarFragment;
            }
        }

        return null;
    }

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
                this.sendShowFragmentRequest(MainActivity.START_FRAGMENT_ID);
                return true;
        }

        return false;
    }

    protected void sendShowFragmentRequest(int fragmentId)
    {
        Intent resultIntent = new Intent(this.getActivity(), MainActivity.class);

        resultIntent.putExtra(MainActivity.SHOW_FRAGMENT_INTENT_NAME, fragmentId);

        this.getActivity().startActivity(resultIntent);
    }

    public int getFragmentId()
    {
        return this.fragmentId;
    }
}
