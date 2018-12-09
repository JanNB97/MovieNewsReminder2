package com.yellowbite.movienewsreminder2.fragments;

import android.content.Context;
import android.content.Intent;

import com.yellowbite.movienewsreminder2.MainActivity;
import com.yellowbite.movienewsreminder2.util.UnremovableSparseArray;

import java.util.Collection;

public class FragmentMaster
{
    private static UnremovableSparseArray<ToolbarFragment> allFragments;

    public interface RequestActivity
    {
        void onShowFragmentRequestSent(int fragmentId);
    }

    // --- --- --- Registration --- --- ---
    private static void registerFragments()
    {
        allFragments = new UnremovableSparseArray<>();

        // Register fragments here
        registerFragment(new MyMoviesFragment());
        registerFragment(new AddMovieFragment());
        registerFragment(new NewMoviesFragment());
        registerFragment(new WishlistFragment());
        registerFragment(new OpeningHoursFragment());
    }

    private static void registerFragment(ToolbarFragment toolbarFragment)
    {
        allFragments.put(toolbarFragment.getFragmentId(), toolbarFragment);
    }

    // --- --- --- Public methods --- --- ---
    public static ToolbarFragment get(int id)
    {
        if(allFragments == null)
        {
            registerFragments();
        }

        ToolbarFragment result = allFragments.get(id);

        if(result == null)
        {
            // You forgot to register your fragment
            throw new NullPointerException();
        }

        return result;
    }

    public static Collection<ToolbarFragment> getAllFragments()
    {
        return allFragments.values();
    }

    public static ToolbarFragment getAddedFragment()
    {
        for(ToolbarFragment toolbarFragment : getAllFragments())
        {
            if(toolbarFragment.isAdded())
            {
                return toolbarFragment;
            }
        }

        return null;
    }

    // --- --- --- Send Request --- --- ---
    public static void sendShowFragmentRequest(RequestActivity requestActivity, Context context, int fragmentId)
    {
        requestActivity.onShowFragmentRequestSent(fragmentId);

        Intent resultIntent = new Intent(context, MainActivity.class);

        resultIntent.putExtra(MainActivity.SHOW_FRAGMENT_INTENT_NAME, fragmentId);

        context.startActivity(resultIntent);
    }
}
