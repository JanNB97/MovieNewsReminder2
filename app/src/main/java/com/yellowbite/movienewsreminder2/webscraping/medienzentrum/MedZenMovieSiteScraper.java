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
    private String url;
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
        int mediaBarcode = getMediaBarcode();
        if(mediaBarcode == -1)
        {
            return null;
        }

        return new Movie(mediaBarcode, url);
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
        Status status = getStatus();
        movie.setStatus(status);
        int vorbestellungen = getVorbestellungen();
        movie.setVorbestellungen(vorbestellungen);
        Date entliehenBis = getEntliehenBis();
        movie.setEntliehenBis(entliehenBis);

        //standort infos
        String standort = getStandort();
        movie.setStandort(standort);
        String interessenkreis = getInteressenkreis();
        movie.setInteressenkreis(interessenkreis);
        String signatur = getSignatur();
        movie.setSignatur(signatur);

        //useful infos
        String titel = getTitel();
        movie.setTitel(titel);
        String kurzbeschreibung = getKurzbeschreibung();
        movie.setKurzbeschreibung(kurzbeschreibung);

        return movie;
    }

    // --- get essentials ---

    public int getMediaBarcode()
    {
        if(this.mediaBarcode != -1)
        {
            return mediaBarcode;
        }

        //TODO
        return -1;
    }

    // --- get status informations ---

    public Status getStatus()
    {
        // TODO
        return null;
    }

    public int getVorbestellungen()
    {
        // TODO
        return -1;
    }

    public Date getEntliehenBis()
    {
        // TODO
        return null;
    }

    // --- get standort informations ---

    public String getStandort()
    {
        // TODO
        return null;
    }

    public String getInteressenkreis()
    {
        // TODO
        return null;
    }

    public String getSignatur()
    {
        // TODO
        return null;
    }

    // --- get other useful informations ---

    public String getTitel()
    {
        // TODO
        return null;
    }

    public String getKurzbeschreibung()
    {
        // TODO
        return null;
    }
}
