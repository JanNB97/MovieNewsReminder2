package com.yellowbite.movienewsreminder2.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted.SortedMyMoviesList;
import com.yellowbite.movienewsreminder2.util.DateHelper;

import java.util.Comparator;
import java.util.Date;

public class Movie implements Comparable<Movie>
{
    public static final Comparator<Movie> STANDARD_COMPARATOR = new MovieComparator();
    public static final Comparator<Movie> SIMPLE_COMPARATOR = new SimpleMovieComparator();

    private Bitmap imageBitmap;
    private boolean isHot;
    private boolean notificationWasShown;
    private String einheitstitel;

    // essential
    private int mediaBarcode = -1;
    private final String url;

    //status informations
    private Status status;
    private int vorbestellungen = -1;
    private Date entliehenBis;

    //standort informations
    private String standort;
    private Date zugang;

    //useful informations
    private String titel;

    public Movie(int mediaBarcode, String url)
    {
        this.mediaBarcode = mediaBarcode;
        this.url = url;
    }

    public Movie(String url)
    {
        this.url = url;
    }

    public Movie(int mediaBarcode, String url,
                 Status status,
                 int vorbestellungen, Date entliehenBis,
                 String standort, Date zugang, String titel,
                 boolean notificationWasShown, boolean isHot)
    {
        this.mediaBarcode = mediaBarcode;
        this.url = url;
        this.status = status;
        this.vorbestellungen = vorbestellungen;
        this.entliehenBis = entliehenBis;
        this.standort = standort;
        this.zugang = zugang;
        this.titel = titel;
        this.notificationWasShown = notificationWasShown;
        this.isHot = isHot;
    }

    // --- --- --- Overritten methods --- --- ---
    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Movie)
        {
            Movie o = (Movie)obj;

            return this.mediaBarcode == o.getMediaBarcode();
        }

        return false;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(mediaBarcode);

        if(this.titel != null)
        {
            builder.append(": " + this.titel);
        }

        if(this.status != null)
        {
            builder.append(" - " + this.status.getValue());
        }

        if(this.entliehenBis != null)
        {
            builder.append(", entliehen bis " + DateHelper.toString(this.entliehenBis));
        }

        if(this.vorbestellungen != -1)
        {
            builder.append(" (" + this.vorbestellungen + " Vorbest.)");
        }

        return builder.toString();
    }

    public String toLongString()
    {
        StringBuilder builder = new StringBuilder(this.toString());

        if(this.standort != null)
        {
            builder.append("\n\tStandort: " + this.standort);
        }

        if(this.zugang != null)
        {
            builder.append("\n\tZugang: " + DateHelper.toString(this.zugang));
        }

        builder.append("\n\tURL: " + this.url);

        return builder.toString();
    }

    @Override
    public int compareTo(@NonNull Movie movie)
    {
        return STANDARD_COMPARATOR.compare(this, movie);
    }

    public int simpleCompareTo(@NonNull Movie movie)
    {
        return SIMPLE_COMPARATOR.compare(this, movie);
    }

    // --- --- --- Subclasses --- --- ---
    public enum Status {
        VERFUEGBAR("verfügbar"),
        ENTLIEHEN("entliehen"),
        VORBESTELLT("vorbestellt"),
        IN_BEARBEITUNG("in Bearbeitung");

        private String value;

        Status(String value)
        {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // --- --- --- Getter and setter --- --- ---
    public void setMediaBarcode(int mediaBarcode)
    {
        this.mediaBarcode = mediaBarcode;
    }

    public int getMediaBarcode() {
        return mediaBarcode;
    }

    public String getURL() {
        return url;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getVorbestellungen() {
        return vorbestellungen;
    }

    public void setVorbestellungen(int vorbestellungen) {
        this.vorbestellungen = vorbestellungen;
    }

    public Date getEntliehenBis() {
        return entliehenBis;
    }

    public void setEntliehenBis(Date entliehenBis) {
        this.entliehenBis = entliehenBis;
    }

    public String getStandort() {
        return standort;
    }

    public void setStandort(String standort) {
        this.standort = standort;
    }

    public Date getZugang()
    {
        return zugang;
    }

    public void setZugang(Date zugang)
    {
        this.zugang = zugang;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public boolean isHot()
    {
        return isHot;
    }

    public void setHot(Context context, boolean hot)
    {
        this.isHot = hot;
        SortedMyMoviesList.getInstance(context).setDirty(true);
    }

    public boolean notificationWasShown()
    {
        return notificationWasShown;
    }

    public void setNotificationWasShown(Context context, boolean notificationWasShown)
    {
        this.notificationWasShown = notificationWasShown;
        SortedMyMoviesList.getInstance(context).setDirty(true);
    }

    public Bitmap getImageBitmap()
    {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap)
    {
        this.imageBitmap = imageBitmap;
    }

    public String getEinheitstitel()
    {
        return einheitstitel;
    }

    public void setEinheitstitel(String einheitstitel)
    {
        this.einheitstitel = einheitstitel;
    }
}
