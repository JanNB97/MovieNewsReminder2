package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.model.enums.Status;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingTestHelper;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

public class MedZenMovieSiteScraperTest
{
    public static final String SEVEN_WORKOUTS_LINK = "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0yJSVvXzA9OCUldl8wPXdvcmtvdXQrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD05NzA3NyZhbXA7cGFnZUlkPTImYW1wO1NyYz0yJmFtcDtwUz0xMA==-YVfW/qVrUvQ=";

    public static final Movie sevenWorkoutsExpected = new Movie(
            80802980, SEVEN_WORKOUTS_LINK,
            Status.VERFUEGBAR, 0, null,
            "Sport", "Sport", "Sachthema",
            "7 Workouts", "7 Workouts - nur 15 Minuten am Tag Miniaufwand - Maxieffekt! Keine Zeit zum Beeilen? Es ist nicht zu wenig Zeit, die man hat, sondern zu viel Zeit, die man nicht nutzt! Diese DVD bietet 7 Quick-Workouts für alle, die flott fit werden oder bleiben wollen. Mit nur 15 Minuten Training lässt sich auch im stressigsten Alltag eine Menge für Körper und Gesundheit tun! Jedes Programm dieser DVD bietet einen anderen Schwerpunkt, um schnell in Bestform zu kommen: 1 Das Cardio-Programm bringt Herz und Kreislauf auf Touren - für mehr Ausdauer mit Fett-weg-Effekt. 2 Das BBP-Programm für die klassischen Problemzonen Bauch, Beine & Po macht schlank und sexy - denn straffe Muskeln sind Make-up von innen.");

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
            siteScraper = new MedZenMovieSiteScraper(sevenWorkoutsExpected.getLink());
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        Movie sevenWorkouts = siteScraper.getEssentialMovie();
        Assert.assertNotNull(sevenWorkouts);

        WebscrapingTestHelper.assertEssentialMovie(sevenWorkouts.getMediaBarcode(), sevenWorkouts.getLink(), sevenWorkouts);
    }

    @Test
    public void testGetMovie()
    {
        MedZenMovieSiteScraper siteScraper = null;
        try
        {
            siteScraper = new MedZenMovieSiteScraper(sevenWorkoutsExpected.getLink());
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        WebscrapingTestHelper.assertMovie(sevenWorkoutsExpected, siteScraper.getMovie());
    }
}
