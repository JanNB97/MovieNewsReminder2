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

    private Future<Document> futureDoc;

    public MedZenMovieSiteScraper(String url)
    {
        Movie movie = null;
        if(url != null)
        {
            movie = new Movie(url);
        }

        this.init(movie);
    }

    public MedZenMovieSiteScraper(Movie movie)
    {
        this.init(movie);
    }

    private void init(Movie movie)
    {
        if(movie == null)
        {
            throw new NullPointerException();
        }

        this.futureDoc = WebscrapingHelper.getFutureDoc(movie.getURL());
        this.movie = movie;
    }

    // --- --- --- Static methods --- --- ---
    public static Movie getMovie(String url) throws IOException
    {
        return new MedZenMovieSiteScraper(new Movie(url)).getMovie();
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
    public Movie getMediaBarcodeMovie() throws IOException
    {
        this.movie.setMediaBarcode(this.getMediaBarcode());

        if(this.movie.getMediaBarcode() == -1)
        {
            return null;
        }

        return this.movie;
    }

    public Movie getMovieStatus() throws IOException
    {
        this.getMediaBarcodeMovie();

        if(this.movie.getMediaBarcode() == -1)
        {
            return null;
        }

        this.addStatusToMovie();
        return this.movie;
    }

    public Movie getMovie() throws IOException
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
        if(this.movie.getMediaBarcode() != -1)
        {
            return this.movie.getMediaBarcode();
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
        if(this.movie.getStandort() != null)
        {
            return this.movie.getStandort();
        }

        return WebscrapingHelper.getText(getDoc(), "span#ContentPlaceHolderMain_LabellocationContent");
    }

    private Date getZugang() throws IOException
    {
        if(this.movie.getZugang() != null)
        {
            return this.movie.getZugang();
        }

        return WebscrapingHelper.getDate(getDoc(), "span.accessDate");
    }

    // --- get other useful informations ---

    private String getTitel() throws IOException
    {
        if(this.movie.getTitel() != null)
        {
            return this.movie.getTitel();
        }

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
