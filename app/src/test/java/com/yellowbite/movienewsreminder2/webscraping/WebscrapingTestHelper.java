package com.yellowbite.movienewsreminder2.webscraping;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.model.enums.Status;

import junit.framework.Assert;

import java.util.Date;

public final class WebscrapingTestHelper
{
    public static void assertMovie(Movie movie,
                                int mediaBarcode, String link,
                                Status status, int vorbestellungen, Date entliehenBis,
                                String standort, String interessenkreis, String signatur,
                                String titel, String kurzbeschreibung)
    {
        assertEssentialMovie(movie, mediaBarcode, link);

        assertOthers(movie, status, vorbestellungen, entliehenBis, standort, interessenkreis, signatur, titel, kurzbeschreibung);
    }

    public static void assertEssentialMovie(Movie movie,
                                        int mediaBarcode, String link)
    {
        assertEssentials(movie, mediaBarcode, link);
        assertOthers(movie, null, -1, null, null, null, null, null, null);
    }

    private static void assertEssentials(Movie movie,
                                            int mediaBarcode, String link)
    {
        Assert.assertEquals(mediaBarcode, movie.getMediaBarcode());
        Assert.assertEquals(link, movie.getLink());
    }

    private static void assertOthers(Movie movie,
                                   Status status, int vorbestellungen, Date entliehenBis,
                                   String standort, String interessenkreis, String signatur,
                                   String titel, String kurzbeschreibung)
    {
        Assert.assertEquals(status, movie.getStatus());
        Assert.assertEquals(vorbestellungen, movie.getVorbestellungen());
        Assert.assertEquals(entliehenBis, movie.getEntliehenBis());

        Assert.assertEquals(standort, movie.getStandort());
        Assert.assertEquals(interessenkreis, movie.getInteressenkreis());
        Assert.assertEquals(signatur, movie.getSignatur());

        Assert.assertEquals(titel, movie.getTitel());
        Assert.assertEquals(kurzbeschreibung, movie.getKurzbeschreibung());
    }
}
