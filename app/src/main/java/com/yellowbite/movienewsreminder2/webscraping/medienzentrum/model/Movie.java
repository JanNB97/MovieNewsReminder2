package com.yellowbite.movienewsreminder2.webscraping.medienzentrum.model;

import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.enums.Status;

import java.util.Date;

public class Movie
{
    // essential
    private final int mediaBarcode;
    private final String link;

    //status informations
    private Status status;
    private int vorbestellungen;
    private Date entliehenBis;

    //standort informations
    private String standort;
    private String interessenkreis;
    private String signatur;

    //useful informations
    private String titel;
    private String erscheinungsjahr;
    private String kurzbeschreibung;

    public Movie(int mediaBarcode, String link)
    {
        this.mediaBarcode = mediaBarcode;
        this.link = link;
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

    public String getErscheinungsjahr() {
        return erscheinungsjahr;
    }

    public void setErscheinungsjahr(String erscheinungsjahr) {
        this.erscheinungsjahr = erscheinungsjahr;
    }

    public String getKurzbeschreibung() {
        return kurzbeschreibung;
    }

    public void setKurzbeschreibung(String kurzbeschreibung) {
        this.kurzbeschreibung = kurzbeschreibung;
    }
}
