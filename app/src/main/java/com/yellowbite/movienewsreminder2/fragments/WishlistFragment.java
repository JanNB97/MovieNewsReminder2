package com.yellowbite.movienewsreminder2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted.WishedMoviesList;
import com.yellowbite.movienewsreminder2.fragments.ui.dialogs.TextDialogFragment;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerviews.ShowInstantlyRecyclerView;

public class WishlistFragment extends ToolbarFragment implements TextDialogFragment.DialogClickListener
{
    public static final int FRAGMENT_ID = 3;

    private ShowInstantlyRecyclerView wishedMovieRecyclerView;
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
        this.wishedMovieRecyclerView = new ShowInstantlyRecyclerView(this.getActivity(),
                R.id.wishedMoviesRecyclerView, true, R.layout.simple_movie_list_row,
                WishedMoviesList.getInstance(this.getContext()));
    }

    private void initFloatingButton()
    {
        this.addWishedMovieButton.setOnClickListener(this::handleOnAddMovieClicked);
    }

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
}
