package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.enums.Status;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.model.Movie;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class MedZenMovieScraper
{
    private static final String movieCssQuery = "tr.ResultItem";

    public static int getMediaBarcode(Document websiteDoc, int index)
    {
        Elements tableEntries = websiteDoc.select(movieCssQuery);

        if(index >= tableEntries.size())
        {
            Logger.getGlobal().severe("No mediabarcode found at index " + index);
            return -1;
        }

        return getMediaBarcode(tableEntries.get(index));
    }

    public static Movie getMovie(Document websiteDoc, int index)
    {
        Elements tableEntries = websiteDoc.select(movieCssQuery);

        if(index >= tableEntries.size())
        {
            Logger.getGlobal().severe("No movie found at index " + index);
            return null;
        }

        Element tableEntry = tableEntries.get(index);

        //essentials
        int mediaBarcode = getMediaBarcode(tableEntry);
        String link = getLink(tableEntry);
        Movie movie = new Movie(mediaBarcode, link);

        //status infos
        Status status = getStatus(tableEntry);
        movie.setStatus(status);
        int vorbestellungen = getVorbestellungen(tableEntry);
        movie.setVorbestellungen(vorbestellungen);
        Date entliehenBis = getEntliehenBis(tableEntry);
        movie.setEntliehenBis(entliehenBis);

        //standort infos
        String standort = getStandort(tableEntry);
        movie.setStandort(standort);
        String interessenkreis = getInteressenkreis(tableEntry);
        movie.setInteressenkreis(interessenkreis);
        String signatur = getSignatur(tableEntry);
        movie.setSignatur(signatur);

        //useful infos
        String titel = getTitel(tableEntry);
        movie.setTitel(titel);
        String erscheinungsjar = getErscheinungsjahr(tableEntry);
        movie.setErscheinungsjahr(erscheinungsjar);
        String kurzbeschreibung = getKurzbeschreibung(tableEntry);
        movie.setKurzbeschreibung(kurzbeschreibung);

        return movie;
    }

    // --- get essentials ---

    private static int getMediaBarcode(Element tableEntry)
    {
        return getInt(tableEntry, "span.mediaBarcode");
    }

    private static String getLink(Element tableEntry)
    {
        return getText(tableEntry, "a [href]");
    }

    // --- get status informations ---

    private static Status getStatus(Element tableEntry)
    {
        return getStatus(tableEntry, "[title='entliehen']");
    }

    private static int getVorbestellungen(Element tableEntry)
    {
        String vorbestellungen = getText(tableEntry, "span.reservationCount");

        if(vorbestellungen == null)
        {
            return 0;
        }

        String[] split = vorbestellungen.split(" ");

        return Integer.parseInt(split[0]);
    }

    private static Date getEntliehenBis(Element tableEntry)
    {
        return getDate(tableEntry, "span.borrowUntil");
    }

    // --- get standort informations ---

    private static String getStandort(Element tableEntry)
    {
        return getText(tableEntry, "span.location");
    }

    private static String getInteressenkreis(Element tableEntry)
    {
        return getText(tableEntry, "span.topics");
    }

    private static String getSignatur(Element tableEntry)
    {
        return getText(tableEntry, "span.systematik");
    }

    // --- get other useful informations ---

    private static String getTitel(Element tableEntry)
    {
        return getText(tableEntry,"[title='Titel']");
    }

    private static String getErscheinungsjahr(Element tableEntry)
    {
        return getText(tableEntry, "[title='Erscheinungsjahr']");
    }

    private static String getKurzbeschreibung(Element tableEntry)
    {
        return getText(tableEntry, "[title='Kurzbeschreibung']");
    }

    // =================================
    //          Generic methods
    // =================================

    // --- Returning text ---

    private static Status getStatus(Element tableEntry, String cssQuery)
    {
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
        Elements elements = tableEntry.select(cssQuery);

        if(elements.size() == 0)
        {
            return null;
        }

        return elements.first().text();
    }

    private static int getInt(Element tableEntry, String cssQuery)
    {
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
        Elements elements = tableEntry.select(cssQuery);

        if(elements.size() == 0)
        {
            return null;
        }

        return elements.attr("href");
    }
}
