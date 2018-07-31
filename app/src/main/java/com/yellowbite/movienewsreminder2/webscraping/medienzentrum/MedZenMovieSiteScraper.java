package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.model.enums.Status;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class MedZenMovieSiteScraper
{
    private Movie movie;

    private final Future<Document> futureDoc;
    private final String url;
    private int mediaBarcode = -1;

    // --- --- --- Constructors --- --- ---
    public MedZenMovieSiteScraper(String url)
    {
        this.url = url;
        this.futureDoc = WebscrapingHelper.getFutureDoc(url);
    }

    public MedZenMovieSiteScraper(Movie movie)
    {
        this.url = movie.getURL();
        this.futureDoc = WebscrapingHelper.getFutureDoc(url);
        this.mediaBarcode = movie.getMediaBarcode();

        this.movie = movie;
    }

    public MedZenMovieSiteScraper(int mediaBarcode, String url)
    {
        this.url = url;
        this.futureDoc = WebscrapingHelper.getFutureDoc(url);
        this.mediaBarcode = mediaBarcode;

        this.movie = new Movie(mediaBarcode, url);
    }

    // --- --- --- Static methods --- --- ---
    public static Movie getMovie(String url) throws IOException
    {
        return new MedZenMovieSiteScraper(url).getMovie();
    }

    public static void getMovies(List<Movie> essentialMovies)
    {
        List<MedZenMovieSiteScraper> siteScrapers = new ArrayList<>();
        for(Movie movie : essentialMovies)
        {
            siteScrapers.add(new MedZenMovieSiteScraper(movie));
        }

        for(MedZenMovieSiteScraper siteScraper : siteScrapers)
        {
            try
            {
                siteScraper.getMovie();
            } catch (IOException ignored) {}
        }

        Collections.sort(essentialMovies);
    }

    // --- --- --- Add information to local movie --- --- ---
    public Movie getEssentialMovie() throws IOException
    {
        if(this.mediaBarcode == -1)
        {
            this.mediaBarcode = getMediaBarcode();

            if(this.mediaBarcode == -1)
            {
                return null;
            }

            this.movie = new Movie(this.mediaBarcode, url);
        }

        return this.movie;
    }

    public Movie getMovieStatus() throws IOException
    {
        if(this.movie == null)
        {
            this.getEssentialMovie();

            if(this.movie == null)
            {
                return null;
            }
        }

        this.addStatusToMovie();
        return this.movie;
    }

    public Movie getMovie() throws IOException
    {
        if(this.movie == null)
        {
            this.getEssentialMovie();
            if(this.movie == null)
            {
                return null;
            }
        }

        //status infos
        this.addStatusToMovie();

        //standort infos
        this.movie.setStandort(this.getStandort());
        this.movie.setZugang(this.getZugang());

        //useful infos
        this.movie.setTitel(getTitel());

        return this.movie;
    }

    private void addStatusToMovie() throws IOException
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

    private int getMediaBarcode() throws IOException
    {
        if(this.mediaBarcode != -1)
        {
            return mediaBarcode;
        }

        return WebscrapingHelper.getInt(this.getDoc(), "span.mediabarcode");
    }

    // --- get status informations ---

    private Status getStatus() throws IOException
    {
        return WebscrapingHelper.getStatus(this.getDoc(), "span#ContentPlaceHolderMain_LabelStatus");
    }

    private int getVorbestellungen() throws IOException
    {
        String vorbestellungen = WebscrapingHelper.getText(getDoc(), "span.DetailLeftContent");

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

    private Date getEntliehenBis() throws IOException
    {
        return WebscrapingHelper.getDate(this.getDoc(), "span.borrowUntil");
    }

    // --- get standort informations ---

    private String getStandort() throws IOException
    {
        return WebscrapingHelper.getText(getDoc(), "span#ContentPlaceHolderMain_LabellocationContent");
    }

    private Date getZugang() throws IOException
    {
        return WebscrapingHelper.getDate(getDoc(), "span.accessDate");
    }

    // --- get other useful informations ---

    private String getTitel() throws IOException
    {
        return WebscrapingHelper.getText(this.getDoc(),"table.DetailInformation td.DetailInformationEntryName:containsOwn(Titel):not(:containsOwn(zusatz)) + td");
    }

    // --- --- --- Others --- --- ---
    private Document getDoc() throws IOException
    {
        try
        {
            return futureDoc.get();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e)
        {
            throw new IOException();
        }
    }
}
