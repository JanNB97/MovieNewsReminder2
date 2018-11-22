package com.yellowbite.movienewsreminder2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted.WishedMoviesList;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerview.MovieRecyclerView;

public class WishlistFragment extends ToolbarFragment
{
    public static final int FRAGMENT_ID = 3;

    private MovieRecyclerView wishedMovieRecyclerView;
    private FloatingActionButton addWishedMovieButton;

    public WishlistFragment()
    {
        super(FRAGMENT_ID, R.layout.fragment_wishlist, 1, "Wunschliste");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        this.findViewsById();
        super.onViewCreated(view, savedInstanceState);
    }

    private void findViewsById()
    {
        this.addWishedMovieButton = super.getView().findViewById(R.id.addWishedMovieFloatingButton);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        this.initialize();
        super.onActivityCreated(savedInstanceState);
    }

    private void initialize()
    {
        this.initRecyclerView();
        this.initFloatingButton();
    }

    private void initRecyclerView()
    {
        this.wishedMovieRecyclerView = new MovieRecyclerView(this.getActivity(),
                R.id.wishedMoviesRecyclerView, WishedMoviesList.getInstance(this.getContext()),
                true, R.layout.simple_movie_list_row);
        this.wishedMovieRecyclerView.showItems();
    }

    private void initFloatingButton()
    {
        this.addWishedMovieButton.setOnClickListener(this::handleOnAddMovieClicked);
    }

    public void handleOnAddMovieClicked(View view)
    {
        // TODO
        Movie movie = new Movie(32423, "");
        movie.setTitel("Test");
        this.wishedMovieRecyclerView.addItem(movie);
    }
}
