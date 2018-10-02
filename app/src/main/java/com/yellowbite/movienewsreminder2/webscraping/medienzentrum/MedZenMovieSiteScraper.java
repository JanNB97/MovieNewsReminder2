package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import android.graphics.Bitmap;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.ImageDownloader;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;
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

    public static Movie getMovieWithDetails(Movie essentialMovie) throws IOException
    {
        return new MedZenMovieSiteScraper(essentialMovie).getMovieWithDetails();
    }

    public static void getMovie(Movie essentialMovie) throws IOException
    {
        new MedZenMovieSiteScraper(essentialMovie).getMovie();
    }

    public static boolean isVerfuegbar(Movie movie) throws IOException
    {
        Movie.Status status = (new MedZenMovieSiteScraper(movie)).getStatus();
        return status != null && status == Movie.Status.VERFUEGBAR;
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

    public Movie getMovieWithDetails()
    {
        this.getMovie();
        if(this.movie == null)
        {
            return null;
        }

        this.movie.setImageBitmap(this.getImageBitmap());
        this.movie.setEinheitstitel(this.getEinheitstitel());
        return this.movie;
    }

    private void addStatusToMovie()
    {
        Movie.Status status = this.getStatus();
        if(status == null)
        {
            return;
        }
        this.movie.setStatus(status);

        if(status == Movie.Status.VERFUEGBAR)
        {
            this.movie.setVorbestellungen(0);
            this.movie.setEntliehenBis(null);
            return;
        }

        if(status == Movie.Status.VORBESTELLT || status == Movie.Status.IN_BEARBEITUNG)
        {
            this.movie.setEntliehenBis(null);
        }
        else if(status == Movie.Status.ENTLIEHEN)
        {
            this.movie.setEntliehenBis(this.getEntliehenBis());
        }

        this.movie.setVorbestellungen(this.getVorbestellungen());
    }

    // --- get detail informations ---

    private Bitmap getImageBitmap()
    {
        String url = WebscrapingHelper.getImageURL(this.doc, "img.cover");
        if(url == null)
        {
            return null;
        }

        return ImageDownloader.getImageBitmap(url);
    }

    private String getEinheitstitel()
    {
        return WebscrapingHelper.getText(this.doc, "table.DetailInformation td.DetailInformationEntryName:containsOwn(Einheitssachtitel) + td");
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

    private Movie.Status getStatus()
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
