package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedZenMovieListScraper
{
    private Document doc;
    private Elements listEntries;
    private static final String movieCssQuery = "tr.ResultItem";

    private static final String MOVIE_MEDIAGROUP = "46S-DVD (Spielfilm)";

    public MedZenMovieListScraper(String url) throws IOException
    {
        this.doc = WebscrapingHelper.getDoc(url);
        this.listEntries = doc.select(movieCssQuery);

        if(this.listEntries.isEmpty())
        {
            throw new IOException();
        }
    }

    public List<Movie> getAllMovie()
    {
        List<Movie> movies = new ArrayList<>();

        for(int i = 0; i < this.getListEntrySize(); i++)
        {
            Movie movie = getMovie(i);
            if(movie != null)
            {
                movies.add(movie);
            }
        }

        return movies;
    }

    public Movie getMovie(int index)
    {
        if(!isMovie(index))
        {
            return null;
        }

        Movie movie = this.getEssentialMovie(index);
        if(movie == null)
        {
            return null;
        }

        movie.setStandort(this.getBestStandort(index));
        movie.setTitel(this.getTitel(index));

        return movie;
    }

    public Movie getEssentialMovie(int index)
    {
        int mediaBarcode = getMediaBarcode(index);
        if(mediaBarcode == -1)
        {
            return null;
        }

        String url = getURL(index);
        if(url == null)
        {
            return null;
        }

        return new Movie(mediaBarcode, url);
    }

    // --- --- --- find out if list item is movie --- --- ---
    public boolean isMovie(int index)
    {
        String mediagroup = this.getMediengruppe(index);
        return mediagroup.contains(MOVIE_MEDIAGROUP);
    }

    public String getMediengruppe(int index)
    {
        return WebscrapingHelper.getText(getListEntry(index), "span.mediagroup");
    }

    // --- --- --- get movie informations --- --- ---
    // --- essentials ---
    private int getMediaBarcode(int index)
    {
        return WebscrapingHelper.getInt(getListEntry(index), "span.mediaBarcode");
    }

    private String getURL(int index)
    {
        return WebscrapingHelper.getURL(getListEntry(index), "a[href]");
    }

    // --- other info ---
    private String getBestStandort(int index)
    {
        String standort = this.getStandort(index);

        if(standort != null)
        {
            return standort;
        }

        String signatur = this.getSignatur(index);
        if(signatur != null)
        {
            return signatur;
        }

        return this.getInteressenkreis(index);
    }

    private String getStandort(int index)
    {
        return WebscrapingHelper.getText(getListEntry(index), "span.location");
    }

    private String getSignatur(int index)
    {
        return WebscrapingHelper.getText(getListEntry(index), "span.systematik");
    }

    private String getInteressenkreis(int index)
    {
        return WebscrapingHelper.getText(getListEntry(index), "span.topics");
    }

    public Date getZugang(int index)
    {
        return WebscrapingHelper.getDate(getListEntry(index), "span.accessDate");
    }

    private String getTitel(int index)
    {
        return WebscrapingHelper.getText(getListEntry(index), "span.title");
    }

    // --- --- --- access to list --- --- ---
    public String getURLToNextPage()
    {
        return WebscrapingHelper.getURL(doc, "a#ContentPlaceHolderMain_resultList_searchPagingView_HyperlinkNext");
    }

    private Element getListEntry(int index)
    {
        if(index < 0 || index >= listEntries.size())
        {
            throw new IndexOutOfBoundsException();
        }

        return listEntries.get(index);
    }

    public int getListEntrySize()
    {
        return listEntries.size();
    }
}
