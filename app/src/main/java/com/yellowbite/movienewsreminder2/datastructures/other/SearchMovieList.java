package com.yellowbite.movienewsreminder2.datastructures.other;

import com.yellowbite.movienewsreminder2.datastructures.MovieList;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieListScraper;

import java.util.ArrayList;
import java.util.List;

public class SearchMovieList implements MovieList
{
    private static SearchMovieList instance;

    private List<Movie> movies;

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
    public Movie get(int i)
    {
        return this.movies.get(i);
    }

    @Override
    public List<Movie> getAll()
    {
        return this.movies;
    }

    // --- --- --- add --- --- ---
    public boolean addMovieSite(MedZenMovieListScraper listScraper)
    {
        List<Movie> results = listScraper.getAllMovies();
        this.addAll(results);
        return !results.isEmpty();
    }

    @Override
    public void addAll(List<Movie> movies)
    {
        this.movies.addAll(movies);
    }

    @Override
    public boolean add(Movie movie)
    {
        this.movies.add(movie);
        return true;
    }

    // --- --- --- remove -- --- ---
    @Override
    public boolean remove(int i)
    {
        this.movies.remove(i);
        return true;
    }

    @Override
    public boolean remove(Movie movie)
    {
        return this.movies.remove(movie);
    }

    @Override
    public boolean removeLast()
    {
        return this.remove(this.size() - 1);
    }

    public void clear()
    {
        this.movies.clear();
    }

    // --- --- --- get info --- --- ---
    @Override
    public int size()
    {
        return this.movies.size();
    }

    @Override
    public boolean contains(Movie movie)
    {
        for(Movie m : movies)
        {
            if(m.equals(movie))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isEmpty()
    {
        return this.size() == 0;
    }
}
