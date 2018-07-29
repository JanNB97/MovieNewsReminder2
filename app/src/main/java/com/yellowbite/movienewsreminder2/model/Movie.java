package com.yellowbite.movienewsreminder2.model;

import android.support.annotation.NonNull;

import com.yellowbite.movienewsreminder2.model.enums.Status;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class Movie implements Comparable<Movie>
{
    // essential
    private final int mediaBarcode;
    private final String url;

    //status informations
    private Status status;
    private int vorbestellungen = -1;
    private Date entliehenBis;

    //standort informations
    private String standort;
    private String interessenkreis;
    private String signatur;

    //useful informations
    private String titel;
    private String kurzbeschreibung;

    public Movie(int mediaBarcode, String url)
    {
        this.mediaBarcode = mediaBarcode;
        this.url = url;
    }

    public Movie(int mediaBarcode, String url, Status status, int vorbestellungen, Date entliehenBis, String standort, String interessenkreis, String signatur, String titel, String kurzbeschreibung)
    {
        this.mediaBarcode = mediaBarcode;
        this.url = url;
        this.status = status;
        this.vorbestellungen = vorbestellungen;
        this.entliehenBis = entliehenBis;
        this.standort = standort;
        this.interessenkreis = interessenkreis;
        this.signatur = signatur;
        this.titel = titel;
        this.kurzbeschreibung = kurzbeschreibung;
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

    public String getInteressenkreis() {
        return interessenkreis;
    }

    public void setInteressenkreis(String interessenkreis) {
        this.interessenkreis = interessenkreis;
    }

    public String getSignatur() {
        return signatur;
    }

    public void setSignatur(String signatur) {
        this.signatur = signatur;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getKurzbeschreibung() {
        return kurzbeschreibung;
    }

    public void setKurzbeschreibung(String kurzbeschreibung) {
        this.kurzbeschreibung = kurzbeschreibung;
    }

    // --- --- --- Overritten methods --- --- ---

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Movie)
        {
            Movie o = (Movie)obj;

            return this.mediaBarcode == o.getMediaBarcode()
                    && this.url.equals(o.getURL())

                    && equalsAndNotNull(this.status, o.getStatus())
                    && equalsAndNotNull(this.vorbestellungen, o.getVorbestellungen())
                    && equalsAndNotNull(this.entliehenBis, o.getEntliehenBis())

                    && equalsAndNotNull(this.standort, o.getStandort())
                    && equalsAndNotNull(this.interessenkreis, o.getInteressenkreis())
                    && equalsAndNotNull(this.signatur, o.getSignatur())

                    && equalsAndNotNull(this.titel, o.getTitel())
                    && equalsAndNotNull(this.kurzbeschreibung, o.getKurzbeschreibung());
        }

        return false;
    }

    private boolean equalsAndNotNull(Object a, Object b)
    {
        if(a == null && b == null)
        {
            return true;
        }

        if(a == null)
        {
            return false;
        }

        return a.equals(b);
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
            builder.append(", entliehen bis " + this.entliehenBis);
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

        if(this.interessenkreis != null)
        {
            builder.append("\n\tInteressenkreis: " + this.interessenkreis);
        }

        if(this.signatur != null)
        {
            builder.append("\n\tSignatur: " + this.signatur);
        }

        if(this.kurzbeschreibung != null)
        {
            builder.append("\n\tKurzbeschreibung: " + this.kurzbeschreibung);
        }

        builder.append("\n\tURL: " + this.url);

        return builder.toString();
    }

    @Override
    public int compareTo(@NonNull Movie movie)
    {
        final int THIS_EARLIER = -1;     // >
        final int THIS_LATER = 1;    // <
        final int EQUAL = 0;

        if(this.status == null && movie.status == null)
        {
            Logger.getGlobal().severe(movie.getTitel() + " and " + this.titel + ": status is null");
            return EQUAL;
        }

        if(this.status == null)
        {
            Logger.getGlobal().severe(this.getTitel() + ": status is null");
            return THIS_LATER;
        }

        if(movie.status == null)
        {
            Logger.getGlobal().severe(movie.getTitel() + ": status is null");
            return THIS_EARLIER;
        }

        Callable<Integer> compareEntliehenBis = () -> this.entliehenBis.compareTo(movie.getEntliehenBis());
        Callable<Integer> compareVorbestellungen = () -> this.vorbestellungen - movie.getVorbestellungen();
        Callable<Integer> compareTitel = () -> {
            if(titel == null && movie.titel == null)
            {
                return EQUAL;
            }

            if(titel == null)
            {
                return THIS_LATER;
            }

            if(movie.titel == null)
            {
                return THIS_EARLIER;
            }

            return this.titel.compareTo(movie.getTitel());
        };

        switch (this.status)
        {
            case VERFUEGBAR:
                if(movie.getStatus() == Status.VERFUEGBAR)
                {
                    try
                    {
                        return compareTitel.call();
                    } catch (Exception e)
                    {
                        Logger.getGlobal().severe("Something went wrong");
                        return EQUAL;
                    }
                }
                return THIS_EARLIER;

            case ENTLIEHEN:
                switch (movie.status)
                {
                    case VERFUEGBAR:
                        return THIS_LATER;
                    case ENTLIEHEN:
                        return this.compareTo(/* Last */ compareTitel,
                                /* First checked */ compareVorbestellungen, compareEntliehenBis);
                    case VORBESTELLT:
                        return this.compareTo(/* Last */ () -> THIS_EARLIER,
                                /* First checked */ compareVorbestellungen);
                }

            case VORBESTELLT:
                switch (movie.status)
                {
                    case VERFUEGBAR:
                        return THIS_LATER;
                    case ENTLIEHEN:
                        return this.compareTo(/* Last */ () -> THIS_LATER,
                                /* First checked */ compareVorbestellungen);
                    case VORBESTELLT:
                        return this.compareTo(/* Last */ compareTitel,
                                /* First checked */ compareVorbestellungen);
                }
        }

        Logger.getGlobal().severe("Something went wrong: Reached unreachable statement");
        return EQUAL;
    }

    private int compareTo(Callable<Integer> lastCompare, Callable<Integer> ... compares)
    {
        for(Callable<Integer> callable : compares)
        {
            try
            {
                int i = callable.call();

                if(i != 0)
                {
                    return i;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            return lastCompare.call();
        } catch (Exception e)
        {
            Logger.getGlobal().severe("Something went wrong");
            return 0;
        }
    }
}
