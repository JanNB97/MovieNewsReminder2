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
    }

    public MedZenMovieSiteScraper(int mediaBarcode, String url)
    {
        this.url = url;
        this.futureDoc = WebscrapingHelper.getFutureDoc(url);
        this.mediaBarcode = mediaBarcode;
    }

    // --- --- --- Public methods --- --- ---
    public Movie getEssentialMovie() throws IOException
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

    public void getMovieStatus(Movie movie) throws IOException
    {
        if(movie == null)
        {
            return;
        }

        this.addStatusToMovie(movie);
    }

    public Movie getMovieStatus() throws IOException
    {
        Movie movie = this.getEssentialMovie();
        this.getMovieStatus(movie);
        return movie;
    }

    public void getMovie(Movie movie) throws IOException
    {
        if(movie == null)
        {
            return;
        }

        //status infos
        this.addStatusToMovie(movie);

        //standort infos
        movie.setStandort(this.getStandort());
        movie.setZugang(this.getZugang());

        //useful infos
        movie.setTitel(getTitel());
    }

    public Movie getMovie() throws IOException
    {
        Movie movie = getEssentialMovie();
        this.getMovie(movie);
        return movie;
    }

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

        int i = 0;
        for(MedZenMovieSiteScraper siteScraper : siteScrapers)
        {
            try
            {
                siteScraper.getMovie(essentialMovies.get(i));
            } catch (IOException ignored) {}

            i++;
        }

        Collections.sort(essentialMovies);
    }

    private void addStatusToMovie(Movie movie) throws IOException
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
            return;
        }

        if(status == Status.VORBESTELLT)
        {
            movie.setEntliehenBis(null);
        }
        else if(status == Status.ENTLIEHEN)
        {
            movie.setEntliehenBis(this.getEntliehenBis());
        }

        movie.setVorbestellungen(this.getVorbestellungen());
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
