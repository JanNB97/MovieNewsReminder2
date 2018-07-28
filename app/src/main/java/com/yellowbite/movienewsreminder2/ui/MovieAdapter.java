package com.yellowbite.movienewsreminder2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>
{
    private String[] myDataset;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTextView;
        ViewHolder(TextView v)
        {
            super(v);
            mTextView = v;
        }
    }

    public MovieAdapter(String[] myDataset)
    {
        this.myDataset = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.mTextView.setText(myDataset[position]);
    }

    @Override
    public int getItemCount()
    {
        return myDataset.length;
    }
}
