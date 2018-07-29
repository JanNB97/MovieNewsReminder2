package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.model.enums.Status;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingTestHelper;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

public class MedZenMovieSiteScraperTest
{
    private static final String SEVEN_WORKOUTS_URL = "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0yJSVvXzA9OCUldl8wPXdvcmtvdXQrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD05NzA3NyZhbXA7cGFnZUlkPTImYW1wO1NyYz0yJmFtcDtwUz0xMA==-YVfW/qVrUvQ=";
    private static final String DANCE_WITH_ME_URL = "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1FcnNjaGVpbnVuZ3NqYWhyJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD0yJSVvXzA9OCUldl8wPXdvcmtvdXQrK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkmYW1wO2NtZD0xJmFtcDtDYXRhbG9ndWVJZD04OTYxNCZhbXA7cGFnZUlkPTImYW1wO1NyYz0yJmFtcDtwUz0xMA==-BNobu7LWRRY=";

    private static final Movie SEVEN_WORKOUTS_EXPECTED = new Movie(
            80802980, SEVEN_WORKOUTS_URL,
            Status.VERFUEGBAR, 0, null,
            "Sport", "Sport", "Sachthema",
            "7 Workouts", "7 Workouts - nur 15 Minuten am Tag Miniaufwand - Maxieffekt! Keine Zeit zum Beeilen? Es ist nicht zu wenig Zeit, die man hat, sondern zu viel Zeit, die man nicht nutzt! Diese DVD bietet 7 Quick-Workouts für alle, die flott fit werden oder bleiben wollen. Mit nur 15 Minuten Training lässt sich auch im stressigsten Alltag eine Menge für Körper und Gesundheit tun! Jedes Programm dieser DVD bietet einen anderen Schwerpunkt, um schnell in Bestform zu kommen: 1 Das Cardio-Programm bringt Herz und Kreislauf auf Touren - für mehr Ausdauer mit Fett-weg-Effekt. 2 Das BBP-Programm für die klassischen Problemzonen Bauch, Beine & Po macht schlank und sexy - denn straffe Muskeln sind Make-up von innen.");

    private static final Movie DANCE_WITH_ME_EXPECTED = new Movie(
            80710247, DANCE_WITH_ME_URL,
            Status.VERFUEGBAR, 0, null,
            "Sport", "Sport", "Sachthema / Sport",
            "Dance with me!", "Billy Blanks jr. macht seinem Namen alle Ehre: Der Sohn des legendären Tae-Bo-Erfinders Billy Blanks hat mit \"Dance With Me!\" ein Workout vorgelegt, bei dem die Pfunde nur so purzeln. Spielerisch kombiniert er lässige Dance-Moves aus den Bereichen Hip-Hop, Country und Bollywood zu einer schweißtreibenden Choreographie, die sich leicht erlernen lässt. \"Dance With Me\" ist daher für Einsteiger ebenso geeignet wie für Profis. Inklusive Cardio-Bursts zur Maximierung des Kalorienverbrauchs."
    );

    @Test
    public void testWrongURL()
    {
        try
        {
            new MedZenMovieSiteScraper(WebscrapingTestHelper.WRONG_URL).getEntliehenBis();
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
    public void testGetMovie()
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
