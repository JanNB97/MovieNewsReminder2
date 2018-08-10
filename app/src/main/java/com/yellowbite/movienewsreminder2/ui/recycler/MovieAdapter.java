package com.yellowbite.movienewsreminder2.ui.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.data.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.data.MyMoviesSortedList;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.notifications.NotificationMan;

import java.util.List;

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

    public void handleClickedOnMovieItem(View view, int position)
    {

    }

    public void handleClickedLongOnMovieItem(View view, int position)
    {
        Movie movie = MyMoviesSortedList.get(this.context, position);
        if(HotMoviesSortedList.switchSave(this.context, movie))
        {
            this.dataSetChanged(false);
        }
    }

    public void addItems(List<Movie> movies, boolean saveInFile)
    {
        MyMoviesSortedList.addAll(this.context, movies);
        this.dataSetChanged(saveInFile);
    }

    public void addItem(Movie movie, boolean saveInFile)
    {
        if(!MyMoviesSortedList.add(context, movie))
        {
            NotificationMan.showShortToast(this.context, movie.getTitel() + " is already in the database");
        }
        else
        {
            // no movie added
            this.dataSetChanged(saveInFile);
        }
    }

    public void removeItem(int position)
    {
        Movie movieToRemove = MyMoviesSortedList.get(this.context, position);
        if(movieToRemove.isHot())
        {
            HotMoviesSortedList.deleteSave(this.context, movieToRemove);
        }

        MyMoviesSortedList.remove(this.context, position);
        this.dataSetChanged(true);
    }

    public void dataSetChanged(boolean saveInFile)
    {
        this.notifyDataSetChanged();

        if(saveInFile)
        {
            new Thread(() -> MyMoviesSortedList.save(this.context))
                    .start();
        }
    }
}
