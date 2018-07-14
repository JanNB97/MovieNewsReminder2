package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.model.enums.Status;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingTestHelper;

import junit.framework.Assert;

import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

public class MedZenMovieListScraperTest
{
    public static final String MED_ZEN_STAR_TREK_LIST = "https://opac.winbiap.net/mzhr/search.aspx?data=Y21kPTUmYW1wO3NDPWNfMD0xJSVtXzA9MSUlZl8wPTEyJSVvXzA9OCUldl8wPXN0YXIgdHJlaysrY18xPTElJW1fMT0xJSVmXzE9NDIlJW9fMT0xJSV2XzE9NDZTLURWRCAoU3BpZWxmaWxtKSZhbXA7U29ydD1FcnNjaGVpbnVuZ3NqYWhy-%2frgfFQ0Onp8%3d";
    public static final String WRONG_URL = "https://lsdkjf.net//dsf.dsfjl//ldskjf.de";

    @Test
    public void testWrongLink()
    {
        try
        {
            new MedZenMovieListScraper(WRONG_URL);
            Assert.fail();
        } catch (IOException ignored) {}
    }

    @Test
    public void testGetEssentialMovie()
    {
        MedZenMovieListScraper listScraper = null;
        try
        {
            listScraper = new MedZenMovieListScraper(MED_ZEN_STAR_TREK_LIST);
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        Movie starTrekBeyond = listScraper.getEssentialMovie(0);
        Movie starTrekIntoDarkness = listScraper.getEssentialMovie(1);
        Movie starTrek = listScraper.getEssentialMovie(2);
        Assert.assertEquals(3, listScraper.getListEntrySize());
        MedZenMovieListScraper finalListScraper = listScraper;
        assertIndexOutOfBoundException(() -> finalListScraper.getEssentialMovie(3));

        Assert.assertNotNull(starTrekBeyond);
        Assert.assertNotNull(starTrekIntoDarkness);
        Assert.assertNotNull(starTrek);

        WebscrapingTestHelper.assertEssentialMovie(starTrekBeyond,
                80985421, "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0xMiUlb18wPTglJXZfMD1zdGFyIHRyZWsrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD0xNTUwMDImYW1wO3BhZ2VJZD0yJmFtcDtTcmM9MiZhbXA7cFM9MTA=-2h8U/0uiHso=");
        WebscrapingTestHelper.assertEssentialMovie(starTrekIntoDarkness,
                80819887, "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0xMiUlb18wPTglJXZfMD1zdGFyIHRyZWsrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD05ODg3MyZhbXA7cGFnZUlkPTImYW1wO1NyYz0yJmFtcDtwUz0xMA==-oN6/Zdmw3kA=");
        WebscrapingTestHelper.assertEssentialMovie(starTrek,
                80700000, "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0xMiUlb18wPTglJXZfMD1zdGFyIHRyZWsrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD04ODk2MCZhbXA7cGFnZUlkPTImYW1wO1NyYz0yJmFtcDtwUz0xMA==-vUBCHfQlATY=");
    }

    @Test
    public void testWrongIndex()
    {
        MedZenMovieListScraper listScraper = null;
        try
        {
            listScraper = new MedZenMovieListScraper(MED_ZEN_STAR_TREK_LIST);
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        MedZenMovieListScraper finalListScraper = listScraper;

        this.assertIndexOutOfBoundException(() -> finalListScraper.getEssentialMovie(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getEssentialMovie(-1));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getMovie(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getMovie(-1));

        // Essentials
        this.assertIndexOutOfBoundException(() -> finalListScraper.getMediaBarcode(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getMediaBarcode(-1));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getLink(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getLink(-1));

        // status informations
        this.assertIndexOutOfBoundException(() -> finalListScraper.getStatus(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getStatus(-1));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getEntliehenBis(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getEntliehenBis(-1));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getVorbestellungen(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getVorbestellungen(-1));

        // standort informations
        this.assertIndexOutOfBoundException(() -> finalListScraper.getStandort(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getStandort(-1));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getInteressenkreis(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getInteressenkreis(-1));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getSignatur(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getSignatur(-1));

        // useful informations
        this.assertIndexOutOfBoundException(() -> finalListScraper.getTitel(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getTitel(-1));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getKurzbeschreibung(1000));
        this.assertIndexOutOfBoundException(() -> finalListScraper.getKurzbeschreibung(-1));
    }

    private void assertIndexOutOfBoundException(Runnable runnable)
    {
        try
        {
            runnable.run();
            Assert.fail();
        }
        catch (IndexOutOfBoundsException ignored) {}
    }
}
