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
        return getInt(getListEntry(index), "span.mediaBarcode");
    }

    public String getLink(int index)
    {
        return getLink(getListEntry(index), "a[href]");
    }

    // --- get status informations ---

    public Status getStatus(int index)
    {
        return getStatus(getListEntry(index), "[title='entliehen']");
    }

    public int getVorbestellungen(int index)
    {
        String vorbestellungen = getText(getListEntry(index), "span.reservationCount");

        if(vorbestellungen == null)
        {
            return 0;
        }

        String[] split = vorbestellungen.split(" ");

        return Integer.parseInt(split[0]);
    }

    public Date getEntliehenBis(int index)
    {
        return getDate(getListEntry(index), "span.borrowUntil");
    }

    // --- get standort informations ---

    public String getStandort(int index)
    {
        return getText(getListEntry(index), "span.location");
    }

    public String getInteressenkreis(int index)
    {
        return getText(getListEntry(index), "span.topics");
    }

    public String getSignatur(int index)
    {
        return getText(getListEntry(index), "span.systematik");
    }

    // --- get other useful informations ---

    public String getTitel(int index)
    {
        return getText(getListEntry(index),"[title='Titel']");
    }

    public String getKurzbeschreibung(int index)
    {
        return getText(getListEntry(index), "[title='Kurzbeschreibung']");
    }

    // =================================
    //          Generic methods
    // =================================

    // --- Returning text ---

    private static Status getStatus(Element tableEntry, String cssQuery)
    {
        if(tableEntry == null)
        {
            return null;
        }

        String text = getText(tableEntry, cssQuery);

        if(text == null)
        {
            return null;
        }
        else
        {
            for(Status status : Status.values())
            {
                if(status.getValue().equals(text))
                {
                    return status;
                }
            }

            Logger.getGlobal().severe("No fitting status has been found for " + text);
            return null;
        }
    }

    private static String getText(Element tableEntry, String cssQuery)
    {
        if(tableEntry == null)
        {
            return null;
        }

        Elements elements = tableEntry.select(cssQuery);

        if(elements.size() == 0)
        {
            return null;
        }

        return elements.first().text();
    }

    private static int getInt(Element tableEntry, String cssQuery)
    {
        if(tableEntry == null)
        {
            return -1;
        }

        String text = getText(tableEntry, cssQuery);

        if(text == null)
        {
            return -1;
        }
        else
        {
            return Integer.parseInt(text);
        }
    }

    private static Date getDate(Element tableEntry, String cssQuery)
    {
        if(tableEntry == null)
        {
            return null;
        }

        DateFormat df = new SimpleDateFormat("dd.mm.yyyy");

        String string = getText(tableEntry, cssQuery);

        if(string == null)
        {
            return null;
        }
        else
        {
            try {
                return df.parse(string);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    // --- Returns attributes ---

    private static String getLink(Element tableEntry, String cssQuery)
    {
        if(tableEntry == null)
        {
            return null;
        }

        Elements elements = tableEntry.select(cssQuery);

        if(elements.size() == 0)
        {
            return null;
        }

        return elements.attr("href");
    }
}
