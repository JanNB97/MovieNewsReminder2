package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.model.enums.Status;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingTestHelper;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

public class MedZenMovieSiteScraperTest
{
    public static final String SEVEN_WORKOUTS = "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0yJSVvXzA9OCUldl8wPXdvcmtvdXQrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD05NzA3NyZhbXA7cGFnZUlkPTImYW1wO1NyYz0yJmFtcDtwUz0xMA==-YVfW/qVrUvQ=";
    public static final String WRONG_URL = "https://lsdkjf.net//dsf.dsfjl//ldskjf.de";

    @Test
    public void testWrongLink()
    {
        try
        {
            new MedZenMovieSiteScraper(WRONG_URL);
            Assert.fail();
        } catch (IOException ignored){}
    }

    @Test
    public void testGetEssentialMovie()
    {
        MedZenMovieSiteScraper siteScraper = null;
        try
        {
            siteScraper = new MedZenMovieSiteScraper(SEVEN_WORKOUTS);
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        Movie sevenWorkouts = siteScraper.getEssentialMovie();
        Assert.assertNotNull(sevenWorkouts);

        WebscrapingTestHelper.assertEssentialMovie(80802980, SEVEN_WORKOUTS, sevenWorkouts);
    }

    @Test
    public void testGetMovie()
    {
        MedZenMovieSiteScraper siteScraper = null;
        try
        {
            siteScraper = new MedZenMovieSiteScraper(SEVEN_WORKOUTS);
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        Movie sevenWorkouts = siteScraper.getMovie();
        // TODO
    }
}
