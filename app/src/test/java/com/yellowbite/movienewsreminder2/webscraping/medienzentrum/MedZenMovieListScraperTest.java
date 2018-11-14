package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingTestHelper;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

public class MedZenMovieListScraperTest
{
    public static final String MED_ZEN_STAR_TREK_LIST = "https://opac.winbiap.net/mzhr/search.aspx?data=Y21kPTUmYW1wO3NDPWNfMD0xJSVtXzA9MSUlZl8wPTEyJSVvXzA9OCUldl8wPXN0YXIgdHJlaysrY18xPTElJW1fMT0xJSVmXzE9NDIlJW9fMT0xJSV2XzE9NDZTLURWRCAoU3BpZWxmaWxtKSZhbXA7U29ydD1FcnNjaGVpbnVuZ3NqYWhy-%2frgfFQ0Onp8%3d";

    public static final Movie STAR_TREK_BEYOND_EXPECTED = new Movie(80985421, "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0xMiUlb18wPTglJXZfMD1zdGFyIHRyZWsrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD0xNTUwMDImYW1wO3BhZ2VJZD0yJmFtcDtTcmM9MiZhbXA7cFM9MTA=-2h8U/0uiHso=");
    public static final Movie STAR_TREK_INTO_DARKNESS_EXPECTED = new Movie(80819887, "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0xMiUlb18wPTglJXZfMD1zdGFyIHRyZWsrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD05ODg3MyZhbXA7cGFnZUlkPTImYW1wO1NyYz0yJmFtcDtwUz0xMA==-oN6/Zdmw3kA=");
    public static final Movie STAR_TREK_EXPECTED = new Movie(80700000, "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0xMiUlb18wPTglJXZfMD1zdGFyIHRyZWsrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD04ODk2MCZhbXA7cGFnZUlkPTImYW1wO1NyYz0yJmFtcDtwUz0xMA==-vUBCHfQlATY=");

    @Test
    public void testWrongURL()
    {
        try
        {
            new MedZenMovieListScraper(WebscrapingTestHelper.WRONG_URL);
            Assert.fail();
        } catch (IOException ignored) {}
    }

    @Test
    public void testGetEssentialMovie()
    {
        MedZenMovieListScraper listScraper;
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
        WebscrapingTestHelper.assertIndexOutOfBoundException(() -> finalListScraper.getEssentialMovie(3));

        Assert.assertNotNull(starTrekBeyond);
        Assert.assertNotNull(starTrekIntoDarkness);
        Assert.assertNotNull(starTrek);

        WebscrapingTestHelper.assertEssentialMovie(STAR_TREK_BEYOND_EXPECTED, starTrekBeyond);
        WebscrapingTestHelper.assertEssentialMovie(STAR_TREK_INTO_DARKNESS_EXPECTED, starTrekIntoDarkness);
        WebscrapingTestHelper.assertEssentialMovie(STAR_TREK_EXPECTED, starTrek);
    }

    @Test
    public void testGetPageMax()
    {
        Assert.assertEquals(MedZenMovieListScraper.getMaxPages("41 von 47 Seiten"), 47);
        Assert.assertEquals(MedZenMovieListScraper.getMaxPages("1 von 1 Seiten"), 1);
        Assert.assertEquals(MedZenMovieListScraper.getMaxPages("41 von 34234 Seiten"), 34234);

        Assert.assertEquals(MedZenMovieListScraper.getMaxPages(" von 1 Seiten"), 1);
        Assert.assertEquals(MedZenMovieListScraper.getMaxPages("3242 von  Seiten"), -1);
        Assert.assertEquals(MedZenMovieListScraper.getMaxPages("von 500"), 500);
    }
}
