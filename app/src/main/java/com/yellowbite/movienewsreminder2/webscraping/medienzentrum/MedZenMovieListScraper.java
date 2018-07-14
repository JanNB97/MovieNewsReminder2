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

    public Movie getMovie(int index)
    {
        //essentials
        Movie movie = getEssentialMovie(index);
        if(movie == null)
        {
            return null;
        }

        //status infos
        Status status = getStatus(index);
        movie.setStatus(status);
        int vorbestellungen = getVorbestellungen(index);
        movie.setVorbestellungen(vorbestellungen);
        Date entliehenBis = getEntliehenBis(index);
        movie.setEntliehenBis(entliehenBis);

        //standort infos
        String standort = getStandort(index);
        movie.setStandort(standort);
        String interessenkreis = getInteressenkreis(index);
        movie.setInteressenkreis(interessenkreis);
        String signatur = getSignatur(index);
        movie.setSignatur(signatur);

        //useful infos
        String titel = getTitel(index);
        movie.setTitel(titel);
        String kurzbeschreibung = getKurzbeschreibung(index);
        movie.setKurzbeschreibung(kurzbeschreibung);

        return movie;
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

    // --- get status informations ---

    public Status getStatus(int index)
    {
        return WebscrapingHelper.getStatus(getListEntry(index), "[title='entliehen']");
    }

    public int getVorbestellungen(int index)
    {
        String vorbestellungen = WebscrapingHelper.getText(getListEntry(index), "span.reservationCount");

        if(vorbestellungen == null)
        {
            return 0;
        }

        String[] split = vorbestellungen.split(" ");

        try
        {
            return Integer.parseInt(split[0]);
        } catch (NumberFormatException e)
        {
            Logger.getGlobal().severe("\"Vorbestellung\" don't have the right format");
            return -1;
        }
    }

    public Date getEntliehenBis(int index)
    {
        return WebscrapingHelper.getDate(getListEntry(index), "span.borrowUntil");
    }

    // --- get standort informations ---

    public String getStandort(int index)
    {
        return WebscrapingHelper.getText(getListEntry(index), "span.location");
    }

    public String getInteressenkreis(int index)
    {
        return WebscrapingHelper.getText(getListEntry(index), "span.topics");
    }

    public String getSignatur(int index)
    {
        return WebscrapingHelper.getText(getListEntry(index), "span.systematik");
    }

    // --- get other useful informations ---

    public String getTitel(int index)
    {
        return WebscrapingHelper.getText(getListEntry(index),"[title='Titel']");
    }

    public String getKurzbeschreibung(int index)
    {
        return WebscrapingHelper.getText(getListEntry(index), "[title='Kurzbeschreibung']");
    }
}
