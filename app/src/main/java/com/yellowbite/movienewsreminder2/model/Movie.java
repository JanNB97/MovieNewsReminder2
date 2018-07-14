package com.yellowbite.movienewsreminder2.model;

import com.yellowbite.movienewsreminder2.model.enums.Status;
import java.util.Date;

public class Movie
{
    // essential
    private final int mediaBarcode;
    private final String link;

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

    public Movie(int mediaBarcode, String link)
    {
        this.mediaBarcode = mediaBarcode;
        this.link = link;
    }

    public Movie(int mediaBarcode, String link, Status status, int vorbestellungen, Date entliehenBis, String standort, String interessenkreis, String signatur, String titel, String kurzbeschreibung)
    {
        this.mediaBarcode = mediaBarcode;
        this.link = link;
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

    public String getLink() {
        return link;
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

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Movie)
        {
            Movie o = (Movie)obj;

            return this.mediaBarcode == o.getMediaBarcode()
                    && this.link.equals(o.getLink())

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
        return mediaBarcode + ": " + this.titel + " - " + status + ", entliehen bis " + entliehenBis + " (" + vorbestellungen + " Vorbest.)";
    }
}
