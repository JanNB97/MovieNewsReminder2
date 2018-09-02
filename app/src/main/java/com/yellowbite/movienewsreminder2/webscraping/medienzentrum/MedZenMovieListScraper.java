package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

public class MedZenMovieListScraper
{
    private Document doc;
    private Elements listEntries;
    private static final String movieCssQuery = "tr.ResultItem";

    public MedZenMovieListScraper(String url) throws IOException
    {
        this.doc = WebscrapingHelper.getDoc(url);
        this.listEntries = doc.select(movieCssQuery);

        if(this.listEntries.isEmpty())
        {
            throw new IOException();
        }
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

    public Date getZugang(int index)
    {
        return WebscrapingHelper.getDate(getListEntry(index), "span.accessDate");
    }

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

    // --- getAll essentials ---

    private int getMediaBarcode(int index)
    {
        return WebscrapingHelper.getInt(getListEntry(index), "span.mediaBarcode");
    }

    private String getURL(int index)
    {
        return WebscrapingHelper.getURL(getListEntry(index), "a[href]");
    }
}
