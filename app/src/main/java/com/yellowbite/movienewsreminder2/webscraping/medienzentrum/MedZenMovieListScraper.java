package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.enums.Status;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class MedZenMovieListScraper
{
    private Elements listEntries;
    private static final String movieCssQuery = "tr.ResultItem";

    public MedZenMovieListScraper(String url) throws IOException
    {
        this.listEntries = WebscrapingHelper.getDoc(url).select(movieCssQuery);
    }

    public Movie getEssentialMovie(int index)
    {
        int mediaBarcode = getMediaBarcode(index);
        if(mediaBarcode == -1)
        {
            return null;
        }

        String link = getLink(index);
        if(link == null)
        {
            return null;
        }

        return new Movie(mediaBarcode, link);
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

    // --- get essentials ---

    public int getMediaBarcode(int index)
    {
        return WebscrapingHelper.getInt(getListEntry(index), "span.mediaBarcode");
    }

    public String getLink(int index)
    {
        return WebscrapingHelper.getLink(getListEntry(index), "a[href]");
    }
}
