package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.model.enums.Status;
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
            Status.VERFUEGBAR, 0, null,
            "Sport",
            "7 Workouts");

    private static final Movie DANCE_WITH_ME_EXPECTED = new Movie(
            80710247, DANCE_WITH_ME_URL,
            Status.VERFUEGBAR, 0, null,
            "Sport",
            "Dance with me!"
    );

    private static final Movie DIE_EISKOENINGIN_EXPECTED = new Movie(
            80891534, DIE_EISKOENINGIN_URL,
            Status.ENTLIEHEN, 0, DateHelper.toDate("02.08.2018"),
            "Walt Disney",
            "Die Eiskönigin - Völlig Unverfroren"
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
        MedZenMovieSiteScraper siteScraper = new MedZenMovieSiteScraper(SEVEN_WORKOUTS_EXPECTED.getURL());

        Movie sevenWorkouts = null;
        try
        {
            sevenWorkouts = siteScraper.getEssentialMovie();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Assert.assertNotNull(sevenWorkouts);

        WebscrapingTestHelper.assertEssentialMovie(SEVEN_WORKOUTS_EXPECTED, sevenWorkouts);
    }

    @Test
    public void testGetMovieVerfuegbar()
    {
        MedZenMovieSiteScraper siteScraper = new MedZenMovieSiteScraper(DANCE_WITH_ME_EXPECTED.getURL());

        try
        {
            WebscrapingTestHelper.assertMovie(DANCE_WITH_ME_EXPECTED, siteScraper.getMovie());
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
        MedZenMovieSiteScraper siteScraper = new MedZenMovieSiteScraper(SEVEN_WORKOUTS_EXPECTED.getURL());

        try
        {
            WebscrapingTestHelper.assertMovie(SEVEN_WORKOUTS_EXPECTED, siteScraper.getMovie());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
