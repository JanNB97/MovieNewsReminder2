package com.yellowbite.movienewsreminder2.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Adapter;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;

import java.util.List;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback
{
    private Context context;
    private List<Movie> myMovies;
    private RecyclerView.Adapter<MovieViewHolder> adapter;

    public SwipeCallback(Context context, List<Movie> movies, RecyclerView.Adapter<MovieViewHolder> adapter)
    {
        super(0, ItemTouchHelper.LEFT);
        this.context = context;
        this.myMovies = movies;
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        int position = viewHolder.getAdapterPosition();
        this.myMovies.remove(position);
        this.adapter.notifyDataSetChanged();
        MedZenFileMan.setMyMovies(context, myMovies);
    }
}
