package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.model.enums.Status;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class MedZenMovieSiteScraper
{
    private Movie movie;

    private Document doc;

    public MedZenMovieSiteScraper(String url) throws IOException
    {
        Movie movie = null;
        if(url != null)
        {
            movie = new Movie(url);
        }

        this.init(movie);
    }

    public MedZenMovieSiteScraper(Movie movie) throws IOException
    {
        this.init(movie);
    }

    private void init(Movie movie) throws IOException
    {
        if(movie == null)
        {
            throw new NullPointerException();
        }

        this.doc = WebscrapingHelper.getDoc(movie.getURL());
        this.movie = movie;
    }

    // --- --- --- Static methods --- --- ---
    public static Movie getMovie(String url) throws IOException
    {
        return new MedZenMovieSiteScraper(new Movie(url)).getMovie();
    }

    public static void getMovies(List<Movie> essentialMovies)
    {
        for(Movie essentialMovie : essentialMovies)
        {
            try
            {
                new MedZenMovieSiteScraper(essentialMovie).getMovie();
            }
            catch (IOException ignored) {}
        }

        Collections.sort(essentialMovies);
    }

    // --- --- --- Add information to local movie --- --- ---
    public Movie getMediaBarcodeMovie()
    {
        this.movie.setMediaBarcode(this.getMediaBarcode());

        if(this.movie.getMediaBarcode() == -1)
        {
            return null;
        }

        return this.movie;
    }

    public Movie getMovieStatus()
    {
        this.getMediaBarcodeMovie();

        if(this.movie.getMediaBarcode() == -1)
        {
            return null;
        }

        this.addStatusToMovie();
        return this.movie;
    }

    public Movie getMovie()
    {
        this.getMediaBarcodeMovie();

        if(this.movie.getMediaBarcode() == -1)
        {
            return null;
        }

        //status infos
        this.addStatusToMovie();

        //standort infos
        this.movie.setStandort(this.getStandort());
        this.movie.setZugang(this.getZugang());

        //useful infos
        this.movie.setTitel(this.getTitel());

        return this.movie;
    }

    private void addStatusToMovie()
    {
        Status status = this.getStatus();
        if(status == null)
        {
            return;
        }
        this.movie.setStatus(status);

        if(status == Status.VERFUEGBAR)
        {
            this.movie.setVorbestellungen(0);
            this.movie.setEntliehenBis(null);
            return;
        }

        if(status == Status.VORBESTELLT)
        {
            this.movie.setEntliehenBis(null);
        }
        else if(status == Status.ENTLIEHEN)
        {
            this.movie.setEntliehenBis(this.getEntliehenBis());
        }

        this.movie.setVorbestellungen(this.getVorbestellungen());
    }

    // --- get essentials ---

    private int getMediaBarcode()
    {
        if(this.movie.getMediaBarcode() != -1)
        {
            return this.movie.getMediaBarcode();
        }

        return WebscrapingHelper.getInt(this.doc, "span.mediabarcode");
    }

    // --- get status informations ---

    private Status getStatus()
    {
        return WebscrapingHelper.getStatus(this.doc, "span#ContentPlaceHolderMain_LabelStatus");
    }

    private int getVorbestellungen()
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

    private Date getEntliehenBis()
    {
        return WebscrapingHelper.getDate(this.doc, "span.borrowUntil");
    }

    // --- get standort informations ---

    private String getStandort()
    {
        if(this.movie.getStandort() != null)
        {
            return this.movie.getStandort();
        }

        String standort = WebscrapingHelper.getText(doc, "span#ContentPlaceHolderMain_LabellocationContent");

        if(standort != null)
        {
            return standort;
        }

        String signatur = this.getSignatur();
        if(signatur != null)
        {
            return signatur;
        }

        return this.getInteressenkreis();
    }

    private String getSignatur()
    {
        return WebscrapingHelper.getText(this.doc, "span.signatur");
    }

    private String getInteressenkreis()
    {
        return WebscrapingHelper.getText(this.doc,"table.DetailInformation td.DetailInformationEntryName:containsOwn(Interessenkreis) + td");
    }

    private Date getZugang()
    {
        if(this.movie.getZugang() != null)
        {
            return this.movie.getZugang();
        }

        return WebscrapingHelper.getDate(doc, "span.accessDate");
    }

    // --- get other useful informations ---

    private String getTitel()
    {
        if(this.movie.getTitel() != null)
        {
            return this.movie.getTitel();
        }

        return WebscrapingHelper.getText(this.doc,"table.DetailInformation td.DetailInformationEntryName:containsOwn(Titel):not(:containsOwn(zusatz)) + td");
    }
}
