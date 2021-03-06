package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.util.DateHelper;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingTestHelper;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

public class MedZenMovieSiteScraperTest
{
    private static final String SEVEN_WORKOUTS_URL = "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0yJSVvXzA9OCUldl8wPXdvcmtvdXQrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD05NzA3NyZhbXA7cGFnZUlkPTImYW1wO1NyYz0yJmFtcDtwUz0xMA==-YVfW/qVrUvQ=";
    private static final String DANCE_WITH_ME_URL = "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0yJSVvXzA9OCUldl8wPXdvcmtvdXQrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD04OTYxNCZhbXA7cGFnZUlkPTImYW1wO1NyYz0yJmFtcDtwUz0xMA==-BNobu7LWRRY=";
    private static final String DIE_EISKOENINGIN_URL = "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0yJSVvXzA9OCUldl8wPWRpZSBlaXNrJiMyNDY7bmlnaW4mYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD05OTYxMyZhbXA7cGFnZUlkPTImYW1wO1NyYz0yJmFtcDtwUz0xMA==-tPSt0NbDt5Y=";

    private static final Movie SEVEN_WORKOUTS_EXPECTED = new Movie(
            80802980, SEVEN_WORKOUTS_URL,
            Movie.Status.VERFUEGBAR, 0, null,
            "Sport", DateHelper.toDate("18.07.2013"),
            "7 Workouts", false, false);

    private static final Movie DANCE_WITH_ME_EXPECTED = new Movie(
            80710247, DANCE_WITH_ME_URL,
            Movie.Status.VERFUEGBAR, 0, null,
            "Sport", DateHelper.toDate("05.07.2011"),
            "Dance with me!", false, false
    );

    private static final Movie DIE_EISKOENINGIN_EXPECTED = new Movie(
            80891534, DIE_EISKOENINGIN_URL,
            Movie.Status.ENTLIEHEN, 0, DateHelper.toDate("20.08.2018"),
            "Walt Disney", DateHelper.toDate("06.05.2014"),
            "Die Eiskönigin - Völlig Unverfroren", false, false
    );

    @Test
    public void testWrongURL()
    {
        try
        {
            new MedZenMovieSiteScraper(WebscrapingTestHelper.WRONG_URL).getMovie();
            Assert.fail();
        } catch (IOException ignored){}
    }

    @Test
    public void testGetEssentialMovie()
    {
        try
        {
            Movie sevenWorkouts = new MedZenMovieSiteScraper(SEVEN_WORKOUTS_EXPECTED.getURL()).getMediaBarcodeMovie();
            Assert.assertNotNull(sevenWorkouts);
            WebscrapingTestHelper.assertEssentialMovie(SEVEN_WORKOUTS_EXPECTED, sevenWorkouts);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMovieVerfuegbar()
    {
        try
        {
            WebscrapingTestHelper.assertMovie(DANCE_WITH_ME_EXPECTED,
                    new MedZenMovieSiteScraper(DANCE_WITH_ME_EXPECTED.getURL()).getMovie());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMovieEntliehen()
    {
        try
        {
            WebscrapingTestHelper.assertMovie(DIE_EISKOENINGIN_EXPECTED, MedZenMovieSiteScraper.getMovie(DIE_EISKOENINGIN_URL));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMovieWithHiddenTitle()
    {
        try
        {
            WebscrapingTestHelper.assertMovie(SEVEN_WORKOUTS_EXPECTED,
                    new MedZenMovieSiteScraper(SEVEN_WORKOUTS_EXPECTED.getURL()).getMovie());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
