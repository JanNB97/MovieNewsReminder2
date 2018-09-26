package com.yellowbite.movienewsreminder2.files.datatypes.otherDatastructures;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieListScraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchMovieList implements MovieList
{
    private static SearchMovieList instance;

    private List<Movie> movies;
    private MedZenMovieListScraper listScraper;

    private SearchMovieList()
    {
        movies = new ArrayList<>();
    }

    public static SearchMovieList getInstance()
    {
        if(instance == null)
        {
            instance = new SearchMovieList();
        }

        return instance;
    }

    // --- --- --- get --- --- ---
    @Override
    public Movie get(Context context, int i)
    {
        return this.movies.get(i);
    }

    @Override
    public List<Movie> getAll(Context context)
    {
        return this.movies;
    }

    // --- --- --- add --- --- ---
    public void addMovieSite(String searchWord)
    {
        try
        {
            this.listScraper = new MedZenMovieListScraper(WebscrapingHelper.getSearchURL(searchWord));
            this.addAll(null, this.listScraper.getAllMovie());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void addAll(Context context, List<Movie> movies)
    {
        this.movies.addAll(movies);
    }

    @Override
    public boolean add(Context context, Movie movie)
    {
        this.movies.add(movie);
        return true;
    }

    // --- --- --- remove -- --- ---
    @Override
    public void remove(Context context, int i)
    {
        this.movies.remove(i);
    }

    public void clear()
    {
        this.movies.clear();
    }

    // --- --- --- get info --- --- ---
    @Override
    public int size(Context context)
    {
        return this.movies.size();
    }

    // --- --- --- unnecessary --- --- ---
    @Override
    public void save(Context context) {}
}
