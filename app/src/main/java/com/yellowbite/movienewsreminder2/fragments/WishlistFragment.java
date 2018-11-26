package com.yellowbite.movienewsreminder2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted.DoneWishedMoviesList;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted.ToDoWishedMoviesList;
import com.yellowbite.movienewsreminder2.fragments.ui.dialogs.TextDialogFragment;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerviews.ShowInstantlyRecyclerView;

public class WishlistFragment extends ToolbarFragment implements TextDialogFragment.DialogClickListener
{
    public static final int FRAGMENT_ID = 3;

    private ShowInstantlyRecyclerView wishedMovieRecyclerView;
    private FloatingActionButton addWishedMovieButton;

    private TabLayout tabLayout;

    // --- --- --- Initialize --- --- ---
    public WishlistFragment()
    {
        super(FRAGMENT_ID, R.layout.fragment_wishlist, 1, "Wunschliste");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        this.findViewsById();
        this.initialize();
        super.onViewCreated(view, savedInstanceState);
    }

    private void findViewsById()
    {
        this.addWishedMovieButton = super.getView().findViewById(R.id.addWishedMovieFloatingButton);
        this.tabLayout = this.getView().findViewById(R.id.wishlistTabLayout);
    }

    private void initialize()
    {
        this.initFloatingButton();
        this.initTabLayout();
    }

    private void initFloatingButton()
    {
        this.addWishedMovieButton.setOnClickListener(this::handleOnAddMovieClicked);
    }

    private void initTabLayout()
    {
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                handleOnTabClicked(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        this.initializeAfterActivityCreated();
        super.onActivityCreated(savedInstanceState);
    }

    private void initializeAfterActivityCreated()
    {
        this.initRecyclerView();
    }

    private void initRecyclerView()
    {
        this.wishedMovieRecyclerView = new ShowInstantlyRecyclerView(this.getActivity(),
                R.id.wishedMoviesRecyclerView, true, R.layout.simple_movie_list_row,
                ToDoWishedMoviesList.getInstance(this.getContext()),
                DoneWishedMoviesList.getInstance(this.getContext()));
    }

    // --- --- --- User interaction --- --- ---
    public void handleOnAddMovieClicked(View view)
    {
        // TODO
        TextDialogFragment textDialogFragment = TextDialogFragment.newInstance(this);
        textDialogFragment.show(super.getFragmentManager(), "tag");
    }

    @Override
    public void onDialogPositiveClicked(String movieName)
    {
        Movie movie = new Movie("");
        movie.setTitel(movieName);
        this.wishedMovieRecyclerView.addItem(movie);
    }

    public void handleOnTabClicked(TabLayout.Tab tab)
    {
        this.wishedMovieRecyclerView.swapAdapter(tab.getPosition());
    }
}
