package com.yellowbite.movienewsreminder2.ui.mainActivity.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datastructures.MyMoviesSortedList;
import com.yellowbite.movienewsreminder2.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder>
{
    private Context context;

    public MovieAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position)
    {
        Movie movieToShow = MyMoviesSortedList.get(this.context, position);

        if(movieToShow != null)
        {
            holder.showMovie(movieToShow);
        }
    }

    @Override
    public int getItemCount()
    {
        return MyMoviesSortedList.size(this.context);
    }
}
