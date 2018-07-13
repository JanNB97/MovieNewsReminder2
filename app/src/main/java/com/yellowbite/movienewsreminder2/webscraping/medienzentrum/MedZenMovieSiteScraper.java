package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.model.enums.Status;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;

public class MedZenMovieSiteScraper
{
    private Document doc;

    public MedZenMovieSiteScraper(String url) throws IOException
    {
        this.doc = WebscrapingHelper.getDoc(url);
    }

    public Movie getMovie(Document websiteDoc)
    {
        // TODO
        return null;
    }

    public Status getStatus()
    {
        // TODO
        return null;
    }

    public Date getEntliehenBis()
    {
        // TODO
        return null;
    }
}
