package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.model.enums.Status;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

public class MedZenMovieSiteScraper
{
    private final Document doc;
    private final String url;
    private int mediaBarcode = -1;

    public MedZenMovieSiteScraper(String url) throws IOException
    {
        this.url = url;
        this.doc = WebscrapingHelper.getDoc(url);
    }

    public MedZenMovieSiteScraper(int mediaBarcode, String url) throws IOException
    {
        this.url = url;
        this.doc = WebscrapingHelper.getDoc(url);
        this.mediaBarcode = mediaBarcode;
    }

    public Movie getEssentialMovie()
    {
        if(this.mediaBarcode == -1)
        {
            this.mediaBarcode = getMediaBarcode();

            if(this.mediaBarcode == -1)
            {
                return null;
            }
        }

        return new Movie(this.mediaBarcode, url);
    }

    public Movie getMovieStatus()
    {
        Movie movie = this.getEssentialMovie();

        if(movie == null)
        {
            return null;
        }

        this.addStatusToMovie(movie);

        return movie;
    }

    public Movie getMovie()
    {
        //essentials
        Movie movie = getEssentialMovie();
        if(movie == null)
        {
            return null;
        }

        //status infos
        this.addStatusToMovie(movie);

        //standort infos
        movie.setStandort(this.getStandort());
        movie.setInteressenkreis(this.getInteressenkreis());
        movie.setSignatur(this.getSignatur());

        //useful infos
        movie.setTitel(getTitel());
        movie.setKurzbeschreibung(getKurzbeschreibung());

        return movie;
    }

    private void addStatusToMovie(Movie movie)
    {
        Status status = this.getStatus();
        if(status == null)
        {
            return;
        }
        movie.setStatus(status);

        if(status == Status.VERFUEGBAR)
        {
            movie.setVorbestellungen(0);
            movie.setEntliehenBis(null);
        }
        else
        {
            movie.setVorbestellungen(this.getVorbestellungen());
            movie.setEntliehenBis(this.getEntliehenBis());
        }
    }

    // --- get essentials ---

    public int getMediaBarcode()
    {
        if(this.mediaBarcode != -1)
        {
            return mediaBarcode;
        }

        return WebscrapingHelper.getInt(doc, "span.mediabarcode");
    }

    // --- get status informations ---

    public Status getStatus()
    {
        return WebscrapingHelper.getStatus(doc, "span.StatusAvailable");
    }

    public int getVorbestellungen()
    {
        String vorbestellungen = WebscrapingHelper.getText(doc, "span.DetailLeftContent");

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

    public Date getEntliehenBis()
    {
        return WebscrapingHelper.getDate(doc, "span.borrowUntil");
    }

    // --- get standort informations ---

    public String getStandort()
    {
        return WebscrapingHelper.getText(doc, "span#ContentPlaceHolderMain_LabellocationContent");
    }

    public String getInteressenkreis()
    {
        return WebscrapingHelper.getText(doc, "a[title='Alle Medien mit diesem Interessenkreis suchen']");
    }

    public String getSignatur()
    {
        return WebscrapingHelper.getText(doc, "span.signatur");
    }

    // --- get other useful informations ---

    public String getTitel()
    {
        return WebscrapingHelper.getText(doc,"table.DetailInformation td.DetailInformationEntryName:containsOwn(Titel):not(:containsOwn(zusatz)) + td");
    }

    public String getKurzbeschreibung()
    {
        return WebscrapingHelper.getText(doc,"table.DetailInformation td.DetailInformationEntryName:containsOwn(Annotation) + td");
    }
}
