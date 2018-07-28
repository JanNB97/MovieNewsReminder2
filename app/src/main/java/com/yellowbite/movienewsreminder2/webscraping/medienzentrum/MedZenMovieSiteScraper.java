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
        String titel = WebscrapingHelper.getText(doc,"table.DetailInformation td.DetailInformationEntryName:containsOwn(Titel):not(:containsOwn(zusatz)) + td");

        if(titel == null)
        {
            titel = this.getTitelUnsave();
        }

        return titel;
    }

    private String getTitelUnsave()
    {
        // TODO
        return WebscrapingHelper.getText(doc, null);
    }

    public String getKurzbeschreibung()
    {
        return WebscrapingHelper.getText(doc,"table.DetailInformation td.DetailInformationEntryName:containsOwn(Annotation) + td");
    }
}
