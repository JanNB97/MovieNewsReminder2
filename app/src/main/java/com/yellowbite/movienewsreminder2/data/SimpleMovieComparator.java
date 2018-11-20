package com.yellowbite.movienewsreminder2.data;

import java.util.Comparator;

public class SimpleMovieComparator implements Comparator<Movie>
{
    @Override
    public int compare(Movie m1, Movie m2)
    {
        return m1.getMediaBarcode() - m2.getMediaBarcode();
    }
}
